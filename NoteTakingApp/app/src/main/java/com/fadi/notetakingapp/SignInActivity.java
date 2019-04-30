package com.fadi.notetakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fadi.notetakingapp.utility.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import butterknife.BindView;
import butterknife.ButterKnife;


// this activity will manage sign in using Firebase auth
public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "SignInActivity";


    private FirebaseRemoteConfig mFBConfig;



    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @BindView(R.id.etPassword)
     EditText etPass;

    @BindView(R.id.etEmailAddr)
     EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Set up click handlers and view item references
        findViewById(R.id.btnCreate).setOnClickListener(this);
        findViewById(R.id.btnSignIn).setOnClickListener(this);
        findViewById(R.id.btnSignOut).setOnClickListener(this);

        ButterKnife.bind(this);

        // Get a reference to the Firebase auth object
        mAuth = FirebaseAuth.getInstance();
        // reference to the Firebase remote congfig
        mFBConfig = FirebaseRemoteConfig.getInstance();

        // Enable developer mode to perform more rapid testing.
        // Config fetches are normally limited to 5 per hour. This
        // enables many more requests to facilitate testing.
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFBConfig.setConfigSettings(configSettings);

        // Get the default parameter settings from the XML file
        mFBConfig.setDefaults(R.xml.config);




        // Attach a new AuthListener to detect sign in and out
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "Signed in: " + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "Currently signed out");
                }
            }
        };

        updateStatus();
        checkValuesFromFB();

        //TODO save email __________________________________________________
        SharedPreferences prefs =
                getSharedPreferences(Constant.SignInActivity_MY_GLOBAL_PREFS, MODE_PRIVATE);
        String email = prefs.getString(Constant.SignInActivity_EMAIL_KEY, "");

        if (!TextUtils.isEmpty(email)) {
            etEmail.setText(email);
        }

    }

    private void checkValuesFromFB() {
        // If in developer mode cacheExpiration is set to 0 so each fetch will retrieve values from
        // the server.
        if (mFBConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            Constant.SignInActivity_PROMO_CACHE_DURATION = 0;
        }
        // fetch the values from the Remote Config service
        mFBConfig.fetch(Constant.SignInActivity_PROMO_CACHE_DURATION)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, " check successful");

                            // If the fetch was successful, then "activate" the
                            // values that were retrieved from the server
                            mFBConfig.activateFetched();
                        }
                        else {
                            Log.e(TAG, " check failed");
                        }

                        ShowLogMessage();
                    }
                });
    }

    private void ShowLogMessage() {

        String logMessage = "";


        logMessage = mFBConfig.getString(Constant.SignInActivity_CONFIG_DENMARK_KEY);

        Toast.makeText(this, logMessage , Toast.LENGTH_LONG).show();


    }

    /**
     * When the Activity starts and stops, the app needs to connect and
     * disconnect the AuthListener
     */
    @Override
    public void onStart() {
        super.onStart();
        //  add the AuthListener
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        //  Remove the AuthListener
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                signUserIn();

                break;

            case R.id.btnCreate:
                createUserAccount();
                break;

            case R.id.btnSignOut:
                signUserOut();
                break;
        }
    }

    private boolean checkFormFields() {
        String email, password;

        email = etEmail.getText().toString();
        password = etPass.getText().toString();

        if (email.isEmpty()) {
            etEmail.setError("Email Required");
            return false;
        }
        if (password.isEmpty()) {
            etPass.setError("Password Required");
            return false;
        }

        return true;
    }

    private void updateStatus() {
        TextView tvStat = (TextView) findViewById(R.id.tvSignInStatus);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            tvStat.setText("Signed in: " + user.getEmail());
        } else {
            tvStat.setText("Signed Out");
        }
    }

    private void updateStatus(String stat) {
        TextView tvStat = (TextView) findViewById(R.id.tvSignInStatus);
        tvStat.setText(stat);
    }

    private void signUserIn() {
        if (!checkFormFields())
            return;

        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        // sign the user in with email and password credentials
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignInActivity.this, "Signed in", Toast.LENGTH_SHORT)
                                            .show();
                                    // TODO create the activity call here
                                    Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                                    startActivity(intent);


                                    //TODO save credentials in shared preference
                                    //________________________________________
                            String myEmail = etEmail.getText().toString();
                                    SharedPreferences.Editor editor =
                                            getSharedPreferences(Constant.SignInActivity_MY_GLOBAL_PREFS, MODE_PRIVATE).edit();
                                    editor.putString(Constant.SignInActivity_EMAIL_KEY, myEmail);
                                    editor.apply();



                                } else {
                                    Toast.makeText(SignInActivity.this, "Sign in failed", Toast.LENGTH_SHORT)
                                            .show();
                                }

                                updateStatus();


                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            updateStatus("Invalid password.");
                        } else if (e instanceof FirebaseAuthInvalidUserException) {
                            updateStatus("No account with this email.");
                        } else {
                            updateStatus(e.getLocalizedMessage());
                        }
                    }
                });
    }

    private void signUserOut() {
        // sign the user out
        mAuth.signOut();
        updateStatus();
    }

    private void createUserAccount() {
        if (!checkFormFields())
            return;

        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        // Create the user account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignInActivity.this, "User created", Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    Toast.makeText(SignInActivity.this, "Account creation failed", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            updateStatus("This email address is already in use.");
                        } else {
                            updateStatus(e.getLocalizedMessage());
                        }
                    }
                });
    }


    public void SeeIntroduction(View view) {
        //
Intent intent = new Intent(SignInActivity.this,YoutubeActivity.class);
                                   startActivity(intent);

    }

    /*
    this for showing that the event has been sent to firebase
    /adb shell setprop log.tag.FA VERBOSE
    adb shell setprop log.tag.FA-SVC VERBOSE
    adb logcat -v time -s FA FA-SVC
     */
}


