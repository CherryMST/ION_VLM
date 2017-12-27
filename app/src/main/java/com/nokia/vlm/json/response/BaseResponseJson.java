package com.nokia.vlm.json.response;

import android.text.TextUtils;
import android.util.Log;

import com.qx.framelib.utlis.SecureUtils;
import com.qx.framelib.utlis.ZLog;
import com.nokia.vlm.event.QXEventDispatcherEnum;
import com.nokia.vlm.ui.QXApp;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 服务器json数据返回基类，负责解析一些通用字段，自定义字段由子类完成解析
 */
public abstract class BaseResponseJson {

    public int code;
    public String msg;
    public String action;

    //服务器返回固定字段key
    public static final String CODE = "code";
    public static final String MSG = "msg";
    public static final String DATA = "data";
    public static final String ACTION = "action";

    //标准形式，返回content的JsonObject
    public JSONObject contentJson;

    /**
     * 公有字段，基类完成解析
     *
     * @param jsonStr
     */
    protected void parseCommon(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            this.code = jsonObject.optInt(CODE);
            this.msg = jsonObject.optString(MSG);
            this.action = jsonObject.optString(ACTION);
            this.contentJson = jsonObject.optJSONObject(DATA);
        } catch (Exception e) {
            e.printStackTrace();
            //msg = "服务端代码出错:" + e.toString();
        }
    }

    /**
     * 解析的入口，外部调用
     *
     * @param jsonStr
     */
    public void parse(String jsonStr) {

        parseCommon(jsonStr);

        if (code <= -101 && code >= -104 || code == -1001 || code == -1004) {
            ZLog.d("BaseResponseJson", "退出" + jsonStr);
            QXApp.getAppSelf().getEventDispatcher().sendEmptyMessage(QXEventDispatcherEnum.UI_EVENT_TOKEN_NO_USE);
            return;
        }
//        else if (code == -2000) { //未完善资料
//            YbbApp.getAppSelf().getEventDispatcher().sendEmptyMessage(YBBEventDispatcherEnum.UI_EVENT_NO_USER_INFO);
//        }

        parseCustom(jsonStr);
    }

    /**
     * 剩下字段需子类来完成解析
     *
     * @param jsonStr
     */
    protected abstract void parseCustom(String jsonStr);


    /**
     * 用于请求的参数签名
     *
     * @param listKey     所有的请求参数key
     * @param keyValueMap 请求参数key对应的value
     * @param api_key     API密钥
     * @return
     */
    public static String encode(List<String> listKey, Map<String, Object> keyValueMap, String api_key) {
        if (listKey == null || listKey.size() <= 0) {
            return "";
        }

        if (keyValueMap == null || keyValueMap.size() <= 0) {
            return "";
        }

        if (TextUtils.isEmpty(api_key)) {
            return "";
        }

        //先转换为strs
        String[] arrays = new String[listKey.size()];
        for (int j = 0; j < listKey.size(); j++) {
            arrays[j] = listKey.get(j);
        }

        //再排序
        Arrays.sort(arrays);

        StringBuilder sb = new StringBuilder();

        int len = arrays.length;
        for (int i = 0; i < len; i++) {
            String key = arrays[i];
            sb.append(key);
            sb.append("=");
            sb.append(keyValueMap.get(key));
            sb.append("&");
        }

        sb.append("key");
        sb.append("=");
        sb.append(api_key);

        Log.d("zooer", "encode str: " + sb.toString());
        return SecureUtils.md5Encode(sb.toString()).toUpperCase();
    }

}
