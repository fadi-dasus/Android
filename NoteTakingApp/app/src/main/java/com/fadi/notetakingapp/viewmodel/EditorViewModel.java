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
    // for this view model I will use an object called mutable live data
    // this object has a method that let me change its value at runtime
    //unlike live data which is immutable
    // each time you post a value the object published that change to any observer
    // activity or fragment that observe to this view model

    public MutableLiveData<Note> liveData =
            new MutableLiveData<>();

    private Executor executor = Executors.newSingleThreadExecutor();
    private AppRepository repository;

    public EditorViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(getApplication());
    }


    public void getData(final int noteId) {
        // because it is not a live data object I need to manage the thread
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Note note = repository.getNoteByid(noteId);
                // when I am getting the note back I will post it using my MutableLiveData object
                // whenever I call post value this will cause the observer to call its change method and diplay the result
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
