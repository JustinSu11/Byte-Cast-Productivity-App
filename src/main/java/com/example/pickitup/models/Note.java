package com.example.pickitup.models;

import java.time.LocalDateTime; //For time of creation/update

public class Note {
    //Create String variables to contain a note title, the contents of the note, and the id for storing and retrieving
    //A 'long' data type is used here because long can store a bigger number than 'int'.
    //'BigInteger' cannot be used here because in SQLite, a table row can hold up to a 64-bit for IDs
    private long id = 0;
    private String title = null;
    private String content = null;
    //variables for last updated and creation time
    private LocalDateTime createdAt = null;
    private LocalDateTime updatedAt = null;

    //Default constructor
    public Note() {}

    //Parameterized constructor
    public Note(String title, String content){
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    //Parameterized constructor for retrieving note from the database
    public Note(long id, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //Getters
    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    //Setters
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
