package com.nokia.vlm.entity;

import android.util.Base64;

import com.qx.framelib.utlis.SecureUtils;

import org.json.JSONObject;

/**
 * 消息列表
 */
public class MessageInfo {

    public String id;// 5258,  //消息ID
    public String title;// 消息标题, //标题
    public String content;// 消息内容, //内容
    public long send_time;// 1459654818, //发送时间
    public String type;
    public String itemId;
    public int isRead; //是否已读 0:未读 1：已读


    public static final String SECUREKEY = "#@zooer@#$%123";

    public void parse(JSONObject json) {
        if (json == null) return;

        id = json.optString("id");
        title = json.optString("title");
        content = json.optString("content");
        send_time = json.optLong("send_time");

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
            json.put("send_time", send_time);
            json.put("isRead", isRead);

            String content = json.toString();
            byte[] userInfo = SecureUtils.encryptDES(content.getBytes(), SECUREKEY);
            return Base64.encode(userInfo, Base64.NO_WRAP);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MessageInfo decode(byte[] strArray) {
        if (strArray == null || strArray.length <= 0) {
            return null;
        }

        try {
            byte[] content = Base64.decode(strArray, Base64.NO_WRAP);
            content = SecureUtils.decryptDES(content, SECUREKEY);
            JSONObject json = new JSONObject(new String(content));

            MessageInfo message = new MessageInfo();

            message.id = json.optString("id");
            message.title = json.optString("title");
            message.content = json.optString("content");
            message.send_time = json.optLong("send_time");

            message.type = json.optString("type");
            message.itemId = json.optString("itemId");
            message.isRead = json.optInt("isRead");

            return message;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
