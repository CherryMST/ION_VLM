package com.nokia.vlm.entity;

import com.nokia.vlm.json.response.BaseResponseJson;

public class CompleteMessageInfo extends BaseResponseJson{
    public String portrait;
    public String rongytoken;
    public String nickname;
    public String surname;
    public int sex;


    @Override
    protected void parseCustom(String jsonStr) {
        if (contentJson == null)return;

        portrait = contentJson.optString("portrait");
        rongytoken = contentJson.optString("rongytoken");
        nickname = contentJson.optString("nickname");
        surname = contentJson.optString("surname");
        sex = contentJson.optInt("sex");
    }
}
