package com.nokia.vlm.entity;

import org.json.JSONObject;


public class SelfUpdateInfo {

    public String type;
    public String down_url;
    public String title;
    public String desc;
    public int version_code;
    public String version_name;
    public int update_type;
    public String createtime;

    public void parse(JSONObject json) {
        if (json == null) return;
        type = json.optString("type");
        down_url = json.optString("down_url");
        title = json.optString("title");
        desc = json.optString("desc");
        version_code = json.optInt("version_code");
        version_name = json.optString("version_name");
        update_type = json.optInt("update_type");
        createtime = json.optString("createtime");
    }
}
