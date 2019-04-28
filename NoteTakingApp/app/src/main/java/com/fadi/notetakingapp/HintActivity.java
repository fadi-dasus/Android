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


    TextView textView;
    private boolean networkOk;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //receiving the message from the broadcast passing the payload key

            DataItem[] items = (DataItem[]) intent.getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);

            for (DataItem item: items) {

                textView.append(item.getItemName() + "\n");
            }
            // after doing this we need to register for listening to the message which has
            // the message identifier key which I send with(in the on create method)

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView) findViewById(R.id.output);

        // listening for messages coming from the broadcast instance
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(receiver, new IntentFilter(MyService.MY_SERVICE_MESSAGE));


        networkOk = NetworkHelper.checkConnection(this);

        if (networkOk) {

            Intent intent = new Intent(this, MyService.class);
            startService(intent);

        } else {
            Toast.makeText(this, "Network not available!", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(receiver);
    }

}
