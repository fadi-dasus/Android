package com.fadi.notetakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.fadi.notetakingapp.database.AppRepository;
import com.fadi.notetakingapp.model.Note;

import java.util.List;

// WE NEED a view model class for each activity
public class MainViewModel extends AndroidViewModel {
// using a live data object, hence no need to the executor class( live data mange the thread in the background)
    public LiveData<List<Note>> notes;
    private AppRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application.getApplicationContext());
        notes = repository.notes;
    }
// method for testing purposes
    public void addSampleData() {
        repository.addDummyData();
    }

    public void removeAllData() {
        repository.removeAllNotes();
    }
}
