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
                "FOREIGN KEY (workspace_id) REFERENCES workspaces(workspace_id) " +
                ");";

        String journalNotesTable = "CREATE TABLE IF NOT EXISTS journal_notes (" +
                "journal_id INTEGER NOT NULL, " +
                "note_id INTEGER NOT NULL, " +
                "PRIMARY KEY (journal_id, note_id), " +
                "FOREIGN KEY (note_id) REFERENCES notes(notes_id), " +
                "FOREIGN KEY (journal_id) REFERENCES journals(journals_id) " +
                ");";

        String chatMemoryTable = "CREATE TABLE IF NOT EXISTS chat_messages (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "role TEXT NOT NULL, " +
                "content TEXT NOT NULL, " +
                "timestamp TEXT NOT NULL)";

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
            statement.execute(journalNotesTable);
            System.out.println("Journal notes table created");
            statement.execute(chatMemoryTable);
            System.out.println("Chat memory table created");
            connection.close();
        } catch (Exception e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }
    public static void main(String[] args){
        createTables();
    }
}
