package com.example.pickitup.ai;
/***************************
 Author: Maaz Haqe
 Date: 3/6/2025
 Version: 1.0
 Purpose: This class contains the necessary parameters for the Large Language Model Class
 ***********************/
import io.github.cdimascio.dotenv.Dotenv;


public class LargeLanguageModelParameters {
    //API Key for OpenAI
    private static final Dotenv dotenv = Dotenv.configure().load();
    public static final String API_KEY = dotenv.get("OPENAI_API_KEY");
    //Model Name
    public static final String MODEL_NAME = "gpt-4o-mini";
    //Max Tokens that the model can generate (2048 is usually the default but since we are on trial rn we will use 500)
    public static final int MAX_TOKENS = 500;
    //Temperature of the model (between 0.0 and 1.0, the less it is the less creative and more precise the model is)
    public static final double TEMPERATURE = 0.0;

    //The System Prompt for the Large Language Model
    public static final String SYSTEM_MESSAGE = "You are an AI Chatbot assistant who is helping a user who is currently working with a text editor to take notes. Based on the Chat Message History, and the current notes file if the user attaches it, answer the users question";

} //end of class LargeLanguageModelParameters
