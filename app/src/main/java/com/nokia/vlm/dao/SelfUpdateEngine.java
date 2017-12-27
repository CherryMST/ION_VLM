package com.nokia.vlm.dao;


import com.nokia.vlm.callback.SelfUpdateCallback;
import com.nokia.vlm.json.request.SeltUpdateRequestJson;
import com.nokia.vlm.json.response.SeltUpdateResponseJson;
import com.qx.framelib.callback.CallbackHelper;
import com.qx.framelib.dao.BaseDao;
import com.qx.framelib.entity.RequestEntity;

import okhttp3.Call;


public class SelfUpdateEngine extends BaseDao<SelfUpdateCallback> {

    private final static String url = "public/update/checkVersion";

    public void sendSelftUpdateRequest(int version_code,String name) {
        RequestEntity         entity      = new RequestEntity();
        SeltUpdateRequestJson requestJson = new SeltUpdateRequestJson();
        requestJson.from_type = name;
        requestJson.version_code = version_code;
        entity.requestBody = requestJson.encodeRequest();
        entity.url = buildUrl(url);

        postRequest(entity);
    }

    @Override
    protected void onRequestFailed(Call call, Exception e) {

        notifyDataChanged(new CallbackHelper.Caller<SelfUpdateCallback>() {
            @Override
            public void call(SelfUpdateCallback cb) {
                cb.selfUpdateCallback(-1001, null);
            }
        });
    }

    @Override
    protected void onRequestSuccess(String response) {

        final SeltUpdateResponseJson responseJson = new SeltUpdateResponseJson();
        responseJson.parse(response);
        notifyDataChanged(new CallbackHelper.Caller<SelfUpdateCallback>() {
            @Override
            public void call(SelfUpdateCallback cb) {
                if (responseJson != null) {
                    cb.selfUpdateCallback(responseJson.code, responseJson.selfUpdateInfo);
                }
            }
        });
    }
}
