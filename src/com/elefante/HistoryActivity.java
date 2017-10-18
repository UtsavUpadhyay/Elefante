package com.elefante;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class HistoryActivity extends Activity implements OnItemLongClickListener {

    ListView listViewHistory;
    RadioGroup radioGroup;

    ActiviyListAdapter adapter;

    ProgressDialog pDialog;

    int projectIndex, elephantIndex;

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

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
        // TODO Auto-generated method stub
        if (item.getItemId() == R.id.action_refresh) {

            refresh();

            return true;
        }


        return super.onOptionsItemSelected(item);


    }


    public void start() {

        projectIndex = getIntent().getIntExtra("project_index", 0);
        elephantIndex = getIntent().getIntExtra("elephant_index", 0);

        listViewHistory = (ListView) findViewById(R.id.listViewhistory);

        listViewHistory.setOnItemLongClickListener(this);

        radioGroup = (RadioGroup) findViewById(R.id.radiohistorygroup);

        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    RadioButton r = (RadioButton) findViewById(checkedId);
                    if (r.getText().equals("All")) {

                        adapter.setArrayList(GlobalJSONData.getInstance().getUser().getProject().get(projectIndex).getElephant().get(elephantIndex).getActivity());
                    }
                    if (r.getText().equals("DeW")) {
                        adapter.setArrayList(GlobalJSONData.getInstance().getUser().getProject().get(projectIndex).getElephant().get(elephantIndex).getNoteActivity());
                    }
                    if (r.getText().equals("Trea")) {
                        adapter.setArrayList(GlobalJSONData.getInstance().getUser().getProject().get(projectIndex).getElephant().get(elephantIndex).getTreatmentActivity());
                    }
                    if (r.getText().equals("Vac")) {
                        adapter.setArrayList(GlobalJSONData.getInstance().getUser().getProject().get(projectIndex).getElephant().get(elephantIndex).getVaccineActivity());
                    }

                    adapter.notifyDataSetChanged();

                } catch (NullPointerException e) {

                }

            }
        });


        refresh();


    }


    public void refresh() {
        new AsyncTask<Void, Void, Void>() {


            Boolean success = false;

            @Override
            protected void onPreExecute() {
                pDialog = null;
                pDialog = ProgressDialog.show(HistoryActivity.this, "Please Wait...", "Getting Lastest Activities...", true);
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

                adapter = new ActiviyListAdapter(HistoryActivity.this, GlobalJSONData.getInstance().getUser().getProject().get(projectIndex).getElephant().get(elephantIndex).getActivity());

                listViewHistory.setAdapter(adapter);


            }

        }.execute();

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {

        ViewHolder holder = (ViewHolder) arg1.getTag();

        Log.i("Elefante", "holder id: " + holder.id);

        if (holder.id.equalsIgnoreCase("offline")) {

            final String url = holder.offlineURL;

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);

            arrayAdapter.add("Delete");

            builderSingle.setNegativeButton("cancel",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    });

            builderSingle.setAdapter(arrayAdapter,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int position) {

                            String strName = arrayAdapter.getItem(position);

                            if (position == 0) {

                                GlobalJSONData.getInstance()
                                        .getUser()
                                        .getProject().get(projectIndex)
                                        .getElephant().get(elephantIndex).removeFromOffline(HistoryActivity.this, url);

                                mHandler.post(new Runnable() {

                                    @Override
                                    public void run() {

                                        adapter.notifyDataSetChanged();

                                    }
                                });


                            }

                        }
                    });

            builderSingle.show();

        }


        return false;
    }

    static class ViewHolder {

        public LinearLayout linearLayout;
        public TextView date;
        public TextView takenBy;
        public TextView type;
        public TextView title;
        public TextView discription;
        public String id;
        public String offlineURL;
        public int position;

    }

    class ActiviyListAdapter extends BaseAdapter {
        /***********
         * Declare Used Variables
         *********/
        Context context;
        int i = 0;
        private ArrayList<CustomActivity> customActivityArrayList;
        private LayoutInflater inflater = null;

        public ActiviyListAdapter(Context c, ArrayList<CustomActivity> d) {
            context = c;
            customActivityArrayList = d;

            /***********  Layout inflator to call external xml layout () ***********/
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        /********
         * What is the size of Passed Arraylist Size
         ************/
        public int getCount() {

            return customActivityArrayList.size();
        }

        public Object getItem(int position) {
            return customActivityArrayList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        /********* Create a holder Class to contain inflated xml file elements *********/


        /******
         * Depends upon data size called for each row , Create each ListView row
         *****/
        public View getView(int position, View convertView, ViewGroup parent) {

            View vi = null;
            convertView = null;
            vi = convertView;

            ViewHolder holder;


            if (convertView == null) {

                /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
                vi = inflater.inflate(R.layout.single_activity_row, null);

                /****** View Holder Object to contain tabitem.xml file elements ******/

                holder = new ViewHolder();
                holder.date = (TextView) vi.findViewById(R.id.tv_dateValue);
                holder.takenBy = (TextView) vi.findViewById(R.id.tv_takenByValue);
                holder.type = (TextView) vi.findViewById(R.id.tv_typeValue);
                holder.title = (TextView) vi.findViewById(R.id.tv_titleValue);
                holder.discription = (TextView) vi.findViewById(R.id.tv_discriptionValue);

                holder.linearLayout = (LinearLayout) vi.findViewById(R.id.single_activity_row_root);


                if (customActivityArrayList.get(position).getId().equals("offline")) {
                    holder.linearLayout.setBackgroundColor(Color.parseColor("#00a0cc"));
                } else {
                    holder.linearLayout.setBackgroundColor(Color.TRANSPARENT);
                }

                holder.date.setText(customActivityArrayList.get(position).getDate());
                holder.takenBy.setText(customActivityArrayList.get(position).getEnterby());
                holder.title.setText(customActivityArrayList.get(position).getTitle());
                holder.type.setText(customActivityArrayList.get(position).getType());
                holder.discription.setText(customActivityArrayList.get(position).getDiscription());

                Log.i("Elefante", "Elephante ID: " + customActivityArrayList.get(position).getId());

                holder.id = customActivityArrayList.get(position).getId();
                holder.position = position;
                holder.offlineURL = customActivityArrayList.get(position).getOfflineURL();
                /************  Set holder with LayoutInflater ************/
                vi.setTag(holder);
            }


            return vi;


        }


        public void setArrayList(ArrayList<CustomActivity> c) {
            this.customActivityArrayList = c;
        }

    }


}
