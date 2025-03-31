/*
    *******************************************************************************
    ScrollPane Class
    Last Updated 03/26/2025
    Developer CJ Quintero

    This class is for making the scroll pane which holds the
    text area.

    Please remember to update the version date if any changes
    are made to this file.
    *******************************************************************************
*/
package com.example.pickitup.ui;


import javax.swing.*;


public class NoteEditor
{
    // fields
    private JTextArea textArea = null;
    private JScrollPane scrollPane = null;


    // constructor
    public NoteEditor()
    {
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
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

        // write a line to the text area to guide users
        textArea.setText("Begin writing here: ");

        // set the "cursor line" to appear after the example text
        textArea.setCaretPosition(20);
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

} // end Notepad class