package com.nokia.vlm.callback;


import com.qx.framelib.callback.ActionCallback;
import com.nokia.vlm.entity.UserInfo;



public interface LoginCallBack extends ActionCallback {

    void sendLogin(int code, String msg, UserInfo userInfo);
}
