/*
 * @author Justin Nguyen
 * @version 0.1
 * @updated 03/24/2025
 * */

package com.example.pickitup.services.dao;

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
        String insertStatement = "INSERT INTO notes (title, content, journal_id) VALUES (?, ?, ?) RETURNING notes_id";

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
        ){
            preparedStatement.setString(1, note.getTitle());
            preparedStatement.setString(2, note.getContent());
            preparedStatement.setInt(3, note.getJournalID());

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
    //method to retrieve a note by title
    public static void getNote(int noteID) {
        String selectStatement = "SELECT * FROM notes WHERE noteID = " + noteID;

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(selectStatement);
                ResultSet resultSet = preparedStatement.executeQuery()
                ){
            //retrieve the columns and store into variables
            while (resultSet.next()) {
                int note_id = resultSet.getInt("id");
                int journal_id = resultSet.getInt("journal_id");
                String noteTitle = resultSet.getString("title");
                String noteContent = resultSet.getString("content");
                Note note = new Note(note_id, journal_id, noteTitle, noteContent);
            }
        } catch (SQLException error) {
            System.out.println("Error getting note: " + error.getMessage());
        }
    }
//    //method to retrieve all notes belonging to a journal
//    public static Note getNotesByJournalId(int journalId) {
//        String retrieveStatement = "SELECT notes_id, title, content FROM notes WHERE journal_id = ?";
//        try (
//                Connection connection = DatabaseConnection.connect();
//                PreparedStatement preparedStatement = connection.prepareStatement(retrieveStatement)
//        ) {
//            preparedStatement.setInt(1, journalId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                int note_id = resultSet.getInt("notes_id");
//                int journal_id = resultSet.getInt("journal_id");
//                String content = resultSet.getString("content");
//                String title = resultSet.getString("title");
//                Note note = new Note(note_id, journal_id, content, title);
//                NoteEditor newNoteEditor = new NoteEditor(note);
//
//                newNoteEditor.makeScrollPane();
//                newNoteEditor.getTextArea().setText(content);
//            }
//        } catch (SQLException error) {
//            System.out.println("Error getting notes: " + error.getMessage());
//        }
//    }

    //UPDATE methods
    //method to save an existing note
    public static void saveNote() {
        String saveNoteStatement = "UPDATE notes SET title = ?, content = ? WHERE journal_id = ? AND notes_id = ?";

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(saveNoteStatement)
                ){
            NoteEditor noteTextArea = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor();
            String noteContent = noteTextArea.getTextInTextEditor();
            String noteTitle = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getTitle();
            int noteID = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getNoteID();
            int journalID = JournalsPane.getSelectedNotesPane().getCurrentNoteEditor().getNoteItem().getJournalID();

            preparedStatement.setString(1, noteTitle);
            preparedStatement.setString(2, noteContent);
            preparedStatement.setInt(3, journalID);
            preparedStatement.setInt(4, noteID);
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
