package com.nokia.vlm.json.response;


import com.nokia.vlm.entity.MessageInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息列表
 * Created by DingCuiLin on 2016/8/12.
 */

public class MessageResponse extends BaseResponseJson {

    public List<MessageInfo> list = new ArrayList<>();

    @Override
    protected void parseCustom(String jsonStr) {
        if (contentJson == null) return;

        JSONObject arr = contentJson.optJSONObject("lists");
        if (arr == null) return;
        JSONArray array = arr.optJSONArray("rows");

        if (array == null) return;

        int len = array.length();

        for (int i = 0; i < len; i++) {
            MessageInfo message = new MessageInfo();
            message.parse(array.optJSONObject(i));
            list.add(message);
        }


}
}
