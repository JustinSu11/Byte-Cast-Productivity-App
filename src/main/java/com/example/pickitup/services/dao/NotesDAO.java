/**
 * DAO for notes in database
 *
 * @author Justin Nguyen
 * @date 04/12/2025
 */
package com.example.pickitup.services.dao;

import com.example.pickitup.services.models.Note;
import com.example.pickitup.services.database.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotesDAO {
    //CREATE methods
    //method to insert note into SQLite database
    public static void insertNote(Note note) {
        String insertStatement = "INSERT INTO notes (title, content) VALUES (?, ?)";

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(insertStatement)
        ){

            preparedStatement.setString(1, note.getTitle());
            preparedStatement.setString(2, note.getContent());
            preparedStatement.executeUpdate();
            System.out.println("Successfully inserted note");
        } catch (SQLException error){
            System.out.println("Error inserting note: " + error.getMessage());
        }
    }

    //READ methods
    //method to retrieve a note by title
    public static void getNote(String title) {
        String selectStatement = "SELECT * FROM notes WHERE title = ?";

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(selectStatement);
                ResultSet resultSet = preparedStatement.executeQuery()
                ){
            while (resultSet.next()) {
                //retrieve the columns and store into variables
                long note_id = resultSet.getLong("id");
                long journal_id = resultSet.getLong("journal_id");
                String noteTitle = resultSet.getString("title");
                String noteContent = resultSet.getString("content");

                Note note = new Note(note_id, journal_id, noteTitle, noteContent);
            }
        } catch (SQLException error) {
            System.out.println("Error getting note: " + error.getMessage());
        }
    }
    //method to retrieve all notes belonging to a journal
    public static List<Note> getNotesByJournalId(int journalId) {
        List<Note> notes = new ArrayList<>();
        String retrieveStatement = "SELECT notes_id, journal_id, content FROM notes WHERE journal_id = ?";
        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(retrieveStatement)
        ) {
            preparedStatement.setInt(1, journalId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int note_id = resultSet.getInt("notes_id");
                int journal_id = resultSet.getInt("journal_id");
                String content = resultSet.getString("content");
                String title = resultSet.getString("title");
                Note note = new Note(note_id, journal_id, content, title);
                notes.add(note);
            }
        } catch (SQLException error) {
            System.out.println("Error getting notes: " + error.getMessage());
        }
        return notes;
    }

    //UPDATE methods
    //method to save an existing note
    public static void saveNote(Note note) {
        String updateStatement = "UPDATE notes SET title = ?, content = ? WHERE notes_id = ?";

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(updateStatement)
                ){
            preparedStatement.setString(1, note.getTitle());
            preparedStatement.setString(2, note.getContent());
            preparedStatement.setLong(3, note.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            System.out.println("Error saving note: " + error.getMessage());
        }
    }

    //DELETE methods
    //method to delete a note
    public static void deleteNote(Note note) {
        String deleteStatement = "DELETE FROM notes WHERE notes_id = ?";

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(deleteStatement)
                ){
            preparedStatement.setLong(1, note.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            System.out.println("Error deleting note: " + error.getMessage());
        }
    }
    //delete all notes belonging to a journal (*will continue once journals are implemented*)

}
