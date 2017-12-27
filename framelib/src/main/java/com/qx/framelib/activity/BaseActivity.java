package com.qx.framelib.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;

import com.qixiang.framelib.R;
import com.qx.framelib.application.ZooerApp;
import com.qx.framelib.dialog.LoadingDialog;
import com.qx.framelib.event.EventDispatcherEnum;
import com.qx.framelib.event.listener.UIEventListener;
import com.qx.framelib.net.NetWorkUtil;
import com.qx.framelib.utlis.ErrorPageUtils;
import com.qx.framelib.utlis.LollipopUtils;
import com.qx.framelib.utlis.TextUtil;
import com.qx.framelib.utlis.ViewUtils;
import com.qx.framelib.utlis.ZLog;
//import com.umeng.analytics.MobclickAgent;


/**
 * Created by ZhaoWei on 2016/6/27.LoadingDialog
 */
public class BaseActivity extends AppCompatActivity implements UIEventListener {

    public  Activity      mActivity;
    public  Dialog        mDialog;
    private LoadingDialog mLoading;
    protected boolean isStatusBarEnald = false; //是否启用沉浸式 false:不启用 true 启用
    protected ErrorPageUtils mErrorUtils;

    private BroadcastReceiver mExitAppReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onAppExit(intent);
        }
    };

    public void setStatusBarEnald(boolean statusBarEnald) {
        isStatusBarEnald = statusBarEnald;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        mActivity = this;
        this.setStatusBarEnald(true);
        //所有页面都注册退出广播
        IntentFilter filter = new IntentFilter(ZooerApp.INTENT_ACTION_EXIT_APP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mExitAppReceiver, filter);

        ZooerApp.setCurrentActivity(this);

//        initNetWorkListener();


    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(mActivity.getLayoutInflater().inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        if (isStatusBarEnald){
            LollipopUtils.setStatusbarColor(mActivity, getResources().getColor(R.color.color_nokia_blue));
        }

//        //引入：注解方法二
//        ViewUtils.inject(this, view);

    }

    /**
     * 注册网络监听变化
     */
    private void initNetWorkListener() {
        ZooerApp.getAppSelf().getEventController().addUIEventListener(EventDispatcherEnum.UI_EVENT_NETWORK_CONNECT, this);
        ZooerApp.getAppSelf().getEventController().addUIEventListener(EventDispatcherEnum.UI_EVENT_NETWORK_DISCONNECT, this);
    }

    /**
     * 移除网络监听变化
     */
    private void removeNetWorkListener() {
        ZooerApp.getAppSelf().getEventController().removeUIEventListener(EventDispatcherEnum.UI_EVENT_NETWORK_CONNECT, this);
        ZooerApp.getAppSelf().getEventController().removeUIEventListener(EventDispatcherEnum.UI_EVENT_NETWORK_DISCONNECT, this);
    }


    protected void onAppExit(Intent intent) {
        String this_activity = this.getClass().getName();
        String intent_activity = intent.getStringExtra("activity");
        ZLog.d("activity", "this_activity:" + this_activity + " intent_activity: " + intent_activity);
        if (!isFinishing() && !TextUtil.isEmpty(intent_activity) && !this_activity.equals(intent_activity)) {
            finish();
        }
    }

    public void startActivity(Class cls) {
        startActivity(cls, null);
    }


    public void startActivity(Class cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    public void startActivityForResult(Class cls, int requestCode) {
        startActivityForResult(cls, requestCode, null);
    }

    public void startActivityForResult(Class cls, int requestCode, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        ZooerApp.setCurrentActivity(this);
//        MobclickAgent.onPageStart(this.getClass().getName()); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
//        MobclickAgent.onResume(this); //统计session 时长
        if (!NetWorkUtil.isNetworkActive()) {
            closeNetWork();
        }

        // 自定义摇一摇的灵敏度，默认为950，数值越小灵敏度越高。
//        PgyFeedbackShakeManager.setShakingThreshold(1000);

        // 以对话框的形式弹出
        //PgyFeedbackShakeManager.register(BaseActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(this.getClass().getName()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
//        MobclickAgent.onPause(this);
        //PgyFeedbackShakeManager.unregister();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mExitAppReceiver);
        if (ZooerApp.getCurrentActivity() == this) {
            ZooerApp.setCurrentActivity(null);
        }
        dismissDialogLoading();
//        removeNetWorkListener();
        //解除自动蒲公英更新注册   注册代码在initview中
        super.onDestroy();
    }


    @Override
    public void handleUIEvent(Message msg) {
        switch (msg.what) {
            case EventDispatcherEnum.UI_EVENT_NETWORK_CONNECT: //网络连接成功
                openNetWork();
                break;
            case EventDispatcherEnum.UI_EVENT_NETWORK_DISCONNECT: //网络断开
                closeNetWork();
                break;
        }
    }

    public void setDialog(Dialog dialog) {
        this.mDialog = dialog;
    }


    public void showDialogLoading() {
        showDialogLoading("正在加载");
    }

    public void showDialogLoading(String text) {
        if (mLoading == null) {
            mLoading = new LoadingDialog(this);
        }
        if (mLoading.isShowing()) {
            dismissDialogLoading();
        }
        mLoading.show(text);
    }

    public void setLoadingText(String text) {
        if (mLoading == null) {
            mLoading = new LoadingDialog(this);
        }
        if (mLoading.isShowing()) {
            mLoading.setLoading(text);
        } else
            mLoading.show(text);
    }

    public void dismissDialogLoading() {
        if (mLoading != null) {
            mLoading.dismiss();
        }
    }

    public boolean isShowLoading() {
        if (mLoading != null) {
            return mLoading.isShowing();
        } else {
            return false;
        }

    }

    public void openNetWork() {

    }

    public void closeNetWork() {
    }


    public boolean isNetworkActive() {
        return NetWorkUtil.isNetworkActive();
    }


    public void showError(ViewStub mViewStub, String error, int resId) {
        if (mErrorUtils == null) {
            mErrorUtils = new ErrorPageUtils();
        }
        if (!mErrorUtils.isShowing()) {
            mErrorUtils.init(this, mViewStub).
                    setErrorText(error).
                    setErrorIco(resId).
                    setErrorIconParams(ViewUtils.dip2px(this, 64), ViewUtils.dip2px(this, 64)).
                    setBackgroudColor(Color.parseColor("#ffffff"))//#f5f4f0
                    .show();
        } else {
            mErrorUtils.setErrorText(error).
                    setErrorIco(resId).
                    show();
        }
    }

    public void hiddenError() {
        if (mErrorUtils == null) return;
        if (mErrorUtils.isShowing()) {
            mErrorUtils.hidden();
        }
    }



}
