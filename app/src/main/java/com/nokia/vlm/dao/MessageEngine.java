package com.nokia.vlm.dao;


import com.nokia.vlm.callback.MessageCallBack;
import com.nokia.vlm.json.request.MessageRequest;
import com.nokia.vlm.json.response.MessageResponse;
import com.nokia.vlm.manager.UserInfoManager;
import com.qx.framelib.callback.CallbackHelper;
import com.qx.framelib.dao.BaseDao;
import com.qx.framelib.entity.RequestEntity;

import okhttp3.Call;

/**
 * Created by DingCuiLin on 2016/8/12.
 */

public class MessageEngine extends BaseDao<MessageCallBack> {

    private final static String url = "public/message/index";
    public String last_id;

    public void jumpGetMessage(String last_id) {
        RequestEntity entity = new RequestEntity();
        MessageRequest requestJson = new MessageRequest();
        requestJson.token = UserInfoManager.getInstance().getToken();
        requestJson.last_id = last_id;

        entity.requestBody = requestJson.encodeRequest();
        entity.url = buildUrl(url);

        postRequest(entity);
    }


    @Override
    protected void onRequestFailed(Call call, Exception e) {

        notifyDataChangedInMainThread(new CallbackHelper.Caller<MessageCallBack>() {
            @Override
            public void call(MessageCallBack cb) {
                cb.jumpMessageList(-1001, "请求失败,请检查网络", null);
            }
        });
    }

    @Override
    protected void onRequestSuccess(String response) {

        final MessageResponse responseJson = new MessageResponse();
        responseJson.parse(response);
        notifyDataChangedInMainThread(new CallbackHelper.Caller<MessageCallBack>() {
            @Override
            public void call(MessageCallBack cb) {
                if (responseJson != null) {
                    cb.jumpMessageList(responseJson.code, responseJson.msg, responseJson);
                }
            }
        });
    }
}
