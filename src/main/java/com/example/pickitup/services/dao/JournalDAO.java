/*
 * @author Justin Nguyen
 * @version 1.0
 * @updated 03/24/2025
 * */

package com.example.pickitup.services.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.example.pickitup.services.database.DatabaseConnection;
import com.example.pickitup.services.models.Journal;
import com.example.pickitup.ui.JournalsPane;
import com.example.pickitup.ui.NotesPane;

public class JournalDAO {
    //CREATE
    //method to insert journal into database
    public static void insertJournal(Journal journal) {
        String insertStatement = "INSERT INTO journals (title, selected_note_index, selected_flag, journal_order) VALUES (?, 0, 0, 0) RETURNING journal_id";
        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(insertStatement)
                ) {
            preparedStatement.setString(1, journal.getTitle());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    journal.setJournalID(resultSet.getInt("journal_id"));
                }
            }

            System.out.println("Successfully inserted journal");
        } catch (SQLException error) {
            System.out.println("Error inserting journal: " + error.getMessage());
        }
    }

    //READ
    //method to retrieve all journals from database along with the notes associated with the journal
    public static List<NotesPane> getAllJournals() {
        List<NotesPane> journals = new ArrayList<>();
        String selectStatement = "SELECT journal_id, title FROM journals";
        try (
                Connection connection = DatabaseConnection.connect();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectStatement)
                ) {
            while (resultSet.next()) {
                String title = resultSet.getString("title");
//                Journal journal = new Journal(title);
                //Load journals
//                journal.loadNotes();
//                journals.add(journal);
            }
        } catch (SQLException error) {
            System.out.println("Error retrieving journals: " + error.getMessage());
        }
        return journals;
    }

    //UPDATE
    //method to update journal title
    public static void updateJournal(int journalID, String title) {
        String updateStatement = "UPDATE journals SET title = ? WHERE journal_id = ?";
        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(updateStatement)
                ) {
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, journalID);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            System.out.println("Error updating journal: " + error.getMessage());
        }
    }

    //DELETE
    //method to delete journal from database
    public static void deleteJournal(int journalId) {
        String deleteJournalStatement = "DELETE FROM journals WHERE journal_id = ?";
        String deleteNotesBelongingToJournalStatement = "DELETE FROM notes WHERE journal_id = ?";
        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(deleteJournalStatement);
                PreparedStatement preparedStatement2 = connection.prepareStatement(deleteNotesBelongingToJournalStatement)
                ) {
            preparedStatement.setInt(1, journalId);
            preparedStatement.executeUpdate();
            preparedStatement2.setInt(1, journalId);
            preparedStatement2.executeUpdate();
        } catch (SQLException error) {
            System.out.println("Error deleting journal: " + error.getMessage());
        }
    }
}
