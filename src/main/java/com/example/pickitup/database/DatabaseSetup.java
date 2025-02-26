package com.example.pickitup.database;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSetup {
    //Define tables for storing notes
    public static void createTables() {
        //create a notes table with the structure below for each note
        String notesTable = "CREATE TABLE IF NOT EXISTS notes (" +
                "notes_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL," +
                "content TEXT NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        //Create a calendar events table
        String calendarEventTable = "CREATE TABLE IF NOT EXISTS calendar_events(" +
                "event_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "event_description TEXT NOT NULL, " +
                "start_time DATETIME NOT NULL, " +
                "end_time DATETIME NOT NULL, " +
                "location TEXT, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");";

        //if connection is successful execute the above sql statements to make tables if they don't exist already
        try (
                Connection connection = DatabaseConnection.connect();
                Statement statement = connection.createStatement()
        ) {
            statement.execute(notesTable);
            System.out.println("Notes table created");
            statement.execute(calendarEventTable);
            System.out.println("Calendar events table created");
        } catch (Exception e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }
    public static void main(String[] args){
        createTables();
    }
}
