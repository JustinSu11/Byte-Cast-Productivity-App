package com.example.pickitup.services.dao;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing chat conversation memory in SQLite
 * @author Maaz Haque
 * @version 1.0
 */
public class ChatMemoryDAO {
    
    private static final String DB_URL = "jdbc:sqlite:chat_memory.db";
    
    /**
     * Initializes the database and creates necessary tables if they don't exist
     */
    public ChatMemoryDAO() {
        try {
            // Create the database and table if they don't exist
            Connection connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement();
            
            // Create table for chat messages
            String createTableSQL = "CREATE TABLE IF NOT EXISTS chat_messages (" +
                                   "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                   "role TEXT NOT NULL, " +
                                   "content TEXT NOT NULL, " +
                                   "timestamp TEXT NOT NULL)";
            statement.execute(createTableSQL);
            
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error initializing SQLite database: " + e.getMessage());
        }
    }
    
    /**
     * Saves a new chat message to the database
     * 
     * @param role The role of the message sender (user or assistant)
     * @param content The content of the message
     */
    public void saveMessage(String role, String content) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(
                     "INSERT INTO chat_messages (role, content, timestamp) VALUES (?, ?, ?)")) {
            
            pstmt.setString(1, role);
            pstmt.setString(2, content);
            pstmt.setString(3, LocalDateTime.now().toString());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving message: " + e.getMessage());
        }
    }
    
    /**
     * Retrieves all chat messages from the database
     * 
     * @return List of ChatMessage objects containing role and content
     */
    public List<ChatMessage> getAllMessages() {
        List<ChatMessage> messages = new ArrayList<>();
        
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT role, content FROM chat_messages ORDER BY id ASC")) {
            
            while (rs.next()) {
                String role = rs.getString("role");
                String content = rs.getString("content");
                messages.add(new ChatMessage(role, content));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving messages: " + e.getMessage());
        }
        
        return messages;
    }
    
    /**
     * Clears all chat messages from the database
     */
    public void clearAllMessages() {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement()) {
            
            stmt.executeUpdate("DELETE FROM chat_messages");
        } catch (SQLException e) {
            System.err.println("Error clearing messages: " + e.getMessage());
        }
    }
    
    /**
     * Inner class representing a chat message
     */
    public static class ChatMessage {
        private String role;
        private String content;
        
        public ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
        
        public String getRole() {
            return role;
        }
        
        public String getContent() {
            return content;
        }
    }
}
