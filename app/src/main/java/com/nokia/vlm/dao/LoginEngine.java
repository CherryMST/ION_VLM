package com.nokia.vlm.dao;


import com.qx.framelib.callback.CallbackHelper;
import com.qx.framelib.dao.BaseDao;
import com.qx.framelib.entity.RequestEntity;
import com.qx.framelib.utlis.SecureUtils;
import com.nokia.vlm.callback.LoginCallBack;
import com.nokia.vlm.entity.UserInfo;
import com.nokia.vlm.json.request.LoginRequestJson;

import okhttp3.Call;

public class LoginEngine extends BaseDao<LoginCallBack> {

    private String Register_URL = "public/login/user_login";

    public String username;//用户名
    public String password ;// 密码


    public void sendLogin(String username,String password) {
        RequestEntity entity = new RequestEntity();
        LoginRequestJson requestJson = new LoginRequestJson();
        requestJson.uin = username;
        requestJson.a1 = SecureUtils.md5Encode(password);
        requestJson.client_type = 1;
        requestJson.platform_id = "20";

        entity.requestBody = requestJson.encodeRequest();
        entity.url = buildUrl(Register_URL);
        postRequest(entity);
    }

    @Override
    protected void onRequestFailed(Call call, Exception e) {
        notifyDataChangedInMainThread(new CallbackHelper.Caller<LoginCallBack>() {
            @Override
            public void call(LoginCallBack cb) {
                cb.sendLogin(-1001, "请求失败,请检查网络", null);
            }
        });

    }

    @Override
    protected void onRequestSuccess(String response) {

        final UserInfo userInfo=new UserInfo();
        userInfo.parse(response);
        notifyDataChangedInMainThread(new CallbackHelper.Caller<LoginCallBack>() {
            @Override
            public void call(LoginCallBack cb) {
                if(userInfo!=null){
                    cb.sendLogin(userInfo.code,userInfo.msg,userInfo);
                }
            }
        });
    }
}
