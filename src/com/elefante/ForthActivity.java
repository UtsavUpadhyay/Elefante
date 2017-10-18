package com.elefante;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class ForthActivity extends Activity {

    Button imgprofile, imghistory, imgactivity;

    RoundedImageView imgphotos;


    TextView correspondentname;
    Elephant elef;
    int elephantIndex, projectIndex;

    @Override
    public void onSaveInstanceState(Bundle outState,
                                    PersistableBundle outPersistentState) {

        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);

        start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.action_refresh) {

            refresh(this);

            return true;
        }


        return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onStop() {

        super.onStop();
        Log.i("ele", "on stop");

        SharedPreferences sp = getSharedPreferences("elefante_shared_preferences", MODE_PRIVATE);

        Set<String> data = new HashSet<String>();

        data = sp.getStringSet("offline_url_set", new HashSet<String>());

        for (String s : data) {
            Log.i("ele", "URL: " + s);
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.i("ele", "on destroy");

        SharedPreferences sp = getSharedPreferences("elefante_shared_preferences", MODE_PRIVATE);

        Set<String> data = new HashSet<String>();

        data = sp.getStringSet("offline_url_set", new HashSet<String>());

        for (String s : data) {
            Log.i("ele", "URL: " + s);
        }

    }

    @Override
    protected void onPause() {

        super.onPause();
        Log.i("ele", "on pause");

        SharedPreferences sp = getSharedPreferences("elefante_shared_preferences", MODE_PRIVATE);

        Set<String> data = new HashSet<String>();

        data = sp.getStringSet("offline_url_set", new HashSet<String>());

        for (String s : data) {
            Log.i("ele", "URL: " + s);
        }
    }

    public void refresh(Context context) {

        final Context c = context;

        new AsyncTask<Void, Void, Void>() {

            ProgressDialog pDialog;

            Boolean success = false;

            @Override
            protected void onPreExecute() {
                pDialog = null;
                pDialog = ProgressDialog.show(c, "Please Wait...", "Refreshing...", true);
                pDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {


                ServiceHandler sh = new ServiceHandler();
                String response = sh.login(GlobalSettings.getInstance().getLoginId(), GlobalSettings.getInstance().getPassword());
                Log.i("ele", response);
                try {
                    JSONObject obj = new JSONObject(response).getJSONObject("user");
                    GlobalJSONData.getInstance().setUser(null);
                    GlobalJSONData.getInstance().setUserFromJSON(obj);
                    success = true;
                } catch (JSONException e) {
                    try {
                        e.printStackTrace();
                        GlobalJSONData.getInstance().parseError(response);

                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

                pDialog.dismiss();

                if (success == false) {
                    try {

                        MySharedPreferences sp = new MySharedPreferences(c);

                        JSONObject obj = new JSONObject(GlobalJSONData.getInstance().getOfflineData(sp)).getJSONObject("user");
                        GlobalJSONData.getInstance().setUserFromJSON(obj);

                        Toast.makeText(c, "you are working offline", 5000).show();

                    } catch (JSONException e) {
                        Toast.makeText(c, "error cannot work offline", 5000).show();
                        e.printStackTrace();
                    }
                }

            }

        }.execute();
    }

    public void start() {


        projectIndex = getIntent().getIntExtra("project_index", 0);
        elephantIndex = getIntent().getIntExtra("elephant_index", 0);

        try {

            elef = GlobalJSONData.getInstance().getUser().getProject().get(projectIndex).getElephant().get(elephantIndex);

        } catch (NullPointerException e) {
            refresh(this);
        }

        String b = Integer.toString(elephantIndex);
        imgphotos = (RoundedImageView) findViewById(R.id.photosview);
        imgprofile = (Button) findViewById(R.id.profileview);
        imghistory = (Button) findViewById(R.id.historyview);
        imgactivity = (Button) findViewById(R.id.activitiesview);
        correspondentname = (TextView) findViewById(R.id.textViewforth);


        correspondentname.setText(elef.getElephantid());
        imghistory.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(ForthActivity.this, HistoryActivity.class);
                i.putExtra("project_index", projectIndex);
                i.putExtra("elephant_index", elephantIndex);
                startActivity(i);

            }
        });

        imgprofile.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(ForthActivity.this, ProfileActivity.class);
                i.putExtra("project_index", projectIndex);
                i.putExtra("elephant_index", elephantIndex);
                startActivity(i);

            }
        });

        imgactivity.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(ForthActivity.this, ActivitySelect.class);
                String elephantId = GlobalJSONData.getInstance().getUser().getProject().get(projectIndex).getElephant().get(elephantIndex).getElephantid();
                i.putExtra("elephant_id", elephantId);
                i.putExtra("project_index", projectIndex);
                i.putExtra("elephant_index", elephantIndex);
                startActivity(i);

            }
        });


        loadBitmap(imgphotos, "http://hms.harvisolution.com/images/" + elef.getImagename());


    }


    public void loadBitmap(RoundedImageView imageView, String url) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, url);
        task.execute();
    }

    private Bitmap download_Image(String url) {
        //---------------------------------------------------
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("Hub", "Error getting the image from server : " + e.getMessage().toString());
        }
        return bm;
        //---------------------------------------------------
    }


    class BitmapWorkerTask extends AsyncTask<Void, Void, Bitmap> {
        private final WeakReference<RoundedImageView> imageViewReference;
        //private int data = 0;
        private final String url;

        public BitmapWorkerTask(RoundedImageView imageView, String url) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            this.url = url;
            imageViewReference = new WeakReference<RoundedImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Void... params) {
            //data = params[0];
            return download_Image(url);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final RoundedImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    //roundedImage = new RoundImage(bitmap);
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }


}
