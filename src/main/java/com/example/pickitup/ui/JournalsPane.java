package com.example.pickitup.ui;

import javax.swing.*;
import java.awt.*;
import com.example.pickitup.services.dao.JournalDAO;
import com.example.pickitup.services.models.Journal;

public class JournalsPane extends JTabbedPane {
    private JTabbedPane journalsPane = null;
    private NotesPane notesPane = null;
    private String title = null;
    private int selectedJournalIndex = 0;
    private int selectedNoteIndex = 0;

    public JournalsPane() {
        journalsPane = new JTabbedPane();
    }

    public NotesPane getNotesPane() {
        return notesPane;
    }

    public void makeJournalsPane() {
        title = "Journal " + (journalsPane.getTabCount() + 1);
        notesPane = new NotesPane();
        notesPane.makeNotesPane();
        journalsPane.addTab(title, notesPane.getTabbedPane());
    }

    public void addJournalTab() {
        title = "Journal " + (journalsPane.getTabCount() + 1);
        NotesPane newNotesPane = new NotesPane();
        newNotesPane.makeNotesPane();
        journalsPane.addTab(title, newNotesPane.getTabbedPane());
    }

    public void deleteJournalTab() {
        selectedJournalIndex = journalsPane.getSelectedIndex();
        if(selectedJournalIndex >= 0) {
            journalsPane.removeTabAt(selectedJournalIndex);
        }
    }

    public JTabbedPane getJournalsPane() {
        return journalsPane;
    }
}
