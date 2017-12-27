package com.qx.framelib.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

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

/**
 * Created by LiShang on 2017/4/25.
 */

public class BaseToolBarActivity extends AppCompatActivity implements UIEventListener {

    public    Activity       mActivity;
    private   LoadingDialog  mLoading;
    protected ErrorPageUtils mErrorUtils;
    private boolean isStatusBar = true;

    private ViewGroup rootView;
    private Toolbar toolbar;
    protected boolean isCheckNetWork = true; //是否检查网络异常


    public void setCheckNetWork(boolean checkNetWork) {
        isCheckNetWork = checkNetWork;
    }


    private BroadcastReceiver mExitAppReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onAppExit(intent);
        }
    };


    public void setStatusBarEnald(boolean isEnald) {
        this.isStatusBar = isEnald;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//        //不显示系统的标题栏
//        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN );

//        FullScreenUtils.assistActivity(this);

        mActivity = this;

        //所有页面都注册退出广播
        IntentFilter filter = new IntentFilter(ZooerApp.INTENT_ACTION_EXIT_APP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mExitAppReceiver, filter);

        ZooerApp.setCurrentActivity(this);

        initNetWorkListener();

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
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(mActivity.getLayoutInflater().inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {

        if (view == null) {
            try {
                throw new NullPointerException("view is null");
            } catch (Exception e) {
                return;
            }
        }

        rootView = (ViewGroup) mActivity.getLayoutInflater().inflate(R.layout.layout_toobar_main, null);
        ViewGroup contentView = (ViewGroup) rootView.findViewById(R.id.layout_main_content);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        contentView.addView(view, params);
        super.setContentView(rootView);

        if (isStatusBar) {
            LollipopUtils.setStatusbarHeight(mActivity, toolbar);
            LollipopUtils.setStatusbarColor(mActivity, 0x33000000);
        }

        setOnLeftBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    @SuppressLint("WrongConstant")
    public boolean isShowTitle() {
        return toolbar.getVisibility() == View.VISIBLE;
    }

    @SuppressLint("WrongConstant")
    public void showTitle(boolean isShow) {
        if (!isShow) {
            toolbar.setVisibility(View.GONE);
        } else {
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
//        super.setTitle(title);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textView.setText(title);
    }

    @Override
    public void setTitleColor(int textColor) {
//        super.setTitleColor(textColor);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textView.setTextColor(textColor);
    }

    @SuppressLint("WrongConstant")
    public void showLeftBtn(boolean isShow) {
        if (isShow) {
            toolbar.findViewById(R.id.toolbar_left_btn).setVisibility(View.VISIBLE);
        } else {
            toolbar.findViewById(R.id.toolbar_left_btn).setVisibility(View.GONE);
        }
    }

    public void showRightBtn(boolean isShow) {
        if (isShow) {
            toolbar.findViewById(R.id.toolbar_right_btn).setVisibility(View.VISIBLE);
        } else {
            toolbar.findViewById(R.id.toolbar_right_btn).setVisibility(View.GONE);
        }
    }

    public void setLeftBtnRes(int leftBtnRes) {
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.toolbar_left_btn);
        imageView.setImageResource(leftBtnRes);
    }

    public void setOnLeftBtnClickListener(View.OnClickListener leftBtnClickListener) {
        toolbar.findViewById(R.id.toolbar_left_btn).setOnClickListener(leftBtnClickListener);
    }

    public void setTitleBackgroundColor(int color) {
        toolbar.setBackgroundColor(color);
    }

    public void setRightText(String text) {
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_right_txt);
        textView.setPadding(ViewUtils.dip2px(this, 10), ViewUtils.dip2px(this, 5), ViewUtils.dip2px(this, 10), ViewUtils.dip2px(this, 5));
        textView.setText(text);
    }

    public void setRightTextColor(int color) {
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_right_txt);
        textView.setTextColor(color);
    }

    public void setRightTextBackground(int rightBtnRes) {
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_right_txt);
        textView.setBackgroundResource(rightBtnRes);
    }

    public void setRightBtnRes(int rightBtnRes) {
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.toolbar_right_btn);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(rightBtnRes);
    }

    public void setOnRightTextClickListener(View.OnClickListener leftBtnClickListener) {
        toolbar.findViewById(R.id.toolbar_right_txt).setOnClickListener(leftBtnClickListener);
    }


    public void setOnRightBtnClickListener(View.OnClickListener rightBtnClickListener) {
        toolbar.findViewById(R.id.toolbar_right_btn).setOnClickListener(rightBtnClickListener);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        ZLog.d("onResume");
        ZooerApp.setCurrentActivity(this);
        if (!NetWorkUtil.isNetworkActive()) {
            closeNetWork();
        }

//        PgyFeedbackShakeManager.register(this,false);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        PgyFeedbackShakeManager.unregister();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mExitAppReceiver);
        if (ZooerApp.getCurrentActivity() == this) {
            ZooerApp.setCurrentActivity(null);
        }
        dismissDialogLoading();
        removeNetWorkListener();

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

//    public void setDialog(Dialog dialog) {
//        this.mDialog = dialog;
//    }


    public void showDialogLoading() {
        showDialogLoading(null);
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
        showError(mViewStub, error, resId, 64, 64);
    }

    public void showError(ViewStub mViewStub, String error, int resId, int width, int height) {
        if (mErrorUtils == null) {
            mErrorUtils = new ErrorPageUtils();
        }
        if (!mErrorUtils.isShowing()) {
            mErrorUtils.init(this, mViewStub).
                    setErrorText(error).
                    setErrorIco(resId).
                    setErrorIconParams(ViewUtils.dip2px(this, width), ViewUtils.dip2px(this, height)).
                    setBackgroudColor(Color.parseColor("#f1f1f1"))
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
