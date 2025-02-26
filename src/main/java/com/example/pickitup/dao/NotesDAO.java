package com.example.pickitup.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.example.pickitup.models.Note;
import com.example.pickitup.database.DatabaseConnection;

public class NotesDAO {
    public static void insertNote(String title, String content) {
        String sql = "INSERT INTO notes (title, content) VALUES (?, ?)";

        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, content);
            preparedStatement.executeUpdate();
            System.out.println("Successfully inserted note");
        } catch (SQLException e){
            System.out.println("Error inserting note: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        insertNote("test note", "This is a test of the insertNote function");
    }
}
