package com.nokia.vlm.callback;


import com.nokia.vlm.entity.SelfUpdateInfo;
import com.qx.framelib.callback.ActionCallback;


public interface SelfUpdateCallback extends ActionCallback {

    void selfUpdateCallback(int code, SelfUpdateInfo selfUpdateInfo);
}
