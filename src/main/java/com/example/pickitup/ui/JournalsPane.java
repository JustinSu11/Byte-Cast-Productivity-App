/**
 * Makes the panel and methods for the to do list
 *
 * @author CJ Quintero
 * @date 04/12/2025
 */
package com.example.pickitup.ui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class JournalsPane extends JTabbedPane
{
    private JTabbedPane journalsPane = null;

    // store a list of different notesPanes for the different journals
    private List<NotesPane> notesPanes = new ArrayList<>();
    private String title = null;
    private int selectedJournalIndex = 0;


    // constructor
    public JournalsPane()
    {
        journalsPane = new JTabbedPane();
    }


    //Replace with save loading if possible
    public void journalConstructor()
    {
        title = "Journal " + (journalsPane.getTabCount() + 1);
        NotesPane newNotesPane = new NotesPane();
        newNotesPane.notesPaneConstuctor();
        journalsPane.addTab(title, newNotesPane.getTabbedPane());
        notesPanes.add(newNotesPane); // Add the new NotesPane
    }


    // this method is used by the file menu to add a journal tab
    public void addJournalTab()
    {
        //New name setter for tabs
        String title = JOptionPane.showInputDialog("Enter title for new journal");
        if (title == null || title.equals("")){
            title = "Journal " + (journalsPane.getTabCount() + 1);
            JOptionPane.showMessageDialog(
                    null,
                    "No name was provided!\nDefault title is: " + title,
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        NotesPane newNotesPane = new NotesPane();
        newNotesPane.addPageTab();
        journalsPane.addTab(title, newNotesPane.getTabbedPane());
        notesPanes.add(newNotesPane); // Add the new NotesPane
    }


    // used by file menu to delete a journal
    public void deleteJournalTab()
    {
        if(this.getSelectedNotesPane() != null)
        {
            int yesNo = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to delete this journal?",  // Updated message
                    "Delete Journal?",  // Updated title
                    JOptionPane.YES_NO_OPTION
            );
            if (yesNo == JOptionPane.YES_OPTION){
                selectedJournalIndex = journalsPane.getSelectedIndex();
                if (selectedJournalIndex >= 0)
                {
                    journalsPane.removeTabAt(selectedJournalIndex);
                    notesPanes.remove(selectedJournalIndex); // Remove the NotesPane
                }}
        }
        else
        {
            // Show a message if the user tries to delete a journal when no journals exist
            JOptionPane.showMessageDialog(null,
                    "Cannot delete journal: No journals exist.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // return a JTabbedPane object to App class to use on main panel
    public JTabbedPane getJournalsPane()
    {
        return journalsPane;
    }


    // method for updating the current notesPane variable stored in MenuBar
    // This tells the menu bar what journal you want to add or delete pages from
    public NotesPane getSelectedNotesPane()
    {
        int selectedIndex = journalsPane.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < notesPanes.size())
        {
            return notesPanes.get(selectedIndex);
        }
        return null; // Return null if no journal is selected or list is empty
    } // end getSelectedNotesPane

    public void setNewJournalName(String newName) {
        journalsPane.setTitleAt(journalsPane.getSelectedIndex(), newName);
    }

} // end class
