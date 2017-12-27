package com.nokia.vlm.callback;

import com.nokia.vlm.entity.LeftMenuInfo;
import com.qx.framelib.callback.ActionCallback;

public interface LeftMenuCallBack extends ActionCallback {
    void obationMineInfo(int code, String msg, LeftMenuInfo info);
}
