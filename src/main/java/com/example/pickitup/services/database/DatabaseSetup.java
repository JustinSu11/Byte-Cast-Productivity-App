package com.example.pickitup.services.database;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSetup {
    //Define tables for storing notes
    public static void createTables() {
        //create a notes table with the structure below for each note
        String notesTable = "CREATE TABLE IF NOT EXISTS notes (" +
                "notes_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "journal_id INTEGER NOT NULL, " +
                "title TEXT NOT NULL, " +
                "content TEXT NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "note_order INTEGER, " +
                "FOREIGN KEY (journal_id) REFERENCES journal(journal_id) " +
                ");";

        //Create a calendar events table
        String calendarEventsTable = "CREATE TABLE IF NOT EXISTS calendar_events (" +
                "event_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "event_description TEXT NOT NULL, " +
                "start_time TIMESTAMP NOT NULL, " +
                "end_time TIMESTAMP NOT NULL, " +
                "location TEXT, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
                ");";

        String journalsTable = "CREATE TABLE IF NOT EXISTS journals (" +
                "journal_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "selected_flag BOOLEAN NOT NULL, " +
                "journal_order INTEGER NOT NULL, " +
                "selected_note_index INTEGER NOT NULL " +
                ");";


        String chatMemoryTable = "CREATE TABLE IF NOT EXISTS chat_messages (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "role TEXT NOT NULL, " +
                "content TEXT NOT NULL, " +
                "timestamp TEXT NOT NULL " +
                ");";

        String toDoItemsTable = "CREATE TABLE IF NOT EXISTS to_do_items (" +
                "to_do_item_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "task_name TEXT NOT NULL, " +
                "due_date BLOB, " +
                "completed_flag INTEGER NOT NULL " +
                ");";

        //if connection is successful execute the above sql statements to make tables if they don't exist already
        try (
                Connection connection = DatabaseConnection.connect();
                Statement statement = connection.createStatement()
        ) {
            statement.execute(notesTable);
            System.out.println("Notes table created");
            statement.execute(calendarEventsTable);
            System.out.println("Calendar events table created");
            statement.execute(journalsTable);
            System.out.println("Journal table created");
            statement.execute(chatMemoryTable);
            System.out.println("Chat memory table created");
            statement.execute(toDoItemsTable);
            System.out.println("To do items table created");
        } catch (Exception e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }
}
