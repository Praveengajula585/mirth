/*
 * GlobalScriptsPanel.java
 *
 * Created on July 12, 2007, 11:51 AM
 */

package com.webreach.mirth.client.ui;

import java.util.Iterator;
import java.util.Map;

import com.webreach.mirth.client.core.ClientException;
import com.webreach.mirth.model.CodeTemplate.ContextType;

/**
 *
 * @author  brendanh
 */
public class GlobalScriptsPanel extends javax.swing.JPanel
{
    Frame parent;
    /**
     * Creates new form GlobalScriptsPanel
     */
    public GlobalScriptsPanel()
    {
        parent = PlatformUI.MIRTH_FRAME;
        initComponents();
    }
    
    public void edit()
    {
        try
        {           
            scriptPanel.setScripts(parent.mirthClient.getGlobalScripts());
        }
        catch(ClientException e)
        {
            parent.alertException(e.getStackTrace(), e.getMessage());
        }
    }
    
    public void validateCurrentScript()
    {
        scriptPanel.validateCurrentScript();
    }
    
    public String validateAllScripts()
    {
    	Map<String, String> scripts = scriptPanel.getScripts();   	
    	String errors = "";
    	
    	Iterator it = scriptPanel.getScripts().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry)it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            
            String validationMessage = scriptPanel.validateScript(value);
            if (validationMessage != null)
        		errors += "Error in global script \"" + key + "\":\n" + validationMessage + "\n\n";
        }
        
        if (errors.equals(""))
        	errors = null;
        
        return errors;        
    }
    
    public void importAllScripts(Map<String, String> scripts)
    {
    	scriptPanel.setScripts(scripts);
    	parent.enableSave();
    }
    
    public Map<String, String> exportAllScripts()
    {
    	return scriptPanel.getScripts();
    }
    
    public void save()
    {
    	String validationMessage = validateAllScripts();
        if (validationMessage != null)
        {
        	parent.alertCustomError(validationMessage, CustomErrorDialog.ERROR_VALIDATING_GLOBAL_SCRIPTS);
        }
        
        try
        {
            parent.mirthClient.setGlobalScripts(scriptPanel.getScripts());
        }
        catch(ClientException e)
        {
            parent.alertException(e.getStackTrace(), e.getMessage());
        }
    }
            
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        scriptPanel = new ScriptPanel(ContextType.GLOBAL_CONTEXT.getContext());

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scriptPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scriptPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.webreach.mirth.client.ui.ScriptPanel scriptPanel;
    // End of variables declaration//GEN-END:variables
    
}
