package com.nokia.vlm.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.nokia.vlm.ui.activity.ZooerBrowserActivity;

import java.lang.reflect.Method;

/**
 * 支持的最低版本Api 8+，适用于Android sdk>=14，解决flash在4.0+的系统上无法全屏的问题
 */
public class FullscreenableChromeClient extends WebChromeClient {

    protected ZooerBrowserActivity.WebChromeClientListener mListener = null;
    protected Activity mActivity = null;

    private View mCustomView;
    private CustomViewCallback mCustomViewCallback;
    private int mOriginalOrientation;

    private FrameLayout mContentView;
    private FrameLayout mFullscreenContainer;
    private String url;

    private static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
            ViewGroup.LayoutParams.FILL_PARENT);

    public FullscreenableChromeClient(ZooerBrowserActivity.WebChromeClientListener listener)
    {
        mListener = listener;
        this.mActivity = listener.getActivity();
    }

    public void onProgressChanged(WebView view, int newProgress)
    {
        mListener.onProgressChanged(view, newProgress);
    }

    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, false);
    }

    public void onReceivedTitle(WebView view, String title){
        if (view != null) {
            url = view.getUrl();
        }
        Log.d("zooer","onReceivedTitle webview url: "+ url);
        mListener.onReceivedTitle(view, title);

        if (view != null) {
            try {
                Class<?> clazz = view.getClass();
                Method method = clazz.getMethod("removeJavascriptInterface", String.class);
                if (method != null)
                    method.invoke(view, "searchBoxJavaBridge_");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        if (Build.VERSION.SDK_INT >= 14) {
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            mOriginalOrientation = mActivity.getRequestedOrientation();
            FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
            mFullscreenContainer = new FullscreenHolder(mActivity);
            mFullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
            decor.addView(mFullscreenContainer, COVER_SCREEN_PARAMS);
            mCustomView = view;
            setFullscreen(true);
            mCustomViewCallback = callback;
            mActivity.setRequestedOrientation(requestedOrientation);
        }

    }

    public void onHideCustomView() {
        if (mCustomView == null) {
            return;
        }

        setFullscreen(false);
        FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
        decor.removeView(mFullscreenContainer);
        mFullscreenContainer = null;
        mCustomView = null;
        mCustomViewCallback.onCustomViewHidden();
        mActivity.setRequestedOrientation(mOriginalOrientation);
    }

    private void setFullscreen(boolean enabled) {
        Window win = mActivity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        if (enabled) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
            if (mCustomView != null) {
                setSystemUiVisibility(mCustomView);
            } else {
                setSystemUiVisibility(mContentView);

            }
        }
        win.setAttributes(winParams);
    }
    // js上传文件的<input type="file" name="fileField" id="fileField" />事件捕获
    // Android > 4.1.1 调用这个方法
    public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                String acceptType, String capture) {

        Log.d("zooer","openFileChooser@@@ > 4.1.1");
        try {
            if (mActivity instanceof ZooerBrowserActivity) {
                ((ZooerBrowserActivity) mActivity).setUploadMsg(uploadMsg);
            }
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            mActivity.startActivityForResult(
                    Intent.createChooser(intent, "请选择一个文件"),
                    ZooerBrowserActivity.FILECHOOSER_RESULTCODE);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // 3.0 + 调用这个方法
    public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                String acceptType) {

        Log.d("zooer","openFileChooser@@@ >3.0+");
        try {
            if (mActivity instanceof ZooerBrowserActivity) {
                ((ZooerBrowserActivity) mActivity).setUploadMsg(uploadMsg);
            }
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            mActivity.startActivityForResult(
                    Intent.createChooser(intent, "请选择一个文件"),
                    ZooerBrowserActivity.FILECHOOSER_RESULTCODE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Android < 3.0 调用这个方法
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        Log.d("zooer","openFileChooser@@@ < 3.0");
        try {
            if (mActivity instanceof ZooerBrowserActivity) {
                ((ZooerBrowserActivity) mActivity).setUploadMsg(uploadMsg);
            }
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            mActivity.startActivityForResult(
                    Intent.createChooser(intent, "请选择一个文件"),
                    ZooerBrowserActivity.FILECHOOSER_RESULTCODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /************** end ***************/
    private static class FullscreenHolder extends FrameLayout {
        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setSystemUiVisibility(View view){
        if(Build.VERSION.SDK_INT >= 14){
            try {
                Class<?> clazz = view.getClass();
                Method method = clazz.getMethod("setSystemUiVisibility", int.class);
                method.invoke(view, 0x00000000);
            } catch (Exception e) {
            }
        }
    }
}
