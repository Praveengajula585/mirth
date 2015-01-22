/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package org.fife.ui.rsyntaxtextarea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.event.UndoableEditEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class EOLPreservingRSyntaxDocument extends RSyntaxDocument {

    private static final char[] CR = new char[] { '\r' };
    private static final char[] LF = new char[] { '\n' };
    private static final char[] CRLF = new char[] { '\r', '\n' };

    private boolean replaceControlCharacters;
    private TreeMap<Integer, char[]> eolMap = new TreeMap<Integer, char[]>();
    private UndoableEdit lastEdit = null;

    public EOLPreservingRSyntaxDocument(String syntaxStyle) {
        this(syntaxStyle, true);
    }

    public EOLPreservingRSyntaxDocument(String syntaxStyle, boolean replaceControlCharacters) {
        super(syntaxStyle);
        this.replaceControlCharacters = replaceControlCharacters;
    }

    public EOLPreservingRSyntaxDocument(TokenMakerFactory tmf, String syntaxStyle, boolean replaceControlCharacters) {
        super(tmf, syntaxStyle);
        this.replaceControlCharacters = replaceControlCharacters;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        lastEdit = null;
        if (StringUtils.isEmpty(str)) {
            return;
        }

        PeekReader reader = null;
        CompoundEdit edit = new CompoundEdit();

        try {
            reader = new PeekReader(new StringReader(str));
            StringBuilder builder = new StringBuilder();
            TreeMap<Integer, char[]> tempMap = new TreeMap<Integer, char[]>();
            char[] buff = new char[1024];
            int nch;
            int cOffset = offset;
            boolean wasCR = false;

            while ((nch = reader.read(buff, 0, buff.length)) != -1) {
                for (int i = 0; i < nch; i++) {
                    char c = buff[i];

                    if (c == '\r') {
                        boolean updated = false;
                        if (i == nch - 1 && !reader.peek() && Arrays.equals(eolMap.get(offset), LF)) {
                            edit.addEdit(new ChangeEOLEdit(offset, CRLF));
                            eolMap.put(offset, CRLF);
                            updated = true;
                        }

                        if (!updated && (wasCR || i == nch - 1 && !reader.peek())) {
                            // Insert CR
                            tempMap.put(cOffset++, CR);
                            builder.append(LF);
                        }

                        wasCR = true;
                    } else if (c == '\n') {
                        boolean updated = false;
                        if (cOffset == offset) {
                            if (Arrays.equals(eolMap.get(offset - 1), CR)) {
                                edit.addEdit(new ChangeEOLEdit(offset - 1, CRLF));
                                eolMap.put(offset - 1, CRLF);
                                updated = true;
                            }
                        }

                        if (!updated) {
                            if (wasCR) {
                                // Insert CRLF
                                tempMap.put(cOffset++, CRLF);
                                builder.append(LF);
                            } else {
                                // Insert LF
                                tempMap.put(cOffset++, LF);
                                builder.append(LF);
                            }
                        }

                        wasCR = false;
                    } else if (replaceControlCharacters && c != '\t' && (c < ' ' || c == 0x7F)) {
                        // Insert control character
                        cOffset++;
                        builder.append((char) (c == 0x7F ? '\u2421' : '\u2400' + c));
                        wasCR = false;
                    } else {
                        if (wasCR) {
                            // Insert previous CR
                            tempMap.put(cOffset++, CR);
                            builder.append(LF);
                        }

                        // Insert regular character
                        cOffset++;
                        builder.append(c);
                        wasCR = false;
                    }
                }
            }

            str = builder.toString();

            Integer key = eolMap.isEmpty() ? null : eolMap.lastKey();
            while (key != null && key >= offset) {
                edit.addEdit(new ChangeEOLEdit(key, null));
                char[] eol = eolMap.remove(key);

                int newKey = key + str.length();
                edit.addEdit(new ChangeEOLEdit(newKey, eol));
                eolMap.put(newKey, eol);

                key = eolMap.lowerKey(key);
            }

            for (Entry<Integer, char[]> entry : tempMap.entrySet()) {
                edit.addEdit(new ChangeEOLEdit(entry.getKey(), entry.getValue()));
            }
            eolMap.putAll(tempMap);
        } catch (IOException e) {
            // Only using a StringReader, so should not happen
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }

        if (edit != null) {
            edit.end();
            lastEdit = edit;
        }
        super.insertString(offset, str, a);
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        lastEdit = null;

        // Combine edge CRs and LFs if necessary
        if (Arrays.equals(eolMap.get(offs - 1), CR) && Arrays.equals(eolMap.get(offs + len), LF)) {
            eolMap.put(offs - 1, CRLF);
            len++;
        }

        super.remove(offs, len);

        for (Integer offset : eolMap.keySet().toArray(new Integer[eolMap.size()])) {
            if (offset >= offs) {
                char[] eol = eolMap.remove(offset);
                if (offset >= offs + len) {
                    eolMap.put(offset - len, eol);
                }
            }
        }
    }

    @Override
    public void replace(int offset, int length, String text, AttributeSet a) throws BadLocationException {
        remove(offset, length);
        insertString(offset, text, a);
    }

    public char[] getEOL(int line) {
        int i = 0;
        for (char[] eol : eolMap.values()) {
            if (i == line) {
                return eol;
            }
            i++;
        }
        return null;
    }

    @Override
    public String getText(int offset, int length) throws BadLocationException {
        if (replaceControlCharacters) {
            try {
                StringBuilder builder = new StringBuilder();
                Reader reader = new StringReader(super.getText(offset, length));
                char[] buff = new char[1024];
                int nch;

                while ((nch = reader.read(buff, 0, buff.length)) != -1) {
                    for (int i = 0; i < nch; i++) {
                        if (buff[i] >= '\u2400' && buff[i] <= '\u241F') {
                            buff[i] = (char) (buff[i] - '\u2400');
                        } else if (buff[i] == '\u2421') {
                            buff[i] = 0x7F;
                        }
                    }

                    builder.append(buff, 0, nch);
                }

                return builder.toString();
            } catch (IOException e) {
                // Only using a StringReader, so should not happen
            }
        }

        return super.getText(offset, length);
    }

    public String getEOLFixedText(int offset, int length) throws BadLocationException {
        String text = getText(offset, length);
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char[] eol = eolMap.get(offset + i);
            if (ArrayUtils.isNotEmpty(eol)) {
                builder.append(eol);
            } else {
                builder.append(text.charAt(i));
            }
        }

        return builder.toString();
    }

    private class PeekReader extends BufferedReader {

        public PeekReader(Reader in) {
            super(in);
        }

        public boolean peek() throws IOException {
            mark(1);
            try {
                return read() != -1;
            } finally {
                reset();
            }
        }
    }

    @Override
    protected void fireUndoableEditUpdate(UndoableEditEvent evt) {
        if (lastEdit != null) {
            CompoundEdit edit = new CompoundEdit();
            edit.addEdit(evt.getEdit());
            edit.addEdit(lastEdit);
            edit.end();
            super.fireUndoableEditUpdate(new UndoableEditEvent(evt.getSource(), edit));
            lastEdit = null;
        } else {
            super.fireUndoableEditUpdate(evt);
        }
    }

    private class ChangeEOLEdit implements UndoableEdit {

        private int offset;
        private char[] oldEOL;
        private char[] newEOL;

        public ChangeEOLEdit(int offset, char[] newEOL) {
            this.offset = offset;
            this.oldEOL = eolMap.get(offset);
            this.newEOL = newEOL;
        }

        @Override
        public void undo() throws CannotUndoException {
            if (oldEOL != null) {
                eolMap.put(offset, oldEOL);
            } else {
                eolMap.remove(offset);
            }
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void redo() throws CannotRedoException {
            if (newEOL != null) {
                eolMap.put(offset, newEOL);
            } else {
                eolMap.remove(offset);
            }
        }

        @Override
        public boolean canRedo() {
            return true;
        }

        @Override
        public void die() {}

        @Override
        public boolean addEdit(UndoableEdit anEdit) {
            return false;
        }

        @Override
        public boolean replaceEdit(UndoableEdit anEdit) {
            return false;
        }

        @Override
        public boolean isSignificant() {
            return true;
        }

        @Override
        public String getPresentationName() {
            return null;
        }

        @Override
        public String getUndoPresentationName() {
            return null;
        }

        @Override
        public String getRedoPresentationName() {
            return null;
        }
    }
}