package com.nokia.vlm.callback;


import com.qx.framelib.callback.ActionCallback;

public interface LogoutCallback extends ActionCallback {

    void sendLogout(int code, String msg);
}
