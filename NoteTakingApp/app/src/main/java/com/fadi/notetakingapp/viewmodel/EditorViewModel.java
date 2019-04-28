package com.fadi.notetakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fadi.notetakingapp.database.AppRepository;
import com.fadi.notetakingapp.model.Note;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class EditorViewModel extends AndroidViewModel {
    // in this view model I will use a mutable live data object
    // which allows me change its value at runtime
    //unlike live data which is immutable
    // each time we post a value the object publishes the changes to all observers
    // (activity or fragment) that observe this view model
    // one downside of this object is that I need to manage the threading unlike the live data which do it in the background
    public MutableLiveData<Note> liveData =
            new MutableLiveData<>();
// Executor object to manage the thread
    private Executor executor = Executors.newSingleThreadExecutor();
    private AppRepository repository;

    public EditorViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(getApplication());
    }


    public void getData(final int noteId) {
        // getting the data using the executor object for managing threading
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Note note = repository.getNoteByid(noteId);
                // when I am getting the note back I will post it using my MutableLiveData object
                // whenever I call post value this will cause the observer to call its change method and display the content
                liveData.postValue(note);
            }
        });
    }

    public void saveChanges(String text) {
// to check if it is a new note or I already have it
        Note note = liveData.getValue();
        if (note == null) {
            if (TextUtils.isEmpty(text.trim())) {
                return;
            }
            note = new Note(new Date(), text.trim());
        } else {
            note.setText(text);
        }
        repository.insertNote(note);
    }

    public void deleteNote() {
        // I have reference to my note object inside my liveNote object
        // but I do not want to pass the live data I just want the value or the note
        repository.deleteNote(liveData.getValue());
    }
}
