/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.donkey.model.channel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.mirth.connect.donkey.model.message.Status;
import com.mirth.connect.donkey.util.DonkeyElement;
import com.mirth.connect.donkey.util.migration.Migratable;
import com.mirth.connect.donkey.util.purge.Purgable;

public class SourceConnectorProperties implements Serializable, Migratable, Purgable {

    /**
     * "Respond From" key indicating that no response should be sent back
     */
    public static final String RESPONSE_NONE = "None";

    /**
     * "Respond From" key indicating that the response returned by the source connector should be
     * auto-generated by the inbound data type, with a status of SENT
     */
    public static final String RESPONSE_AUTO_BEFORE = "Auto-generate (Before processing)";

    /**
     * "Respond From" key indicating that the response returned by the source connector should be
     * auto-generated by the inbound data type and based on whether or not a message is filtered or
     * errored in the source filter/transformer
     */
    public static final String RESPONSE_SOURCE_TRANSFORMED = "Auto-generate (After source transformer)";

    /**
     * "Respond From" key indicating that the response returned by the source connector should be
     * auto-generated by the inbound data type and based on whether or not all destinations sent or
     * queued the message successfully
     */
    public static final String RESPONSE_DESTINATIONS_COMPLETED = "Auto-generate (Destinations completed)";

    /**
     * Response map key to be used to store the post-processor's custom response
     */
    public static final String RESPONSE_POST_PROCESSOR = "Postprocessor";

    /**
     * When returning a response status based on the statuses of all destinations, use this
     * precedence order in determining which status to use when the destination statuses are
     * different
     */
    public static final Status[] RESPONSE_STATUS_PRECEDENCE = new Status[] { Status.ERROR,
            Status.QUEUED, Status.SENT, Status.FILTERED };

    public static final String[] QUEUE_ON_RESPONSES = new String[] { RESPONSE_NONE,
            RESPONSE_AUTO_BEFORE };

    public static final String[] QUEUE_OFF_RESPONSES = new String[] { RESPONSE_NONE,
            RESPONSE_AUTO_BEFORE, RESPONSE_SOURCE_TRANSFORMED, RESPONSE_DESTINATIONS_COMPLETED,
            RESPONSE_POST_PROCESSOR };

    private String responseVariable;
    private boolean respondAfterProcessing;
    private boolean processBatch;
    private boolean firstResponse;
    private Set<String> resourceIds;

    public SourceConnectorProperties() {
        this(RESPONSE_NONE);
    }

    public SourceConnectorProperties(String defaultResponse) {
        this.responseVariable = defaultResponse;
        this.respondAfterProcessing = true;
        this.processBatch = false;
        this.firstResponse = false;
        this.resourceIds = new LinkedHashSet<String>();
        this.resourceIds.add("Default Resource");
    }

    public String getResponseVariable() {
        return responseVariable;
    }

    public void setResponseVariable(String responseVariable) {
        this.responseVariable = responseVariable;
    }

    public boolean isRespondAfterProcessing() {
        return respondAfterProcessing;
    }

    public void setRespondAfterProcessing(boolean respondAfterProcessing) {
        this.respondAfterProcessing = respondAfterProcessing;
    }

    public boolean isProcessBatch() {
        return processBatch;
    }

    public void setProcessBatch(boolean processBatch) {
        this.processBatch = processBatch;
    }

    public boolean isFirstResponse() {
        return firstResponse;
    }

    public void setFirstResponse(boolean firstResponse) {
        this.firstResponse = firstResponse;
    }

    public Set<String> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(Set<String> resourceIds) {
        this.resourceIds = resourceIds;
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public void migrate3_0_1(DonkeyElement element) {}

    @Override
    public void migrate3_0_2(DonkeyElement element) {}

    @Override
    public void migrate3_1_0(DonkeyElement element) {
        element.addChildElementIfNotExists("processBatch", "false");
        element.addChildElementIfNotExists("firstResponse", "false");

        element.removeChild("defaultQueueOffResponses");
        element.removeChild("defaultQueueOnResponses");
    }

    @Override
    public void migrate3_2_0(DonkeyElement element) {
        DonkeyElement resourceIdsElement = element.addChildElement("resourceIds");
        resourceIdsElement.setAttribute("class", "linked-hash-set");
        resourceIdsElement.addChildElement("string", "Default Resource");
    }

    @Override
    public Map<String, Object> getPurgedProperties() {
        Map<String, Object> purgedProperties = new HashMap<String, Object>();
        purgedProperties.put("respondAfterProcessing", respondAfterProcessing);
        purgedProperties.put("processBatch", processBatch);
        purgedProperties.put("firstResponse", firstResponse);
        purgedProperties.put("resourceIdsCount", resourceIds.size());
        return purgedProperties;
    }
}
