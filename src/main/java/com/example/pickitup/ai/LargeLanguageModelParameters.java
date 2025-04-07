package com.example.pickitup.ai;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Contains the necessary parameters for the Large Language Model Class
 * Includes API key retrieval, model configuration and system prompts
 * 
 * @author Maaz Haque
 * @version 1.0
 */
public class LargeLanguageModelParameters {
    // Default API key environment variable name
    private static final String API_KEY_ENV_VAR = "OPENAI_API_KEY";
    
    // Default error message for missing API key
    private static final String ERROR_MISSING_API_KEY = "OpenAI API key not found in environment variables";
    
    // Default fallback API key (should be replaced in production)
    private static final String DEFAULT_API_KEY = "sk-default-key-for-development-only";
    
    // Initialize environment variables
    private static final Dotenv dotenv;
    
    // API Key for OpenAI
    public static final String API_KEY;
    
    // Model Name
    public static final String MODEL_NAME = "gpt-4o-mini";
    
    // Max Tokens that the model can generate (2048 is usually the default but using 500 for trial)
    public static final int MAX_TOKENS = 500;
    
    // Temperature of the model (between 0.0 and 1.0, lower value means less creative and more precise)
    public static final double TEMPERATURE = 0.0;

    // The System Prompt for the Large Language Model
    public static final String SYSTEM_MESSAGE = "You are an AI Chatbot assistant who is helping a user who is currently working with a text editor to take notes. Based on the Chat Message History, and the current notes file if the user attaches it, answer the users question";

    // Static initializer to load environment variables safely
    static {
        // Initialize dotenv
        try {
            dotenv = Dotenv.configure().load();
        } catch (Exception e) {
            System.err.println("Failed to load environment variables: " + e.getMessage());
            throw new RuntimeException("Failed to initialize environment configuration", e);
        }
        
        // Get API key from environment
        String apiKey = null;
        try {
            apiKey = dotenv.get(API_KEY_ENV_VAR);
            if (apiKey == null || apiKey.trim().isEmpty()) {
                System.err.println(ERROR_MISSING_API_KEY);
                System.err.println("Using default development key. This will not work in production.");
                apiKey = DEFAULT_API_KEY;
            }
        } catch (Exception e) {
            System.err.println("Error retrieving API key: " + e.getMessage());
            System.err.println("Using default development key. This will not work in production.");
            apiKey = DEFAULT_API_KEY;
        }
        
        API_KEY = apiKey;
    }
}
