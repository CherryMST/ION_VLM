package com.nokia.vlm.callback;


import com.nokia.vlm.json.response.MessageResponse;
import com.qx.framelib.callback.ActionCallback;

/**
 * Created by DingCuiLin on 2016/8/12.
 */

public interface MessageCallBack extends ActionCallback {

    void jumpMessageList(int code, String msg, MessageResponse responseJson);
}
