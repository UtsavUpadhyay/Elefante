package com.elefante;


import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@SuppressWarnings("deprecation")
public class ServiceHandler {

    String response = "{\"error\":{\"message\":\"Cannot Connect\",\"comment\":\"Not Connected\"}}";

    private String host = "http://jewelfare.org/webservice/index.php?";


    public String login(String userName, String password) {
        return executeCall(host + "method=login&login_id=" + userName + "&password=" + password);
    }

    public String addActivity(String elephantId, String type, String title, String desc, String dateTime) {
        String url = host + "method=add_activity&login_id=" + GlobalSettings.getInstance().getLoginId() + "&password=" + GlobalSettings.getInstance().getPassword() + "&elephant_id=" + elephantId + "&type=" + type + "&title=" + URLEncoder.encode(title) + "&date_time=" + URLEncoder.encode(dateTime) + "&discription=" + URLEncoder.encode(desc);
        String returnData = executeCall(url);
        Log.i("ele", "returned data is: " + returnData);
        if (returnData.equals("{\"error\":{\"message\":\"Cannot Connect\",\"comment\":\"Not Connected\"}}")) {
            return url;
        }

        return returnData;
    }

    public String fireURL(String url) {
        return executeCall(url);
    }

    public String executeCall(String url) {
        try {

            HttpGet httpGet = new HttpGet(url);
            HttpParams httpParameters = new BasicHttpParams();

            int timeoutConnection = 10000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

            int timeoutSocket = 10000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse httpResponse = httpClient.execute(httpGet);

            HttpEntity httpEntity = null;

            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }


}

