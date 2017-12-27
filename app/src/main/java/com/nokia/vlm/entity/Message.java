package com.nokia.vlm.entity;

import android.util.Base64;

import com.qx.framelib.utlis.SecureUtils;

import org.json.JSONObject;

/**
 * 消息
 */

public class Message {

    public String id;// 5258,  //消息ID
    public String content;// 消息内容, //内容
    public String title;// 消息标题, //标题
    public long addtime;// 1459654818, //发送时间
    public String type;
    public String itemId;
    public int isRead; //是否已读 0:未读 1：已读
    public String add_time_name;
//    public String type_id;//:1邀请参与承诺，2邀请鉴证承诺


    public static final String SECUREKEY = "#@zooer@#$%123";

    public void parse(JSONObject json) {
        if (json == null) return;

        id = json.optString("id");
        title = json.optString("title");
        content = json.optString("content");
        addtime = json.optLong("createtime");
        add_time_name = json.optString("add_time_name");
//        type_id = json.optString("type_id");

        String string = json.optString("msginfo");
        if (string == null) return;

        try {
            JSONObject object = new JSONObject(string);

            type = object.optString("msgtype");
            itemId = object.optString("itemid");
        } catch (Exception e) {

        }


    }

    public byte[] encode() {
        JSONObject json = new JSONObject();
        try {

            json.put("id", id);
            json.put("title", title);
            json.put("content", content);
            json.put("type", type);
            json.put("itemId", itemId);
            json.put("addtime", addtime);
            json.put("isRead", isRead);
            json.put("add_time_name",add_time_name);

            String content = json.toString();
            byte[] userInfo = SecureUtils.encryptDES(content.getBytes(), SECUREKEY);
            return Base64.encode(userInfo, Base64.NO_WRAP);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Message decode(byte[] strArray) {
        if (strArray == null || strArray.length <= 0) {
            return null;
        }

        try {
            byte[] content = Base64.decode(strArray, Base64.NO_WRAP);
            content = SecureUtils.decryptDES(content, SECUREKEY);
            JSONObject json = new JSONObject(new String(content));

            Message message = new Message();

            message.id = json.optString("id");
            message.title = json.optString("title");
            message.content = json.optString("content");
            message.addtime = json.optLong("addtime");

            message.type = json.optString("type");
            message.itemId = json.optString("itemId");
            message.isRead = json.optInt("isRead");
            message.add_time_name = json.optString("add_time_name");


            return message;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
