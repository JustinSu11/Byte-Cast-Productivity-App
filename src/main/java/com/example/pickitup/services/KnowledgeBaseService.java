package com.example.pickitup.services;

import com.example.pickitup.services.models.KnowledgeBase;
import com.example.pickitup.services.models.DocumentData;
import com.example.pickitup.services.dao.KnowledgeBaseDAO;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Service for managing knowledge bases
 * @author Maaz Haque
 * @version 1.0
 */
public class KnowledgeBaseService {
    
    // Constants for default knowledge base
    private static final String DEFAULT_KB_NAME = "Default Knowledge Base";
    private static final String DEFAULT_KB_DESCRIPTION = "Default knowledge base for documents";
    
    // Member variables for managing knowledge bases
    private final KnowledgeBaseDAO knowledgeBaseDAO;
    private final Map<String, KnowledgeBase> knowledgeBases;
    private String activeKnowledgeBaseId;
    
    /**
     * Constructor initializes the knowledge base service and loads existing knowledge bases
     */
    public KnowledgeBaseService() {
        // Initialize the DAO and data structures
        this.knowledgeBaseDAO = new KnowledgeBaseDAO();
        this.knowledgeBases = new HashMap<>();
        
        // Load existing knowledge bases from database
        loadKnowledgeBasesFromDatabase();
        
        // If no knowledge bases exist, create a default one
        if (knowledgeBases.isEmpty()) {
            createKnowledgeBase(DEFAULT_KB_NAME, DEFAULT_KB_DESCRIPTION);
        }
        
        // Set active knowledge base to the first one if not set
        if (activeKnowledgeBaseId == null && !knowledgeBases.isEmpty()) {
            activeKnowledgeBaseId = knowledgeBases.values().iterator().next().getId();
        }
    }
    
    /**
     * Loads knowledge bases from the database
     */
    private void loadKnowledgeBasesFromDatabase() {
        // Get all knowledge bases from the DAO
        List<KnowledgeBase> loadedKnowledgeBases = knowledgeBaseDAO.getAllKnowledgeBases();
        
        // Add each knowledge base to the map
        for (KnowledgeBase kb : loadedKnowledgeBases) {
            // Skip null knowledge bases
            if (kb == null) {
                continue;
            }
            
            knowledgeBases.put(kb.getId(), kb);
        }
    }
    
    /**
     * Creates a new knowledge base
     * 
     * @param name Name of the knowledge base
     * @param description Description of the knowledge base
     * @return The created knowledge base
     */
    public KnowledgeBase createKnowledgeBase(String name, String description) {
        // Validate input parameters
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Cannot create knowledge base with null or empty name");
            return null;
        }
        
        // Initialize variables at the top
        String id = UUID.randomUUID().toString();
        KnowledgeBase knowledgeBase = null;
        
        try {
            // Create a new knowledge base with the given parameters
            knowledgeBase = new KnowledgeBase(id, name, description);
            
            // Save to memory and database
            knowledgeBases.put(id, knowledgeBase);
            knowledgeBaseDAO.saveKnowledgeBase(knowledgeBase);
            
            // If this is the first knowledge base, make it active
            if (activeKnowledgeBaseId == null) {
                activeKnowledgeBaseId = id;
            }
        } catch (Exception e) {
            // Log the error
            System.err.println("Error creating knowledge base: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Return the created knowledge base
        return knowledgeBase;
    }
    
    /**
     * Deletes a knowledge base
     * 
     * @param knowledgeBaseId ID of the knowledge base to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteKnowledgeBase(String knowledgeBaseId) {
        // Validate input parameter
        if (knowledgeBaseId == null || knowledgeBaseId.trim().isEmpty()) {
            System.err.println("Cannot delete knowledge base with null or empty ID");
            return false;
        }
        
        // Don't delete if it's the only knowledge base
        if (knowledgeBases.size() <= 1) {
            System.err.println("Cannot delete the only knowledge base");
            return false;
        }
        
        try {
            // Remove the knowledge base from the map
            KnowledgeBase knowledgeBase = knowledgeBases.remove(knowledgeBaseId);
            
            // If knowledge base was found and removed
            if (knowledgeBase != null) {
                // Delete from database
                knowledgeBaseDAO.deleteKnowledgeBase(knowledgeBaseId);
                
                // If the active knowledge base was deleted, set another one as active
                if (knowledgeBaseId.equals(activeKnowledgeBaseId)) {
                    // Get the first available knowledge base ID
                    activeKnowledgeBaseId = knowledgeBases.keySet().iterator().next();
                }
                
                return true;
            }
        } catch (Exception e) {
            // Log the error
            System.err.println("Error deleting knowledge base: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Return false if deletion failed
        return false;
    }
    
    /**
     * Gets all knowledge bases
     * 
     * @return List of all knowledge bases
     */
    public List<KnowledgeBase> getAllKnowledgeBases() {
        // Return a new list to avoid exposing the internal collection
        return new ArrayList<>(knowledgeBases.values());
    }
    
    /**
     * Gets a knowledge base by ID
     * 
     * @param id The ID of the knowledge base to get
     * @return The knowledge base or null if not found
     */
    public KnowledgeBase getKnowledgeBase(String id) {
        // Validate input parameter
        if (id == null || id.trim().isEmpty()) {
            System.err.println("Cannot get knowledge base with null or empty ID");
            return null;
        }
        
        // Return the knowledge base from the map
        return knowledgeBases.get(id);
    }
    
    /**
     * Gets the active knowledge base
     * 
     * @return The active knowledge base
     */
    public KnowledgeBase getActiveKnowledgeBase() {
        // Check if active knowledge base ID is set
        if (activeKnowledgeBaseId == null) {
            System.err.println("No active knowledge base ID set");
            return null;
        }
        
        // Return the active knowledge base
        return knowledgeBases.get(activeKnowledgeBaseId);
    }
    
    /**
     * Sets the active knowledge base
     * 
     * @param knowledgeBaseId ID of the knowledge base to set as active
     * @return true if successful, false if the ID is invalid
     */
    public boolean setActiveKnowledgeBase(String knowledgeBaseId) {
        // Validate input parameter
        if (knowledgeBaseId == null || knowledgeBaseId.trim().isEmpty()) {
            System.err.println("Cannot set active knowledge base with null or empty ID");
            return false;
        }
        
        // Check if the knowledge base exists
        if (knowledgeBases.containsKey(knowledgeBaseId)) {
            // Set as active
            activeKnowledgeBaseId = knowledgeBaseId;
            return true;
        }
        
        // Knowledge base not found
        System.err.println("Knowledge base not found: " + knowledgeBaseId);
        return false;
    }
    
    /**
     * Adds a document to a knowledge base
     * 
     * @param knowledgeBaseId ID of the knowledge base
     * @param document The document to add
     * @return true if successful, false otherwise
     */
    public boolean addDocumentToKnowledgeBase(String knowledgeBaseId, DocumentData document) {
        // Validate input parameters
        if (knowledgeBaseId == null || knowledgeBaseId.trim().isEmpty()) {
            System.err.println("Cannot add document to knowledge base with null or empty ID");
            return false;
        }
        
        if (document == null) {
            System.err.println("Cannot add null document to knowledge base");
            return false;
        }
        
        try {
            // Get the knowledge base
            KnowledgeBase knowledgeBase = knowledgeBases.get(knowledgeBaseId);
            
            // If knowledge base was found
            if (knowledgeBase != null) {
                // Add document to knowledge base
                knowledgeBase.addDocument(document);
                
                // Save changes to database
                knowledgeBaseDAO.saveKnowledgeBase(knowledgeBase);
                
                return true;
            }
        } catch (Exception e) {
            // Log the error
            System.err.println("Error adding document to knowledge base: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Return false if adding document failed
        return false;
    }
    
    /**
     * Adds a document to the active knowledge base
     * 
     * @param document The document to add
     * @return true if successful, false otherwise
     */
    public boolean addDocumentToActiveKnowledgeBase(DocumentData document) {
        // Validate active knowledge base ID
        if (activeKnowledgeBaseId == null) {
            System.err.println("No active knowledge base set");
            return false;
        }
        
        // Add document to the active knowledge base
        return addDocumentToKnowledgeBase(activeKnowledgeBaseId, document);
    }
} 