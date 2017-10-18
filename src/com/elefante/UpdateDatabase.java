package com.elefante;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class UpdateDatabase extends BroadcastReceiver {

    SharedPreferences sp;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("ele", "network state changed");

        sp = context.getSharedPreferences("elefante_shared_preferences", Context.MODE_PRIVATE);

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (null != activeNetwork && activeNetwork.isConnected()) {
            if (!sp.getString("flag", "0").equals("1")) {
                Intent i = new Intent(context, UpdateDatabaseService.class);
                context.startService(i);
            }
        }

    }

}
