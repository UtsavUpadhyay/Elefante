package dbo;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddActivityURL {

    ArrayList<ActivityURL> activityURL = new ArrayList<ActivityURL>();

    public AddActivityURL(JSONObject obj) throws JSONException {

        JSONArray arr = obj.getJSONArray("activity_url");

        Log.i("ele", "offline data array length is: " + arr.length());

        for (int i = 0; i < arr.length(); i++) {
            ActivityURL au = new ActivityURL(arr.getJSONObject(i));
            activityURL.add(au);
        }

    }

    public ArrayList<String> getURLArray() {
        ArrayList<String> lst = new ArrayList<>();
        for (int i = 0; i < activityURL.size(); i++) {
            lst.add(activityURL.get(i).getURL());
        }

        return lst;
    }

}
