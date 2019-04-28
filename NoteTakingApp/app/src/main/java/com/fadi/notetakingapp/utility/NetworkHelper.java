package com.fadi.notetakingapp.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
// a class checking connection status
public class NetworkHelper {

    public static boolean checkConnection(Context context) {

        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
