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
 * 我的消息
 */
public class MessageDao extends BaseDao<MessageCallBack> {
    private static final String MSG_PATH = "public/message/index";

    public void sendRequest(String last_id) {
        MessageRequest request = new MessageRequest();
        request.token = UserInfoManager.getInstance().getToken();
        request.last_id = last_id;
        RequestEntity entity = new RequestEntity();
        entity.requestBody = request.encodeRequest();
        entity.url = buildUrl(MSG_PATH);

        postRequest(entity);

    }

    @Override
    protected void onRequestFailed(Call call, Exception e) {
        notifyDataChangedInMainThread(new CallbackHelper.Caller<MessageCallBack>() {
            @Override
            public void call(MessageCallBack cb) {
                cb.jumpMessageList(-1,"",null);
            }
        });
    }

    @Override
    protected void onRequestSuccess(final String response) {
        notifyDataChangedInMainThread(new CallbackHelper.Caller<MessageCallBack>() {
            @Override
            public void call(MessageCallBack cb) {
                MessageResponse json = new MessageResponse();
                json.parse(response);

                cb.jumpMessageList(json.code,"", json);

            }
        });
    }
}
