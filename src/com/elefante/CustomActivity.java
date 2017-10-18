package com.elefante;

import android.content.Context;
import android.util.Log;

import com.elefante.MySharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("unused")
public class CustomActivity extends JSONObject {
    public static final String TAG = "ELE " + CustomActivity.class.getSimpleName();

    private String id;

    private String title;

    private String enterby;

    private String discription;

    private String elephantid;

    private String type;

    private String date;

    private String offlineURL;

    public CustomActivity(String id, String title, String enterby, String discription, String elephantid, String type, String date) {
        this.id = id;
        this.title = title;
        this.enterby = enterby;
        this.discription = discription;
        this.elephantid = elephantid;
        this.type = type;
        this.date = date;
    }

    public CustomActivity(CustomActivity c) {
        this.id = c.id;
        this.title = c.title;
        this.enterby = c.enterby;
        this.discription = c.discription;
        this.elephantid = c.elephantid;
        this.type = c.type;
        this.date = c.date;
    }


    public CustomActivity(JSONObject obj) throws JSONException {

        id = obj.getString("id");
        title = obj.getString("title");
        enterby = obj.getString("enterby");
        discription = obj.getString("discription");
        elephantid = obj.getString("elephantid");
        type = obj.getString("type");
        date = obj.getString("date");

    }

    public String getOfflineURL() {
        return offlineURL;
    }

    public void setOfflineURL(Context context, String offlineURL) {

        this.offlineURL = offlineURL;

        Log.i(TAG, "storing activity offline URL: " + offlineURL);

        MySharedPreferences sp = new MySharedPreferences(context);

        sp.addIntoSet("offline_url_set", offlineURL);


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEnterby() {
        return enterby;
    }

    public void setEnterby(String enterby) {
        this.enterby = enterby;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getElephantid() {
        return elephantid;
    }

    public void setElephantid(String elephantid) {
        this.elephantid = elephantid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", title = " + title + ", enterby = " + enterby + ", discription = " + discription + ", elephantid = " + elephantid + ", type = " + type + ", date = " + date + "]";
    }


}
