package com.nokia.vlm.entity;

import android.util.Base64;

import com.qx.framelib.utlis.SecureUtils;
import com.qx.framelib.utlis.ZLog;
import com.nokia.vlm.json.response.BaseResponseJson;

import org.json.JSONObject;

/**
 * 用户信息
 */
public class UserInfo extends BaseResponseJson {

    public String id;
    public String username;
    public String nickname;
    public String surname;
    public String portrait;
    public String token;
    public String refresh_token;
    public String rongytoken;
    public int sex;
    public double longitude = 0;
    public double latitude = 0;
    public int status;
    public boolean is_vip;
    public boolean is_pay_pwd;


    public static final String SECUREKEY = "#@zooer@#$%123";//待修改


    /**
     * 加密用户资料信息
     *
     * @return
     */
    public byte[] encodeUserInfo() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("username", username);
//            json.put("nickname", nickname);
//            json.put("surname", surname);
//            json.put("portrait", portrait);
            json.put("token", token);
            json.put("refresh_token", refresh_token);
//            json.put("rongytoken", rongytoken);
            json.put("sex", sex);
            json.put("status", status);
//            json.put("longitude", longitude);
//            json.put("latitude", latitude);
//            json.put("is_vip", is_vip);
//            json.put("is_pay_pwd", is_pay_pwd);


            String content = json.toString();
            byte[] userInfo = SecureUtils.encryptDES(content.getBytes(), SECUREKEY);
            return Base64.encode(userInfo, Base64.NO_WRAP);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密用户资料信息
     *
     * @param strArray
     * @return
     */
    public static UserInfo decodeUserInfo(byte[] strArray) {
        if (strArray == null || strArray.length <= 0) {
            return null;
        }
        try {
            byte[] content = Base64.decode(strArray, Base64.NO_WRAP);
            content = SecureUtils.decryptDES(content, SECUREKEY);
            JSONObject json = new JSONObject(new String(content));

            UserInfo info = new UserInfo();
            info.id = json.optString("id");
            info.username = json.optString("username");
//            info.nickname = json.optString("nickname");
//            info.surname = json.optString("surname");
//            info.portrait = json.optString("portrait");
            info.token = json.optString("token");
            info.refresh_token = json.optString("refresh_token");
//            info.rongytoken = json.optString("rongytoken");
            info.sex = json.optInt("sex");
            info.status = json.optInt("status");
//            info.longitude = json.optDouble("longitude");
//            info.latitude = json.optDouble("latitude");
//            info.is_vip = json.optBoolean("is_vip");
//            info.is_pay_pwd = json.optBoolean("is_pay_pwd");


            return info;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void parseCustom(String jsonStr) {

        try {
            if (contentJson == null) return;
            ZLog.d("UserInfo", contentJson + "contenJson");

            this.id = contentJson.optString("id");
            this.username = contentJson.optString("username");
//            this.nickname = contentJson.optString("nickname");
//            this.surname = contentJson.optString("surname");
//            this.portrait = contentJson.optString("portrait");
            this.token = contentJson.optString("token");
            this.refresh_token = contentJson.optString("refresh_token");
//            this.rongytoken = contentJson.optString("rongytoken");
            this.sex = contentJson.optInt("sex");
            this.status = contentJson.optInt("status");
//            this.longitude = contentJson.optDouble("longitude");
//            this.latitude = contentJson.optDouble("latitude");
//            this.is_vip = contentJson.optInt("is_vip") == 1;
//            this.is_pay_pwd = contentJson.optInt("is_pay_pwd") == 1;

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
