package com.example.pickitup.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static final String url = "jdbc:sqlite:pickitup.db";  // Adjust this path if needed

    // Attempt to connect to the database or create it if it doesn't exist
    public static Connection connect()
    {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Successfully connected to the database.");
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
        return connection;
    }
}
