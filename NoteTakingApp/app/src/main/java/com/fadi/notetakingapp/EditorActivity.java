package com.fadi.notetakingapp;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import com.fadi.notetakingapp.database.NoteEntity;
import com.fadi.notetakingapp.viewmodel.EditorViewModel;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.fadi.notetakingapp.adapter.NoteAdapter;


public class EditorActivity extends AppCompatActivity {


    // using butter knife to bind the text view that I have in the content.xml
    //which is the view for this note data
//then I need to bind it on create line35
    // when I did this, Ihave a place to disply the data

    @BindView(R.id.note_text)
    TextView mTextView;
    private EditorViewModel mViewModel;
    private boolean mNewNote = false;
    private boolean mEdit = false;
    public static final String My_Key = "KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            mEdit = savedInstanceState.getBoolean(My_Key);

        }
        initViewModel();
    }

    private void initViewModel() {

        // in this method I need to initialize the vew model to the appropriate class

        mViewModel = ViewModelProviders.of(this)
                .get(EditorViewModel.class);
        // I am going to be observing the view model live data and display the new note when it is
        // posted so I need a reference to my View in my layout (we are using content editor.xml)
        mViewModel.mLiveNote.observe(this, new Observer<NoteEntity>() {
            @Override
            public void onChanged(@Nullable NoteEntity noteEntity) {
                if (!mEdit) {
                    // requireNonNull is just to ensure that the note is not null it is like if not null
                    mTextView.setText(Objects.requireNonNull(noteEntity).getText());
                }
            }
        });

        Bundle mBundle = getIntent().getExtras();
        if (mBundle == null) {
            setTitle("New Note");
            mNewNote = true;
        } else {
            setTitle("Edit Note");
            int noteId = mBundle.getInt(NoteAdapter.NOTE_KEY);
            mViewModel.getData(noteId);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // we are checking the boolean to show the garbage just
        // for an existing note not for a new one
        if (!mNewNote) {
            // here we are getting the xml file that represent the
            // the xml for the menu editor content which have the check icon and the garbage
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //this is an id reserved for the up button
        if (item.getItemId() == android.R.id.home) {
            saveChanges();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            // no need to pass the note object because the view model knows which note
            //I am working on
            mViewModel.deleteNote();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveChanges();
    }

    private void saveChanges() {
        mViewModel.saveChanges(mTextView.getText().toString());
        // the finish is for closing the activity and return to the list
        finish();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(My_Key, true);

        super.onSaveInstanceState(outState);
    }
}
