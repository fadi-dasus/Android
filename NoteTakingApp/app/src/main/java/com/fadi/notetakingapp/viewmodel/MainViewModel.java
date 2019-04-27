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

    public LiveData<List<Note>> notes;
    private AppRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application.getApplicationContext());
        notes = repository.notes;
    }

    public void addSampleData() {
        repository.addDummyData();
    }

    public void removeAllData() {
        repository.removeAllNotes();
    }
}
