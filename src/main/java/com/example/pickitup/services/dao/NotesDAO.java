package com.example.pickitup.services.dao;

import com.example.pickitup.services.models.Note;
import com.example.pickitup.services.database.*;
import java.sql.*;
import java.time.LocalDateTime;

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
                long id = resultSet.getLong("id");
                String noteTitle = resultSet.getString("title");
                String noteContent = resultSet.getString("content");
                LocalDateTime createdAt = resultSet.getTimestamp("createdAt").toLocalDateTime();
                LocalDateTime updatedAt = resultSet.getTimestamp("createdAt").toLocalDateTime();

                Note note = new Note(id, noteTitle, noteContent, createdAt, updatedAt);
            }


        } catch (SQLException error) {
            System.out.println("Error getting note: " + error.getMessage());
        }
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
