package dbo;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User extends JSONObject {
    private String id;

    private ArrayList<Project> project = new ArrayList<>();

    private String joindate;

    private String email;

    private String address;

    private String name;

    private String designation;

    private String surname;

    private String contact;

    public User(JSONObject obj) throws JSONException {
        try {
            JSONArray arr = obj.getJSONArray("project");
            project.clear();
            for (int i = 0; i < arr.length(); i++) {
                Project p = new Project(arr.getJSONObject(i));
                project.add(p);
            }
        } catch (Exception e) {
            Log.i("ele", "project empty in JSON");
        }

        id = obj.getString("id");
        joindate = obj.getString("joindate");
        email = obj.getString("email");
        address = obj.getString("address");
        name = obj.getString("name");
        designation = obj.getString("designation");
        surname = obj.getString("surname");
        contact = obj.getString("contact");

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Project> getProject() {
        return project;
    }

    public void setProject(ArrayList<Project> project) {
        this.project = project;
    }

    public String getJoindate() {
        return joindate;
    }

    public void setJoindate(String joindate) {
        this.joindate = joindate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }


    public ArrayList<String> getProjectNames() {
        ArrayList<String> projectNames = new ArrayList<>();
        for (int i = 0; i < project.size(); i++) {
            projectNames.add(project.get(i).getProjectname());
        }
        return projectNames;
    }


    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", project = " + project + ", joindate = " + joindate + ", email = " + email + ", address = " + address + ", name = " + name + ", designation = " + designation + ", surname = " + surname + ", contact = " + contact + "]";
    }
}
