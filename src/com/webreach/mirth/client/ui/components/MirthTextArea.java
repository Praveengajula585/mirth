package com.webreach.mirth.client.ui.components;

import javax.swing.JPopupMenu;

import com.webreach.mirth.client.ui.Frame;
import com.webreach.mirth.client.ui.PlatformUI;
import com.webreach.mirth.client.ui.actions.CopyAction;
import com.webreach.mirth.client.ui.actions.CutAction;
import com.webreach.mirth.client.ui.actions.DeleteAction;
import com.webreach.mirth.client.ui.actions.PasteAction;
import com.webreach.mirth.client.ui.actions.SelectAllAction;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;

/** 
 * Mirth's implementation of the JTextArea.  Adds enabling of
 * the save button in parent.  Also adds a trigger button (right click)
 * editor menu with Cut, Copy, Paste, Delete, and Select All.
 */
public class MirthTextArea extends javax.swing.JTextArea
{
    private Frame parent;
    private JPopupMenu menu;
    private CutAction cutAction;
    private CopyAction copyAction;
    private PasteAction pasteAction;
    private DeleteAction deleteAction;
    private SelectAllAction selectAllAction;

    public MirthTextArea()
    {
        super();
        this.parent = PlatformUI.MIRTH_FRAME;
        
        cutAction = new CutAction(this);
        copyAction = new CopyAction(this);
        pasteAction = new PasteAction(this);
        deleteAction = new DeleteAction(this);
        selectAllAction = new SelectAllAction(this);
        
        menu = new JPopupMenu(); 
        menu.add(cutAction); 
        menu.add(copyAction); 
        menu.add(pasteAction); 
        menu.add(deleteAction); 
        menu.addSeparator();
        menu.add(selectAllAction);

        this.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                showPopupMenu(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                showPopupMenu(evt);
            }
        });
    }
    
    /**
     * Shows the popup menu for the trigger button
     */
    private void showPopupMenu(java.awt.event.MouseEvent evt)
    {
        if (evt.isPopupTrigger())
        {
            menu.getComponent(0).setEnabled(cutAction.isEnabled());
            menu.getComponent(1).setEnabled(copyAction.isEnabled());
            menu.getComponent(2).setEnabled(pasteAction.isEnabled());
            menu.getComponent(3).setEnabled(deleteAction.isEnabled());
            menu.getComponent(5).setEnabled(selectAllAction.isEnabled());
            
            menu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }
    
    /**
     * Overrides setDocument(Document doc) so that a document listener
     * is added to the current document to listen for changes.
     */
    public void setDocument(Document doc)
    {
        super.setDocument(doc);
        
        this.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
            }
            public void removeUpdate(DocumentEvent e) {
                parent.enableSave();
            }
            public void insertUpdate(DocumentEvent e) {
                parent.enableSave();
            }
        });
    }
    
    /**
     * Overrides setText(String t) so that the save button is
     * disabled when Mirth sets the text of a field.
     */
    public void setText(String t)
    {
    	//super.write(new Tex)
        super.setText(t.replaceAll("\\r ", "\\\r\\\n"));
        parent.disableSave();
    }
    public String getText(){
    	return super.getText().replaceAll("\\n",System.getProperty("line.separator"));
   }
}
