package com.example.pickitup.ai;
import com.example.pickitup.ai.LargeLanguageModelParameters;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;

/**
 * Class responsible for creating and managing the large language model instance
 *
 * @author Maaz Haque
 * @version 1.0
 */
public class LargeLanguageModel {
    // Constants for error messages
    private static final String ERROR_MODEL_INIT = "Failed to initialize language model: ";
    private static final String ERROR_RESPONSE_GEN = "Failed to generate response: ";

    // Initializing the langchain4j Large Language Model object instance
    private final ChatLanguageModel largeLanguageModel;

    /**
     * Constructor for the Large Language Model
     * Initializes the model using the createChatModel method
     */
    public LargeLanguageModel() {
        this.largeLanguageModel = createChatModel();
    } //End of constructor

    /**
     * Creates and configures a ChatLanguageModel using OpenAI
     * Uses parameters from LargeLanguageModelParameters class
     *
     * @return Configured ChatLanguageModel
     */
    public static ChatLanguageModel createChatModel() {
        try {
            return OpenAiChatModel.builder()
                .apiKey(LargeLanguageModelParameters.API_KEY)
                .modelName(LargeLanguageModelParameters.MODEL_NAME)
                .maxTokens(LargeLanguageModelParameters.MAX_TOKENS)
                .temperature(LargeLanguageModelParameters.TEMPERATURE)
                .build();
        } catch (Exception e) {
            // Log the error
            System.err.println(ERROR_MODEL_INIT + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(ERROR_MODEL_INIT, e);
        }
    }

    /**
     * Method to generate a response from the Large Language Model
     *
     * @param userMessage Message from user to respond to
     * @return AI generated response
     */
    public String generateResponse(String userMessage) {
        // Validate input
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Message cannot be empty";
        }

        try {
            // Generating a response from the Large Language Model
            return largeLanguageModel.chat(userMessage);
        } catch (Exception e) {
            // Log the error
            System.err.println(ERROR_RESPONSE_GEN + e.getMessage());
            e.printStackTrace();
            return "Error generating response. Please try again.";
        }
    }

    /**
     * Sample main method to demonstrate usage
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Creating an instance of the Large Language Model
            LargeLanguageModel largeLanguageModel = new LargeLanguageModel();

            // Test message
            String testMessage = "Hello, how are you?";

            // Generating a response from the Large Language Model
            String response = largeLanguageModel.generateResponse(testMessage);

            // Printing the response
            System.out.println(response);
        } catch (Exception e) {
            // Log any errors during execution
            System.err.println("Error in main method: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
