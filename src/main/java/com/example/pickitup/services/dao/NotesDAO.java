package com.example.pickitup.services.dao;

import com.example.pickitup.services.models.NotesDataModel;
import com.example.pickitup.services.MongoDBManager;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

public class NotesDAO {
    private final MongoCollection<Document> collection;

    public NotesDAO() {
        this.collection = MongoDBManager.getDatabase().getCollection("Notes");
    }

    //CRUD operations
    //Create
    public void createNote(NotesDataModel note) {
        collection.insertOne(note.toDocument());
    }
    //Read
    public void listNotes() {
        try (MongoCursor<Document> cursor = collection.find().iterator()){
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                System.out.println("Title: " + doc.getString("title"));
                System.out.println("Author: " + doc.getString("content"));
                System.out.println("-----------------------------------------");
            }
        }
    }
    //Update
    public void updateNote(String title, String newContent) {
        Document query = new Document("title", title);
        Document update = new Document("$set", new Document("content", newContent));
        collection.updateOne(query, update);
    }
    //Delete
    public void deleteNote(String title) {
        Document query = new Document("title", title);
        collection.deleteOne(query);
    }
}
