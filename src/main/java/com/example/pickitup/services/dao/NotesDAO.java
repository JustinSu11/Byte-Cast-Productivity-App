/**
 * DAO for notes in database
 *
 * @author Justin Nguyen
 * @date 04/12/2025
 */
package com.example.pickitup.services.dao;

import com.example.pickitup.services.models.Journal;
import com.example.pickitup.services.models.Note;
import com.example.pickitup.services.database.*;
import com.example.pickitup.ui.JournalsPane;
import com.example.pickitup.ui.NoteEditor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotesDAO {
    //CREATE methods
    //method to insert note into SQLite database
    public static void insertNoteAtCreation(Note note) {
        String insertStatement = "INSERT INTO notes (title, content, journal_id, font_type, font_size, text_color, background_color) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING notes_id";

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
        ){
            preparedStatement.setString(1, note.getTitle());
            preparedStatement.setString(2, note.getContent());
            preparedStatement.setInt(3, note.getJournalID());
            preparedStatement.setString(4, note.getFontType());
            preparedStatement.setInt(5, note.getFontSize());
            preparedStatement.setInt(6, note.getTextColor().getRGB());
            preparedStatement.setInt(7, note.getBackgroundColor().getRGB());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    note.setNoteID(resultSet.getInt("notes_id"));
                }
            }

            System.out.println("Successfully inserted note");
        } catch (SQLException error){
            System.out.println("Error inserting note: " + error.getMessage());
        }
    }

    //READ methods

    //UPDATE methods
    //method to save an existing note
    public static void saveNote() {
        String saveNoteStatement = "UPDATE notes SET title = ?, content = ?, font_type = ?, font_size = ?, text_color = ?, background_color = ? WHERE journal_id = ? AND notes_id = ?";

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(saveNoteStatement)
                ){
            NoteEditor noteTextArea = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor();
            String noteContent = noteTextArea.getTextInTextEditor();
            String noteTitle = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getTitle();
            String fontType = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getFontType();
            int fontSize = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getFontSize();
            int textColor = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getTextColor().getRGB();
            int backgroundColor = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getBackgroundColor().getRGB();
            int noteID = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getNoteID();
            int journalID = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getJournalID();

            preparedStatement.setString(1, noteTitle);
            preparedStatement.setString(2, noteContent);
            preparedStatement.setString(3, fontType);
            preparedStatement.setInt(4, fontSize);
            preparedStatement.setInt(5, textColor);
            preparedStatement.setInt(6, backgroundColor);
            preparedStatement.setInt(7, journalID);
            preparedStatement.setInt(8, noteID);
            preparedStatement.executeUpdate();
            System.out.println("Saving note content");
        } catch (SQLException error) {
            System.out.println("Error saving note: " + error.getMessage());
        }
    }

    //method to rename note
    public static void renameNote(String newName) {
        String renameNoteStatement = "UPDATE notes SET title = ? WHERE journal_id = ? AND notes_id = ?";

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(renameNoteStatement)
        ){
            int noteID = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getNoteID();
            int journalID = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getJournalID();

            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, journalID);
            preparedStatement.setInt(3, noteID);
            preparedStatement.executeUpdate();
            System.out.println("Successfully renamed note");
        } catch (SQLException error) {
            System.out.println("Error saving note: " + error.getMessage());
        }
    }

    //methods to change note font type, font size, text color, and backgroundColor
    public static void changeFontType(String newFontType) {
        String changeFontTypeStatement = "UPDATE notes SET font_type = ? WHERE notes_id = ? AND journal_id = ?";

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(changeFontTypeStatement)
                ){
            int noteID = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getNoteID();
            int journalID = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getJournalID();

            preparedStatement.setString(1, newFontType);
            preparedStatement.setInt(2, noteID);
            preparedStatement.setInt(3, journalID);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            System.out.println("Error saving font type: " + error.getMessage());
        }
    }

    public static void changeFontSize(int newFontSize) {
        String changeFontSizeStatement = "UPDATE notes SET font_size = ? WHERE notes_id = ? AND journal_id = ?";

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(changeFontSizeStatement)
        ){
            int noteID = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getNoteID();
            int journalID = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getJournalID();

            preparedStatement.setInt(1, newFontSize);
            preparedStatement.setInt(2, noteID);
            preparedStatement.setInt(3, journalID);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            System.out.println("Error saving font size: " + error.getMessage());
        }
    }

    public static void changeTextColor(int newTextColor) {
        String changeTextColorStatement = "UPDATE notes SET text_color = ? WHERE notes_id = ? AND journal_id = ?";

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(changeTextColorStatement)
        ){
            int noteID = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getNoteID();
            int journalID = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getJournalID();

            preparedStatement.setInt(1, newTextColor);
            preparedStatement.setInt(2, noteID);
            preparedStatement.setInt(3, journalID);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            System.out.println("Error saving text color: " + error.getMessage());
        }
    }

    public static void changeBackgroundColor(int newBackgroundColor) {
        String changeBackgroundColorStatement = "UPDATE notes SET background_color = ? WHERE notes_id = ? AND journal_id = ?";

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(changeBackgroundColorStatement)
        ){
            int noteID = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getNoteID();
            int journalID = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getJournalID();

            preparedStatement.setInt(1, newBackgroundColor);
            preparedStatement.setInt(2, noteID);
            preparedStatement.setInt(3, journalID);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            System.out.println("Error saving text color: " + error.getMessage());
        }
    }


    //DELETE methods
    //method to delete a note
    public static void deleteNote(Note note) {
        String deleteStatement = "DELETE FROM notes WHERE notes_id = ? AND journal_id = ?";

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(deleteStatement)
                ){
            preparedStatement.setInt(1, note.getNoteID());
            preparedStatement.setInt(2, note.getJournalID());
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            System.out.println("Error deleting note: " + error.getMessage());
        }
    }
    //delete all notes belonging to a journal (*will continue once journals are implemented*)

}
