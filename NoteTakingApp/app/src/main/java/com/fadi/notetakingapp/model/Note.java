package com.fadi.notetakingapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

// the main entity in the app


@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date date;
    private String text;

    public Note(int id, Date date, String text) {
        this.id = id;
        this.date = date;
        this.text = text;
    }

    // for editing an existing note
    @Ignore
    public Note(Date date, String text) {
        this.date = date;
        this.text = text;
    }
    // room library will use only one
    // constructor from this class, so we annotate other constructors with Ignore
    @Ignore
    public Note() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "NoteEntity{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text + '\'' +
                '}';
    }

}
