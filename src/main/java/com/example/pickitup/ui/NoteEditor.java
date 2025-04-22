/**
 * Used by NotesPane to make scroll panes and text areas
 * inside pages
 *
 * @author CJ Quintero
 * @date 04/12/2025
 */
package com.example.pickitup.ui;


import javax.swing.*;
import com.example.pickitup.services.models.Note;

import java.awt.*;


public class NoteEditor extends JTabbedPane
{
    // fields
    private JTextArea textArea = null;
    private JScrollPane scrollPane = null;
    private Note noteItem = null;
    private ThemeManager themeManager = ThemeManager.getInstance();


    // constructor
    public NoteEditor(String title, int journal_id)
    {
        textArea = new JTextArea();
        textArea.setText("Begin writing here: ");
        textArea.setCaretPosition(textArea.getText().length());
        scrollPane = new JScrollPane(textArea);
        makeScrollPane();
        //creates a data object for storing the note into the database
        noteItem = new Note(title, getTextInTextEditor(), journal_id);
    }

    public NoteEditor(int noteID, int journalID, String title, String content, String fontType, int fontSize, int textColor, int backgroundColor) {
        textArea = new JTextArea();
        textArea.setText(content);
        textArea.setFont(new Font(fontType, Font.PLAIN, fontSize));
        textArea.setForeground(new Color(textColor, true));
        textArea.setBackground(new Color(backgroundColor, true));
        themeManager.setCustomForegroundColor(textArea, new Color(textColor, true));
        themeManager.setCustomBackgroundColor(textArea, new Color(backgroundColor, true));
        scrollPane = new JScrollPane(textArea);
        makeScrollPane();
        noteItem = new Note(noteID, journalID, title, content, fontType, fontSize, textColor, backgroundColor);
    }

    // makes the scroll pane
    // the text area is part of the scroll pane
    public void makeScrollPane()
    {
        // allows the scroll bar to always be on screen
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // allows the word and text to wrap around to the next line
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

    } // end addScrollPane


    // get the scroll pane to add it to the main panel
    public JScrollPane getScrollPane()
    {
        return scrollPane;
    } // end getScrollPane

    //get all  the text in the text area
    public String getTextInTextEditor()
    {
        return textArea.getText();
    } // end getText

    //Get text area itself
    public JTextArea getTextArea() {
        return textArea;
    }

    //get noteItem
    public Note getNoteItem() {
        return noteItem;
    }

} // end Notepad class