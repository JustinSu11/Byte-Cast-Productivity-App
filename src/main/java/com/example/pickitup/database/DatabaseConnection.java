package com.example.pickitup.database;

import java.sql.Connection; //Connection to db
import java.sql.DriverManager;
import java.sql.SQLException; //Error handling

public class DatabaseConnection {
    public static final String url = "jdbc:sqlite:pickitup.db";

    //attempt to connect to db if it exists or create the db if it doesn't exist. If unsuccessful print an error message to console.
    public static Connection connect() {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Error connecting to database" + e.getMessage());
            return null;
        }
    }
}
