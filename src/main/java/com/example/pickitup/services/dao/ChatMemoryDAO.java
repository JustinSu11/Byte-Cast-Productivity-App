// Package declaration for the DAO class that handles chat message persistence
package com.example.pickitup.services.dao;

// Import necessary classes for database operations and data handling
import com.example.pickitup.services.database.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing chat conversation memory in SQLite
 * This class provides methods to save, retrieve, and clear chat messages
 * from a local SQLite database, maintaining conversation history between sessions.
 * @author Maaz Haque
 * @version 1.0
 */
public class ChatMemoryDAO {

    /**
     * Initializes the database and creates necessary tables if they don't exist
     * This constructor sets up the database structure upon instantiation of the DAO
     */
    public ChatMemoryDAO() {
        try {
            // Create the database and table if they don't exist
            // Establish a connection to the SQLite database using the DatabaseConnection utility class
            Connection databaseConnection = DatabaseConnection.connect();
            
            // Create a statement object for executing SQL commands
            Statement tableCreationStatement = databaseConnection.createStatement();
            
            // SQL query to create the chat_messages table if it doesn't already exist
            // The table has columns for:
            // - id: unique identifier for each message with auto-increment
            // - role: indicates whether the message is from the user or assistant
            // - content: stores the actual message text
            // - timestamp: records when the message was created
            String chatTableCreationQuery = "CREATE TABLE IF NOT EXISTS chat_messages (" +
                                   "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                   "role TEXT NOT NULL, " +
                                   "content TEXT NOT NULL, " +
                                   "timestamp TEXT NOT NULL)";
            
            // Execute the SQL command to create the table
            tableCreationStatement.execute(chatTableCreationQuery);
            
            // Release resources by closing the statement and database connection
            tableCreationStatement.close();
            databaseConnection.close();
        } catch (SQLException e) {
            // Print an error message if database initialization fails
            System.err.println("Error initializing SQLite database: " + e.getMessage());
        }
    }
    
    /**
     * Saves a new chat message to the database
     * This method persists individual messages with their metadata
     * 
     * @param role The role of the message sender (user or assistant)
     * @param content The content of the message
     */
    public void saveMessage(String role, String content) {
        try (
            // Establish a connection to the database using try-with-resources for automatic resource management
            Connection databaseConnection = DatabaseConnection.connect();
            
            // Prepare a SQL statement with placeholders to prevent SQL injection
            PreparedStatement insertMessageStatement = databaseConnection.prepareStatement(
                     "INSERT INTO chat_messages (role, content, timestamp) VALUES (?, ?, ?)")
        ) {
            
            // Set the first parameter in the prepared statement to the role value
            insertMessageStatement.setString(1, role);
            
            // Set the second parameter to the message content
            insertMessageStatement.setString(2, content);
            
            // Set the third parameter to the current timestamp in string format
            insertMessageStatement.setString(3, LocalDateTime.now().toString());
            
            // Execute the SQL statement to insert the new message into the database
            insertMessageStatement.executeUpdate();
        } catch (SQLException e) {
            // Print an error message if saving the message fails
            System.err.println("Error saving message: " + e.getMessage());
        }
    }
    
    /**
     * Retrieves all chat messages from the database
     * This method fetches the complete conversation history in chronological order
     * 
     * @return List of ChatMessage objects containing role and content
     */
    public List<ChatMessage> getAllMessages() {
        // Initialize an empty list to store the retrieved messages
        List<ChatMessage> retrievedMessages = new ArrayList<>();
        
        try (
            // Establish a connection to the database
            Connection databaseConnection = DatabaseConnection.connect();
            
            // Create a statement for executing SQL queries
            Statement retrieveMessagesStatement = databaseConnection.createStatement();
            
            // Execute a query to retrieve all messages ordered by ID (chronological order)
            ResultSet messageResults = retrieveMessagesStatement.executeQuery(
                    "SELECT role, content FROM chat_messages ORDER BY id ASC")
        ) {
            
            // Iterate through each row in the result set
            while (messageResults.next()) {
                // Extract the role value from the current row
                String senderRole = messageResults.getString("role");
                
                // Extract the message content from the current row
                String messageContent = messageResults.getString("content");
                
                // Create a new ChatMessage object with the retrieved data and add it to the messages list
                retrievedMessages.add(new ChatMessage(senderRole, messageContent));
            }
        } catch (SQLException e) {
            // Print an error message if retrieving messages fails
            System.err.println("Error retrieving messages: " + e.getMessage());
        }
        
        // Return the list of messages
        return retrievedMessages;
    }
    
    /**
     * Clears all chat messages from the database
     * This method provides a way to reset the conversation history
     */
    public void clearAllMessages() {
        try (
            // Establish a connection to the database
            Connection databaseConnection = DatabaseConnection.connect();
            
            // Create a statement for executing SQL commands
            Statement deleteMessagesStatement = databaseConnection.createStatement()
        ) {
            
            // Execute a command to delete all rows from the chat_messages table
            deleteMessagesStatement.executeUpdate("DELETE FROM chat_messages");
        } catch (SQLException e) {
            // Print an error message if clearing messages fails
            System.err.println("Error clearing messages: " + e.getMessage());
        }
    }
    
    /**
     * Inner class representing a chat message
     * This class encapsulates the data structure for individual messages
     */
    public static class ChatMessage {
        // Field to store the role of the message sender (user or assistant)
        private String senderRole;
        
        // Field to store the actual content of the message
        private String messageContent;
        
        /**
         * Constructor to initialize a ChatMessage object
         * 
         * @param role The sender's role (user or assistant)
         * @param content The message text
         */
        public ChatMessage(String role, String content) {
            // Assign the role parameter to the role field
            this.senderRole = role;
            
            // Assign the content parameter to the content field
            this.messageContent = content;
        }
        
        /**
         * Getter method for the role field
         * 
         * @return The role of the message sender
         */
        public String getRole() {
            // Return the value of the role field
            return senderRole;
        }
        
        /**
         * Getter method for the content field
         * 
         * @return The content of the message
         */
        public String getContent() {
            // Return the value of the content field
            return messageContent;
        }
    }
}
