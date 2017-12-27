package com.nokia.vlm.dao;

import com.qx.framelib.callback.CallbackHelper;
import com.qx.framelib.dao.BaseDao;
import com.qx.framelib.entity.RequestEntity;
import com.nokia.vlm.callback.LogoutCallback;
import com.nokia.vlm.json.request.LogoutRequestJson;
import com.nokia.vlm.json.response.ShareResponseJson;
import com.nokia.vlm.manager.UserInfoManager;

import okhttp3.Call;

public class LogoutEngine extends BaseDao<LogoutCallback> {

    private final static String url = "user/userinfo/logout";

    public void sendLogout() {
        RequestEntity entity = new RequestEntity();
        LogoutRequestJson requestJson = new LogoutRequestJson();
        requestJson.token = UserInfoManager.getInstance().getToken();
        entity.requestBody = requestJson.encodeRequest();
        entity.url = buildUrl(url);

        postRequest(entity);
    }

    @Override
    protected void onRequestFailed(Call call, Exception e) {

        notifyDataChanged(new CallbackHelper.Caller<LogoutCallback>() {
            @Override
            public void call(LogoutCallback cb) {
                cb.sendLogout(-1001, "请求失败");
            }
        });
    }

    @Override
    protected void onRequestSuccess(String response) {

        final ShareResponseJson responseJson = new ShareResponseJson();
        responseJson.parse(response);
        notifyDataChanged(new CallbackHelper.Caller<LogoutCallback>() {
            @Override
            public void call(LogoutCallback cb) {
                if (responseJson != null) {
                    cb.sendLogout(responseJson.code, responseJson.msg);
                }
            }
        });
    }
}
