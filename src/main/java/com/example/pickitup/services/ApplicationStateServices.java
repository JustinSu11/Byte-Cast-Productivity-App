/*
* Author: Justin Nguyen
* Version 1.0
* Purpose: Saves the application state before exiting and loads the application state
* where it was left at before exiting
* */

package com.example.pickitup.services;

import com.example.pickitup.services.database.DatabaseConnection;
import com.example.pickitup.ui.JournalsPane;
import com.example.pickitup.ui.NoteEditor;
import com.example.pickitup.ui.NotesPane;

import javax.swing.*;
import java.sql.*;

public class ApplicationStateServices {
    //This method saves the state of the application on exit for reload upon opening
    public static void saveApplicationState() {
        try (Connection connection = DatabaseConnection.connect()) {
            //Disable auto commit
            connection.setAutoCommit(false);
            //sql statements for saving
            String saveJournalStatement = "UPDATE journals SET title = ?, selected_note_index = ?, selected_flag = ?, journal_order = ? WHERE journal_id = ?";
            String saveNoteStatement = "UPDATE notes SET title = ?, content = ?, note_order = ? WHERE journal_id = ? AND notes_id = ?";
            //prepare statements for execution
            PreparedStatement saveJournal = connection.prepareStatement(saveJournalStatement);
            PreparedStatement saveNote = connection.prepareStatement(saveNoteStatement);

            JTabbedPane journalsPane = JournalsPane.getJournalsPane();

            //iterate through each journal (NotePane) and save each journal and note
            for (int i = 0; i < JournalsPane.getJournalsPane().getTabCount(); i++) {

                boolean isSelected = (journalsPane.getSelectedComponent() == journalsPane.getComponentAt(i));
                String journalTitle = journalsPane.getTitleAt(i);
                int journalID = JournalsPane.getNotesPanes().get(i).getJournalIDFromNotesPane();
                int selectedNoteIndex = JournalsPane.getNotesPanes().get(i).getSelectedNoteIndex();

                saveJournal.setString(1, journalTitle);
                saveJournal.setInt(2, selectedNoteIndex);
                saveJournal.setBoolean(3, isSelected);
                saveJournal.setInt(4, i);
                saveJournal.setInt(5, journalID);
                saveJournal.executeUpdate();



                //iterate through each note (NoteEditor) in this journal (NotePane) and save into the database
                for (int j = 0; j < JournalsPane.getNotesPanes().get(i).getNoteEditors().size(); j++) {
                    NoteEditor noteTextArea = JournalsPane.getNotesPanes().get(i).getNoteEditors().get(j);
                    String noteContent = noteTextArea.getTextInTextEditor();
                    String noteTitle = JournalsPane.getNotesPanes().get(i).getNoteEditors().get(j).getNoteItem().getTitle();
                    int noteID = JournalsPane.getNotesPanes().get(i).getNoteEditors().get(j).getNoteItem().getNoteID();

                    saveNote.setString(1, noteTitle);
                    saveNote.setString(2, noteContent);
                    saveNote.setInt(3, j);
                    saveNote.setInt(4, journalID);
                    saveNote.setInt(5, noteID);
                    saveNote.executeUpdate();
                    System.out.println("Saving note content");
                }
            }
            //One single commit for each journal instead of each note
            connection.commit();
        } catch (SQLException error) {
            System.out.println("Error with save before exit: " + error.getMessage());
        }
    }

    //load application from the last save
    public static void loadApplicationState() {
        try (Connection connection = DatabaseConnection.connect()) {
            String loadJournalStatement = "SELECT journal_id, title, selected_note_index, selected_flag FROM journals ORDER BY journal_order";
            Statement loadJournal = connection.createStatement();
            ResultSet journalResultSet = loadJournal.executeQuery(loadJournalStatement);

            JTabbedPane journalsOutterPane = JournalsPane.getJournalsPane();
            int tabToSelect = -1;
            int currentTabIndex = 0;

            while (journalResultSet.next()) {
                int journalID = journalResultSet.getInt("journal_id");
                String journalTitle = journalResultSet.getString("title");
                int selectedNoteIndex = journalResultSet.getInt("selected_note_index");
                boolean isSelectedJournal = journalResultSet.getBoolean("selected_flag");

                NotesPane journalPane = new NotesPane(journalID, journalTitle);
                JournalsPane.getNotesPanes().add(journalPane);

                String loadNoteStatement = "SELECT notes_id, content, title FROM notes WHERE journal_id = " + journalID + " ORDER BY note_order";
                Statement loadNote = connection.createStatement();
                ResultSet noteResultSet = loadNote.executeQuery(loadNoteStatement);

                while (noteResultSet.next()) {
                    int noteID = noteResultSet.getInt("notes_id");
                    String noteContent = noteResultSet.getString("content");
                    String noteTitle = noteResultSet.getString("title");
                    NoteEditor newNoteEditor = new NoteEditor(noteID, journalID, noteTitle, noteContent);

                    newNoteEditor.makeScrollPane();
                    newNoteEditor.getTextArea().setText(noteContent);
                    journalPane.addPageTabForLoad(newNoteEditor);
                }

                //Add journal to journal pane
                journalsOutterPane.addTab(journalTitle, journalPane.getTabbedPane());

                //select the selected note before the last exit
                journalPane.getTabbedPane().setSelectedIndex(selectedNoteIndex);

                if(isSelectedJournal) {
                    tabToSelect = currentTabIndex;
                }
                currentTabIndex++;
            }
            if(tabToSelect != -1) {
                journalsOutterPane.setSelectedIndex(tabToSelect);
            }
        } catch (SQLException error) {
            System.out.println("Error loading last save: " + error.getMessage());
        }
    }

}
