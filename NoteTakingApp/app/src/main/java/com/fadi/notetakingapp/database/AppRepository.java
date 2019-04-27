package com.fadi.notetakingapp.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.fadi.notetakingapp.model.Note;
import com.fadi.notetakingapp.utility.SampleData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// the repository is singleton so that everything (activities) in the
//application can share the same data
public class AppRepository {

    private static AppRepository ourInstance;

    // live data object can publish changes and then an activity can subscribe to observe those changes
    // and react whenever the data need to be updated visually
    public LiveData<List<Note>> notes;
    private AppDatabase db;
    // this object is for running the methods in the room library synchronised
    // and this will ensure that we are not running multiple database operations at the same time
    private Executor executor = Executors.newSingleThreadExecutor();

    public static AppRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AppRepository(context);
        }
        return ourInstance;
    }

    private AppRepository(Context context) {
        db = AppDatabase.getInstance(context);
        notes = getAllNotes();

    }

    public void addDummyData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {

                db.noteDao().insertAll(SampleData.getNotes());


            }
        });

    }


    // we do not need to use the executor object here because the threding will be managed
    // by the room library in the background
    private LiveData<List<Note>> getAllNotes() {
        return db.noteDao().getAll();
    }


    // note every time the room library returns Live data object it will handle the threading for you
    // all other cases you need to do it yourself
    public void removeAllNotes() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.noteDao().deleteAll();
            }
        });

    }

    public Note getNoteByid(int noteId) {
        return db.noteDao().getNoteById(noteId);
    }

    public void insertNote(final Note note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // it will work for both cases (new note or edited one)
                // because in the dao class the insert method has the on Conflict annotation = replace
                db.noteDao().insertNote(note);

            }
        });
    }

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
