package com.fadi.notetakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fadi.notetakingapp.adapter.NoteAdapter;
import com.fadi.notetakingapp.model.Note;

import com.fadi.notetakingapp.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    // butter knife library for reducing Boilerplate code when binding component
    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;

    @OnClick(R.id.fab)
    void fabClickHandler() {
        Intent intent = new Intent(this, EditorActivity.class);
        startActivity(intent);
    }

    private List<Note> notesData = new ArrayList<>();
    private NoteAdapter adapter;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // binding the two component initialised above
        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();

    }

    private void initViewModel() {
        // observing the changes coming from the live data object
        // this method will be called automatically whenever the data has been updated

        final Observer<List<Note>> notesObserver =
                new Observer<List<Note>>() {
                    @Override
                    // here I am receiving the notes data (the changed data)
                    // I am not getting the live data object but just the data it is wrapped around
                    public void onChanged(@Nullable List<Note> notes) {
                        // empty the collection because a reference it
                        // will be maintained through the entire activity life cycle

                        notesData.clear();
                        // then adding the new data coming from the view model
                        notesData.addAll(notes);

                        if (adapter == null) {
                            //activity.this and not only this because it is wrapped inside another object now
                            adapter = new NoteAdapter(notesData,
                                    MainActivity.this);
                            mRecyclerView.setAdapter(adapter);
                        }
                        // when I got the data I need to refresh the recycle view
                        else {
                            adapter.notifyDataSetChanged();
                        }

                    }
                };
// finally I need to subscribe to this data, whenever it changes my on changed method will be called automatically
        viewModel = ViewModelProviders.of(this)
                .get(MainViewModel.class);
        viewModel.notes.observe(this, notesObserver);
    }
// initialising the recycle view
    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
// add a line between the notes to increase readability
        DividerItemDecoration divider = new DividerItemDecoration(
                mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(divider);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //adding functionality
//        if (id == R.id.action_add_dummy_data) {
//            addSampleData();
//            return true;
//        } else
            if (id == R.id.action_delete_all) {
            removeAllData();
        }
        else if (id == R.id.action_get_hint) {
            Intent intent = new Intent(this,HintActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.action_sign_in) {
            Intent intent = new Intent(this,SignInActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    private void removeAllData() {
        viewModel.removeAllData();

    }

// for testing
    private void addSampleData() {
        viewModel.addSampleData();
    }
}