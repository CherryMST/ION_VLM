package com.nokia.vlm.callback;


import com.nokia.vlm.entity.CodeCaptureInfo;
import com.qx.framelib.callback.ActionCallback;

/**
 * Created by DingCuiLin on 2016/8/12.
 */

public interface CodeCaptureCallBack extends ActionCallback {

    void codeCaptureMsg(int code, String msg, CodeCaptureInfo responseJson);
}
