package com.fadi.notetakingapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.fadi.notetakingapp.model.DataItem;
import com.fadi.notetakingapp.utility.Constant;

import java.io.IOException;

import retrofit2.Call;

// an intent service is designed to run services in the background and it can handle long time tasks
// it is completely detached from the user interface and it has its own lifecycle
// the service runs on its own thread and it does not have access to the main thread
// it is not contained in the activity means any configuration changes in the activity will not interrupt
// the service component
// downsides : it can not call the activity method
// but it can communicate with the front tier using broadcast messages
// because of the broadcast messages it can send data to any component in the app not just a single activity
// the steps for that is to create the service and registered in the manifest
// to communicate with the app (activities) using the broadcast messages we can use the local broadcast message
// it needs two keys one for identifying the message, and one for the payload(the content)
// I will package the content in an intent object then send it to whoever is listening to the broadcast
// so we need a local broadcast manager and a receiver in where we want to get the content back
public class MyService extends IntentService {




// used for testing purposes
    public static final String TAG = "MyService";

    public MyService() {
        // the string is an arbitrary value
        super("Hello From the class");
    }

    // when we send an intent to the service we get it in here
    @Override
    protected void onHandleIntent(Intent intent) {
        // using retrofit library for Making web service requests
        MyWebService webService = MyWebService.retrofit.create(MyWebService.class);
        // receiving the Call object
        Call<DataItem[]> call = webService.dataItems();

        DataItem[] dataItems;

        try {
            // extracting the data from the call object synchronously
            dataItems = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "onHandleIntent: " + e.getMessage());
            return;
        }

//        Return the data to MainActivity using the local broadcast manager
        Intent messageIntent = new Intent(Constant.MyService_MY_SERVICE_MESSAGE);
        messageIntent.putExtra(Constant.MyService_MY_SERVICE_PAYLOAD, dataItems);
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);




    }

}
