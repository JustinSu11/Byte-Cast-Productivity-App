package com.example.pickitup.services.dao;

import com.example.pickitup.services.database.*;
import java.sql.*;

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

    public static void main(String[] args) {
        insertNote("test note", "This is a test of the insertNote function");
    }
}
