package dbo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityURL extends JSONObject {

    String url;

    public ActivityURL(JSONObject obj) throws JSONException {

        url = obj.getString("url");

        Log.i("ele", "URL is: " + url);

    }

    public String getURL() {
        return url;
    }

}
