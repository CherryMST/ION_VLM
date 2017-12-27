package com.qx.framelib.dao;


import android.text.TextUtils;

import com.qx.framelib.application.ZooerApp;
import com.qx.framelib.callback.ActionCallback;
import com.qx.framelib.callback.CallbackHelper;
import com.qx.framelib.entity.RequestEntity;
import com.qx.framelib.utlis.HandlerUtils;
import com.qx.framelib.utlis.TimeUtils;
import com.qx.framelib.utlis.ZLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.Call;

/**
 * 业务基类，与BaseModuleEngine的区别是，继承这个类的可以有回调方法通知UI。
 *
 * @param <T>
 */
public abstract class BaseDao<T extends ActionCallback> {

    protected CallbackHelper<T> callbacks = new CallbackHelper<T>();

    public BaseDao() {

    }

    public void register(T cb) {
        callbacks.register(cb);
    }

    public void unregister(T cb) {
        callbacks.unregister(cb);
    }

    public void unregisterAll() {
        callbacks.unregisterAll();
    }

    /**
     * 通知UI数据改变了。在主线程通知
     *
     * @param caller
     */
    protected void notifyDataChangedInMainThread(final CallbackHelper.Caller<T> caller) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataChanged(caller);
            }
        });
    }

    /**
     * 延迟一些时间通知UI数据改变了。在主线程通知
     *
     * @param caller
     * @param delayMillis
     */
    protected void delayNotifyDataChangedInMainThread(final CallbackHelper.Caller<T> caller, long delayMillis) {
        HandlerUtils.getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataChanged(caller);
            }
        }, delayMillis);
    }

    /**
     * 通知UI数据改变了。这个方法跑在哪个线程取决于调用方。如果需要通知UI，而没有在主线程调用，那么请使用notifyDataChangedInMainThread
     *
     * @param caller
     */
    protected void notifyDataChanged(CallbackHelper.Caller<T> caller) {
        callbacks.broadcast(caller);
    }

    /**
     * 在主线程执行一些事情。UI数据变更跟通知一定要在同一个runnable里执行完毕。通知的话，一定要使用notifyDataChanged，而不是notifyDataChangedInMainThread。保证数据变更跟通知是一个原子操作
     *
     * @param r
     */
    protected void runOnUiThread(Runnable r) {
        HandlerUtils.getMainHandler().post(r);
    }

    protected void postString(String url, String requestBody) {
        try {
            PostFormBuilder builder = OkHttpUtils.post();

            if (!TextUtils.isEmpty(requestBody)) {
                JSONObject json = new JSONObject(requestBody);
                Iterator<String> it = json.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = json.optString(key);

                    builder.addParams(key, value);
                }
            }

            builder.url(url);

            ZLog.d(getClass().getName(), "builder:" + builder.toString());

            RequestCall call = builder.build();

            call.execute(new StringCallback() {

                @Override
                public void onError(Call call, Exception e) {
                    onRequestFailed(call, e);
                }

                @Override
                public void onResponse(String response) {
                    onRequestSuccess(response);
                }
            });
        } catch (Exception e) {
            onRequestFailed(null, e);
        }
    }

    /**
     * get请求
     *
     * @param request
     */
    public void getRequest(final RequestEntity request) {
        try {

            if (request == null) return;
            if (TextUtils.isEmpty(request.url)) return;


            GetBuilder builder = OkHttpUtils.get();
//            request.startTime = System.currentTimeMillis();
//
//            request.tag = String.valueOf(CommonUtil.getUniqueId());
//
            if (!TextUtils.isEmpty(request.requestBody)) {

                JSONObject json = new JSONObject(request.requestBody);
                Iterator<String> it = json.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = json.optString(key);

                    builder.addParams(key, value);
                }

            }
            builder.url(request.url);

            RequestCall call = builder.build();
            call.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    onRequestFinish(0, request, "", call, e);
                }

                @Override
                public void onResponse(String response) {
                    onRequestFinish(1, request, response, null, null);
                }

            });

        } catch (Exception e) {
            onRequestFinish(-1, request, "", null, e);
        }
    }

    protected void postRequest(final RequestEntity request) {
        try {

            if (request == null) return;
            if (TextUtils.isEmpty(request.url)) return;

//            request.startTime = System.currentTimeMillis();
//
//            request.tag = String.valueOf(CommonUtil.getUniqueId());

            PostFormBuilder builder = OkHttpUtils.post();
            if (!TextUtils.isEmpty(request.requestBody)) {

                JSONObject json = new JSONObject(request.requestBody);
                Iterator<String> it = json.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = json.optString(key);
                    //URLEncoder.encode()
                    builder.addParams(key, value);
                }

            }
//
//            if (request.isUpload && request.file != null) {//上传文件
//                String[] s = request.file.getPath().split("\\.");
//                if (s.length > 1) {
//                    builder.addFile("filedata", "img_upload."+s[s.length-1], request.file);
//                }
//
//            }
//            builder.tag(request.tag);
            builder.url(request.url);

            RequestCall call = builder.build();

            call.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    onRequestFinish(0, request, "", call, e);
                }

                @Override
                public void onResponse(String response) {
                    onRequestFinish(1, request, response, null, null);
                }

                @Override
                public void inProgress(float progress) {
                    super.inProgress(progress);
                    onRequestProgress(progress, 0);
                }


            });

        } catch (Exception e) {
//            onRequestFailed(null, e);
            onRequestFinish(-1, request, "", null, e);
        }
    }



    private void onRequestFinish(final int errorCode, final RequestEntity requestEntity, final String responseBody, final Call call, final Exception e) {

//        if (ZLog.isDebug) {
//
//            StringBuffer sb = new StringBuffer();
//            sb.append(TimeUtils.getDateandMillisecondFromMillisecond(requestEntity.startTime));
//            sb.append(" \n ----------------- \n");
//
//            sb.append(" --- requestUrl :");
//            sb.append(requestEntity.url);
//            sb.append("\n");
//
//            sb.append(" --- requestTag:");
//            sb.append(requestEntity.tag);
//            sb.append("\n");
//
//            sb.append(" --- requestContent:");
//            sb.append(requestEntity.requestBody);
//            sb.append("\n");
//
//
//            sb.append(" --- errorCode:");
//            sb.append(errorCode);
//            sb.append("\n");
//
//            long endTime = System.currentTimeMillis();
//            sb.append("---- endTime:");
//            sb.append(TimeUtils.getDateandMillisecondFromMillisecond(endTime));
//            sb.append("\n");
//            long costTime = endTime - requestEntity.startTime;
//            sb.append(" --- costTime:");
//            sb.append(costTime);
//            sb.append("\n");
//
//            sb.append("responseContent: ");
//            sb.append(responseBody);
//            if (errorCode != 1) { //请求失败
//                sb.append(" \n --------ERROR------- \n");
//                sb.append(e.toString());
//                if (call != null) {
//                    sb.append(" --- Call:");
//                    if (call != null)
//                        sb.append(call.toString());
//                }
//
//
//            }
//            sb.append("\n");
//
//            ZLog.f(sb.toString(), "debug.txt", true);
//
//        }

        if (errorCode == 1) { //请求成功
            ZLog.d(getClass().getName(), "responseBody:" + responseBody);
            onRequestSuccess(responseBody);
        } else if (errorCode == -1) { //try catch 异常
            ZLog.d(getClass().getName(), "Exception:" + e.toString());
            onRequestFailed(null, e);
        } else { //网络请求失败
            ZLog.d(getClass().getName(), "call:" + call.toString() + "  Exception:" + e.toString());
            onRequestFailed(call, e);
        }

        if (!ZLog.isDebug) {
            return;
        }

        HandlerUtils.getHandler("LogThread").postDelayed(new Runnable() {
            @Override
            public void run() {


                StringBuffer sb = new StringBuffer();
                sb.append(TimeUtils.getDateandMillisecondFromMillisecond(requestEntity.startTime));
                sb.append(" \n ----------------- \n");

                sb.append(" --- requestUrl :");
                sb.append(requestEntity.url);
                sb.append("\n");

                sb.append(" --- requestTag:");
                sb.append(requestEntity.tag);
                sb.append("\n");

                sb.append(" --- requestContent:");
                sb.append(requestEntity.requestBody);
                sb.append("\n");


                sb.append(" --- errorCode:");
                sb.append(errorCode);
                sb.append("\n");

                long endTime = System.currentTimeMillis();
                sb.append("---- endTime:");
                sb.append(TimeUtils.getDateandMillisecondFromMillisecond(endTime));
                sb.append("\n");
                long costTime = endTime - requestEntity.startTime;
                sb.append(" --- costTime:");
                sb.append(costTime);
                sb.append("\n");

                sb.append("responseContent: ");
                sb.append(responseBody);
                if (errorCode != 1) { //请求失败
                    sb.append(" \n --------ERROR------- \n");
                    sb.append(e.toString());
                    if (call != null) {
                        sb.append(" --- Call:");
                        if (call != null)
                            sb.append(call.toString());
                    }


                }
                sb.append("\n");

                ZLog.f(sb.toString(), "debug.txt", true);

            }
        }, 50);

    }


    //组装url
    protected String buildUrl(String path) {
        StringBuffer sb = new StringBuffer();
        sb.append(ZooerApp.api_host);
        sb.append("/api/");
        sb.append(path);
        ZLog.d(getClass().getName(), "buildUrl--->" + sb.toString());
        return sb.toString();
    }

    protected abstract void onRequestFailed(Call call, Exception e);

    protected abstract void onRequestSuccess(String response);

    /**
     * 上传 或 下载 是用到
     *
     * @param progress 上传 或 下载的进度
     * @param total    文件的大小 上传不用
     */
    protected void onRequestProgress(float progress, long total) {
    }


}
