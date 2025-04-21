/**
 * DAO for KnowledgeBase persistence
 *
 * @author Maaz Haque
 * @date 04/12/2025
 */
package com.example.pickitup.services.dao;

import com.example.pickitup.services.models.KnowledgeBase;
import com.example.pickitup.services.models.DocumentData;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class KnowledgeBaseDAO {
    
    // Database connection URL constant
    private static final String DB_URL = "jdbc:sqlite:pickitup.db";
    
    // SQL query constants
    private static final String CREATE_KB_TABLE = 
            "CREATE TABLE IF NOT EXISTS knowledge_bases (" +
            "id TEXT PRIMARY KEY, " +
            "name TEXT NOT NULL, " +
            "description TEXT, " +
            "created_at TEXT NOT NULL)";
    
    private static final String CREATE_KB_DOCS_TABLE = 
            "CREATE TABLE IF NOT EXISTS knowledge_base_documents (" +
            "id TEXT PRIMARY KEY, " +
            "knowledge_base_id TEXT NOT NULL, " +
            "content TEXT NOT NULL, " +
            "source TEXT, " +
            "FOREIGN KEY (knowledge_base_id) REFERENCES knowledge_bases(id))";
    
    private static final String INSERT_KB = 
            "INSERT OR REPLACE INTO knowledge_bases (id, name, description, created_at) VALUES (?, ?, ?, ?)";
    
    private static final String DELETE_KB_DOCS = 
            "DELETE FROM knowledge_base_documents WHERE knowledge_base_id = ?";
    
    private static final String INSERT_KB_DOC = 
            "INSERT INTO knowledge_base_documents (id, knowledge_base_id, content, source) VALUES (?, ?, ?, ?)";
    
    private static final String SELECT_ALL_KB = 
            "SELECT * FROM knowledge_bases";
    
    private static final String SELECT_KB_DOCS = 
            "SELECT * FROM knowledge_base_documents WHERE knowledge_base_id = ?";
    
    private static final String DELETE_KB = 
            "DELETE FROM knowledge_bases WHERE id = ?";
    
    /**
     * Constructor initializes the database tables if they don't exist
     */
    public KnowledgeBaseDAO() {
        // Initialize database tables
        initializeDatabase();
    }
    
    /**
     * Creates the necessary database tables if they don't exist
     */
    private void initializeDatabase() {
        // Declare connection and statement outside try block for scope
        Connection dbConnection = null;
        Statement dbStatement = null;
        
        try {
            // Establish database connection
            dbConnection = DriverManager.getConnection(DB_URL);
            // Create statement for executing SQL
            dbStatement = dbConnection.createStatement();
            
            // Create knowledge_bases table if it doesn't exist
            dbStatement.execute(CREATE_KB_TABLE);
            
            // Create knowledge_base_documents table if it doesn't exist
            dbStatement.execute(CREATE_KB_DOCS_TABLE);
            
        } catch (SQLException sqlException) {
            // Log the error 
            System.err.println("Error initializing database: " + sqlException.getMessage());
            sqlException.printStackTrace();
        } finally {
            // Close resources in finally block to ensure they are closed
            try {
                if (dbStatement != null) {
                    dbStatement.close();
                }
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException closeException) {
                System.err.println("Error closing database resources: " + closeException.getMessage());
            }
        }
    }
    
    /**
     * Saves a knowledge base to the database
     * 
     * @param knowledgeBase The knowledge base to save
     */
    public void saveKnowledgeBase(KnowledgeBase knowledgeBase) {
        // Check for null input
        if (knowledgeBase == null) {
            System.err.println("Cannot save null knowledge base");
            return;
        }

        // Declare variables at the top
        Connection dbConnection = null;
        PreparedStatement knowledgeBaseStatement = null;
        PreparedStatement deleteDocsStatement = null;
        PreparedStatement documentInsertStatement = null;
        
        try {
            // Establish database connection
            dbConnection = DriverManager.getConnection(DB_URL);
            
            // First save or update the knowledge base
            knowledgeBaseStatement = dbConnection.prepareStatement(INSERT_KB);
            knowledgeBaseStatement.setString(1, knowledgeBase.getId());
            knowledgeBaseStatement.setString(2, knowledgeBase.getName());
            knowledgeBaseStatement.setString(3, knowledgeBase.getDescription());
            knowledgeBaseStatement.setString(4, knowledgeBase.getCreatedAt().toString());
            knowledgeBaseStatement.executeUpdate();
            
            // Then handle documents
            // First, remove all existing documents for this knowledge base
            deleteDocsStatement = dbConnection.prepareStatement(DELETE_KB_DOCS);
            deleteDocsStatement.setString(1, knowledgeBase.getId());
            deleteDocsStatement.executeUpdate();
            
            // Get the list of documents
            List<DocumentData> documents = knowledgeBase.getDocuments();
            
            // Then insert all current documents
            if (documents != null && !documents.isEmpty()) {
                documentInsertStatement = dbConnection.prepareStatement(INSERT_KB_DOC);
                
                for (DocumentData document : documents) {
                    // Skip null documents
                    if (document == null) {
                        continue;
                    }
                    
                    // Generate a new UUID for the document ID
                    String documentId = UUID.randomUUID().toString();
                    
                    documentInsertStatement.setString(1, documentId);
                    documentInsertStatement.setString(2, knowledgeBase.getId());
                    documentInsertStatement.setString(3, document.getContent());
                    documentInsertStatement.setString(4, document.getSource());
                    documentInsertStatement.executeUpdate();
                }
            }
            
        } catch (SQLException sqlException) {
            // Log the error
            System.err.println("Error saving knowledge base: " + sqlException.getMessage());
            sqlException.printStackTrace();
        } finally {
            // Close resources in finally block to ensure they are closed
            try {
                if (documentInsertStatement != null) {
                    documentInsertStatement.close();
                }
                if (deleteDocsStatement != null) {
                    deleteDocsStatement.close();
                }
                if (knowledgeBaseStatement != null) {
                    knowledgeBaseStatement.close();
                }
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException closeException) {
                System.err.println("Error closing database resources: " + closeException.getMessage());
            }
        }
    }
    
    /**
     * Gets all knowledge bases from the database
     * 
     * @return List of all knowledge bases
     */
    public List<KnowledgeBase> getAllKnowledgeBases() {
        // Initialize the result list
        List<KnowledgeBase> knowledgeBases = new ArrayList<>();
        
        // Declare variables at the top
        Connection dbConnection = null;
        Statement dbStatement = null;
        ResultSet resultSet = null;
        
        try {
            // Establish database connection
            dbConnection = DriverManager.getConnection(DB_URL);
            
            // Create statement and execute query
            dbStatement = dbConnection.createStatement();
            resultSet = dbStatement.executeQuery(SELECT_ALL_KB);
            
            // Process each row in the result set
            while (resultSet.next()) {
                // Extract knowledge base data from result set
                String kbId = resultSet.getString("id");
                String kbName = resultSet.getString("name");
                String kbDescription = resultSet.getString("description");
                
                // Create a new knowledge base object
                KnowledgeBase knowledgeBase = new KnowledgeBase(kbId, kbName, kbDescription);
                
                // Load documents for this knowledge base
                loadDocumentsForKnowledgeBase(dbConnection, knowledgeBase);
                
                // Add the knowledge base to the result list
                knowledgeBases.add(knowledgeBase);
            }
            
        } catch (SQLException sqlException) {
            // Log the error
            System.err.println("Error retrieving knowledge bases: " + sqlException.getMessage());
            sqlException.printStackTrace();
        } finally {
            // Close resources in finally block to ensure they are closed
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (dbStatement != null) {
                    dbStatement.close();
                }
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException closeException) {
                System.err.println("Error closing database resources: " + closeException.getMessage());
            }
        }
        
        // Return the list of knowledge bases
        return knowledgeBases;
    }
    
    /**
     * Loads documents for a knowledge base
     * 
     * @param dbConnection Database connection
     * @param knowledgeBase Knowledge base to load documents for
     */
    private void loadDocumentsForKnowledgeBase(Connection dbConnection, KnowledgeBase knowledgeBase) {
        // Check for null input
        if (dbConnection == null || knowledgeBase == null) {
            System.err.println("Null connection or knowledge base in loadDocumentsForKnowledgeBase");
            return;
        }
        
        // Declare variables at the top
        PreparedStatement documentQuery = null;
        ResultSet documentResults = null;
        
        try {
            // Prepare statement to query documents for this knowledge base
            documentQuery = dbConnection.prepareStatement(SELECT_KB_DOCS);
            documentQuery.setString(1, knowledgeBase.getId());
            
            // Execute query and process results
            documentResults = documentQuery.executeQuery();
            while (documentResults.next()) {
                // Extract document data from result set
                String documentContent = documentResults.getString("content");
                String documentSource = documentResults.getString("source");
                
                // Create a new document and add it to the knowledge base
                DocumentData document = new DocumentData(documentContent, documentSource);
                knowledgeBase.addDocument(document);
            }
        } catch (SQLException sqlException) {
            // Log the error
            System.err.println("Error loading documents for knowledge base: " + sqlException.getMessage());
            sqlException.printStackTrace();
        } finally {
            // Close resources in finally block to ensure they are closed
            try {
                if (documentResults != null) {
                    documentResults.close();
                }
                if (documentQuery != null) {
                    documentQuery.close();
                }
                // Note: We don't close the connection here as it's managed by the calling method
            } catch (SQLException closeException) {
                System.err.println("Error closing database resources: " + closeException.getMessage());
            }
        }
    }
    
    /**
     * Deletes a knowledge base from the database
     * 
     * @param knowledgeBaseId ID of the knowledge base to delete
     */
    public void deleteKnowledgeBase(String knowledgeBaseId) {
        // Check for null or empty input
        if (knowledgeBaseId == null || knowledgeBaseId.isEmpty()) {
            System.err.println("Cannot delete knowledge base with null or empty ID");
            return;
        }
        
        // Declare variables at the top
        Connection dbConnection = null;
        PreparedStatement deleteDocumentsStatement = null;
        PreparedStatement deleteKnowledgeBaseStatement = null;
        
        try {
            // Establish database connection
            dbConnection = DriverManager.getConnection(DB_URL);
            
            // First delete associated documents
            deleteDocumentsStatement = dbConnection.prepareStatement(DELETE_KB_DOCS);
            deleteDocumentsStatement.setString(1, knowledgeBaseId);
            deleteDocumentsStatement.executeUpdate();
            
            // Then delete the knowledge base
            deleteKnowledgeBaseStatement = dbConnection.prepareStatement(DELETE_KB);
            deleteKnowledgeBaseStatement.setString(1, knowledgeBaseId);
            deleteKnowledgeBaseStatement.executeUpdate();
            
        } catch (SQLException sqlException) {
            // Log the error
            System.err.println("Error deleting knowledge base: " + sqlException.getMessage());
            sqlException.printStackTrace();
        } finally {
            // Close resources in finally block to ensure they are closed
            try {
                if (deleteKnowledgeBaseStatement != null) {
                    deleteKnowledgeBaseStatement.close();
                }
                if (deleteDocumentsStatement != null) {
                    deleteDocumentsStatement.close();
                }
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException closeException) {
                System.err.println("Error closing database resources: " + closeException.getMessage());
            }
        }
    }
} 
