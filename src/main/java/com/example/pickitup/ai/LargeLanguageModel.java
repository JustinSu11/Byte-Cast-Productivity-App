package com.example.pickitup.ai;
import com.example.pickitup.ai.LargeLanguageModelParameters;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;

public class LargeLanguageModel {
    //Initializing the langchain 4j Large Language Model object  instance
    private final ChatLanguageModel largeLanguageModel;

    //Constructor for the Large Language Model
    public LargeLanguageModel()
    {
        this.largeLanguageModel = createChatModel();
    } //End of constructor

    /**
     * Creates and configures a ChatLanguageModel using OpenAI
     * 
     * @return Configured ChatLanguageModel
     */
    public static ChatLanguageModel createChatModel() {
        return OpenAiChatModel.builder()
            .apiKey(LargeLanguageModelParameters.API_KEY)
            .modelName(LargeLanguageModelParameters.MODEL_NAME)
            .maxTokens(LargeLanguageModelParameters.MAX_TOKENS)
            .temperature(LargeLanguageModelParameters.TEMPERATURE)
            .build();
    }

    //Method to generate a response from the Large Language Model 
    public String generateResponse(String userMessage)
    {
        //Generating a response from the Large Language Model
        return largeLanguageModel.generate(userMessage);
    } //End of method generateResponse

    public static void main (String[] args)
    {
        //Creating an instance of the Large Language Model
        LargeLanguageModel largeLanguageModel = new LargeLanguageModel();
        //Generating a response from the Large Language Model
        String response = largeLanguageModel.generateResponse("Hello, how are you?");
        //Printing the response
        System.out.println(response);
    } //End of main method

}
