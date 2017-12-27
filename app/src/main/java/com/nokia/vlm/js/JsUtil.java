package com.nokia.vlm.js;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.qx.framelib.application.ZooerApp;
import com.nokia.vlm.ui.ZooerConstants;

public class JsUtil {

    public static final String AUTH_TYPE_NONE = "NONE";
    public static final String AUTH_TYPE_QQ = "QQ";
    public static final String AUTH_TYPE_WECHAT = "WX";
    public static final String AUTH_TYPE_ALL = "ALL";

    /**
     * 将cookie同步到webview中去。
     *
     * @param context
     * @param url
     * @param authKeyType
     */
    public synchronized static void synCookies(Context context, String url, String authKeyType) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        //	    cookieManager.removeSessionCookie();//这行不能加。移除是在一个线程上进行的，有可能将后面增加的又移除了 http://stackoverflow.com/questions/5729266/cookies-webview-cookiesyncmanager-in-android

        Uri uri = Uri.parse(url);
        if (uri == null || uri.getHost() == null) {
            return;
        }
        String host = uri.getHost().toLowerCase();
        String cookieDomain = null;
        if (host.endsWith(".qq.com")) {
            cookieDomain = ".qq.com";
        }

        if ((AUTH_TYPE_QQ.equals(authKeyType) || AUTH_TYPE_ALL.equals(authKeyType))
//                && LoginProxy.getInstance().isMobileQLogin()
                ) {
//            cookieManager.setCookie(url, getSetCookieSring("logintype", LoginProxy.getInstance().getIdentityType().name(), cookieDomain));
//            cookieManager.setCookie(url, getSetCookieSring("skey", LoginProxy.getInstance().getMobileQSkey(), cookieDomain));
//            cookieManager.setCookie(url, getSetCookieSring("uin", "o0" + LoginProxy.getInstance().getMObileQUin(), cookieDomain));
//            cookieManager.setCookie(url, getSetCookieSring("sid", LoginProxy.getInstance().getMobileQSid(), cookieDomain));
//            cookieManager.setCookie(url, getSetCookieSring("vkey", LoginProxy.getInstance().getMobileQVkey(), cookieDomain));
        }
        else if ((AUTH_TYPE_WECHAT.equals(authKeyType) || AUTH_TYPE_ALL.equals(authKeyType))
//                && LoginProxy.getInstance().isWXLogin()
                ) {
//            cookieManager.setCookie(url, getSetCookieSring("logintype", LoginProxy.getInstance().getIdentityType().name(), cookieDomain));
//            cookieManager.setCookie(url, getSetCookieSring("openid", LoginProxy.getInstance().getWXOpenId(), cookieDomain));
//            cookieManager.setCookie(url, getSetCookieSring("accesstoken", LoginProxy.getInstance().getWXAccessToken(), cookieDomain));
        }
        else//这里要保证之前的登录态cookie会被移除。写个空的
        {
            cookieManager.setCookie(url, getSetCookieSring("logintype", ZooerConstants.IdentityType.NONE.name(), cookieDomain));
            cookieManager.setCookie(url, getSetCookieSring("skey", "", cookieDomain));
            cookieManager.setCookie(url, getSetCookieSring("uin", "", cookieDomain));
            cookieManager.setCookie(url, getSetCookieSring("openid", "", cookieDomain));
            cookieManager.setCookie(url, getSetCookieSring("accesstoken", "", cookieDomain));
            cookieManager.setCookie(url, getSetCookieSring("sid", "", cookieDomain));
            cookieManager.setCookie(url, getSetCookieSring("vkey", "", cookieDomain));
        }
        CookieSyncManager.getInstance().sync();
    }

    public synchronized static void saveExternalInfo(Context context,String url,CookieInfo cookieInfo){
        if (TextUtils.isEmpty(url)) {
            return;
        }
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        //	    cookieManager.removeSessionCookie();//这行不能加。移除是在一个线程上进行的，有可能将后面增加的又移除了 http://stackoverflow.com/questions/5729266/cookies-webview-cookiesyncmanager-in-android

        Uri uri = Uri.parse(url);
        if (uri == null || uri.getHost() == null) {
            return;
        }
        String host = uri.getHost().toLowerCase();
        String cookieDomain = null;
        if (host.endsWith(".qq.com")) {
            cookieDomain = ".qq.com";
        }
        cookieManager.setCookie(url, getSetCookieSring("qopenid", cookieInfo.openId, cookieDomain));
        cookieManager.setCookie(url, getSetCookieSring("qaccesstoken", cookieInfo.accessToken, cookieDomain));
        cookieManager.setCookie(url, getSetCookieSring("openappid", cookieInfo.openAppid+"", cookieDomain));
        CookieSyncManager.getInstance().sync();
    }
    public static void saveUserFit(Context context, String url,int flag){
        saveCookie(context, url, "userfit", flag+"");
    }
    /**这个不要多次调用*/
    public synchronized static void saveCookie(Context context, String url,String key,String value){
        if (TextUtils.isEmpty(url)) {
            return;
        }
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        Uri uri = Uri.parse(url);
        if (uri == null || uri.getHost() == null) {
            return;
        }
        String host = uri.getHost().toLowerCase();
        String cookieDomain = null;
        if (host.endsWith(".qq.com")) {
            cookieDomain = ".qq.com";
        }
        cookieManager.setCookie(url, getSetCookieSring(key, value, cookieDomain));
        CookieSyncManager.getInstance().sync();
    }
    private static String getSetCookieSring(String name, String value, String domain) {
        String v = name + "=" + value;
        if (domain != null) {
            v += "; path=/";
            v += "; domain=" + domain;
        }
        return v;
    }

    public static boolean isQQdomain(String host)
    {
        if(host!=null)
        {
            return host.toLowerCase().endsWith(".qq.com");
        }
        return false;
    }
    //用强制对象同步的方式来观察SQLiteDiskIOException的问题，理论上也不会太耗时
    public static synchronized void clearCookie(){
        // Edge case: an illegal state exception is thrown if an instance of
        // CookieSyncManager has not be created.  CookieSyncManager is normally
        // created by a WebKit view, but this might happen if you start the
        // app, restore saved state, and click logout before running a UI
        // dialog in a WebView -- in which case the app crashes
        @SuppressWarnings("unused")
        CookieSyncManager cookieSyncMngr =
                CookieSyncManager.createInstance(ZooerApp.getAppSelf());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }

    public static class CookieInfo{
        public String openId;
        public String accessToken;
        public long openAppid;
    }
}
