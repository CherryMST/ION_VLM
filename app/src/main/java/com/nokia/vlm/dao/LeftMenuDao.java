package com.nokia.vlm.dao;

import com.nokia.vlm.callback.LeftMenuCallBack;
import com.nokia.vlm.json.request.TokenRequest;
import com.nokia.vlm.json.response.MineInfoResponse;
import com.nokia.vlm.manager.UserInfoManager;
import com.qx.framelib.callback.CallbackHelper;
import com.qx.framelib.dao.BaseDao;
import com.qx.framelib.entity.RequestEntity;

import okhttp3.Call;

public class LeftMenuDao extends BaseDao<LeftMenuCallBack> {
    private static final String MINE_INFO_PATH = "";//TODO---待补充

    public void sendRequest() {
        TokenRequest request = new TokenRequest();
        request.token = UserInfoManager.getInstance().getToken();

        RequestEntity entity = new RequestEntity();
//        entity.requestBody = request.encodeRequest();
        entity.url = buildUrl(MINE_INFO_PATH);

        postRequest(entity);

    }


    @Override
    protected void onRequestFailed(Call call, Exception e) {
        notifyDataChangedInMainThread(new CallbackHelper.Caller<LeftMenuCallBack>() {
            @Override
            public void call(LeftMenuCallBack cb) {
                cb.obationMineInfo(-1, "网络请求失败", null);
            }
        });
    }

    @Override
    protected void onRequestSuccess(final String response) {
        notifyDataChangedInMainThread(new CallbackHelper.Caller<LeftMenuCallBack>() {
            @Override
            public void call(LeftMenuCallBack cb) {
                MineInfoResponse json = new MineInfoResponse();
                json.parse(response);

                cb.obationMineInfo(json.code, json.msg, json.info);
            }
        });
    }
}
