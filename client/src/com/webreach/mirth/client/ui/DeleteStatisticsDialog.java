/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mirth.
 *
 * The Initial Developer of the Original Code is
 * WebReach, Inc.
 * Portions created by the Initial Developer are Copyright (C) 2006
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *   Gerald Bortis <geraldb@webreachinc.com>
 *
 * ***** END LICENSE BLOCK ***** */

package com.webreach.mirth.client.ui;

import java.awt.Dimension;
import java.awt.Point;

/** Creates the About Mirth dialog. The content is loaded from about.txt. */
public class DeleteStatisticsDialog extends javax.swing.JDialog
{
    private Frame parent;
    private int statusToClear;
    private boolean clearAll;
    /**
     * Creates new form ViewContentDialog
     */
    public DeleteStatisticsDialog(int statusToClear, boolean clearAll)
    {
        super(PlatformUI.MIRTH_FRAME);
        this.parent = PlatformUI.MIRTH_FRAME;
        this.statusToClear = statusToClear;
        this.clearAll = clearAll;
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        pack();
        Dimension dlgSize = getPreferredSize();
        Dimension frmSize = parent.getSize();
        Point loc = parent.getLocation();
        setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        jPanel1 = new javax.swing.JPanel();
        invertButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        deleteReceived = new javax.swing.JCheckBox();
        deleteFiltered = new javax.swing.JCheckBox();
        deleteQueued = new javax.swing.JCheckBox();
        deleteSent = new javax.swing.JCheckBox();
        deleteErrored = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Clear Statistics");
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        invertButton.setText("Invert Selection");
        invertButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                invertButtonActionPerformed(evt);
            }
        });

        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                okButtonActionPerformed(evt);
            }
        });

        deleteReceived.setText("Received");
        deleteReceived.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        deleteReceived.setMargin(new java.awt.Insets(0, 0, 0, 0));

        deleteFiltered.setText("Filtered");
        deleteFiltered.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        deleteFiltered.setMargin(new java.awt.Insets(0, 0, 0, 0));

        deleteQueued.setText("Queued");
        deleteQueued.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        deleteQueued.setMargin(new java.awt.Insets(0, 0, 0, 0));

        deleteSent.setText("Sent");
        deleteSent.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        deleteSent.setMargin(new java.awt.Insets(0, 0, 0, 0));

        deleteErrored.setText("Errored");
        deleteErrored.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        deleteErrored.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTextPane1.setBackground(new java.awt.Color(226, 226, 226));
        jTextPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTextPane1.setEditable(false);
        jTextPane1.setText("Please select the statistics that you would like to reset:");
        jTextPane1.setAutoscrolls(false);
        jTextPane1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jTextPane1.setEnabled(false);
        jTextPane1.setFocusable(false);
        jTextPane1.setOpaque(false);
        jScrollPane1.setViewportView(jTextPane1);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(21, 21, 21)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, deleteFiltered)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, deleteReceived)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, deleteQueued)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, deleteSent)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, deleteErrored))
                        .add(71, 71, 71))
                    .add(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(invertButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(okButton)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(deleteReceived)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(deleteFiltered)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(deleteQueued)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(deleteSent)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(deleteErrored)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 16, Short.MAX_VALUE)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(invertButton)
                    .add(okButton))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void invertButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_invertButtonActionPerformed
    {//GEN-HEADEREND:event_invertButtonActionPerformed
        deleteReceived.setSelected(!deleteReceived.isSelected());
        deleteFiltered.setSelected(!deleteFiltered.isSelected());
        deleteQueued.setSelected(!deleteQueued.isSelected());
        deleteSent.setSelected(!deleteSent.isSelected());
        deleteErrored.setSelected(!deleteErrored.isSelected());
    }//GEN-LAST:event_invertButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_okButtonActionPerformed
    {//GEN-HEADEREND:event_okButtonActionPerformed
        if(!clearAll && (deleteReceived.isSelected() || deleteFiltered.isSelected() || deleteQueued.isSelected() || deleteSent.isSelected() || deleteErrored.isSelected()))
            parent.clearStats(statusToClear, deleteReceived.isSelected(), deleteFiltered.isSelected(), deleteQueued.isSelected(), deleteSent.isSelected(), deleteErrored.isSelected());
        else if(clearAll && (deleteReceived.isSelected() || deleteFiltered.isSelected() || deleteQueued.isSelected() || deleteSent.isSelected() || deleteErrored.isSelected()))
            parent.clearStatsAllChannels(deleteReceived.isSelected(), deleteFiltered.isSelected(), deleteQueued.isSelected(), deleteSent.isSelected(), deleteErrored.isSelected());
        
        this.dispose();
    }//GEN-LAST:event_okButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox deleteErrored;
    private javax.swing.JCheckBox deleteFiltered;
    private javax.swing.JCheckBox deleteQueued;
    private javax.swing.JCheckBox deleteReceived;
    private javax.swing.JCheckBox deleteSent;
    private javax.swing.JButton invertButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables

}
