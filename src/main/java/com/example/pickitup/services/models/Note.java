/*
* @author Justin Nguyen
* @version 1.0
* @updated 03/24/2025
* */

package com.example.pickitup.services.models;

import com.example.pickitup.services.dao.NotesDAO;

public class Note {
    //Create String variables to contain a note title, the contents of the note, and the id for storing and retrieving
    //A 'long' data type is used here because long can store a bigger number than 'int'.
    //'BigInteger' cannot be used here because in SQLite, a table row can hold up to a 64-bit for IDs
    private long note_id = 0;
    private long journal_id = 0;
    private String title = null;
    private String content = null;

    //Parameterized constructor
    public Note(String title, String content){
        this.title = title;
        this.content = content;
        NotesDAO.insertNoteAtCreation(this);
    }

    //Parameterized constructor for retrieving note from the database
    public Note(long note_id, long journal_id, String title, String content) {
        this.note_id = note_id;
        this.journal_id = journal_id;
        this.title = title;
        this.content = content;
    }

    //Getters
    public long getNoteId() {
        return note_id;
    }
    public long getJournalId() {return journal_id;}
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }

    //Setters
    public void setTitle(String title) {
        this.title = title;
        //updates existing note in database
        NotesDAO.saveNote(this);
    }
    public void setContent(String content) {
        this.content = content;
        //updates existing note in database
        NotesDAO.saveNote(this);
    }
    public void setNoteId(long note_id) {this.note_id = note_id;}
    public void setJournalId(long journal_id) {this.journal_id = journal_id;}
}
