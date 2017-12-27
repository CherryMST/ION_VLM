package com.nokia.vlm.dao;


import com.nokia.vlm.callback.UpdateDataCallBack;
import com.nokia.vlm.entity.UpdateDataInfo;
import com.qx.framelib.callback.CallbackHelper;
import com.qx.framelib.dao.BaseDao;
import com.qx.framelib.entity.RequestEntity;

import okhttp3.Call;

public class UpdateDataEngine extends BaseDao<UpdateDataCallBack> {

    private String url = "update";

    public void postUpdateRequest(String params) {
        RequestEntity entity      = new RequestEntity();
        entity.requestBody = params;

        entity.url = buildUrl(url);

        postRequest(entity);
    }

    /*
    public void getCodeCapture(String params) {
        RequestEntity                entity      = new RequestEntity();
                GetRequestWithParams requestJson = new GetRequestWithParams();
                requestJson.params = params;
                entity.requestBody = requestJson.encodeRequest();
        entity.url = buildUrl(url);

        getRequest(entity);
    }*/


    @Override
    protected void onRequestFailed(Call call, Exception e) {
        notifyDataChangedInMainThread(new CallbackHelper.Caller<UpdateDataCallBack>() {
            @Override
            public void call(UpdateDataCallBack cb) {
                cb.updateDataMsg(-1001, "请求失败，请检查网络", null);
            }
        });
    }

//                    cb.codeCaptureMsg(1,responseJson.msg, responseJson);
    @Override
    protected void onRequestSuccess(String response) {
        final UpdateDataInfo responseJson = new UpdateDataInfo();
        responseJson.parse(response);

        notifyDataChangedInMainThread(new CallbackHelper.Caller<UpdateDataCallBack>() {
            @Override
            public void call(UpdateDataCallBack cb) {
                cb.updateDataMsg(1,responseJson.msg, responseJson);
            }
        });
    }
}
