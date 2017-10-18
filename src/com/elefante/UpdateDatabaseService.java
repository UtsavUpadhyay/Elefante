package com.elefante;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class UpdateDatabaseService extends IntentService {

    public UpdateDatabaseService() {
        super("UpdateDatabase");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        new UpdateDB().execute();

    }


    class UpdateDB extends AsyncTask<Void, Void, Void> {

        Boolean success = false;

        Notification.Builder notificationBuilder;

        NotificationManager nManager;

        @Override
        protected void onPreExecute() {

            notificationBuilder = new Notification.Builder(UpdateDatabaseService.this);

            nManager = (NotificationManager) UpdateDatabaseService.this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationBuilder
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("Elefante").build();

            Log.i("ele", "Service Started!!!");

        }


        @Override
        protected Void doInBackground(Void... params) {

            SharedPreferences sp = getSharedPreferences("elefante_shared_preferences", MODE_PRIVATE);

            Set<String> data = new HashSet<String>();

            data = sp.getStringSet("offline_url_set", new HashSet<String>());

            if (data != null && data.size() > 0) {

                for (String s : data) {
                    Log.i("ele", "URL: " + s);
                }

                Editor editor = sp.edit();
                editor.putString("flag", "1");
                editor.commit();

                Log.i("ele", "flag is set");

                Log.i("ele", "data in shared preferences: " + data);

                String response = null;

                try {

                    ServiceHandler sh = new ServiceHandler();

                    for (String url : data) {
                        Elefante.getInstance().teve("Service says firing URL", url, "");
                        response = sh.fireURL(url);
                        Log.i("ele", "response: " + response);
                        success = true;
                    }


                    Editor e = sp.edit();
                    e.remove("offline_url_set");
                    e.commit();

                    Elefante.getInstance().teve("Service says", "Database updated", "");

                } catch (Exception e) {
                    Log.i("ele", "Exception");
                } finally {

                    editor.putString("flag", "0");
                    editor.commit();
                    Log.i("ele", "flag unset....................OK");
                }

            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {

            if (success) {
                notificationBuilder.setContentText("Database Updated!").setOnlyAlertOnce(true);
                nManager.notify(106, notificationBuilder.build());
            }

        }


    }


}
