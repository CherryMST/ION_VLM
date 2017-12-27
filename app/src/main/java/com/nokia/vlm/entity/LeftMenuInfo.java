package com.nokia.vlm.entity;

import org.json.JSONObject;

public class LeftMenuInfo {

    public String username;// 18657149171,

    public String department;//FSV    部门
    public String fullname;//全名



    public void parse(JSONObject json) {
        if (json == null) return;
        username = json.optString("username");
        department = json.optString("department");
        fullname = json.optString("fullname");

        if ("null".equals(username)) {
            username = "";
        }
        if ("null".equals(department)) {
            department = "";
        }
        if ("null".equals(fullname)) {
            fullname = "";
        }


    }
}
