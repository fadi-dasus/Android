package com.fadi.notetakingapp.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.fadi.notetakingapp.model.Note;
import com.fadi.notetakingapp.utility.SampleData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// the repository is singleton so that activities and fragments will share the same data
public class AppRepository {

    private static AppRepository ourInstance;

    // live data object can publish changes and then an activity can subscribe to observe these changes
    // and react whenever the data need to be updated visually
    public LiveData<List<Note>> notes;

    private AppDatabase db;
    // this object is for running the methods in the room library synchronously
    // in order to run multiple database operations at the same time
    private Executor executor = Executors.newSingleThreadExecutor();

    // getting singleton repository instance
    public static AppRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AppRepository(context);
        }
        return ourInstance;
    }
    // constructor for initialising the fields
    private AppRepository(Context context) {
        db = AppDatabase.getInstance(context);
        notes = getAllNotes();
    }

    // this method has been used for testing purposes
    public void addDummyData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {

                db.noteDao().insertAll(SampleData.getNotes());
            }
        });

    }


    // we do not need to use the executor object here because the thread will be managed
    // by the room library in the background
    private LiveData<List<Note>> getAllNotes() {
        return db.noteDao().getAll();
    }


    // every time the room library returns Live data object it will handle the threading
    // all other cases we need to do it using the Executor class
    public void removeAllNotes() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.noteDao().deleteAll();
            }
        });

    }

    // simple db operation no need to run it on another thread
    public Note getNoteByid(int noteId) {
        return db.noteDao().getNoteById(noteId);
    }


    // inserting a note
    // it will work for both new or edited one)
    // because in the dao class the insert method annotated with Conflict annotation = replace
    public void insertNote(final Note note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {

                db.noteDao().insertNote(note);

            }
        });
    }

    //delete a note
    // it should be final because I am referencing from an outside object
    public void deleteNote(final Note value) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.noteDao().deleteNote(value);
            }
        });
    }
}
