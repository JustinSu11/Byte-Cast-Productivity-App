package com.example.pickitup.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CalendarEventDAO {

    // Method to save an event to the database
    public void saveEventToDatabase(String title, String description, String startTime, String endTime) {
        String sql = "INSERT INTO calendar_events (title, event_description, start_time, end_time) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.connect()) {
            assert connection != null;
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, title);
                pstmt.setString(2, description);
                pstmt.setString(3, startTime);
                pstmt.setString(4, endTime); // Saving end_time as well
                pstmt.executeUpdate();
                System.out.println("Event saved: " + title);
            }
        } catch (SQLException e) {
            System.out.println("Error saving event: " + e.getMessage());
        }
    }

    // Method to fetch events based on the start_time
    public List<String> getEvents(String startTime) {
        List<String> events = new ArrayList<>();
        String sql = "SELECT title, event_description FROM calendar_events WHERE start_time = ?";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, startTime);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                events.add(rs.getString("title") + ": " + rs.getString("event_description"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching events: " + e.getMessage());
        }
        return events;
    }
}
