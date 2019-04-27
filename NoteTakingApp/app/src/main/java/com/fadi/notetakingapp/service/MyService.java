package com.fadi.notetakingapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.fadi.notetakingapp.model.DataItem;

import java.io.IOException;

import retrofit2.Call;


// an intent service is designed to run services in the background and it can handle long time tasks
// it is completely detached from the user interface and it has its own lifecycle
// and it is running on its own thread an it does not have access to the main thread
// it is not contained in the activity and it can not call the activity method
// but it can communicate with the front tier using broadcast messages
// because of the broadcast messages it can send data to any component in the app not just a single activity
// the steps for that is to create the service and registered in the manifest
// to communicate with the app (activities) using the broadcast messages we can use the local broadcast message
// it needs tow keys one fot identifying the message, and one for the payload(the content)
// I will package the content in an intent object then send it to whoever is listening to the broadcast
// so we need a local broadcast manager and a receiver where we need to get the content back
public class MyService extends IntentService {

    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";



    public static final String TAG = "MyService";

    public MyService() {
        super("Hello From the class");
    }

    // when we send an intent to the service we get it in here
    @Override
    protected void onHandleIntent(Intent intent) {


        // using retrofit
        //  Make the web service request
        MyWebService webService =
                MyWebService.retrofit.create(MyWebService.class);
        // receiving the Call object from the method in the web service class
        Call<DataItem[]> call = webService.dataItems();

        DataItem[] dataItems;

        try {
            // getting the data from the call object synchronously
            dataItems = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "onHandleIntent: " + e.getMessage());
            return;
        }

//        Return the data to MainActivity
        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, dataItems);
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);




    }

}
