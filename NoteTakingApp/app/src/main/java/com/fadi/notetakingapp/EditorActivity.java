package com.fadi.notetakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fadi.notetakingapp.adapter.NoteAdapter;
import com.fadi.notetakingapp.model.Note;

import com.fadi.notetakingapp.utility.Constant;
import com.fadi.notetakingapp.viewmodel.EditorViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditorActivity extends AppCompatActivity {


    // using butter knife to bind the text view that I have in the content.xml
    //which is the view for this note data
    //then I need to bind it on create method

    @BindView(R.id.note_text)
    TextView mTextView;

    private final String TAG = "Firebase";
    TextToSpeech tts;
    private FirebaseAnalytics mFBAnalytics;
    private FirebaseRemoteConfig mFBConfig;

    private EditorViewModel viewModel;
    // two booleans to check if the note is a new note or an old one
    private boolean isNew = false;
    private boolean isOld = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // binding the text view using butter knife
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            // checking the status of the note( new or old)
            isOld = savedInstanceState.getBoolean(Constant.EditorActivity_My_Key);
        }


        // Retrieve an instance of the Analytics package
        mFBAnalytics = FirebaseAnalytics.getInstance(this);
        // Get the Remote Config instance
        mFBConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFBConfig.setConfigSettings(configSettings);



        initViewModel();

        initTTX();

    }

    private void initTTX() {
        tts = new TextToSpeech(EditorActivity.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("error", "This Language is not supported");
                    }
                } else
                    Log.e("error", "Initilization Failed!");
            }
        });
    }

    //initializing the vew model to the appropriate class
    private void initViewModel() {
        viewModel = ViewModelProviders.of(this)
                .get(EditorViewModel.class);
        // observing the view model live data and displaying the note when it is
        // posted, hence we need a reference to my View in my layout (we are using content editor.xml)
        viewModel.liveData.observe(this, new Observer<Note>() {
            @Override
            public void onChanged(@Nullable Note noteEntity) {
                if (!isOld) {
                    mTextView.setText(noteEntity.getText());
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            setTitle("New Note");
            isNew = true;
        } else {
            setTitle("Edit Note");
            int noteId = bundle.getInt(Constant.NoteAdapter_NOTE_KEY);
            viewModel.getData(noteId);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // we are checking the boolean to show the garbage icon
        //only for the old notes
        if (!isNew) {
            // here we are getting the xml file that represent the
            // the xml for the menu editor content which has the garbage icon
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //this is an id reserved for the up button from the android sdk
        if (item.getItemId() == android.R.id.home) {
            saveChanges();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            // no need to pass the note object because the view model knows which note
            //I am working on
            viewModel.deleteNote();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveChanges();
    }

    private void saveChanges() {
        viewModel.saveChanges(mTextView.getText().toString());
        // the finish is for closing the activity and return to the list
        finish();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constant.EditorActivity_My_Key, true);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        if (tts != null) {

            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }



    public void ClickMeToSpeakUP(View view) {

        Bundle params = new Bundle();
        params.putInt("ButtonID", view.getId());


        String text;
        text = mTextView.getText().toString();
        if (text == null || "".equals(text)) {
            text = "Content not available";
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        } else
            tts.speak(text + " ", TextToSpeech.QUEUE_FLUSH, null);

        Log.d(TAG, "Read Button click logged");
        mFBAnalytics.logEvent("Read", params);
    }



    // speak to text
    public void getSpeechInput(View view) {
        Bundle params = new Bundle();
        params.putInt("ButtonID", view.getId());


        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Please Check your Speech Input Sensor", Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, "Speak Button click logged");
        mFBAnalytics.logEvent("Speak", params);




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mTextView.append(" " + result.get(0));
                }
                break;
        }
    }
}

//Android Debug Bridge (adb) is a versatile command-line tool
// that lets you communicate with a device.
//
//This command displays your events in the Android Studio logcat,
// helping me immediately verify that events are being sent.
//You can view aggregated statistics about your events in the Firebase console dashboards.
// These dashboards update periodically throughout the day
//        adb shell
//        $ setprop log.tag.FA-SVC VERBOSE
//        $ logcat -v time -s FA FA-SVC