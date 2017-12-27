package com.nokia.vlm.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.qx.framelib.activity.BaseToolBarActivity;
import com.qx.framelib.event.listener.UIEventListener;
import com.qx.framelib.utlis.CameraAndPhotoUtils;
import com.qx.framelib.utlis.FileUtils;
import com.qx.framelib.utlis.HandlerUtils;
import com.qx.framelib.utlis.TextUtil;
import com.qx.framelib.utlis.ZLog;
import com.nokia.vlm.R;
import com.nokia.vlm.common.FullscreenableChromeClient;
import com.nokia.vlm.js.JsBridge;
import com.nokia.vlm.js.JsUtil;
import com.nokia.vlm.ui.QXApp;
import com.nokia.vlm.utils.IntentDataUtils;
import com.nokia.vlm.utils.WebNativeUtlis;
import com.nokia.vlm.utils.WebviewHackUtils;
import com.nokia.vlm.view.FixedWebView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * 浏览器activity
 */
public class ZooerBrowserActivity extends BaseToolBarActivity implements UIEventListener {

    private static final String TAG = "ZooerBrowserActivity";

    //外部传入的url
    public static final String PARAMS_URL = "com.qixiangnet.deqin.BROWSER_URL";
    private String url;

    public static final String KEY_GOBACK = "goback"; //是否左上角的返回是返回上一个页面（默认结束当前Activity)
    private String shouldGoBack;

    //点击物理返回键，是否返回上一个页面
    public static final String PARAMS_IS_GO_BACK = "com.qixiangnet.deqin.activity.isGoBack";
    private boolean isGoBack = true; // true:返回上一个页面  false：finish

    //是否隐藏底部前进后退工具栏 itent传参数过来
    public static final String PARAMS_TOOLBAR = "com.qixiangnet.deqin.activity.BROWSER_TOOLBAR";
    private boolean shouldShowToolBar; // “0” 隐藏    “1” 显示

    //是否打开硬件加速
    public static final String PARAMS_ACCELERATE = "com.qixiangnet.deqin.activity.BROWSER_ACCELERATE";
    private boolean shouldHardwareAccelerate;   // 0 关闭  1 打开

    //是否支持缩放
    public static final String PARAMS_SUPPORT_ZOOM = "com.qixiangnet.deqin.activity.BROWSER_ZOOM";
    private boolean shouldSupportZoom;// 0 不支持   1  支持

    private boolean isSwf = false;


    public static final String PARAMS_SHOW_TILE = "com.qixiangnet.deqin.activity.showTitle";
    private boolean showTitle = true;//默认显示标题

    public static final String PARAMS_TITLE_CONTENT = "com.zooernet.amll.activity.title";


    private String title = "浏览器";

    private Context context;

    private FixedWebView webView;
    private FrameLayout webviewContainer;
    private ProgressBar loadingView;
    private JsBridge jsBridge;

//    public WebViewFooter footerView;

    private RelativeLayout browserContentView;

//    private NormalErrorPage errorPage;

    public static final String AUTH_TYPE_NONE = "NONE";
    public static final String AUTH_TYPE_QQ = "QQ";
    public static final String AUTH_TYPE_WECHAT = "WX";
    public static final String AUTH_TYPE_ALL = "ALL";
    private String authKeyType = AUTH_TYPE_NONE;

    //title
//    private CommonTitleView titleView;

    private volatile ValueCallback<Uri> mUploadMessage;
    public final static int FILECHOOSER_RESULTCODE = 100;

    private static ArrayList<String> specialModel = new ArrayList<String>();

    static {
        specialModel.add("MT870");
        specialModel.add("XT910");
        specialModel.add("XT928");
        specialModel.add("MT917");
        specialModel.add("Lenovo A60");
    }


    public boolean isResumeFromPause = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        WebviewHackUtils.clearSGoogleApps();

//        String token = UserInfoManager.getInstance().getToken();
//        if (TextUtil.isEmpty(token) || "null".equals(token)) {
////            LoginUtils.launchLogin(this);
////            finish();
//            return;
//        }

        context = this;
        super.setContentView(R.layout.browser_layout);
        handleIntent(getIntent());


//        ZooerApp.getAppSelf().getEventController().addUIEventListener(EventDispatcherEnum.REMIND_UI_REFRESH, this);//提示刷新
    }

    private void handleIntent(Intent intent) {
        initData(intent);
        initView();
        initExternalInfo();
        initSetting();
        doRefresh();
    }

    private void doRefresh() {
        showWebView(true);
        loadingView.setProgress(0);
        HandlerUtils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(url)) {
                    //在这里设置cookie
//                    JsUtil.synCookies(ZooerBrowserActivity.this, url, authKeyType);
                    if (jsBridge != null) {
                        jsBridge.updateStartLoadTime();
                    }
                    if (webView != null) {
                        webView.loadUrl(url);
                        ZLog.d(TAG, "--> doRefresh " + url);
                    }
                }
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void initSetting() {

        webView.setScrollbarFadingEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        final int sdkVersion = Build.VERSION.SDK_INT;
        WebSettings settings = webView.getSettings();
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);

        settings.setUserAgentString("android  Webkit / zooer webview");
        settings.setJavaScriptEnabled(true);

        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);


        settings.setSavePassword(false);
        {
            Class<?> clazz = settings.getClass();
            try {
                Method method = clazz.getMethod("setPluginsEnabled", boolean.class);
                if (method != null) {
                    method.invoke(settings, true);
                }
            } catch (NoSuchMethodException e) {
                //e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        settings.setDomStorageEnabled(true);
        try {
            Class<?> clazz = webView.getClass();
            Method method = clazz.getMethod("removeJavascriptInterface", String.class);
            if (method != null)
                method.invoke(webView, "searchBoxJavaBridge_");
        } catch (Exception e1) {
            //e1.printStackTrace();
        }

        settings.setAppCachePath(FileUtils.getWebViewCacheDir());
        settings.setDatabasePath(FileUtils.getWebViewCacheDir());
        /**
         * we now does not need geo
         */
        //settings.setGeolocationDatabasePath(FileUtil.getWebViewCacheDir());
        //settings.setGeolocationEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);

        if (supportWebViewFullScreen()) {  // 全屏显示默认浏览器
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);

            settings.setBuiltInZoomControls(false);
            settings.setDisplayZoomControls(false);

//		    if (DeviceUtils.IS_SURPORT_MUTITOUCH_GESTURER) // 如果手机支持多点触摸，那么就屏蔽放大缩小栏
//		    {
//		    	settings.setBuiltInZoomControls(true);
//		    	settings.setDisplayZoomControls(true);
//		    }
        }

        if (sdkVersion < 14) {
//            Log.d("zooer", "chromeclient < 14 is " + sdkVersion);
            webView.setWebChromeClient(mWebChromeClient);
        } else if (sdkVersion >= 21) {
//            Log.d("zooer", "chromeclient >=21 is " + sdkVersion);
            webView.setWebChromeClient(mWebChromeClient_5);
        } else {
//            Log.d("zooer", "chromeclient other is " + sdkVersion);
            webView.setWebChromeClient(new FullscreenableChromeClient(new WebChromeClientListener() {

                @Override
                public Activity getActivity() {
                    return ZooerBrowserActivity.this;
                }

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    loadingView.setProgress(newProgress);
                }

                @Override
                public void onReceivedTitle(WebView view, String title) {
//                    titleView.setTitle(title);
                    setTitle(title);
                }
            }));
        }

        webView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                return true;
            }
        });


        webView.setWebViewClient(new MyWebViewClient());
        webView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                try {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    intent.putExtra(PARAMS_PRE_ACTIVITY_TAG_NAME, getActivityPageId());
//                    startActivity(intent);
//                } catch (Exception e) { //fix rdm:62644428 android.content.ActivityNotFoundException, 部分机型可能不支持
//                }

            }
        });
        WebviewHackUtils.clearSGoogleApps();

    }

    /**
     * 是否支持全屏显示  步步高(vivo不支持)。特定型号不支持
     *
     * @return
     */
    private boolean supportWebViewFullScreen() {
        final String model = Build.MODEL;
        return !model.contains("vivo") && !specialModel.contains(model);
    }

    private ValueCallback<Uri[]> mUploadCallbackAboveL;

    private WebChromeClient mWebChromeClient_5 = new WebChromeClient() {

        public void onProgressChanged(WebView view, int newProgress) {
            loadingView.setProgress(newProgress);
        }

        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        public void onReceivedTitle(WebView view, String title) {
            setTitle(title);
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg) {

        }

        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {

        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {

        }

        // For Android 5.0+
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

            Log.d("zooer", "onShowFileChooser 5.0+");

            mUploadCallbackAboveL = filePathCallback;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(
                    Intent.createChooser(i, "请选择一个文件"),
                    FILECHOOSER_RESULTCODE);
            return true;
        }
    };

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==FILECHOOSER_RESULTCODE)
//        {
//            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
//            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
//            if (mUploadCallbackAboveL != null) {
//                onActivityResultAboveL(requestCode, resultCode, data);
//            }
//            else  if (mUploadMessage != null) {
//                mUploadMessage.onReceiveValue(result);
//                mUploadMessage = null;
//            }
//        }
//    }
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
//        if (requestCode != FILECHOOSER_RESULTCODE
//                || mUploadCallbackAboveL == null) {
//            return;
//        }
//        Uri[] results = null;
//        if (resultCode == Activity.RESULT_OK) {
//            if (data == null) {
//            } else {
//                String dataString = data.getDataString();
//                ClipData clipData = data.getClipData();
//                if (clipData != null) {
//                    results = new Uri[clipData.getItemCount()];
//                    for (int i = 0; i < clipData.getItemCount(); i++) {
//                        ClipData.Item item = clipData.getItemAt(i);
//                        results[i] = item.getUri();
//                    }
//                }
//                if (dataString != null)
//                    results = new Uri[]{Uri.parse(dataString)};
//            }
//        }
//        mUploadCallbackAboveL.onReceiveValue(results);
//        mUploadCallbackAboveL = null;
//        return;
//    }


    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        public void onProgressChanged(WebView view, int newProgress) {
            loadingView.setProgress(newProgress);
        }

        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        public void onReceivedTitle(WebView view, String title) {
            setTitle(title);
        }

        /***************** android中使用WebView来打开本机的文件选择器 *************************/
        // js上传文件的<input type="file" name="fileField" id="fileField" />事件捕获
        // Android > 4.1.1 调用这个方法
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {
            try {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "请选择一个文件"),
                        FILECHOOSER_RESULTCODE);
            } catch (Exception e) {
            }


        }

        // 3.0 + 调用这个方法
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType) {
            try {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(
                        Intent.createChooser(intent, "请选择一个文件"),
                        FILECHOOSER_RESULTCODE);
            } catch (Exception e) {
            }

        }

        // Android < 3.0 调用这个方法
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            try {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(
                        Intent.createChooser(intent, "请选择一个文件"),
                        FILECHOOSER_RESULTCODE);
            } catch (Exception e) {
            }
        }
    };

    //判断当前url是否需要写入登录态相关信息
    private String getAuthKeyType(int flag, String url) {
        String authType = isMaskOn(flag, 0) ? AUTH_TYPE_ALL : AUTH_TYPE_NONE;
        if (authType.equals(AUTH_TYPE_NONE) && !TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            String host = uri.getHost();
            if (JsUtil.isQQdomain(host)) {
                authType = AUTH_TYPE_ALL;
            }
        }
        return authType;
    }

    /**
     * flag中的第position位是否为1
     *
     * @param flag
     * @param position
     * @return
     */
    private boolean isMaskOn(int flag, int position) {
        return (flag >>> position & 0x1) == 1;
    }

    private void initExternalInfo() {
    }

    public void setLayerType(View view) {
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT > 10) {
            Method m;
            try {
                m = view.getClass().getMethod("setLayerType", Integer.TYPE,
                        Paint.class);
                m.invoke(view, 1, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
//        titleView = (CommonTitleView) super.findViewById(R.id.commonTileLayout);
        if (!showTitle) {
//            titleView.setVisibility(View.GONE);
//            titleView.setVisibility(View.GONE);
            showTitle(false);
        } else {
//            titleView.setActivityContext(this);
//            titleView.setVisibility(View.VISIBLE);
            showTitle(true);

            setTitle(title);
        }

        browserContentView = (RelativeLayout) super.findViewById(R.id.browser_content_view);
        webviewContainer = (FrameLayout) super.findViewById(R.id.webview_container);
        webView = new FixedWebView(context);

        if (shouldHardwareAccelerate) {
            setLayerType(webView);
        }
        // 或者android L 也先使用软件加速(android L目前存在bug,会导致webview使用过程中渲染闪退）
        if (!shouldHardwareAccelerate) {
            if (Build.VERSION.SDK_INT > 18) {
                setLayerType(webView);
            }
        }
        webviewContainer.addView(webView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        loadingView = (ProgressBar) super.findViewById(R.id.loading_view);
        jsBridge = new JsBridge(this, webView);
//        initFooterView();
//        initErrorPage();
//        setLayerType(footerView);

        this.webView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                return true;
            }
        });
    }
//
//    private void initErrorPage() {
//        errorPage = (NormalErrorPage) super.findViewById(R.id.error_page_view);
//        errorPage.setButtonClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                webView.setVisibility(View.VISIBLE);
//                errorPage.setVisibility(View.INVISIBLE);
//                webView.reload();
//            }
//        });
//    }

    /**
     * 显示webview还是errorpage
     *
     * @param flag
     */
    @SuppressLint("WrongConstant")
    private void showWebView(boolean flag) {
//        if (errorPage != null) {
//            errorPage.setVisibility(flag ? View.INVISIBLE : View.VISIBLE);
//        }
        if (webView != null) {
            webView.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void setSupportZoom(boolean isEanable) {
        if (webView != null) {
            webView.getSettings().setSupportZoom(isEanable);

            webView.getSettings().setBuiltInZoomControls(false);
            webView.getSettings().setDisplayZoomControls(false);
        }

    }

    @Override
    public void handleUIEvent(Message msg) {

//        if (msg.what == EventDispatcherEnum.REMIND_UI_REFRESH) {
//
//            //这里不自动加载,失败了就失败了,重新再进来一次吧
//
////                reloadPage();
//        }

    }

    private class MyWebViewClient extends WebViewClient {
        @SuppressLint("WrongConstant")
        @Override
        public void onPageFinished(WebView view, String url) {


            ZLog.d(TAG, "onPageFinished " + url);

            loadingView.setVisibility(View.GONE);
//            resetBackAndForwordView();
            setSupportZoom(false);
            if (jsBridge != null) {
                jsBridge.updateLoadedTime();
                jsBridge.doPageLoadFinished();
            }
        }

        @SuppressLint("WrongConstant")
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            initAgentData();
            ZLog.d(TAG, "onPageStarted " + url);

            loadingView.setVisibility(View.VISIBLE);
//            resetBackAndForwordView();
            setSupportZoom(false);
            if (url.startsWith("http") || url.startsWith("https")) {
                //页面加载完成后，做当前页面的安全校验
                if (jsBridge != null) {
                    jsBridge.loadAuthorization(url);
                }
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (TextUtils.isEmpty(url)) {
                return false;
            }

            ZLog.d(TAG, "shouldOverrideUrlLoading " + url);


            if (url.startsWith("http") || url.startsWith("https")) {
                return super.shouldOverrideUrlLoading(view, url);
            } else if (url.startsWith(JsBridge.JS_BRIDGE_SCHEME)) {
                if (jsBridge != null) {
                    jsBridge.invoke(url);
                }
                return true;
            }
            //3.0及以下的webview调用jsb时会调用同时call起的空白页面，将这个页面屏蔽掉不出来
            else if (url.equals("about:blank;") || url.equals("about:blank")) {
                return Build.VERSION.SDK_INT < 11;//Build.VERSION_CODES.HONEYCOMB
            } else {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                //默认有能力去处理每个请求，以后会根据host去匹配来决定是否可以处理，现阶段不去做判断
//                if(IntentUtils.hasAbility(view.getContext(), intent)){
                String scheme = intent.getScheme();

                WebNativeUtlis.shouldOverrideUrlLoading(ZooerBrowserActivity.this, uri);


//                if (scheme != null && scheme.equals(BaseIntentUtils.SCHEME_MAST)) {
////                        Bundle bundle = new Bundle();
////                        //以下增加前端传递当前页面id
////                        int scene = TextUtil.parseIntValue(uri.getQueryParameter("scene"),0);
////                        if (scene != 0) {
////                            bundle.putInt(PARAMS_PRE_ACTIVITY_TAG_NAME, scene);
////                        }else {
////                            bundle.putInt(PARAMS_PRE_ACTIVITY_TAG_NAME, getActivityPageId());
////                        }
////                        IntentUtils.innerForward(context, url, bundle);
//                } else {
////                        intent.putExtra(PARAMS_PRE_ACTIVITY_TAG_NAME, getActivityPageId());
////                        if (!(context instanceof Activity)) {
////                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                        }
////                        context.startActivity(intent);
//                }
                return true;
//                }else{
//                    return false;
//                }
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            ZLog.d(TAG, "onReceivedError " + failingUrl);

//            if (!NetWorkUtil.isNetworkActive()) {
//                //如果是没有网络的情况下
//                showErrorPage(NormalErrorPage.ERROR_TYPE_FAIL);
//            } else {
//                showErrorPage(NormalErrorPage.ERROR_TYPE_FAIL);
//            }
        }
    }

    /**
     * 显示错误页面
     *
     * @param type
     */
    public void showErrorPage(int type) {
        showWebView(false);
//        errorPage.setErrorType(type);

        //这里加个判断，可能H5打开的时候没有显示native的title,这里显示native的title让其可以返回
//        if (titleView.getVisibility() == View.GONE) {
////            titleView.setActivityContext(this);
//            titleView.setVisibility(View.VISIBLE);
//            titleView.setTitle("找不到网页");
//        }
    }

    public WebView getWebView() {
        return webView;
    }

    /**
     * 初始化AgentData，存入一些不常变化的信息，比如deviceInfo、NetworkInfo等
     */
    private void initAgentData() {


    }


    /**
     * footer的按钮监听
     */
//    private WebViewFooter.IWebViewFooterListener footerListener = new WebViewFooter.IWebViewFooterListener() {
//
//        @Override
//        public void onFresh() {
//            showWebView(true);
//            webView.reload();
//        }
//
//        @Override
//        public void onForward() {
//            showWebView(true);
//            webView.goForward();
//        }
//
//        @Override
//        public void onBack() {
//            showWebView(true);
//            webView.goBack();
//        }
//    };

//
//    private void initFooterView() {
//        footerView = (WebViewFooter) super.findViewById(R.id.browser_footer_view);
//        footerView.setWebViewFooterListener(footerListener);
//        footerView.setVisibility(View.GONE);
//
//        resetBackAndForwordView();
//        setFooterShow(shouldShowToolBar);
//    }
    public void setFooterShow(boolean flag) {
//        if(!flag){
//        footerView.setVisibility(View.GONE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) browserContentView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        }
        layoutParams.bottomMargin = 0;
        browserContentView.setLayoutParams(layoutParams);
//        }
//        else{
//
//            footerView.setVisibility(View.VISIBLE);
//
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) browserContentView.getLayoutParams();
//            if (layoutParams == null) {
//                layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
//            }
//            layoutParams.bottomMargin = ViewUtils.dip2px(getApplicationContext(), 50);
//            browserContentView.setLayoutParams(layoutParams);
//        }
    }

    /**
     * 重置回退和前进键
     */
//    private void resetBackAndForwordView() {
//        if (webView != null) {
//            if (webView.canGoBack()) {
//                footerView.setBackEnable(true);
//            } else {
//                footerView.setBackEnable(false);
//            }
//
//            if (webView.canGoForward()) {
//                footerView.setForwardEnable(true);
//            } else {
//                footerView.setForwardEnable(false);
//            }
//        }
//    }
    private void initData(final Intent intent) {
        url = IntentDataUtils.getStringExtra(intent, PARAMS_URL);
        if (TextUtil.isEmpty(url)) { //url都不传直接finish
            finish();
            return;
        }

        showTitle = IntentDataUtils.getBooleanExtra(intent, PARAMS_SHOW_TILE, true);

        title = IntentDataUtils.getStringExtra(intent, PARAMS_TITLE_CONTENT);
        if (TextUtil.isEmpty(title)) {
            title = "浏览器";
        }

        isSwf = url.contains(".swf");

        shouldGoBack = IntentDataUtils.getStringExtra(intent, shouldGoBack);

        String toolbar = IntentDataUtils.getStringExtra(intent, PARAMS_TOOLBAR);
        ZLog.d("toolbar: " + toolbar);
        if ("0".equals(toolbar)) {
            shouldShowToolBar = false;
        } else if ("1".equals(toolbar)) {
            shouldShowToolBar = true;
        } else {
            shouldShowToolBar = true;
        }

        String accelerate = IntentDataUtils.getStringExtra(intent, PARAMS_ACCELERATE);
        ZLog.d("accelerate: " + accelerate);
        if ("0".equals(accelerate)) {
            shouldHardwareAccelerate = false;
        } else if ("1".equals(accelerate)) {
            shouldHardwareAccelerate = true;
        } else {
            shouldHardwareAccelerate = true;
        }

        String zoom = IntentDataUtils.getStringExtra(intent, PARAMS_SUPPORT_ZOOM);
        ZLog.d("zoom: " + zoom);
        if ("0".equals(zoom)) {
            shouldSupportZoom = false;
        } else if ("1".equals(zoom)) {
            shouldSupportZoom = true;
        } else {
            shouldSupportZoom = false;
        }

        isGoBack = IntentDataUtils.getBooleanExtra(intent, PARAMS_IS_GO_BACK, true);


        if (url.startsWith(QXApp.api_host_debug + "/apph5/page/recommend.html?token=")) {
            setRightText("活动规则");
            setOnRightTextClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(ZooerBrowserActivity.this, ZooerBrowserActivity.class);
                    intent1.putExtra(PARAMS_URL, QXApp.api_host_debug + "/apph5/page/rule.html?type=2");
                    startActivity(intent1);
                }
            });
        }

    }

    public interface WebChromeClientListener {

        Activity getActivity();

        public void onProgressChanged(WebView view, int newProgress);

        public void onReceivedTitle(WebView view, String title);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        ZLog.d(TAG, "..  onNewIntent");
    }


    @Override
    protected void onResume() {
        super.onResume();
        ZLog.d(TAG, "..  onResume");
        if (this.jsBridge != null) {
            this.jsBridge.onResume();
        }
        if (isResumeFromPause) {
            isResumeFromPause = false;
            reloadPage();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        isResumeFromPause = true;
        ZLog.d(TAG, "..  onPause");
        if (this.jsBridge != null) {
            this.jsBridge.onPause();
        }
    }

    public void setUploadMsg(ValueCallback<Uri> msg) {
        mUploadMessage = msg;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZLog.d(TAG, "..  onDestroy");
//        ZooerApp.getAppSelf().getEventController().removeUIEventListener(EventDispatcherEnum.REMIND_UI_REFRESH, this);//提示刷新

        if (this.webviewContainer != null) {
            this.webviewContainer.removeView(webView);
            webviewContainer = null;
        }

        if (this.webView != null) {
            this.webView.destroy();
            this.webView = null;
        }

        if (this.jsBridge != null) {
            this.jsBridge.recycle();
            this.jsBridge = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack() && isGoBack) {
                webView.goBack();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (Build.VERSION.SDK_INT >= 21) {
                if (null == mUploadCallbackAboveL) return;
                Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();

                if (mUploadCallbackAboveL != null) {
                    onActivityResultAboveL(requestCode, resultCode, data);
                }

            } else {

                if (null == mUploadMessage) return;
                Uri result = data == null || resultCode != RESULT_OK ? null
                        : data.getData();
                result = zoomBitmap(result);
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
//                        results[i] = item.getUri();
                        results[i] = zoomBitmap(item.getUri());

                    }
                }
                if (dataString != null) {
//                    results = new Uri[]{Uri.parse(dataString)};
                    results = new Uri[]{zoomBitmap(Uri.parse(dataString))};
                }
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
        return;
    }

    //缩放图片
    private Uri zoomBitmap(Uri uri) {
        if (uri == null) return null;
        String path = CameraAndPhotoUtils.getPath(this, uri);
        Bitmap bitmap = CameraAndPhotoUtils.zoomBitmap(Uri.parse(path), 200, 200);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bs);

        String thumPath = FileUtils.getThumbDir() + "/" + System.currentTimeMillis();

        boolean isSuccess = FileUtils.write2File(bs.toByteArray(), thumPath);

        if (isSuccess) {
            return Uri.fromFile(new File(thumPath));
        } else {
            return uri;
        }
    }

    public void reloadPage() {
        showWebView(true);
        if (webView != null) {
            webView.reload();
        }
    }

}
