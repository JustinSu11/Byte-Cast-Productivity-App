package com.example.pickitup.ui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class JournalsPane extends JTabbedPane {
    private JTabbedPane journalsPane = null;
    private List<NotesPane> notesPanes = new ArrayList<>(); // Store NotesPanes
    private String title = null;
    private int selectedJournalIndex = 0;

    public JournalsPane() {
        journalsPane = new JTabbedPane();
    }

    public void makeJournalsPane() {
        title = "Journal " + (journalsPane.getTabCount() + 1);
        NotesPane notesPane = new NotesPane();
        notesPane.makeNotesPane();
        journalsPane.addTab(title, notesPane.getTabbedPane());
        notesPanes.add(notesPane); // Add the NotesPane to the list
    }

    public void addJournalTab() {
        title = "Journal " + (journalsPane.getTabCount() + 1);
        NotesPane newNotesPane = new NotesPane();
        newNotesPane.makeNotesPane();
        journalsPane.addTab(title, newNotesPane.getTabbedPane());
        notesPanes.add(newNotesPane); // Add the new NotesPane
    }

    public void deleteJournalTab() {
        selectedJournalIndex = journalsPane.getSelectedIndex();
        if (selectedJournalIndex >= 0) {
            journalsPane.removeTabAt(selectedJournalIndex);
            notesPanes.remove(selectedJournalIndex); // Remove the NotesPane
        }
    }

    public JTabbedPane getJournalsPane() {
        return journalsPane;
    }

    public NotesPane getSelectedNotesPane() {
        int selectedIndex = journalsPane.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < notesPanes.size()) {
            return notesPanes.get(selectedIndex);
        }
        return null; // Return null if no journal is selected or list is empty
    }
}
