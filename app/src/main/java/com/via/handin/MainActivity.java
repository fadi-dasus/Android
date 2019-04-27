package com.via.handin;

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

import com.via.handin.adapter.Adapter;

import com.via.handin.model.Note;
import com.via.handin.viewModel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;

    private List<Note> notes = new ArrayList<>();
    private Adapter adapter;
    private MainViewModel mainViewModel;

    @OnClick(R.id.fab)
    void fabClickHandler() {
        Intent intent = new Intent(this, Details.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        initiateRecyclerView();
        initiateViewModel();
            }

    private void initiateViewModel() {
        final Observer<List<Note>> notesObserver =
                new Observer<List<Note>>() {
                    @Override
                    // here I am receiving the notes data (the changed data)
                    // I am not getting the live data object but just the data it is wrapped around
                    public void onChanged(@Nullable List<Note> noteEntities) {
                        // first empty my collection because the reference to my collection
                        // will be maintained through the entire activity life cycle

                        notes.clear();
                        // then adding the new data coming from the view model
                        notes.addAll(noteEntities);

                        if (adapter == null) {
                            // it is not just this, but main activity . this because it is wrapped inside another object now
                            adapter = new Adapter(notes,
                                    MainActivity.this);
                            recyclerView.setAdapter(adapter);
                        }
                        // then when I got the data I need to refresh the recycle view
                        else {
                            adapter.notifyDataSetChanged();
                        }

                    }
                };
// finally I need to subscribe to this data, whenever it changes my on changed method will be called automatically
        mainViewModel = ViewModelProviders.of(this)
                .get(MainViewModel.class);
        mainViewModel.mNotes.observe(this, notesObserver);

    }

    private void initiateRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(
                recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_Data) {
            addDummyData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addDummyData() {
    mainViewModel.addDummyData();

    }
}
