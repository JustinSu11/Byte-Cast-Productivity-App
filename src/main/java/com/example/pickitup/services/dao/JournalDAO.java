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

public class JournalDAO {

    //CREATE
    //method to insert journal into database
    public static void insertJournal(Journal journal) throws SQLException {
        String insertStatement = "INSERT INTO journals (journal_id, title) VALUES (?, ?)";
        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(insertStatement)
                ) {
            preparedStatement.setInt(1, journal.getId());
            preparedStatement.setString(2, journal.getTitle());
            preparedStatement.executeUpdate();
        }
    }

    //READ
    //method to retrieve all journals from database along with the notes associated with the journal
    public static List<Journal> getAllJournals() throws SQLException {
        List<Journal> journals = new ArrayList<>();
        String selectStatement = "SELECT journal_id, title FROM journals";
        try (
                Connection connection = DatabaseConnection.connect();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectStatement)
                ) {
            while (resultSet.next()) {
                int id = resultSet.getInt("journal_id");
                String title = resultSet.getString("title");
                Journal journal = new Journal(id, title);
                //Load journals
                journal.loadNotes();
                journals.add(journal);
            }
        }
        return journals;
    }

    //UPDATE
    //method to update journal title
    public static void updateJournal(Journal journal) throws SQLException {
        String updateStatement = "UPDATE journals SET title = ? WHERE journal_id = ?";
        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(updateStatement)
                ) {
            preparedStatement.setString(1, journal.getTitle());
            preparedStatement.setInt(2, journal.getId());
            preparedStatement.executeUpdate();
        }
    }

    //DELETE
    //method to delete journal from database
    public static void deleteJournal(int journalId) throws SQLException {
        String deleteStatement = "DELETE FROM journals WHERE journal_id = ?";
        try (
                Connection connection = DatabaseConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(deleteStatement)
                ) {
            preparedStatement.setInt(1, journalId);
            preparedStatement.executeUpdate();
        }
    }
}
