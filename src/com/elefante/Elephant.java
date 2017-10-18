package com.elefante;

import android.content.Context;

import com.elefante.MySharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Elephant extends JSONObject {
    private String bringfrom;

    private String birthyear;

    private String sex;

    private String diedate;

    private String location;

    private String name;

    private String owner;

    private String workplace;

    private String elephantid;

    private ArrayList<CustomActivity> activity = new ArrayList<>();

    private String chipno;

    private String imagename;

    public Elephant(JSONObject obj) throws JSONException {

        JSONArray arr = obj.getJSONArray("activity");
        activity.clear();
        for (int i = 0; i < arr.length(); i++) {
            CustomActivity ca = new CustomActivity(arr.getJSONObject(i));
            activity.add(ca);
        }

        bringfrom = obj.getString("bringfrom");
        birthyear = obj.getString("birthyear");
        sex = obj.getString("sex");
        diedate = obj.getString("diedate");
        location = obj.getString("location");
        name = obj.getString("name");
        owner = obj.getString("owner");
        workplace = obj.getString("workplace");
        elephantid = obj.getString("elephantid");
        chipno = obj.getString("chipno");
        imagename = obj.getString("imagename");


    }

    public String getBringfrom() {
        return bringfrom;
    }

    public void setBringfrom(String bringfrom) {
        this.bringfrom = bringfrom;
    }

    public String getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(String birthyear) {
        this.birthyear = birthyear;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDiedate() {
        return diedate;
    }

    public void setDiedate(String diedate) {
        this.diedate = diedate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getElephantid() {
        return elephantid;
    }

    public void setElephantid(String elephantid) {
        this.elephantid = elephantid;
    }

    public ArrayList<CustomActivity> getActivity() {
        return activity;
    }

    public void setActivity(ArrayList<CustomActivity> activity) {
        this.activity = activity;
    }

    public String getChipno() {
        return chipno;
    }

    public void setChipno(String chipno) {
        this.chipno = chipno;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public ArrayList<CustomActivity> getNoteActivity() {
        ArrayList<CustomActivity> a = new ArrayList<CustomActivity>();
        for (int i = 0; i < activity.size(); i++) {
            if (activity.get(i).getType().equals("DeWorm")) {
                a.add(new CustomActivity(activity.get(i)));
            }
        }

        return a;

    }

    public ArrayList<CustomActivity> getTreatmentActivity() {
        ArrayList<CustomActivity> a = new ArrayList<CustomActivity>();
        for (int i = 0; i < activity.size(); i++) {
            if (activity.get(i).getType().equals("Treatment")) {
                a.add(new CustomActivity(activity.get(i)));
            }
        }

        return a;
    }

    public ArrayList<CustomActivity> getVaccineActivity() {
        ArrayList<CustomActivity> a = new ArrayList<CustomActivity>();
        for (int i = 0; i < activity.size(); i++) {
            if (activity.get(i).getType().equals("Vaccine")) {
                a.add(new CustomActivity(activity.get(i)));
            }
        }

        return a;
    }

    @Override
    public String toString() {
        return "ClassPojo [bringfrom = " + bringfrom + ", birthyear = " + birthyear + ", sex = " + sex + ", diedate = " + diedate + ", location = " + location + ", name = " + name + ", owner = " + owner + ", workplace = " + workplace + ", elephantid = " + elephantid + ", activity = " + activity + ", chipno = " + chipno + ", imagename = " + imagename + "]";
    }


    public void removeFromOffline(Context context, String url) {
        for (CustomActivity c : activity) {
            if (c.getId().equals("offline")) {
                if (c.getOfflineURL().equals(url)) {
                    MySharedPreferences sp = new MySharedPreferences(context);
                    activity.remove(c);
                    sp.removeFromSet("offline_url_set", url);
                }
            }
        }

    }


//    private void storeActivityOffline()
//    {
//    	for(CustomActivity a:activity)
//    	{
//    		if(a.getId().equals("offline"))
//    		{
//    			storeActivityOffline(a.getOfflineURL());
//    		}
//    	}
//    }
//    
//    
//    private void storeActivityOffline(String response)
//	{
//    	
//		Elefante.getInstance().trackEvent("storing offile", "storing activity offline: "+response, "");
//
//		Log.i("ele", "storing it offline");
//
//		String offlineJSONDataStart="{\"activity_url\":[";
//
//		String offlineData="";
//
//		String offlineJSONDataEnd="]}";
//
//		try
//		{
//			String customActivityJSON=GlobalSettings.getInstance().getSharedPreferences().getSharedPreferences().getString("CustomActivityJSON", "");
//			JSONObject obj=new JSONObject(customActivityJSON);
//			AddActivityURL addActivityURL=new AddActivityURL(obj);
//
//			for(int i=0;i<addActivityURL.getURLArray().size();i++)
//			{
//				offlineData+="{\"url\":\""+addActivityURL.getURLArray().get(i)+"\"},";
//			}
//
//			offlineData+="{\"url\":\""+response+"\"},";
//
//			offlineData=offlineData.substring(0, offlineData.length()-1);
//
//			Log.i("ele", offlineJSONDataStart+offlineData+offlineJSONDataEnd);
//			
//			GlobalSettings.getInstance().getSharedPreferences().addString("CustomActivityJSON", offlineJSONDataStart+offlineData+offlineJSONDataEnd);
//
//			Elefante.getInstance().trackEvent("storing offile(ok appending)", "CustomActivityJSON: "+offlineJSONDataStart+offlineData+offlineJSONDataEnd, "");
//
//		}
//		catch(JSONException e) 	
//		{
//			offlineData="{\"url\":\""+response+"\"}";
//
//			Log.i("ele", offlineJSONDataStart+offlineData+offlineJSONDataEnd);
//
//			GlobalSettings.getInstance().getSharedPreferences().addString("CustomActivityJSON", offlineJSONDataStart+offlineData+offlineJSONDataEnd);
//
//			Elefante.getInstance().trackEvent("storing offile(JSON exception)", "CustomActivityJSON: "+offlineJSONDataStart+offlineData+offlineJSONDataEnd, "");
//		}
//		catch(NullPointerException e2)
//		{
//			offlineData="{\"url\":"+response+"}";
//
//			Log.i("ele", offlineJSONDataStart+offlineData+offlineJSONDataEnd);
//
//			GlobalSettings.getInstance().getSharedPreferences().addString("CustomActivityJSON", offlineJSONDataStart+offlineData+offlineJSONDataEnd);
//
//			Elefante.getInstance().trackEvent("storing offile(Null pointer exception)", "CustomActivityJSON: "+offlineJSONDataStart+offlineData+offlineJSONDataEnd, "");
//		}
//
//	}


}