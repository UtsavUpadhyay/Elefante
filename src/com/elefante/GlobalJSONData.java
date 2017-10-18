package com.elefante;

import org.json.JSONException;
import org.json.JSONObject;

import dbo.ErrorObj;
import dbo.Response;
import dbo.User;

public class GlobalJSONData extends JSONObject {
    private static GlobalJSONData instance = null;
    private User user;
    private Response response;
    private ErrorObj err;

    public GlobalJSONData() {

    }

    public synchronized static GlobalJSONData getInstance() {

        if (instance == null) {
            instance = new GlobalJSONData();
        }

        return instance;

    }

    public void setOfflineData(MySharedPreferences mySharedPreferences, String data) {
        mySharedPreferences.addString("offline_data", data);
    }

    public String getOfflineData(MySharedPreferences mySharedPreferences) {
        return mySharedPreferences.getSharedPreferences().getString("offline_data", "");
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserFromJSON(JSONObject obj) throws JSONException {
        user = new User(obj);
    }

    public void parseResponse(String result) throws JSONException {
        this.response = new Response(result);
    }

    public void parseError(String result) throws JSONException {
        this.err = new ErrorObj(result);
    }

    public Response getResponse() {
        return response;
    }

    public ErrorObj getError() {
        return err;
    }

    @Override
    public String toString() {
        return "ClassPojo [user = " + user + "]";
    }
}

	
