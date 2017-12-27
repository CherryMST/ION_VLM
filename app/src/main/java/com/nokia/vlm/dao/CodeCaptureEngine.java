package com.nokia.vlm.dao;


import com.nokia.vlm.callback.CodeCaptureCallBack;
import com.nokia.vlm.entity.CodeCaptureInfo;
import com.qx.framelib.callback.CallbackHelper;
import com.qx.framelib.dao.BaseDao;
import com.qx.framelib.entity.RequestEntity;

import okhttp3.Call;

public class CodeCaptureEngine extends BaseDao<CodeCaptureCallBack> {

    private String url = "syncall";

    public void getCodeCapture() {
        RequestEntity entity      = new RequestEntity();
//        requestJson.token = UserInfoManager.getInstance().getToken();
        entity.url = buildUrl(url);

        getRequest(entity);
    }

    public void getCodeCapture(String params) {
//        RequestEntity                entity      = new RequestEntity();
//        GetRequestWithParams requestJson = new GetRequestWithParams();
//        requestJson.params = params;
//        entity.requestBody = requestJson.encodeRequest();
//        entity.url = buildUrl(url);
//
//        getRequest(entity);

    }


    @Override
    protected void onRequestFailed(Call call, Exception e) {
        notifyDataChangedInMainThread(new CallbackHelper.Caller<CodeCaptureCallBack>() {
            @Override
            public void call(CodeCaptureCallBack cb) {
                cb.codeCaptureMsg(-1001, "请求失败，请检查网络", null);//离线测试临时注释
            }
        });
    }

    @Override
    protected void onRequestSuccess(String response) {
        final CodeCaptureInfo responseJson = new CodeCaptureInfo();
        responseJson.parse(response);

        notifyDataChangedInMainThread(new CallbackHelper.Caller<CodeCaptureCallBack>() {
            @Override
            public void call(CodeCaptureCallBack cb) {
                if (responseJson != null) {
                    cb.codeCaptureMsg(1,responseJson.msg, responseJson);
                }
            }
        });
    }
}
