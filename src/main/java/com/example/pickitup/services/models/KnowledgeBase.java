/**
 * Model class representing a knowledge base containing multiple documents.
 * This class provides functionality to manage a collection of documents including
 * adding, removing, and retrieving documents. It also maintains metadata about
 * the knowledge base such as name, description, and creation time.
 *
 * @author Maaz Haque
 * @date 04/23/2025
 */
package com.example.pickitup.services.models;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;


public class KnowledgeBase {
    // Member variables for knowledge base

    /**
     * Unique identifier for this knowledge base
     */
    private String knowledgeBaseId;

    /**
     * User-friendly name for this knowledge base
     */
    private String knowledgeBaseName;
    
    /**
     * Optional description providing details about this knowledge base
     */
    private String knowledgeBaseDescription;

    /**
     * Timestamp recording when this knowledge base was created
     */
    private LocalDateTime knowledgeBaseCreationTime;

    /**
     * Collection of documents contained within this knowledge base
     */
    private List<DocumentData> knowledgeBaseDocuments;

    /**
     * Constructor for creating a new knowledge base.
     * Initializes a knowledge base with the provided attributes and
     * sets the creation timestamp to the current time.
     * 
     * @param knowledgeBaseId Unique identifier for the knowledge base
     * @param knowledgeBaseName User-friendly name for the knowledge base
     * @param knowledgeBaseDescription Optional description of the knowledge base
     * @throws IllegalArgumentException if knowledgeBaseId or knowledgeBaseName is null or empty
     */
    public KnowledgeBase(String knowledgeBaseId, String knowledgeBaseName, String knowledgeBaseDescription) {
        // Validate required parameters

        // Check if the knowledgeBaseId is null or empty
        if (knowledgeBaseId == null || knowledgeBaseId.trim().isEmpty()) {
            throw new IllegalArgumentException("Knowledge base ID cannot be null or empty");
        }
        
        // Check if the knowledgeBaseName is null or empty
        if (knowledgeBaseName == null || knowledgeBaseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Knowledge base name cannot be null or empty");
        }
        
        // Initialize member variables

        // Set the knowledgeBaseId field
        this.knowledgeBaseId = knowledgeBaseId;

        // Set the knowledgeBaseName field
        this.knowledgeBaseName = knowledgeBaseName;

        // Handle potential null description by converting to empty string
        if(knowledgeBaseDescription == null)
        {
            this.knowledgeBaseDescription = "";
        }
        else {
            this.knowledgeBaseDescription = knowledgeBaseDescription;
        }

        // Record the current time as creation timestamp
        this.knowledgeBaseCreationTime = LocalDateTime.now();

        // Initialize an empty list of documents
        this.knowledgeBaseDocuments = new ArrayList<>();
    }
    
    /**
     * Adds a document to this knowledge base.
     * The document is appended to the list of documents.
     * If a null document is provided, an error message is logged and no action is taken.
     * 
     * @param document The document to add to the knowledge base
     */
    public void addDocument(DocumentData document) {
        // Validate document parameter

        // Check if the document is null
        if (document == null) {
            System.err.println("Cannot add null document to knowledge base");
            return;
        }
        
        // Add the document to the list of documents
        knowledgeBaseDocuments.add(document);
    }
    
    /**
     * Removes a document from this knowledge base.
     * If the document exists in the knowledge base, it is removed.
     * If a null document is provided, an error message is logged and false is returned.
     * 
     * @param document The document to remove from the knowledge base
     * @return true if the document was successfully removed, false otherwise
     */
    public boolean removeDocument(DocumentData document) {
        // Validate document parameter

        // Check if the document is null
        if (document == null) {
            System.err.println("Cannot remove null document from knowledge base");
            return false;
        }
        
        // Remove the document from the list and return the result
        // This returns true if the document was found and removed, false otherwise
        return knowledgeBaseDocuments.remove(document);
    }
    
    /**
     * Gets all documents in this knowledge base.
     * Returns a new list containing all documents to avoid exposing the internal collection,
     * which prevents external modification of the knowledge base's document list.
     * 
     * @return A copy of the list of all documents in this knowledge base
     */
    public List<DocumentData> getDocuments() {
        // Return a new list to avoid exposing the internal collection
        // This creates a defensive copy of the documents list
        return new ArrayList<>(knowledgeBaseDocuments);
    }
    
    /**
     * Checks if this knowledge base contains any documents.
     * A knowledge base is considered empty if it has no documents.
     * 
     * @return true if the knowledge base has no documents, false otherwise
     */
    public boolean isEmpty() {
        // Check if the documents list is empty
        // This delegates to the isEmpty method of the documents list
        return knowledgeBaseDocuments.isEmpty();
    }
    
    /**
     * Gets the number of documents in this knowledge base.
     * 
     * @return The current count of documents in this knowledge base
     */
    public int getDocumentCount() {
        // Return the size of the documents list
        // This delegates to the size method of the documents list
        return knowledgeBaseDocuments.size();
    }
    
    // Getters and setters
    
    /**
     * Gets the knowledge base ID.
     * The ID is a unique identifier for this knowledge base.
     * 
     * @return The ID of this knowledge base
     */
    public String getId() {
        // Return the knowledgeBaseId field
        return knowledgeBaseId;
    }
    
    /**
     * Gets the knowledge base name.
     * The name is a user-friendly identifier for this knowledge base.
     * 
     * @return The name of this knowledge base
     */
    public String getName() {
        // Return the knowledgeBaseName field
        return knowledgeBaseName;
    }
    
    /**
     * Sets the knowledge base name.
     * The new name must not be null or empty.
     * If an invalid name is provided, an error message is logged and no change is made.
     * 
     * @param knowledgeBaseName The new name for this knowledge base
     */
    public void setName(String knowledgeBaseName) {
        // Validate name parameter

        // Check if the name is null or empty
        if (knowledgeBaseName == null || knowledgeBaseName.trim().isEmpty()) {
            System.err.println("Cannot set null or empty knowledge base name");
            return;
        }
        
        // Update the name field with the new value
        this.knowledgeBaseName = knowledgeBaseName;
    }
    
    /**
     * Gets the knowledge base description.
     * The description provides additional details about this knowledge base.
     * 
     * @return The description of this knowledge base
     */
    public String getDescription() {
        // Return the knowledgeBaseDescription field
        return knowledgeBaseDescription;
    }
    
    /**
     * Sets the knowledge base description.
     * If a null description is provided, it is converted to an empty string.
     * 
     * @param knowledgeBaseDescription The new description for this knowledge base
     */
    public void setDescription(String knowledgeBaseDescription) {
        // Update the description, converting null to empty string if necessary
        this.knowledgeBaseDescription = knowledgeBaseDescription != null ? knowledgeBaseDescription : "";
    }
    
    /**
     * Gets the creation date and time of this knowledge base.
     * The creation timestamp is set when the knowledge base is instantiated.
     * 
     * @return The timestamp when this knowledge base was created
     */
    public LocalDateTime getCreatedAt() {
        // Return the knowledgeBaseCreationTime field
        return knowledgeBaseCreationTime;
    }
    
    /**
     * Returns a string representation of the knowledge base.
     * The string includes the name and the number of documents.
     * 
     * @return A string in the format "name (X documents)"
     */
    @Override
    public String toString() {
        // Return a formatted string with name and document count
        // This creates a human-readable representation of the knowledge base
        return knowledgeBaseName + " (" + knowledgeBaseDocuments.size() + " documents)";
    }
}
