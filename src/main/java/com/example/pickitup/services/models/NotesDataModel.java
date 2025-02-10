package com.example.pickitup.services.models;

import org.bson.Document;

public class NotesDataModel {
    private String title;
    private String content;
    //Constructor
    public NotesDataModel(String title, String content) {
        this.title = title;
        this.content = content;
    }
    //Converts the title and content into BSON (Binary JSON)
    public Document toDocument() {
        return new Document("title", title).append("content", content);
    }
}
