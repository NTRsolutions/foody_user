package com.foodie.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by santhosh@appoets.com on 29-08-2017.
 */

public class ConnectionHelper {
    private Context context;

    public ConnectionHelper(Context context) {
        this.context = context;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "Oops ! Connect your Internet", Toast.LENGTH_LONG).show();
            return false;
        }

    }


}
