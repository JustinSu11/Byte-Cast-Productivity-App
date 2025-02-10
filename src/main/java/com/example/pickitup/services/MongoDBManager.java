package com.example.pickitup.services;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;

public class MongoDBManager {
    //declare database object
    private static MongoDatabase database;
    //The purpose of a static {} block is to run the code within ONE time when the class is loaded into memory even before creating an instance of the class.
    //This prevents repetitive connections and establishes the connection with the database RIGHT when the class is loaded
    static {
        //Uses the dotenv library to get environment variables from a .env file without having to retrieve the .env file path
        //This ensures security when it comes to pushing our database credentials into GitHub
        Dotenv dotenv = Dotenv.load();
        String username = dotenv.get("MONGODB_USERNAME");
        String password = dotenv.get("MONGODB_PASSWORD");
        //The String.format() method uses %s, %f, %etc datatypes as placeholders in the url, this allows us to replace those placeholders with our environmental variables which are the other arguments in the parameters.
        String uri = String.format("mongodb+srv://%s:%s@pickitup.vfk14.mongodb.net/?retryWrites=true&w=majority&appName=PickItUp", username, password);
        //Attempts to make a connection and prints a message when connection is successful and an error message when unsuccessful.
        try(MongoClient mongoClient = MongoClients.create(uri)) {
            database = mongoClient.getDatabase("Pick_It_Up");
            System.out.println("Connected to database successfully");
        } catch (MongoException e) {
            System.err.println("MongoDB connection failed: " + e.getMessage());
        }
    }
    //method to be used by other classes.
    public static MongoDatabase getDatabase() {
        return database;
    }
}
