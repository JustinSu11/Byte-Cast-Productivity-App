package com.example.pickitup.services.dao;

import com.example.pickitup.services.models.Note;
import com.example.pickitup.services.database.*;
import java.sql.*;
import java.time.LocalDateTime;

public class NotesDAO {
    //method to insert note into SQLite database
    public static void insertNote(String title, String content) {
        String insertStatement = "INSERT INTO notes (title, content) VALUES (?, ?)";

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(insertStatement)
        ){
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, content);
            preparedStatement.executeUpdate();
            System.out.println("Successfully inserted note");
        } catch (SQLException error){
            System.out.println("Error inserting note: " + error.getMessage());
        }
    }

    //
    public static void getNote(String title) {
        String selectStatement = "SELECT * FROM notes WHERE title = ?";

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(selectStatement);
                ResultSet resultSet = preparedStatement.executeQuery();
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

    public static void main(String[] args) {
        insertNote("test note", "This is a test of the insertNote function");
    }
}
