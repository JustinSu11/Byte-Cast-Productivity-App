package com.example.pickitup.services.models;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Model class representing a knowledge base containing multiple documents
 * @author Maaz Haque
 * @version 1.0
 */
public class KnowledgeBase {
    // Member variables for knowledge base
    private String id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<DocumentData> documents;
    
    /**
     * Constructor for creating a new knowledge base
     * 
     * @param id Unique identifier for the knowledge base
     * @param name User-friendly name for the knowledge base
     * @param description Optional description of the knowledge base
     */
    public KnowledgeBase(String id, String name, String description) {
        // Validate required parameters
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Knowledge base ID cannot be null or empty");
        }
        
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Knowledge base name cannot be null or empty");
        }
        
        // Initialize member variables
        this.id = id;
        this.name = name;
        this.description = description != null ? description : "";
        this.createdAt = LocalDateTime.now();
        this.documents = new ArrayList<>();
    }
    
    /**
     * Adds a document to this knowledge base
     * 
     * @param document The document to add
     */
    public void addDocument(DocumentData document) {
        // Validate document parameter
        if (document == null) {
            System.err.println("Cannot add null document to knowledge base");
            return;
        }
        
        // Add the document to the list
        documents.add(document);
    }
    
    /**
     * Removes a document from this knowledge base
     * 
     * @param document The document to remove
     * @return true if the document was removed, false if it wasn't found
     */
    public boolean removeDocument(DocumentData document) {
        // Validate document parameter
        if (document == null) {
            System.err.println("Cannot remove null document from knowledge base");
            return false;
        }
        
        // Remove the document from the list and return the result
        return documents.remove(document);
    }
    
    /**
     * Gets all documents in this knowledge base
     * 
     * @return List of all documents
     */
    public List<DocumentData> getDocuments() {
        // Return a new list to avoid exposing the internal collection
        return new ArrayList<>(documents);
    }
    
    /**
     * Checks if this knowledge base contains any documents
     * 
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        // Check if the documents list is empty
        return documents.isEmpty();
    }
    
    /**
     * Gets the number of documents in this knowledge base
     * 
     * @return Document count
     */
    public int getDocumentCount() {
        // Return the size of the documents list
        return documents.size();
    }
    
    // Getters and setters
    
    /**
     * Gets the knowledge base ID
     * 
     * @return The ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the knowledge base name
     * 
     * @return The name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the knowledge base name
     * 
     * @param name The new name
     */
    public void setName(String name) {
        // Validate name parameter
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Cannot set null or empty knowledge base name");
            return;
        }
        
        // Update the name
        this.name = name;
    }
    
    /**
     * Gets the knowledge base description
     * 
     * @return The description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the knowledge base description
     * 
     * @param description The new description
     */
    public void setDescription(String description) {
        // Update the description (null is converted to empty string)
        this.description = description != null ? description : "";
    }
    
    /**
     * Gets the creation date and time
     * 
     * @return The creation date and time
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Returns a string representation of the knowledge base
     * 
     * @return String representation
     */
    @Override
    public String toString() {
        // Return a formatted string with name and document count
        return name + " (" + documents.size() + " documents)";
    }
} 