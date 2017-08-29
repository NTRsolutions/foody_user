package com.foodie.app.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.foodie.app.R;

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
            //Toast.makeText(context, "Oops ! Connect your Internet", Toast.LENGTH_LONG).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getResources().getString(R.string.connection_unavailable));
            builder.setMessage(context.getResources().getString(R.string.check_your_internet_connection));
            builder.setCancelable(false);
            builder.setPositiveButton(context.getResources().getString(R.string.open_settings), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                }
            }).create().show();

            return false;
        }

    }


}
