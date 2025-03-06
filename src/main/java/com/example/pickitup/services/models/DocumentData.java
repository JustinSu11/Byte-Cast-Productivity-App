package com.example.pickitup.services.models;

/**
 * Model class representing a document for RAG retrieval
 * @author GitHub Copilot
 * @version 1.0
 */
public class DocumentData {
    private String content;
    private String source;
    
    /**
     * Constructor for creating a document with content and source
     * 
     * @param content The document content
     * @param source The source or identifier of the document
     */
    public DocumentData(String content, String source) {
        this.content = content;
        this.source = source;
    }
    
    /**
     * Gets the document content
     * 
     * @return The content of the document
     */
    public String getContent() {
        return content;
    }
    
    /**
     * Gets the document source
     * 
     * @return The source or identifier of the document
     */
    public String getSource() {
        return source;
    }
    
    /**
     * Sets the document content
     * 
     * @param content The content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    /**
     * Sets the document source
     * 
     * @param source The source to set
     */
    public void setSource(String source) {
        this.source = source;
    }
}
