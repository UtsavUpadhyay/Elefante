package com.elefante;


public class GlobalSettings {

    private static GlobalSettings instance = null;

    private String loginId, password;

    private MySharedPreferences mySharedPreferences;


    public synchronized static GlobalSettings getInstance() {
        if (instance == null) {
            instance = new GlobalSettings();
        }

        return instance;
    }

    public void setLoginIdPassword(String loginId, String password) {
        mySharedPreferences.addString("tmp_user_id", loginId);
        mySharedPreferences.addString("tmp_password", password);
        this.loginId = loginId;
        this.password = password;
    }

    public String getLoginId() {
        if (!mySharedPreferences.getSharedPreferences().getString("tmp_user_id", "").equals("")) {
            return this.loginId;
        }

        return null;
    }

    public String getPassword() {
        if (!mySharedPreferences.getSharedPreferences().getString("tmp_password", "").equals("")) {
            return this.password;
        }

        return null;
    }

    public void setSharedPreferences(MySharedPreferences sp) {
        this.mySharedPreferences = sp;
    }

}
