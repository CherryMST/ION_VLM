package com.nokia.vlm.callback;


import com.nokia.vlm.entity.UpdateDataInfo;
import com.qx.framelib.callback.ActionCallback;

/**
 * Created by DingCuiLin on 2016/8/12.
 */

public interface UpdateDataCallBack extends ActionCallback {

    void updateDataMsg(int code, String msg, UpdateDataInfo responseJson);
}
