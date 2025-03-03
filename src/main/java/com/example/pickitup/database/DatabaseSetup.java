package com.example.pickitup.database;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSetup {
    // Define tables for storing notes and calendar events
    public static void createTables() {
        // Create a notes table with the structure below for each note
        String notesTable = "CREATE TABLE IF NOT EXISTS notes (" +
                "notes_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "journal_id INTEGER NOT NULL, " +
                "title TEXT NOT NULL, " +
                "content TEXT NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (journal_id) REFERENCES journal(journal_id) " +
                ");";

        // Create a calendar events table
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
                "workspace_id INTEGER NOT NULL, " +
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

        String workspaceJournalsTable = "CREATE TABLE IF NOT EXISTS workspace_journals (" +
                "workspace_id INTEGER NOT NULL," +
                "journal_id INTEGER NOT NULL, " +
                "PRIMARY KEY (workspace_id, journal_id), " +
                "FOREIGN KEY (workspace_id) REFERENCES workspaces(workspace_id), " +
                "FOREIGN KEY (journal_id) REFERENCES journals(journals_id) " +
                ");";

        String workspacesTable = "CREATE TABLE IF NOT EXISTS workspaces (" +
                "workspace_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL " +
                ");";

        // If connection is successful, execute the above SQL statements to create tables if they don't exist already
        try (
                Connection connection = DatabaseConnection.connect();
                Statement statement = connection.createStatement()
        ) {
            statement.execute(workspacesTable);
            System.out.println("Workspace table created");
            statement.execute(notesTable);
            System.out.println("Notes table created");
            statement.execute(calendarEventsTable);
            System.out.println("Calendar events table created");
            statement.execute(journalsTable);
            System.out.println("Journal table created");
            statement.execute(journalNotesTable);
            System.out.println("Journal notes table created");
            statement.execute(workspaceJournalsTable);
            System.out.println("Workspace journal table created");
        } catch (Exception e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        createTables();
    }
}
