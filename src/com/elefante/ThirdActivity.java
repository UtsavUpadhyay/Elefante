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
public class ThirdActivity extends Activity {


    ListView lst3;
    ;
    TextView txt3;

    EditText Search;

    int projectIndex;

    Handler mHandler;

    ArrayAdapter<String> adapter;

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        //GlobalSettings.getInstance().getSharedPreferences().addInt("project_index", projectIndex);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);


        mHandler = new Handler();

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


    public void start() {

        //if(GlobalSettings.getInstance().getSharedPreferences().getSharedPreferences().get)

        projectIndex = getIntent().getIntExtra("project_index", 0);


        txt3 = (TextView) findViewById(R.id.textViewlist1);
        lst3 = (ListView) findViewById(R.id.listelephants);

        Search = (EditText) findViewById(R.id.secondtext2);

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

                ThirdActivity.this.adapter.getFilter().filter(s);


            }


        });

        try {

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, GlobalJSONData.getInstance().getUser().getProject().get(projectIndex).getElephantNamesInProject());

        } catch (NullPointerException e) {
            refresh(this);
        }

        lst3.setAdapter(adapter);

        lst3.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Intent i = new Intent(ThirdActivity.this, ForthActivity.class);
                i.putExtra("project_index", projectIndex);

                String data = (String) arg0.getItemAtPosition(arg2);

                Log.i("elefante", "You clicked on: " + data);

                int index = GlobalJSONData.getInstance().getUser().getProject().get(projectIndex).getElephantNamesInProject().indexOf(data);

                i.putExtra("elephant_index", index);
                startActivity(i);

            }
        });

    }


}
