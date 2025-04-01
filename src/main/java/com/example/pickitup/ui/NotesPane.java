/*
    *******************************************************************************
    TabbedPane Class
    Last Updated 03/26/2025
    Developer CJ Quintero

    This class makes the tabbed pane and has methods to add or delete tabs.

    Please remember to update the version date if any changes
    are made to this file.
    *******************************************************************************
*/
package com.example.pickitup.ui;

import javax.swing.*;
import java.awt.*;
import com.example.pickitup.services.dao.NotesDAO;
import com.example.pickitup.services.models.Note;

public class NotesPane extends JournalsPane
{
    // fields
    private JTabbedPane tabbedPane = null;
    private String title = null;
    private NoteEditor noteEditor = null;
    private int selectedIndex = 0;
    private final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 16); // constant


    // constructor
    public NotesPane()
    {
        // initialize the variables
        tabbedPane = new JTabbedPane();
        noteEditor = new NoteEditor();

        // set the default font
        tabbedPane.setFont(DEFAULT_FONT);
    }

    // makes a single tab as an example
    public void makeNotesPane()
    {
        // sets the tab name, makes the scroll pane (text area)
        title = "Page " + (tabbedPane.getTabCount() + 1);
        noteEditor.makeScrollPane();
        //inserts blank note into database (commented out due to missing journal tabs)
//        Note currentNote = new Note(title, noteEditor.getTextInTextEditor());
//        NotesDAO.insertNote(currentNote);
        // adds the scroll pane to the new tab
        tabbedPane.addTab(title, noteEditor.getScrollPane());
    }


    // method to add a new tab to the tabbed pane
    public void addTab()
    {
        // sets the tab name, makes the scroll pane (text area)
        title = "Page " + (tabbedPane.getTabCount() + 1);
        NoteEditor newNoteEditor = new NoteEditor();
        newNoteEditor.makeScrollPane();
        //inserts blank note into database (commented out due to missing journal tabs)
//        Note currentNote = new Note(title, newNoteEditor.getTextInTextEditor());
//        NotesDAO.insertNote(currentNote);
        // adds the scroll pane to the new tab
        tabbedPane.addTab(title, newNoteEditor.getScrollPane());
    }


    // method to remove tabs
    public void deleteTab()
    {
        // get the index of the selected tab
        selectedIndex = tabbedPane.getSelectedIndex();

        // delete the selected tab
        if(tabbedPane.getTabCount() > 0)
        {
            tabbedPane.removeTabAt(tabbedPane.getSelectedIndex());
        }
    }


    // returns the notes pane to the JournalsPane class
    public JTabbedPane getTabbedPane()
    {
        return tabbedPane;
    } // end getTabbedPane

} // end class
