/**
 * This class contains the necessary parameters for Calendar Events
 *
 * @author Aron Rios
 * @date 04/12/2025
 */
package com.example.pickitup.services.dao;

import com.example.pickitup.services.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CalendarEventDAO {
    // Method to save an event to the database
    public static void saveEventToDatabase(String title, String description, String startTime, String endTime) {
        String sql = "INSERT INTO calendar_events (title, event_description, start_time, end_time) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.connect()) {
            assert connection != null;
            try (PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, title);
                pstmt.setString(2, description);
                pstmt.setString(3, startTime);
                pstmt.setString(4, endTime);
                pstmt.executeUpdate();

                // Retrieve the generated event_id
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    System.out.println("Event saved with event_id: " + generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saving event: " + e.getMessage());
        }
    }

    // Method to fetch events based on the date part of start_time
    public static List<String> getEvents(String date) {
        List<String> events = new ArrayList<>();
        String datePart = date.split(" ")[0];
        String sql = "SELECT id, title, event_description, start_time FROM calendar_events WHERE DATE(start_time) = ?";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, datePart);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int eventId = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("event_description");
                String startTime = rs.getString("start_time");
                String timePart = startTime.split(" ")[1]; // Extract "HH:mm:ss" part
                events.add(eventId + ": " + title + ": " + description + " at " + timePart);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching events: " + e.getMessage());
        }
        return events;
    }

    // Method to delete an event by its event_id
    // Method to delete an event by its event_id and reset AUTOINCREMENT if empty
    public static boolean deleteEvent(int eventId) {
        String deleteSql = "DELETE FROM calendar_events WHERE id = ?";
        String countSql = "SELECT COUNT(*) FROM calendar_events";
        String resetSql = "DELETE FROM sqlite_sequence WHERE name='calendar_events'";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement deleteStmt = connection.prepareStatement(deleteSql);
             PreparedStatement countStmt = connection.prepareStatement(countSql);
             PreparedStatement resetStmt = connection.prepareStatement(resetSql)) {

            deleteStmt.setInt(1, eventId);
            int rowsAffected = deleteStmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = countStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    resetStmt.executeUpdate(); // Reset auto-increment if table is empty
                    System.out.println("Auto-increment counter reset.");
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error deleting event: " + e.getMessage());
        }
        return false;
    }
}