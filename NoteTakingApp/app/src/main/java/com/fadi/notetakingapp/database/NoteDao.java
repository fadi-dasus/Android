package com.fadi.notetakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.fadi.notetakingapp.model.Note;

import java.util.List;
// interface supports annotation using the room library
@Dao
public interface NoteDao {
    // this allows the method to be used as insert or update
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    // this method is used only for testing purposes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Note> note);

    @Delete
    void deleteNote(Note note);

    @Query("SELECT * FROM notes WHERE id = :id")
    Note getNoteById(int id);

    @Query("SELECT * FROM notes ORDER BY date DESC")
    LiveData<List<Note>> getAll();

    @Query("DELETE FROM notes")
    int deleteAll();

    // this query is used only for testing
    @Query("SELECT COUNT(*) FROM notes")
    int getCount();


}
