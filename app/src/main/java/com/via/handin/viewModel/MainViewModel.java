package com.via.handin.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.via.handin.dummyData.Dummy;
import com.via.handin.model.Note;
import com.via.handin.repository.Repository;

import java.util.List;

// WE NEED a view model class for each activity
public class MainViewModel extends AndroidViewModel {

    public LiveData<List<Note>> mNotes;
    private Repository repository;


    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(application.getApplicationContext());
        mNotes = repository.notes;

    }

    public void addDummyData() {
        repository.addDummyData();
    }
}