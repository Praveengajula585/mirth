/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.connectors.ws;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.ConnectException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.namespace.QName;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.log4j.Logger;

import com.mirth.connect.donkey.model.channel.ConnectorProperties;
import com.mirth.connect.donkey.model.event.ConnectionStatusEventType;
import com.mirth.connect.donkey.model.event.ErrorEventType;
import com.mirth.connect.donkey.model.message.ConnectorMessage;
import com.mirth.connect.donkey.model.message.Response;
import com.mirth.connect.donkey.model.message.Status;
import com.mirth.connect.donkey.model.message.attachment.AttachmentHandler;
import com.mirth.connect.donkey.server.DeployException;
import com.mirth.connect.donkey.server.HaltException;
import com.mirth.connect.donkey.server.StartException;
import com.mirth.connect.donkey.server.StopException;
import com.mirth.connect.donkey.server.UndeployException;
import com.mirth.connect.donkey.server.channel.DestinationConnector;
import com.mirth.connect.donkey.server.event.ConnectionStatusEvent;
import com.mirth.connect.donkey.server.event.ErrorEvent;
import com.mirth.connect.server.controllers.ConfigurationController;
import com.mirth.connect.server.controllers.ControllerFactory;
import com.mirth.connect.server.controllers.EventController;
import com.mirth.connect.server.util.TemplateValueReplacer;
import com.mirth.connect.util.ErrorMessageBuilder;

public class WebServiceDispatcher extends DestinationConnector {

    // The system property actually ends up being the maximum request count
    private static final int MAX_REDIRECTS = NumberUtils.toInt(System.getProperty("http.maxRedirects"), 20);

    private Logger logger = Logger.getLogger(this.getClass());
    protected WebServiceDispatcherProperties connectorProperties;
    private EventController eventController = ControllerFactory.getFactory().createEventController();
    private ConfigurationController configurationController = ControllerFactory.getFactory().createConfigurationController();
    private TemplateValueReplacer replacer = new TemplateValueReplacer();
    private WebServiceConfiguration configuration;
    private RegistryBuilder<ConnectionSocketFactory> socketFactoryRegistry;

    /*
     * Dispatch object used for pooling the soap connection, and the current properties used to
     * create the dispatch object
     */
    private Map<Long, DispatchContainer> dispatchContainers = new ConcurrentHashMap<Long, DispatchContainer>();

    @Override
    public void onDeploy() throws DeployException {
        this.connectorProperties = (WebServiceDispatcherProperties) getConnectorProperties();

        // load the default configuration
        String configurationClass = configurationController.getProperty(connectorProperties.getProtocol(), "wsConfigurationClass");

        try {
            configuration = (WebServiceConfiguration) Class.forName(configurationClass).newInstance();
        } catch (Exception e) {
            logger.trace("could not find custom configuration class, using default");
            configuration = new DefaultWebServiceConfiguration();
        }

        try {
            socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.getSocketFactory());
            configuration.configureConnectorDeploy(this);
        } catch (Exception e) {
            throw new DeployException(e);
        }
    }

    @Override
    public void onUndeploy() throws UndeployException {
        configuration.configureConnectorUndeploy(this);
    }

    @Override
    public void onStart() throws StartException {}

    @Override
    public void onStop() throws StopException {
        dispatchContainers.clear();
    }

    @Override
    public void onHalt() throws HaltException {
        dispatchContainers.clear();
    }

    private String sourceToXmlString(Source source) throws TransformerConfigurationException, TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        Writer writer = new StringWriter();
        transformer.transform(source, new StreamResult(writer));
        return writer.toString();
    }

    private void createDispatch(WebServiceDispatcherProperties webServiceDispatcherProperties, DispatchContainer dispatchContainer) throws Exception {
        String wsdlUrl = webServiceDispatcherProperties.getWsdlUrl();
        String username = webServiceDispatcherProperties.getUsername();
        String password = webServiceDispatcherProperties.getPassword();
        String serviceName = webServiceDispatcherProperties.getService();
        String portName = webServiceDispatcherProperties.getPort();

        /*
         * The dispatch needs to be created if it hasn't been created yet (null). It needs to be
         * recreated if any of the above variables are different than what were used to create the
         * current dispatch object. This could happen if variables are being used for these
         * properties.
         */
        if (dispatchContainer.getDispatch() == null || !StringUtils.equals(wsdlUrl, dispatchContainer.getCurrentWsdlUrl()) || !StringUtils.equals(username, dispatchContainer.getCurrentUsername()) || !StringUtils.equals(password, dispatchContainer.getCurrentPassword()) || !StringUtils.equals(serviceName, dispatchContainer.getCurrentServiceName()) || !StringUtils.equals(portName, dispatchContainer.getCurrentPortName())) {
            dispatchContainer.setCurrentWsdlUrl(wsdlUrl);
            dispatchContainer.setCurrentUsername(username);
            dispatchContainer.setCurrentPassword(password);
            dispatchContainer.setCurrentServiceName(serviceName);
            dispatchContainer.setCurrentPortName(portName);

            URL endpointUrl = getWsdlUrl(wsdlUrl, username, password);
            QName serviceQName = QName.valueOf(serviceName);
            QName portQName = QName.valueOf(portName);

            // create the service and dispatch
            logger.debug("Creating web service: url=" + endpointUrl.toString() + ", service=" + serviceQName + ", port=" + portQName);
            Service service = Service.create(endpointUrl, serviceQName);

            dispatchContainer.setDispatch(service.createDispatch(portQName, SOAPMessage.class, Service.Mode.MESSAGE));
        }
    }

    /**
     * Returns the URL for the passed in String. If the URL requires authentication, then the WSDL
     * is saved as a temp file and the URL for that file is returned.
     * 
     * @param wsdlUrl
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    private URL getWsdlUrl(String wsdlUrl, String username, String password) throws Exception {
        URI uri = new URI(wsdlUrl);

        // If the URL points to file, just return it
        if (!uri.getScheme().equalsIgnoreCase("file")) {
            BasicHttpClientConnectionManager httpClientConnectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry.build());
            CloseableHttpClient client = HttpClients.custom().setConnectionManager(httpClientConnectionManager).build();

            try {
                HttpRequestBase method = new HttpGet(wsdlUrl);
                HttpClientContext context = HttpClientContext.create();

                if (username != null && password != null) {
                    CredentialsProvider credsProvider = new BasicCredentialsProvider();
                    AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
                    Credentials credentials = new UsernamePasswordCredentials(username, password);
                    credsProvider.setCredentials(authScope, credentials);
                    AuthCache authCache = new BasicAuthCache();
                    RegistryBuilder<AuthSchemeProvider> registryBuilder = RegistryBuilder.<AuthSchemeProvider> create();
                    registryBuilder.register(AuthSchemes.BASIC, new BasicSchemeFactory());

                    context.setCredentialsProvider(credsProvider);
                    context.setAuthSchemeRegistry(registryBuilder.build());
                    context.setAuthCache(authCache);
                }

                CloseableHttpResponse response = client.execute(method, context);

                try {
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        ContentType responseContentType = ContentType.get(response.getEntity());
                        if (responseContentType == null) {
                            responseContentType = ContentType.TEXT_XML;
                        }

                        Charset responseCharset = responseContentType.getCharset();
                        if (responseContentType.getCharset() == null) {
                            responseCharset = ContentType.TEXT_XML.getCharset();
                        }

                        String wsdl = IOUtils.toString(response.getEntity().getContent(), responseCharset);
                        File tempFile = File.createTempFile("WebServiceSender", ".wsdl");
                        tempFile.deleteOnExit();

                        FileUtils.writeStringToFile(tempFile, wsdl);

                        return tempFile.toURI().toURL();
                    }
                } finally {
                    HttpClientUtils.closeQuietly(response);
                }
            } finally {
                HttpClientUtils.closeQuietly(client);
            }
        }

        return uri.toURL();
    }

    @Override
    public void replaceConnectorProperties(ConnectorProperties connectorProperties, ConnectorMessage connectorMessage) {
        WebServiceDispatcherProperties webServiceDispatcherProperties = (WebServiceDispatcherProperties) connectorProperties;

        // Replace all values in connector properties
        webServiceDispatcherProperties.setWsdlUrl(replacer.replaceValues(webServiceDispatcherProperties.getWsdlUrl(), connectorMessage));
        webServiceDispatcherProperties.setUsername(replacer.replaceValues(webServiceDispatcherProperties.getUsername(), connectorMessage));
        webServiceDispatcherProperties.setPassword(replacer.replaceValues(webServiceDispatcherProperties.getPassword(), connectorMessage));
        webServiceDispatcherProperties.setService(replacer.replaceValues(webServiceDispatcherProperties.getService(), connectorMessage));
        webServiceDispatcherProperties.setPort(replacer.replaceValues(webServiceDispatcherProperties.getPort(), connectorMessage));
        webServiceDispatcherProperties.setLocationURI(replacer.replaceValues(webServiceDispatcherProperties.getLocationURI(), connectorMessage));

        webServiceDispatcherProperties.setSoapAction(replacer.replaceValues(webServiceDispatcherProperties.getSoapAction(), connectorMessage));
        webServiceDispatcherProperties.setEnvelope(replacer.replaceValues(webServiceDispatcherProperties.getEnvelope(), connectorMessage));

        if (webServiceDispatcherProperties.isUseMtom()) {
            replacer.replaceValuesInList(webServiceDispatcherProperties.getAttachmentNames(), connectorMessage);
            replacer.replaceValuesInList(webServiceDispatcherProperties.getAttachmentContents(), connectorMessage);
            replacer.replaceValuesInList(webServiceDispatcherProperties.getAttachmentTypes(), connectorMessage);
        }
    }

    @Override
    public Response send(ConnectorProperties connectorProperties, ConnectorMessage connectorMessage) {
        WebServiceDispatcherProperties webServiceDispatcherProperties = (WebServiceDispatcherProperties) connectorProperties;

        eventController.dispatchEvent(new ConnectionStatusEvent(getChannelId(), getMetaDataId(), getDestinationName(), ConnectionStatusEventType.SENDING));

        String responseData = null;
        String responseError = null;
        String responseStatusMessage = null;
        Status responseStatus = Status.QUEUED;
        boolean validateResponse = false;

        try {
            long dispatcherId = getDispatcherId();
            DispatchContainer dispatchContainer = dispatchContainers.get(dispatcherId);
            if (dispatchContainer == null) {
                dispatchContainer = new DispatchContainer();
                dispatchContainers.put(dispatcherId, dispatchContainer);
            }

            /*
             * Initialize the dispatch object if it hasn't been initialized yet, or create a new one
             * if the connector properties have changed due to variables.
             */
            createDispatch(webServiceDispatcherProperties, dispatchContainer);

            Dispatch<SOAPMessage> dispatch = dispatchContainer.getDispatch();
            configuration.configureDispatcher(this, webServiceDispatcherProperties, dispatch.getRequestContext());

            SOAPBinding soapBinding = (SOAPBinding) dispatch.getBinding();

            if (webServiceDispatcherProperties.isUseAuthentication()) {
                String currentUsername = dispatchContainer.getCurrentUsername();
                String currentPassword = dispatchContainer.getCurrentPassword();

                dispatch.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, currentUsername);
                dispatch.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, currentPassword);
                logger.debug("Using authentication: username=" + currentUsername + ", password length=" + currentPassword.length());
            }

            // See: http://www.w3.org/TR/2000/NOTE-SOAP-20000508/#_Toc478383528
            String soapAction = webServiceDispatcherProperties.getSoapAction();

            if (StringUtils.isNotEmpty(soapAction)) {
                dispatch.getRequestContext().put(BindingProvider.SOAPACTION_USE_PROPERTY, true); // MIRTH-2109
                dispatch.getRequestContext().put(BindingProvider.SOAPACTION_URI_PROPERTY, soapAction);
            }

            // build the message
            logger.debug("Creating SOAP envelope.");
            AttachmentHandler attachmentHandler = getAttachmentHandler();
            String content = attachmentHandler.reAttachMessage(webServiceDispatcherProperties.getEnvelope(), connectorMessage);
            Source source = new StreamSource(new StringReader(content));
            SOAPMessage message = soapBinding.getMessageFactory().createMessage();
            message.getSOAPPart().setContent(source);

            if (webServiceDispatcherProperties.isUseMtom()) {
                soapBinding.setMTOMEnabled(true);

                List<String> attachmentIds = webServiceDispatcherProperties.getAttachmentNames();
                List<String> attachmentContents = webServiceDispatcherProperties.getAttachmentContents();
                List<String> attachmentTypes = webServiceDispatcherProperties.getAttachmentTypes();

                for (int i = 0; i < attachmentIds.size(); i++) {
                    String attachmentContentId = attachmentIds.get(i);
                    String attachmentContentType = attachmentTypes.get(i);
                    String attachmentContent = attachmentHandler.reAttachMessage(attachmentContents.get(i), connectorMessage);

                    AttachmentPart attachment = message.createAttachmentPart();
                    attachment.setBase64Content(new ByteArrayInputStream(attachmentContent.getBytes("UTF-8")), attachmentContentType);
                    attachment.setContentId(attachmentContentId);
                    message.addAttachmentPart(attachment);
                }
            }

            message.saveChanges();

            if (StringUtils.isNotBlank(webServiceDispatcherProperties.getLocationURI())) {
                dispatch.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, webServiceDispatcherProperties.getLocationURI());
            }

            boolean redirect = false;
            int tryCount = 0;

            /*
             * Attempt the invocation until we hit the maximum allowed redirects. The redirections
             * we handle are when the scheme changes (i.e. from HTTP to HTTPS).
             */
            do {
                redirect = false;
                tryCount++;

                try {
                    // Make the call
                    if (webServiceDispatcherProperties.isOneWay()) {
                        logger.debug("Invoking one way service...");
                        dispatch.invokeOneWay(message);
                        responseStatusMessage = "Invoked one way operation successfully.";
                    } else {
                        logger.debug("Invoking web service...");
                        SOAPMessage result = dispatch.invoke(message);
                        responseData = sourceToXmlString(result.getSOAPPart().getContent());
                        responseStatusMessage = "Invoked two way operation successfully.";
                    }
                    logger.debug("Finished invoking web service, got result.");

                    // Automatically accept message; leave it up to the response transformer to find SOAP faults
                    responseStatus = Status.SENT;
                } catch (Exception e) {
                    Integer responseCode = (Integer) dispatch.getResponseContext().get(MessageContext.HTTP_RESPONSE_CODE);

                    String location = null;
                    Map<String, List<String>> headers = (Map<String, List<String>>) dispatch.getResponseContext().get(MessageContext.HTTP_RESPONSE_HEADERS);
                    if (MapUtils.isNotEmpty(headers)) {
                        List<String> locations = headers.get("Location");
                        if (CollectionUtils.isNotEmpty(locations)) {
                            location = locations.get(0);
                        }
                    }

                    if (tryCount < MAX_REDIRECTS && responseCode != null && responseCode >= 300 && responseCode < 400 && StringUtils.isNotBlank(location)) {
                        redirect = true;

                        // Replace the endpoint with the redirected URL
                        dispatch.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, location);
                    } else {
                        // Leave the response status as QUEUED for ConnectException, otherwise ERROR
                        if ((e.getClass() == ConnectException.class) || ((e.getCause() != null) && (e.getCause().getClass() == ConnectException.class))) {
                            responseStatusMessage = ErrorMessageBuilder.buildErrorResponse("Connection refused.", e);
                            eventController.dispatchEvent(new ErrorEvent(getChannelId(), getMetaDataId(), ErrorEventType.DESTINATION_CONNECTOR, getDestinationName(), connectorProperties.getName(), "Connection refused.", e));
                        } else {
                            responseStatus = Status.ERROR;
                            responseStatusMessage = ErrorMessageBuilder.buildErrorResponse("Error invoking web service", e);
                            responseError = ErrorMessageBuilder.buildErrorMessage(connectorProperties.getName(), "Error invoking web service", e);
                            eventController.dispatchEvent(new ErrorEvent(getChannelId(), getMetaDataId(), ErrorEventType.DESTINATION_CONNECTOR, getDestinationName(), connectorProperties.getName(), "Error invoking web service.", e));
                        }
                    }
                }
            } while (redirect && tryCount < MAX_REDIRECTS);
        } catch (Exception e) {
            // Set the response status to ERROR if it failed to create the dispatch
            responseStatus = Status.ERROR;
            responseStatusMessage = ErrorMessageBuilder.buildErrorResponse("Error creating web service dispatch", e);
            responseError = ErrorMessageBuilder.buildErrorMessage(connectorProperties.getName(), "Error creating web service dispatch", e);
            eventController.dispatchEvent(new ErrorEvent(getChannelId(), getMetaDataId(), ErrorEventType.DESTINATION_CONNECTOR, getDestinationName(), connectorProperties.getName(), "Error creating web service dispatch.", e));
        } finally {
            eventController.dispatchEvent(new ConnectionStatusEvent(getChannelId(), getMetaDataId(), getDestinationName(), ConnectionStatusEventType.IDLE));
        }

        return new Response(responseStatus, responseData, responseStatusMessage, responseError, validateResponse);
    }

    private class DispatchContainer {
        /*
         * Dispatch object used for pooling the soap connection, and the current properties used to
         * create the dispatch object
         */
        private Dispatch<SOAPMessage> dispatch = null;
        private String currentWsdlUrl = null;
        private String currentUsername = null;
        private String currentPassword = null;
        private String currentServiceName = null;
        private String currentPortName = null;

        public Dispatch<SOAPMessage> getDispatch() {
            return dispatch;
        }

        public void setDispatch(Dispatch<SOAPMessage> dispatch) {
            this.dispatch = dispatch;
        }

        public String getCurrentWsdlUrl() {
            return currentWsdlUrl;
        }

        public void setCurrentWsdlUrl(String currentWsdlUrl) {
            this.currentWsdlUrl = currentWsdlUrl;
        }

        public String getCurrentUsername() {
            return currentUsername;
        }

        public void setCurrentUsername(String currentUsername) {
            this.currentUsername = currentUsername;
        }

        public String getCurrentPassword() {
            return currentPassword;
        }

        public void setCurrentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
        }

        public String getCurrentServiceName() {
            return currentServiceName;
        }

        public void setCurrentServiceName(String currentServiceName) {
            this.currentServiceName = currentServiceName;
        }

        public String getCurrentPortName() {
            return currentPortName;
        }

        public void setCurrentPortName(String currentPortName) {
            this.currentPortName = currentPortName;
        }
    }

    public RegistryBuilder<ConnectionSocketFactory> getSocketFactoryRegistry() {
        return socketFactoryRegistry;
    }
}