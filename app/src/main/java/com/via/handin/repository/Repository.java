package com.via.handin.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.via.handin.database.Database;
import com.via.handin.dummyData.Dummy;
import com.via.handin.model.Note;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Repository {
    public LiveData<List<Note>> notes;
    private static Repository ourInstance;
    private Database db;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static Repository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new Repository(context);
        }
        return ourInstance;
    }


    private Repository(Context context) {
        notes = getAllNotes();
        db = Database.getInstance(context);
    }


    public void addDummyData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.noteDao().insertAll(Dummy.getNotes());
            }
        });

    }

    private LiveData<List<Note>> getAllNotes() {
        return db.noteDao().getAll();
    }

}
