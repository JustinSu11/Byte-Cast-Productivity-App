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
        Connection conn = null;
        Statement stmt = null;
        
        try {
            // Establish database connection
            conn = DriverManager.getConnection(DB_URL);
            // Create statement for executing SQL
            stmt = conn.createStatement();
            
            // Create knowledge_bases table if it doesn't exist
            stmt.execute(CREATE_KB_TABLE);
            
            // Create knowledge_base_documents table if it doesn't exist
            stmt.execute(CREATE_KB_DOCS_TABLE);
            
        } catch (SQLException e) {
            // Log the error 
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources in finally block to ensure they are closed
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
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
        Connection conn = null;
        PreparedStatement pstmtKb = null;
        PreparedStatement pstmtDelete = null;
        PreparedStatement pstmtDocs = null;
        
        try {
            // Establish database connection
            conn = DriverManager.getConnection(DB_URL);
            
            // First save or update the knowledge base
            pstmtKb = conn.prepareStatement(INSERT_KB);
            pstmtKb.setString(1, knowledgeBase.getId());
            pstmtKb.setString(2, knowledgeBase.getName());
            pstmtKb.setString(3, knowledgeBase.getDescription());
            pstmtKb.setString(4, knowledgeBase.getCreatedAt().toString());
            pstmtKb.executeUpdate();
            
            // Then handle documents
            // First, remove all existing documents for this knowledge base
            pstmtDelete = conn.prepareStatement(DELETE_KB_DOCS);
            pstmtDelete.setString(1, knowledgeBase.getId());
            pstmtDelete.executeUpdate();
            
            // Get the list of documents
            List<DocumentData> documents = knowledgeBase.getDocuments();
            
            // Then insert all current documents
            if (documents != null && !documents.isEmpty()) {
                pstmtDocs = conn.prepareStatement(INSERT_KB_DOC);
                
                for (DocumentData doc : documents) {
                    // Skip null documents
                    if (doc == null) {
                        continue;
                    }
                    
                    // Generate a new UUID for the document ID
                    String docId = UUID.randomUUID().toString();
                    
                    pstmtDocs.setString(1, docId);
                    pstmtDocs.setString(2, knowledgeBase.getId());
                    pstmtDocs.setString(3, doc.getContent());
                    pstmtDocs.setString(4, doc.getSource());
                    pstmtDocs.executeUpdate();
                }
            }
            
        } catch (SQLException e) {
            // Log the error
            System.err.println("Error saving knowledge base: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources in finally block to ensure they are closed
            try {
                if (pstmtDocs != null) {
                    pstmtDocs.close();
                }
                if (pstmtDelete != null) {
                    pstmtDelete.close();
                }
                if (pstmtKb != null) {
                    pstmtKb.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
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
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            // Establish database connection
            conn = DriverManager.getConnection(DB_URL);
            
            // Create statement and execute query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SELECT_ALL_KB);
            
            // Process each row in the result set
            while (rs.next()) {
                // Extract knowledge base data from result set
                String id = rs.getString("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                
                // Create a new knowledge base object
                KnowledgeBase kb = new KnowledgeBase(id, name, description);
                
                // Load documents for this knowledge base
                loadDocumentsForKnowledgeBase(conn, kb);
                
                // Add the knowledge base to the result list
                knowledgeBases.add(kb);
            }
            
        } catch (SQLException e) {
            // Log the error
            System.err.println("Error retrieving knowledge bases: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources in finally block to ensure they are closed
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
        
        // Return the list of knowledge bases
        return knowledgeBases;
    }
    
    /**
     * Loads documents for a knowledge base
     * 
     * @param conn Database connection
     * @param knowledgeBase Knowledge base to load documents for
     */
    private void loadDocumentsForKnowledgeBase(Connection conn, KnowledgeBase knowledgeBase) {
        // Check for null input
        if (conn == null || knowledgeBase == null) {
            System.err.println("Null connection or knowledge base in loadDocumentsForKnowledgeBase");
            return;
        }
        
        // Declare variables at the top
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            // Prepare statement to query documents for this knowledge base
            pstmt = conn.prepareStatement(SELECT_KB_DOCS);
            pstmt.setString(1, knowledgeBase.getId());
            
            // Execute query and process results
            rs = pstmt.executeQuery();
            while (rs.next()) {
                // Extract document data from result set
                String content = rs.getString("content");
                String source = rs.getString("source");
                
                // Create a new document and add it to the knowledge base
                DocumentData doc = new DocumentData(content, source);
                knowledgeBase.addDocument(doc);
            }
        } catch (SQLException e) {
            // Log the error
            System.err.println("Error loading documents for knowledge base: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources in finally block to ensure they are closed
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                // Note: We don't close the connection here as it's managed by the calling method
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
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
        Connection conn = null;
        PreparedStatement pstmtDoc = null;
        PreparedStatement pstmtKb = null;
        
        try {
            // Establish database connection
            conn = DriverManager.getConnection(DB_URL);
            
            // First delete associated documents
            pstmtDoc = conn.prepareStatement(DELETE_KB_DOCS);
            pstmtDoc.setString(1, knowledgeBaseId);
            pstmtDoc.executeUpdate();
            
            // Then delete the knowledge base
            pstmtKb = conn.prepareStatement(DELETE_KB);
            pstmtKb.setString(1, knowledgeBaseId);
            pstmtKb.executeUpdate();
            
        } catch (SQLException e) {
            // Log the error
            System.err.println("Error deleting knowledge base: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources in finally block to ensure they are closed
            try {
                if (pstmtKb != null) {
                    pstmtKb.close();
                }
                if (pstmtDoc != null) {
                    pstmtDoc.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
    }
} 