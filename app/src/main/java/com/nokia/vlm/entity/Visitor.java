package com.nokia.vlm.entity;

import org.json.JSONObject;

/**
 * 访客模式
 */

public class Visitor {

    public int id;// 5,
    public int user_id;// 1,
    public int visitor_user_id;// 2,
    public long createtime;// 1493792691,
    public int status;// 1,
    //             user_info;// {
    public String nickname;// 某某人,
    public int is_vip;// null,
    public String portrait;// http;////ooe2lqsbw.bkt.clouddn.com/image/1492158859.jpg,
    public String interest_name;// 游泳,打篮球,打羽毛球,
    public String profession_name;// null,
    public String level;// null
    public int sex;


    public void parse(JSONObject json) {
        if (json == null) return;

        id = json.optInt("id");
        user_id = json.optInt("user_id");
        visitor_user_id = json.optInt("visitor_user_id");
        status = json.optInt("status");
        createtime = json.optLong("createtime");


        JSONObject info = json.optJSONObject("user_info");
        if (info == null) return;

        is_vip = info.optInt("is_vip");
        nickname = info.optString("nickname");
        portrait = info.optString("portrait");
        interest_name = info.optString("interest_name");
        profession_name = info.optString("profession_name");
        level = info.optString("level");
        sex = info.optInt("sex");


    }
}
