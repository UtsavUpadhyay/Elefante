package com.elefante;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;

import java.util.Calendar;

@SuppressWarnings("unused")
public class ActivitySelect extends Activity {

    final Calendar c = Calendar.getInstance();
    EditText selectdate, selecttime, selecttitle, selectdiscription;
    Button selectsubmit;
    RadioGroup radiogroup;
    RadioButton radiotreat, radiovaccin, radionone;
    ProgressDialog p;
    String type;
    String elephantId;
    long mSecond;
    private int mYear, mMonth, mDay, mHour, mMinute, projectIndex, elephantIndex;

    //SharedPreferences sp=GlobalSettings.getInstance().getSharedPreferences().getSharedPreferences();

    //Editor editor=sp.edit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        type = "DeWorm";
        start();
    }


    public void start() {

        elephantId = getIntent().getStringExtra("elephant_id");

        elephantIndex = getIntent().getIntExtra("elephant_index", 0);

        projectIndex = getIntent().getIntExtra("project_index", 0);

        selectdate = (EditText) findViewById(R.id.textselectdate1);
        selecttime = (EditText) findViewById(R.id.textselecttime1);
        selecttitle = (EditText) findViewById(R.id.textselecttitle1);
        selectdiscription = (EditText) findViewById(R.id.textselectdiscription1);
        selectsubmit = (Button) findViewById(R.id.selectsubmit1);
        radiotreat = (RadioButton) findViewById(R.id.radioButtontreat);
        radiovaccin = (RadioButton) findViewById(R.id.radioButtonvaccin);
        radionone = (RadioButton) findViewById(R.id.radioButtonnone);

        radiogroup = (RadioGroup) findViewById(R.id.radioButtongroup);


        radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    RadioButton r = (RadioButton) findViewById(checkedId);
                    if (r.getText().equals("Vac")) {
                        type = "Vaccine";
                    } else if (r.getText().equals("Trea")) {
                        type = "Treatment";
                    } else {
                        type = "DeWorm";
                    }

                } catch (NullPointerException e) {

                }

            }

        });

        selectdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                setDate();

            }
        });

        selecttime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                setTime();

            }
        });

        selectsubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(selectdate.getText()) && !TextUtils.isEmpty(selecttime.getText()) && !TextUtils.isEmpty(selecttitle.getText()) && !TextUtils.isEmpty(selectdiscription.getText())) {
                    new AsyncTask<Void, Void, Void>() {

                        Boolean success = false;

                        String response;

                        protected void onPreExecute() {
                            p = null;
                            p = ProgressDialog.show(ActivitySelect.this, "Please Wait...", "Adding Activity...", true, false);
                            p.show();
                        }

                        @Override
                        protected Void doInBackground(Void... params) {


                            String dateTime = selectdate.getText() + " " + selecttime.getText();

                            ServiceHandler sh = new ServiceHandler();
                            response = sh.addActivity(elephantId, type, selecttitle.getText().toString(), selectdiscription.getText().toString(), dateTime);

                            try {
                                GlobalJSONData.getInstance().parseResponse(response);
                                success = true;
                                Elefante.getInstance().teve("add activity", "activity data addeed successfully", "");
                            } catch (JSONException e) {
                                Elefante.getInstance().teve("add activity", "Exception Response: " + response, "");
                                Elefante.getInstance().te(e);
                                Log.i("ele", response);
                                e.printStackTrace();


                            }

                            return null;
                        }

                        protected void onPostExecute(Void result) {

                            p.dismiss();

                            if (success) {
                                Toast.makeText(ActivitySelect.this, GlobalJSONData.getInstance().getResponse().getComment(), 3000).show();
                                ActivitySelect.this.finish();
                            } else {

                                //storeActivityOffline(response);

                                try {

                                    CustomActivity offlineActivity = new CustomActivity("offline", selecttitle.getText().toString(), GlobalSettings.getInstance().getLoginId(), selectdiscription.getText().toString(), elephantId, type, selectdate.getText() + " " + selecttime.getText());

                                    offlineActivity.setOfflineURL(ActivitySelect.this, response);

                                    GlobalJSONData.getInstance().getUser().getProject().get(projectIndex).getElephant().get(elephantIndex).getActivity().add(offlineActivity);

                                } catch (NullPointerException e) {


                                }

                                Log.i("ele", "offline activity added to array.");

                                Toast.makeText(ActivitySelect.this, "Data Stored Offline", 3000).show();
                                ActivitySelect.this.finish();

                            }

                        }

                    }.execute();
                } else {

                    Toast.makeText(ActivitySelect.this, "Please Enter All Detials.", 3000).show();

                }

            }
        });

    }


    public void setTime() {
        // Process to get Current Time
        final Calendar c2 = Calendar.getInstance();
        mHour = c2.get(Calendar.HOUR_OF_DAY);
        mMinute = c2.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog tpd = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        // Display Selected time in textbox
                        selecttime.setText(hourOfDay + ":" + minute);
                        mHour = view.getCurrentHour();
                        mMinute = view.getCurrentMinute();
                        mSecond = (((mHour * 60) + mMinute) * 60);
                    }
                }, mHour, mMinute, true);

        tpd.show();

    }


    public void setDate() {

        // Process to get Current Date

        //c.set(Calendar.MILLISECOND, 0);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox

                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;

                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, monthOfYear);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        int dateInSeconds = (int) (c.getTimeInMillis() / 1000);


                        //Log.i("MAA", "DATE IN UNIX IS: "+dateInSeconds);

                        selectdate.setText(year + "-"
                                + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }


}
