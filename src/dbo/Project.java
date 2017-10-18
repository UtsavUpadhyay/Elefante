package dbo;

import com.elefante.Elephant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Project extends JSONObject {
    private String id;

    private String startdate;

    private String joindate;

    private String location;

    private ArrayList<Elephant> elephant = new ArrayList<>();

    private String userid;

    private String discription;

    private String role;

    private String projectname;

    private String incharge;

    private String enddate;

    private String note;

    public Project(JSONObject obj) throws JSONException {

        JSONArray arr = obj.getJSONArray("elephant");
        elephant.clear();
        for (int i = 0; i < arr.length(); i++) {
            Elephant e = new Elephant(arr.getJSONObject(i));
            elephant.add(e);
        }

        id = obj.getString("id");
        startdate = obj.getString("startdate");
        joindate = obj.getString("joindate");
        location = obj.getString("location");
        userid = obj.getString("userid");
        discription = obj.getString("discription");
        role = obj.getString("role");
        projectname = obj.getString("projectname");
        incharge = obj.getString("incharge");
        enddate = obj.getString("enddate");
        note = obj.getString("note");

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getJoindate() {
        return joindate;
    }

    public void setJoindate(String joindate) {
        this.joindate = joindate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<Elephant> getElephant() {
        return elephant;
    }

    public void setElephant(ArrayList<Elephant> elephant) {
        this.elephant = elephant;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getIncharge() {
        return incharge;
    }

    public void setIncharge(String incharge) {
        this.incharge = incharge;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<String> getElephantNamesInProject() {
        ArrayList<String> e = new ArrayList<String>();
        for (int i = 0; i < elephant.size(); i++) {
            e.add(elephant.get(i).getName());
        }
        return e;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", startdate = " + startdate + ", joindate = " + joindate + ", location = " + location + ", elephant = " + elephant + ", userid = " + userid + ", discription = " + discription + ", role = " + role + ", projectname = " + projectname + ", incharge = " + incharge + ", enddate = " + enddate + ", note = " + note + "]";
    }
}
