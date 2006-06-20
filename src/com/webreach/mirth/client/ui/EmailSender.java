/*
 * EmailSender.java
 *
 * Created on May 22, 2006, 12:05 PM
 */

package com.webreach.mirth.client.ui;

import java.util.Properties;

/**
 *
 * @author  brendanh
 */
public class EmailSender extends ConnectorClass
{
	Frame parent;
    /** Creates new form EmailSender */
    public EmailSender(Frame parent)
    {
        this.parent = parent;
        name = "Email Sender";
        initComponents();
    }

    public Properties getProperties()
    {
        Properties properties = new Properties();
        properties.put("DataType", name);
        properties.put("address", SMTPServerHostField.getText());
        properties.put("port", SMTPServerPortField.getText());
        properties.put("Username", emailUsernameField.getText());
        properties.put("Password", new String(emailPasswordField.getPassword()));
        properties.put("To", emailToField.getText());
        properties.put("Subject", emailSubjectField.getText());
        properties.put("Body", emailBodyTextArea.getText());
        return properties;
    }

    public void setProperties(Properties props)
    {
        SMTPServerHostField.setText((String)props.get("address"));
        SMTPServerPortField.setText((String)props.get("port"));
        emailUsernameField.setText((String)props.get("Username"));
        emailPasswordField.setText((String)props.get("Password"));
        emailToField.setText((String)props.get("To"));
        emailSubjectField.setText((String)props.get("Subject"));
        emailBodyTextArea.setText((String)props.get("Body"));
    }

    public void setDefaults()
    {
        SMTPServerHostField.setText("");
        SMTPServerPortField.setText("");
        emailUsernameField.setText("");
        emailPasswordField.setText("");
        emailToField.setText("");
        emailSubjectField.setText("");
        emailBodyTextArea.setText("");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        emailUsernameField = new javax.swing.JTextField();
        SMTPServerPortField = new javax.swing.JTextField();
        SMTPServerHostField = new javax.swing.JTextField();
        emailToField = new javax.swing.JTextField();
        emailSubjectField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        emailBodyTextArea = new javax.swing.JTextArea();
        emailPasswordField = new javax.swing.JPasswordField();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Email Sender", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0)));
        jLabel1.setText("SMTP Server Host:");

        jLabel2.setText("SMTP Server Port:");

        jLabel3.setText("Username:");

        jLabel4.setText("Password:");

        jLabel5.setText("To:");

        jLabel6.setText("Subject:");

        jLabel7.setText("Body:");

        emailBodyTextArea.setColumns(20);
        emailBodyTextArea.setRows(5);
        jScrollPane1.setViewportView(emailBodyTextArea);

        emailPasswordField.setFont(new java.awt.Font("Tahoma", 0, 11));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel2)
                    .add(jLabel1)
                    .add(jLabel3)
                    .add(jLabel4)
                    .add(jLabel5)
                    .add(jLabel6)
                    .add(jLabel7))
                .add(20, 20, 20)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, emailPasswordField)
                        .add(emailUsernameField)
                        .add(SMTPServerPortField)
                        .add(emailToField)
                        .add(emailSubjectField)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, SMTPServerHostField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(SMTPServerHostField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(SMTPServerPortField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(emailUsernameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(emailPasswordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(emailToField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(emailSubjectField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel7)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField SMTPServerHostField;
    private javax.swing.JTextField SMTPServerPortField;
    private javax.swing.JTextArea emailBodyTextArea;
    private javax.swing.JPasswordField emailPasswordField;
    private javax.swing.JTextField emailSubjectField;
    private javax.swing.JTextField emailToField;
    private javax.swing.JTextField emailUsernameField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
