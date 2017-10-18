package com.elefante;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("unused")
public class SecondActivity extends Activity {

    TextView txt2;
    ListView lst2;
    EditText Search;
    ArrayAdapter<String> adapter;

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        mHandler = new Handler();

        starttwo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == R.id.action_refresh) {

            refresh(this);

            return true;
        }


        return super.onOptionsItemSelected(item);


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

                mHandler.post(new Runnable() {

                    @Override
                    public void run() {

                        adapter.notifyDataSetChanged();

                    }
                });
            }

        }.execute();
    }


    public void starttwo() {


        lst2 = (ListView) findViewById(R.id.listProjects);
        txt2 = (TextView) findViewById(R.id.textViewlist);
        Search = (EditText) findViewById(R.id.secondtext);

        Search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                SecondActivity.this.adapter.getFilter().filter(s);


            }


        });

        try {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, GlobalJSONData.getInstance().getUser().getProjectNames());
        } catch (NullPointerException e) {

            refresh(this);

        }

        lst2.setAdapter(adapter);

        lst2.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                String data = (String) arg0.getItemAtPosition(arg2);

                Log.i("elefante", "You clicked on: " + data);

                startThirdActivity(data);

            }
        });


    }


    public void startThirdActivity(String data) {
        int index = GlobalJSONData.getInstance().getUser().getProjectNames().indexOf(data);

        Log.i("elefante", data + " is at index: " + index);

        Intent i = new Intent(SecondActivity.this, ThirdActivity.class);
        i.putExtra("project_index", index);
        startActivity(i);
    }


}


