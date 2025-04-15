/*
    *******************************************************************************
    JournalsPane Class
    Updated 04/02/2025
    Developer CJ Quintero


    This class creates the outer tabbed pane for users to make journals.
    Each journal has its own instance of notesPane to allow multiple pages
    inside a single journal to be made.


    Please remember to update the version date if any changes
    are made to this file.
    *******************************************************************************
 */
package com.example.pickitup.ui;

import com.example.pickitup.services.ApplicationStateServices;
import com.example.pickitup.services.dao.JournalDAO;
import com.example.pickitup.services.models.Journal;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class JournalsPane extends JTabbedPane
{
    private static JTabbedPane journalsPane = new JTabbedPane();
    // store a list of different notesPanes for the different journals
    private static List<NotesPane> notesPanes = new ArrayList<>();
    private String title = null;
    private static int selectedJournalIndex = 0;


    // constructor
    public JournalsPane()
    {
        ApplicationStateServices.loadApplicationState();
        if (journalsPane.getTabCount() == 0) {
            title = "Journal " + (journalsPane.getTabCount() + 1);
            Journal journal = new Journal(title);
            NotesPane newNotesPane = new NotesPane(journal.getJournalID());
            journalsPane.addTab(title, newNotesPane.getTabbedPane());
            notesPanes.add(newNotesPane); // Add the new NotesPane if no tabs are present
        }
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
        Journal journal = new Journal(title);
        NotesPane newNotesPane = new NotesPane(journal.getJournalID());
        journalsPane.addTab(title, newNotesPane.getTabbedPane());
        notesPanes.add(newNotesPane); // Add the new NotesPane
    }


    // used by file menu to delete a journal
    public void deleteJournalTab()
    {
        int yesNo = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete this page!?",
                "Delete Page?",
                JOptionPane.YES_NO_OPTION
        );
        if (yesNo == JOptionPane.YES_OPTION){
            selectedJournalIndex = journalsPane.getSelectedIndex();
            if (selectedJournalIndex > 0)
            {
                journalsPane.removeTabAt(selectedJournalIndex);
                notesPanes.remove(selectedJournalIndex); // Remove the NotesPane
                JournalDAO.deleteJournal(notesPanes.get(selectedJournalIndex).getJournalIDFromNotesPane());
            } else System.out.println("Must have at least one journal.");
        }
    }


    // return a JTabbedPane object to App class to use on main panel
    public static JTabbedPane getJournalsPane()
    {
        return journalsPane;
    }

    //Get the notesPanes array list
    public static List<NotesPane> getNotesPanes() {
        return notesPanes;
    }

    public static int getSelectedJournalIndex() {
        return selectedJournalIndex;
    }

    // method for updating the current notesPane variable stored in MenuBar
    // This tells the menu bar what journal you want to add or delete pages from
    public static NotesPane getSelectedNotesPane()
    {
        int selectedIndex = journalsPane.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < notesPanes.size())
        {
            return notesPanes.get(selectedIndex);
        } else return null; // Return null if no journal is selected or list is empty
    } // end getSelectedNotesPane

    public String getTitleAt(int index) {
        return notesPanes.get(index).getTitle();
    }

    public void setNewJournalName(String newName) {
        journalsPane.setTitleAt(journalsPane.getSelectedIndex(), newName);
        JournalDAO.updateJournal(JournalsPane.getNotesPanes().get(journalsPane.getSelectedIndex()).getJournalIDFromNotesPane(), newName);
    }

} // end class
