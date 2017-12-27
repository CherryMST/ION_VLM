package com.nokia.vlm.js;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

import com.qx.framelib.application.ZooerApp;
import com.qx.framelib.net.APN;
import com.qx.framelib.net.NetInfo;
import com.qx.framelib.net.NetWorkUtil;
import com.qx.framelib.utlis.TextUtil;
import com.qx.framelib.utlis.ZLog;
import com.nokia.vlm.ui.activity.ZooerBrowserActivity;
import com.nokia.vlm.utils.DeviceUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * H5<-->NATIVE 互通的桥梁
 *
 * @author luohongbo
 */
public class JsBridge {

    public static final String TAG = "JsBridge";

    public static final int JSBRIDGE_VERSION = 1;

    public static final String JS_BRIDGE_SCHEME = "js://";

    private static final String CALL_BATCH_NAME = "callBatch";

    //现在权限通通放开，任意url都通过，后续可加条件过滤
    public static final String READY_CALLBACK_FUNCTION_NAME = "readyCallback";//接口授权准备完毕回调

    private WeakReference<Activity> mActivityRef;
    private WebView sysWebView;
    private Context mContext;

    private String currentUrl;//当前访问的url;

    //记录几个时间给统计使用
    private long createTime;
    private long startLoadTime;
    private long loadedTime;


    public JsBridge(Activity activity, WebView webView) {
        this.mActivityRef = new WeakReference<Activity>(activity);
        this.mContext = ZooerApp.getAppSelf();
        this.sysWebView = webView;

        if (sysWebView != null) {
            try {
                Class<?> clazz = sysWebView.getClass();
                Method method = clazz.getMethod("removeJavascriptInterface", String.class);
                if (method != null)
                    method.invoke(sysWebView, "searchBoxJavaBridge_");
            } catch (Exception e1) {
//				e1.printStackTrace();
            }
        }
    }

    /**
     * 用对应的url加载授权
     *
     * @param url
     */
    public void loadAuthorization(String url) {
        if (TextUtil.isEmpty(url)) {
            return;
        }
        currentUrl = url;
    }


    public void updateStartLoadTime() {
        startLoadTime = System.currentTimeMillis();
    }

    public void updateLoadedTime() {
        loadedTime = System.currentTimeMillis();
    }

    /**
     * 页面加载的逻辑
     */
    public void doPageLoadFinished() {
//        if(AuthrizeManger.getInstance().existAuthInfo(currentUrl))//如果权限验证已经完成，回调一下告诉页面，否则不回调
//        {
        response(READY_CALLBACK_FUNCTION_NAME, 1, null, "true");
//        }
    }


    /**
     * 负责url的调用处理逻辑
     *
     * @param url
     */
    public void invoke(String url) {
        Uri uri = Uri.parse(url);
        //将URI中的host作为方法名，path中的第一个作为回call的方法名，如果没有回call的方法名，则不回call
        String hostAsMethodName = uri.getHost();
        if (TextUtil.isEmpty(hostAsMethodName)) {
            return;
        }
        List<String> paths = uri.getPathSegments();
        int seqid = 0;//系列号，任何请求都要系列号，因为是异步调用，不然无法关联上
        String callbackName = null;
        if (paths != null && paths.size() > 0) {
            seqid = TextUtil.parseIntValue(paths.get(0));
            if (paths.size() > 1) {
                callbackName = paths.get(1);
            }
        }

        //合并调用啦~
        if (hostAsMethodName.equals(CALL_BATCH_NAME)) {
            try {
//				int count=TextUtil.parseIntValue(uri.getQueryParameter("count"));
//				int timeout=TextUtil.parseIntValue(uri.getQueryParameter("timeout"));
                String param = uri.getQueryParameter("param");
                JSONArray jsonArray = new JSONArray(param);
                int length = jsonArray.length();
                for (int i = 0; i < length; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String method = jsonObject.getString("method");
                    int seqidOfCall = jsonObject.getInt("seqid");
                    String callback = jsonObject.optString("callback");
                    JSONObject args = jsonObject.getJSONObject("args");
                    StringBuilder uriBuilder = new StringBuilder();
                    uriBuilder.append(JS_BRIDGE_SCHEME).append(method).append("/").append(seqidOfCall).append("/").append(!TextUtil.isEmpty(callback) ? callback : "").append("?");
                    if (args != null) {
                        @SuppressWarnings("rawtypes")
                        Iterator iterator = args.keys();
                        while (iterator.hasNext()) {
                            String key = (String) iterator.next();
                            String value = Uri.decode(args.getString(key));//todo decode
                            uriBuilder.append(key).append("=").append(Uri.encode(value)).append("&");
                        }
                    }
                    Uri uriForCall = Uri.parse(uriBuilder.toString());
                    callAMethod(uriForCall, method, seqidOfCall, callback);
                }
            } catch (Exception ex) {
                if (!TextUtil.isEmpty(callbackName)) {
                    responseFail(callbackName, seqid, hostAsMethodName, JsResult.AUTHORIZE_FAIL);
                }
            } catch (OutOfMemoryError e) {
                if (!TextUtil.isEmpty(callbackName)) {
                    responseFail(callbackName, seqid, hostAsMethodName, JsResult.AUTHORIZE_FAIL);
                }
            }
        } else {
            callAMethod(uri, hostAsMethodName, seqid, callbackName);
        }
    }

    private void callAMethod(Uri uri, String hostAsMethodName, int seqid, String callbackName) {
        if (authorize(hostAsMethodName))//授权通过，调用接口
        {
            try {
                Method method = JsBridge.class.getDeclaredMethod(hostAsMethodName, Uri.class, Integer.TYPE, String.class, String.class);
                method.invoke(this, uri, seqid, hostAsMethodName, callbackName);
            } catch (Exception ex) {
                ex.printStackTrace();
                if (!TextUtil.isEmpty(callbackName)) {
                    responseFail(callbackName, seqid, hostAsMethodName, JsResult.Code_Java_Exception);
                }
            }
        } else {
            ZLog.d(TAG, "authorize failed");
            if (!TextUtil.isEmpty(callbackName)) {
                responseFail(callbackName, seqid, hostAsMethodName, JsResult.AUTHORIZE_FAIL);
            }
        }
    }

    //默认通过，后面又逻辑了再加
    private boolean authorize(String hostAsMethodName) {
        // TODO Auto-generated method stub
        return true;
    }

    public int getVersion() {
        return JSBRIDGE_VERSION;
    }


    private void callback(String function, String result) {
        if (sysWebView != null) {
            StringBuffer sb = new StringBuffer("javascript:");
//			sb.append("if(");
//			sb.append("window." + function);
//			sb.append("){");
            sb.append(function);
            sb.append("(\'");
            sb.append(result);
            sb.append("\')");
            if (ZLog.isDebug) {
                ZLog.d("kevin", "Interface response:" + sb.toString());
            }
            sysWebView.loadUrl(sb.toString());
        }
    }

    private void response(String function, int seqid, String method, String result) {
        response(function, seqid, method, result, null);
    }

    private void response(String function, int seqid, String method, String result, Map<String, String> extMap) {
        if (TextUtil.isEmpty(function)) {
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("result", JsResult.Result_OK);
            json.put("data", result);
            if (!TextUtil.isEmpty(method)) {
                json.put("method", method);
            }
            json.put("seqid", seqid);
            if (extMap != null) {
                for (String key : extMap.keySet()) {
                    json.put(key, extMap.get(key));
                }
            }

            callback(function, json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void responseFail(String callbackFun, int seqid, String method, int code) {
        if (TextUtil.isEmpty(callbackFun)) {
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("result", JsResult.Result_Fail);
            json.put("code", code);
            json.put("method", method);
            json.put("seqid", seqid);
            callback(callbackFun, json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void responseFail(String callbackFun, int seqid, String method, int code, Map<String, String> extMap) {
        if (TextUtil.isEmpty(callbackFun)) {
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("result", JsResult.Result_Fail);
            json.put("code", code);
            json.put("method", method);
            json.put("seqid", seqid);
            if (extMap != null) {
                for (String key : extMap.keySet()) {
                    json.put(key, extMap.get(key));
                }
            }
            callback(callbackFun, json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void responseFailWithData(String function, int seqid, int code, String data) {
        if (TextUtil.isEmpty(function)) {
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("result", JsResult.Result_Fail);
            json.put("code", code);
            json.put("seqid", seqid);
            json.put("data", data);
            callback(function, json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void recycle() {
        if (mActivityRef != null) {
            this.mActivityRef.clear();
        }
    }

    /**********
     * 下面定义所有JS调用的native 方法
     *****************/
    public void goback(final Uri uri, final int seqid, final String method, final String function) {
        if (mActivityRef != null && mActivityRef.get() != null) {
            Activity act = mActivityRef.get();
            if (sysWebView != null && sysWebView.canGoBack()) {
                sysWebView.goBack();
            } else if (act != null) {
                act.finish();
            }
        }
    }

    public void forward(final Uri uri, final int seqid, final String method, final String function) {
        if (sysWebView != null) {
            sysWebView.goForward();
        }
    }

    public void reload(final Uri uri, final int seqid, final String method, final String function) {
        if (sysWebView != null) {
            sysWebView.reload();
        }
    }

    /**
     * 关闭webview
     */
    public void closePage(final Uri uri, final int seqid, final String method, final String callback) {

        ZLog.d("closePage " + uri.toString());
        if (mActivityRef != null && mActivityRef.get() != null) {
            mActivityRef.get().finish();
        }
    }

    /**
     * 上一页，下一页，刷新
     *
     * @param function
     */
    public void pageControl(final Uri uri, final int seqid, final String method, final String function) {
        int type = TextUtil.parseIntValue(uri.getQueryParameter("type"));
        if (sysWebView != null) {
            if (type == 1) {
                sysWebView.goBack();
            } else if (type == 2) {
                sysWebView.goForward();
            } else {
                sysWebView.reload();
            }
        }
        response(function, seqid, method, "");
    }

//    //弹出toast
//    public void toast(final Uri uri, final int seqid, String method, final String callbackFun) {
//        int duration = TextUtil.parseIntValue(uri.getQueryParameter("duration"));
//        String text = uri.getQueryParameter("text");
//        if (text == null) {
//            String str = uri.getQueryParameter("text_encode");
//            text = URLDecoder.decode(str);
//        }
//
//        Toast.makeText(mContext, text, duration).show();
//    }

    //再打开一个webview
    public void openNewWindow(final Uri uri, final int seqid, String method, final String callbackFun) {
        String url = uri.getQueryParameter("url");
        try {
            Intent appbarIntent;
            if (mActivityRef == null || mActivityRef.get() == null) {
                appbarIntent = new Intent(ZooerApp.getAppSelf(), ZooerBrowserActivity.class);
                appbarIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appbarIntent.putExtra(ZooerBrowserActivity.PARAMS_URL, url);
                ZooerApp.getAppSelf().startActivity(appbarIntent);
            } else {
                appbarIntent = new Intent(mActivityRef.get(), ZooerBrowserActivity.class);
                appbarIntent.putExtra(ZooerBrowserActivity.PARAMS_URL, url);
                mActivityRef.get().startActivity(appbarIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //给前端一个接口获取手机信息
    public void getMobileInfo(final Uri uri, final int seqid, final String method, final String function) {
        ZLog.d("getMobileInfo " + uri.toString());
        JSONObject result = new JSONObject();
        try {
            result.put("osVer", android.os.Build.VERSION.SDK_INT);
            JSONObject resolution = new JSONObject();
            resolution.put("width", DeviceUtils.currentDeviceWidth);
            resolution.put("height", DeviceUtils.currentDeviceHeight);
            result.put("resolution", resolution);
            JSONObject netJson = new JSONObject();
            NetInfo netInfo = NetWorkUtil.getNetInfo();
            if (netInfo.apn == APN.UN_DETECT) {
                NetWorkUtil.refreshNetwork();
            }
            netJson.put("apn", netInfo.apn);
            netJson.put("isWap", netInfo.isWap ? 1 : 0);
            netJson.put("networkOperator", netInfo.networkOperator);
            netJson.put("networkType", netInfo.networkType);
            result.put("network", netJson);
            response(function, seqid, method, result.toString());
        } catch (Exception e) {
            responseFail(function, seqid, method, JsResult.Code_Java_Exception);
        }
    }

    //用户返回设备信息
    public void getDeviceInfo(final Uri uri, final int seqid, final String method, final String function) {

        ZLog.d("getDeviceInfo " + uri.toString());
        JSONObject result = new JSONObject();
        try {
            result.put("osVersion", android.os.Build.VERSION.SDK_INT);
            result.put("deviceW", DeviceUtils.currentDeviceWidth);
            result.put("deviceH", DeviceUtils.currentDeviceHeight);
            result.put("terminal", "android");

            result.put("result", JsResult.Result_OK);
            result.put("seqid", seqid);
            result.put("method", method);

            callback(function, result.toString());


//			response(function, seqid, method, result.toString());

//			sysWebView.loadUrl("javascript:alert('123')");

        } catch (Exception e) {
            responseFail(function, seqid, method, JsResult.Code_Java_Exception);
        }

    }


    //弹出dialog
    public void popDialog(final Uri uri, final int seqid, final String method, final String function) {


    }

//
//    //打开商品详情页面
//    public void openProductDetail(final Uri uri, final int seqid, final String method, final String function) {
//        try {
//            Intent intent = new Intent(mActivityRef.get(), ProductDetailActivity.class);
//            String tpl_id = uri.getQueryParameter("tpl_id");
//            if (TextUtil.isEmpty(tpl_id)) {
//                return;
//            }
//            intent.putExtra("tpl_id", tpl_id);
////			String shopid = uri.getQueryParameter("shopid");
////			if(TextUtil.isEmpty(shopid)){
////				shopid = "";
////			}
//            intent.putExtra("shop_id", ZooerApp.shop_id);
//            mActivityRef.get().startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    //加入购物车
//    public void addToCart(final Uri uri, final int seqid, final String method, final String function) {
//        try {
//            String shopid = uri.getQueryParameter("shopid");
//            String id = uri.getQueryParameter("id");
//            String sku_id = uri.getQueryParameter("sku_id");
//            String number = uri.getQueryParameter("number");
//            int _num = 1;
//            try {
//                _num = Integer.parseInt(number);
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//            }
//            AddToCartAction.getInstance().addAction(AddToCartAction.FROM_H5, id, _num + "", sku_id);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


}
