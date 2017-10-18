package com.elefante;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("unused")
public class MainActivity extends Activity {

    Button b1;
    EditText ed1, ed2;
    LinearLayout login1, login2;

    ProgressDialog pDialog;

    //SharedPreferences sharedPreferences;

    MySharedPreferences mySharedPreferences;

    CheckBox chkKeepMeLoggedIn;

    RememberMe rememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GlobalJSONData.getInstance();
        GlobalSettings.getInstance();
        start();
    }


    public void start() {

        mySharedPreferences = new MySharedPreferences(this);


        GlobalSettings.getInstance().setSharedPreferences(mySharedPreferences);


        b1 = (Button) findViewById(R.id.Loginsignupbutton1);
        ed1 = (EditText) findViewById(R.id.LogineditText1);
        ed2 = (EditText) findViewById(R.id.LogineditText2);

        chkKeepMeLoggedIn = (CheckBox) findViewById(R.id.checkBoxlogin);

        rememberMe = new RememberMe(mySharedPreferences, chkKeepMeLoggedIn, ed1, ed2);

        if (rememberMe.rememberMe()) {
            chkKeepMeLoggedIn.setChecked(true);

            tryLogin(this);

            //Toast.makeText(this, "yes it was remembered", 3000).show();
        } else {
            //chkKeepMeLoggedIn.setChecked(false);
            //Toast.makeText(this, "not remembered", 3000).show();
        }

        b1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                tryLogin(MainActivity.this);

            }
        });

    }


    private void tryLogin(Context context) {
        Elefante.getInstance().teve("Elefante Login Screen", "Login", "user clicked on login");

        final Context c = context;

        final String userName = ed1.getText().toString();

        final String password = ed2.getText().toString();

        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {

            new AsyncTask<Void, Void, Void>() {


                Boolean success = false;

                @Override
                protected void onPreExecute() {

                    if (chkKeepMeLoggedIn.isChecked()) {
                        rememberMe.set();
                    } else {
                        rememberMe.unset();
                    }

                    pDialog = null;
                    pDialog = ProgressDialog.show(c, "Please Wait...", "Logging in...", true);
                    pDialog.show();
                }

                @Override
                protected Void doInBackground(Void... params) {


                    ServiceHandler sh = new ServiceHandler();
                    String response = sh.login(userName, password);
                    Log.i("ele", response);
                    try {
                        JSONObject obj = new JSONObject(response).getJSONObject("user");
                        GlobalJSONData.getInstance().setUserFromJSON(obj);
                        GlobalJSONData.getInstance().setOfflineData(mySharedPreferences, response);
                        success = true;
                    } catch (JSONException e) {
                        try {
                            Elefante.getInstance().te(e);
                            e.printStackTrace();
                            GlobalJSONData.getInstance().parseError(response);

                        } catch (JSONException e2) {
                            Elefante.getInstance().te(e2);
                            e2.printStackTrace();
                        }
                    } catch (Exception e) {
                        Elefante.getInstance().te(e);
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    pDialog.dismiss();

                    if (success) {
                        openActivity(userName, password);
                    } else {

                        if (rememberMe.rememberMe()) {

                            try {
                                JSONObject obj = new JSONObject(GlobalJSONData.getInstance().getOfflineData(mySharedPreferences)).getJSONObject("user");
                                GlobalJSONData.getInstance().setUserFromJSON(obj);

                                Toast.makeText(c, "you are working offline", 5000).show();

                                openActivity(userName, password);

                            } catch (JSONException e) {
                                Toast.makeText(c, "error cannot work offline", 5000).show();
                                e.printStackTrace();
                            }

                        }


                        //Toast.makeText(c, GlobalJSONData.getInstance().getError().getComment(), 3000).show();
                    }

                }
            }.execute();

        } else {
            Toast.makeText(c, "Enter User ID and Password.", 3000).show();
        }
    }


    public void openActivity(String userName, String password) {
        if (GlobalJSONData.getInstance().getUser().getProjectNames().size() == 1) {
            GlobalSettings.getInstance().setLoginIdPassword(userName, password);
            Intent i = new Intent(this, ThirdActivity.class);
            i.putExtra("project_index", 0);
            startActivity(i);
        } else {
            GlobalSettings.getInstance().setLoginIdPassword(userName, password);
            Intent i = new Intent(this, SecondActivity.class);
            startActivity(i);
        }
    }


}
