package com.webreach.mirth.client.ui;

import java.util.Properties;

public class FileReader extends ConnectorClass
{
    Frame parent;
    
    /** Creates new form FileWriter */
    public final String DATATYPE = "DataType";
    public final String FILE_DIRECTORY = "host";
    public final String FILE_POLLING_FREQUENCY = "pollingFrequency";
    public final String FILE_MOVE_TO_PATTERN = "moveToPattern";
    public final String FILE_MOVE_TO_DIRECTORY = "moveToDirectory";
    public final String FILE_DELETE_AFTER_READ = "autoDelete";
    public final String FILE_CHECK_FILE_AGE = "checkFileAge";
    public final String FILE_FILE_AGE = "fileAge";
    
    public FileReader()
    {
        this.parent = PlatformUI.MIRTH_FRAME;
        name = "File Reader";
        initComponents();
    }

    public Properties getProperties()
    {
        Properties properties = new Properties();
        properties.put(DATATYPE, name);
        properties.put(FILE_DIRECTORY, directoryField.getText());
        properties.put(FILE_POLLING_FREQUENCY, pollingFreq.getText());
        properties.put(FILE_MOVE_TO_PATTERN, moveToPattern.getText());
        properties.put(FILE_MOVE_TO_DIRECTORY, moveToDirectory.getText());
        
        if (deleteAfterReadYes.isSelected())
            properties.put(FILE_DELETE_AFTER_READ, "YES");
        else
            properties.put(FILE_DELETE_AFTER_READ, "NO");
        
        if (checkFileAgeYes.isSelected())
            properties.put(FILE_CHECK_FILE_AGE, "YES");
        else
            properties.put(FILE_CHECK_FILE_AGE, "NO");
        
        properties.put(FILE_FILE_AGE, fileAge.getText());
        
        return properties;
    }

    public void setProperties(Properties props)
    {
        directoryField.setText((String)props.get(FILE_DIRECTORY));
        pollingFreq.setText((String)props.get(FILE_POLLING_FREQUENCY));
        moveToPattern.setText((String)props.get(FILE_MOVE_TO_PATTERN));
        moveToDirectory.setText((String)props.get(FILE_MOVE_TO_DIRECTORY));
        if(((String)props.get(FILE_DELETE_AFTER_READ)).equalsIgnoreCase("YES"))
            deleteAfterReadYes.setSelected(true);
        else
            deleteAfterReadNo.setSelected(true);
        if(((String)props.get(FILE_CHECK_FILE_AGE)).equalsIgnoreCase("YES"))
        {
            checkFileAgeYes.setSelected(true);
            checkFileAgeYesActionPerformed(null);
        }
        else
        {
            checkFileAgeNo.setSelected(true);
            checkFileAgeNoActionPerformed(null);
        }
        
        fileAge.setText((String)props.get(FILE_FILE_AGE));
    }

    public void setDefaults()
    {
        directoryField.setText("");
        pollingFreq.setText("");
        moveToPattern.setText("");
        moveToDirectory.setText("");
        deleteAfterReadNo.setSelected(true);
        checkFileAgeNo.setSelected(true);
        fileAge.setText("");

    }
    
    public Properties getDefaults()
    {
        Properties properties = new Properties();
        properties.put(DATATYPE, name);
        properties.put(FILE_DIRECTORY, "");
        properties.put(FILE_POLLING_FREQUENCY, "1000");
        properties.put(FILE_MOVE_TO_PATTERN, "");
        properties.put(FILE_MOVE_TO_DIRECTORY, "");
        properties.put(FILE_DELETE_AFTER_READ, "NO");
        properties.put(FILE_CHECK_FILE_AGE, "NO");
        properties.put(FILE_FILE_AGE, "0");
        return properties;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        directoryField = new com.webreach.mirth.client.ui.MirthTextField();
        pollingFreq = new com.webreach.mirth.client.ui.MirthTextField();
        jLabel4 = new javax.swing.JLabel();
        moveToPattern = new com.webreach.mirth.client.ui.MirthTextField();
        moveToDirectory = new com.webreach.mirth.client.ui.MirthTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        fileAgeLabel = new javax.swing.JLabel();
        deleteAfterReadYes = new com.webreach.mirth.client.ui.MirthRadioButton();
        deleteAfterReadNo = new com.webreach.mirth.client.ui.MirthRadioButton();
        checkFileAgeYes = new com.webreach.mirth.client.ui.MirthRadioButton();
        checkFileAgeNo = new com.webreach.mirth.client.ui.MirthRadioButton();
        fileAge = new com.webreach.mirth.client.ui.MirthTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "File Reader", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0)));
        jLabel1.setText("Directory to read:");

        jLabel2.setText("Polling Frequency:");

        jLabel4.setText("Move-to Pattern:");

        jLabel5.setText("Move-to Directory:");

        jLabel6.setText("Delete File After Read: ");

        jLabel7.setText("Check File Age:");

        fileAgeLabel.setText("File Age:");

        deleteAfterReadYes.setBackground(new java.awt.Color(255, 255, 255));
        deleteAfterReadYes.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonGroup1.add(deleteAfterReadYes);
        deleteAfterReadYes.setText("Yes");
        deleteAfterReadYes.setMargin(new java.awt.Insets(0, 0, 0, 0));

        deleteAfterReadNo.setBackground(new java.awt.Color(255, 255, 255));
        deleteAfterReadNo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonGroup1.add(deleteAfterReadNo);
        deleteAfterReadNo.setSelected(true);
        deleteAfterReadNo.setText("No");
        deleteAfterReadNo.setMargin(new java.awt.Insets(0, 0, 0, 0));

        checkFileAgeYes.setBackground(new java.awt.Color(255, 255, 255));
        checkFileAgeYes.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonGroup2.add(checkFileAgeYes);
        checkFileAgeYes.setText("Yes");
        checkFileAgeYes.setMargin(new java.awt.Insets(0, 0, 0, 0));
        checkFileAgeYes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                checkFileAgeYesActionPerformed(evt);
            }
        });

        checkFileAgeNo.setBackground(new java.awt.Color(255, 255, 255));
        checkFileAgeNo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonGroup2.add(checkFileAgeNo);
        checkFileAgeNo.setSelected(true);
        checkFileAgeNo.setText("No");
        checkFileAgeNo.setMargin(new java.awt.Insets(0, 0, 0, 0));
        checkFileAgeNo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                checkFileAgeNoActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(fileAgeLabel)
                    .add(jLabel7)
                    .add(jLabel6)
                    .add(jLabel4)
                    .add(jLabel2)
                    .add(jLabel1)
                    .add(jLabel5))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, moveToDirectory, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, directoryField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, moveToPattern, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, pollingFreq, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(deleteAfterReadYes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(deleteAfterReadNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, fileAge, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, checkFileAgeYes, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(checkFileAgeNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(81, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(directoryField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(pollingFreq, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(moveToPattern, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(moveToDirectory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel6)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(deleteAfterReadYes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(deleteAfterReadNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(checkFileAgeYes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(checkFileAgeNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(fileAgeLabel)
                    .add(fileAge, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void checkFileAgeNoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_checkFileAgeNoActionPerformed
    {//GEN-HEADEREND:event_checkFileAgeNoActionPerformed
        fileAgeLabel.setEnabled(false);
        fileAge.setEnabled(false);
    }//GEN-LAST:event_checkFileAgeNoActionPerformed

    private void checkFileAgeYesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_checkFileAgeYesActionPerformed
    {//GEN-HEADEREND:event_checkFileAgeYesActionPerformed
        fileAgeLabel.setEnabled(true);
        fileAge.setEnabled(true);
    }//GEN-LAST:event_checkFileAgeYesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private com.webreach.mirth.client.ui.MirthRadioButton checkFileAgeNo;
    private com.webreach.mirth.client.ui.MirthRadioButton checkFileAgeYes;
    private com.webreach.mirth.client.ui.MirthRadioButton deleteAfterReadNo;
    private com.webreach.mirth.client.ui.MirthRadioButton deleteAfterReadYes;
    private com.webreach.mirth.client.ui.MirthTextField directoryField;
    private com.webreach.mirth.client.ui.MirthTextField fileAge;
    private javax.swing.JLabel fileAgeLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private com.webreach.mirth.client.ui.MirthTextField moveToDirectory;
    private com.webreach.mirth.client.ui.MirthTextField moveToPattern;
    private com.webreach.mirth.client.ui.MirthTextField pollingFreq;
    // End of variables declaration//GEN-END:variables

}
