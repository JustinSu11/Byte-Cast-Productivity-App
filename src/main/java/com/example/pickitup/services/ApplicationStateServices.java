/**
 * Saves the app state before exiting and loads the app state
 * where it was left at before exiting
 *
 * @author Justin Nguyen
 * @date 04/23/2025
 */

package com.example.pickitup.services;

import com.example.pickitup.services.database.DatabaseConnection;
import com.example.pickitup.ui.JournalsPane;
import com.example.pickitup.ui.NoteEditor;
import com.example.pickitup.ui.NotesPane;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ApplicationStateServices {
    //This method saves the state of the application on exit for reload upon opening
    public static void saveApplicationState() {
        try (Connection connection = DatabaseConnection.connect()) {
            //Disable auto commit
            connection.setAutoCommit(false);
            //sql statements for saving
            String saveJournalStatement = "UPDATE journals SET title = ?, selected_note_index = ?, selected_flag = ?, journal_order = ? WHERE journal_id = ?";
            String saveNoteStatement = "UPDATE notes SET title = ?, content = ?, note_order = ?, font_type = ?, font_size = ?, text_color = ?, background_color = ? WHERE journal_id = ? AND notes_id = ?";
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
                    String fontType = JournalsPane.getNotesPanes().get(i).getNoteEditors().get(j).getNoteItem().getFontType();
                    int fontSize = JournalsPane.getNotesPanes().get(i).getNoteEditors().get(j).getNoteItem().getFontSize();
                    int textColor = JournalsPane.getNotesPanes().get(i).getNoteEditors().get(j).getNoteItem().getTextColor().getRGB();
                    int backgroundColor = JournalsPane.getNotesPanes().get(i).getNoteEditors().get(j).getNoteItem().getBackgroundColor().getRGB();

                    saveNote.setString(1, noteTitle);
                    saveNote.setString(2, noteContent);
                    saveNote.setInt(3, j);
                    saveNote.setString(4, fontType);
                    saveNote.setInt(5, fontSize);
                    saveNote.setInt(6, textColor);
                    saveNote.setInt(7, backgroundColor);
                    saveNote.setInt(8, journalID);
                    saveNote.setInt(9, noteID);
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
            //statement for loading journal
            String loadJournalStatement = "SELECT journal_id, title, selected_note_index, selected_flag FROM journals ORDER BY journal_order";
            Statement loadJournal = connection.createStatement();
            ResultSet journalResultSet = loadJournal.executeQuery(loadJournalStatement);
            //make the container for holding journals
            JTabbedPane journalsOutterPane = JournalsPane.getJournalsPane();
            //To re-select the journal that was selected before closing
            int tabToSelect = -1;
            int currentTabIndex = 0;

            //create a NotesPane for each journal to hold its notes and load the notes in the same order before the application closed
            while (journalResultSet.next()) {
                int journalID = journalResultSet.getInt("journal_id");
                String journalTitle = journalResultSet.getString("title");
                int selectedNoteIndex = journalResultSet.getInt("selected_note_index");
                boolean isSelectedJournal = journalResultSet.getBoolean("selected_flag");
                //create a journal (NotesPane)
                NotesPane journalPane = new NotesPane(journalID, journalTitle);
                //add to the journal container
                JournalsPane.getNotesPanes().add(journalPane);
                //load the notes for each journal
                String loadNoteStatement = "SELECT notes_id, content, title, font_type, font_size, text_color, background_color FROM notes WHERE journal_id = " + journalID + " ORDER BY note_order";
                Statement loadNote = connection.createStatement();
                ResultSet noteResultSet = loadNote.executeQuery(loadNoteStatement);

                while (noteResultSet.next()) {
                    int noteID = noteResultSet.getInt("notes_id");
                    String noteContent = noteResultSet.getString("content");
                    String noteTitle = noteResultSet.getString("title");
                    String fontType = noteResultSet.getString("font_type");
                    int fontSize = noteResultSet.getInt("font_size");
                    int textColor = noteResultSet.getInt("text_color");
                    int backgroundColor = noteResultSet.getInt("background_color");
                    //create a note (NoteEditor) and set all of its items
                    NoteEditor newNoteEditor = new NoteEditor(noteID, journalID, noteTitle, noteContent, fontType, fontSize, textColor, backgroundColor);
                    //add it to the journal (NotesPane)
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
            //set the selectedindex for the selected journal
            if(tabToSelect != -1) {
                journalsOutterPane.setSelectedIndex(tabToSelect);
            }
        } catch (SQLException error) {
            System.out.println("Error loading last save: " + error.getMessage());
        }
    }

}
