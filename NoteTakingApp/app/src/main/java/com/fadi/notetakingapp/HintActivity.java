package com.fadi.notetakingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fadi.notetakingapp.model.DataItem;
import com.fadi.notetakingapp.service.MyService;
import com.fadi.notetakingapp.utility.NetworkHelper;

public class HintActivity extends AppCompatActivity {


    TextView output;
    private boolean networkOk;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // I am receiving the message from the broadcast passing the payload key

            DataItem[] items = (DataItem[]) intent.getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);

            for (DataItem item: items) {

                output.append(item.getItemName() + "\n");
            }
            // after doing this we need to register for listening to the message which has
            // the message identifier key which I send it with(in the on create method)

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        output = (TextView) findViewById(R.id.output);

        // first if I want to listen to many messages I will register to as many as I want by passing the key
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(receiver, new IntentFilter(MyService.MY_SERVICE_MESSAGE));
        // remember to unregister the receiver when the activity is closing down


        networkOk = NetworkHelper.checkConnection(this);
        output.append("Network ok: " + networkOk);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(receiver);
    }

    public void runClickHandler(View view) {
        if (networkOk) {

            Intent intent = new Intent(this, MyService.class);
            startService(intent);

        } else {
            Toast.makeText(this, "Network not available!", Toast.LENGTH_SHORT).show();
        }

    }

    public void clearClickHandler(View view) {
        output.setText("");
    }
}
