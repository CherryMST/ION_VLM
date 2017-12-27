package com.qx.framelib.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.widget.RelativeLayout;

import com.qixiang.framelib.R;
import com.qx.framelib.application.ZooerApp;
import com.qx.framelib.dialog.LoadingDialog;
import com.qx.framelib.event.EventDispatcherEnum;
import com.qx.framelib.event.listener.UIEventListener;
import com.qx.framelib.net.NetWorkUtil;
import com.qx.framelib.utlis.ErrorPageUtils;
import com.qx.framelib.utlis.ViewUtils;
import com.qx.framelib.utlis.ZLog;

import java.lang.reflect.Field;


/**
 * 基类Fragment ，模板类 子类需继承。。。
 * Created by luohongbo on 16/1/2.
 */
public abstract class BaseFragment extends Fragment implements UIEventListener {

    protected Context mContext;
    protected LayoutInflater layoutInflater;
    private View createdView = null;
    private ViewStub mViewStub;
    private ErrorPageUtils mErrorUtils;
    private boolean hasAlreadyOncreate = false;

    protected boolean isInited = false; //fragment是否显示过

    private LoadingDialog mLoading;

    protected RelativeLayout rootView; //根布局
    protected View contentView; // 内容

    protected boolean isCheckNetWork = true; //是否检查网络异常
    private View mErrorView;


    public void setCheckNetWork(boolean checkNetWork) {
        isCheckNetWork = checkNetWork;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasAlreadyOncreate = true;
        ZLog.d(this.getClass().getSimpleName(), "onCreate");
        mContext = getActivity();
        if (mContext == null) {
            mContext = ZooerApp.getAppSelf();
        }
        layoutInflater = LayoutInflater.from(this.mContext);

        //初始化根布局
        rootView = new RelativeLayout(mContext);
        setContentView(rootView);


        //如果 onPageResume 先执行了 就直接将view添加进入
        if (isInited) {
            startCreateView();
        }

    }

    /**
     * 创建 布局
     */
    private void startCreateView() {

        initView();

        //初始化 控件
        startLoadView();
        //网络监听
        initNetWorkListener();
        //初始化数据
        refreshData(false);
    }

    private void initView() {
        rootView.removeAllViews();

        contentView = createContentView();
        rootView.addView(contentView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

    }


    public boolean isAlreadyOncreate() {
        return hasAlreadyOncreate;
    }

    /**
     * 设置页面的布局资源id
     *
     * @param resId
     */
    public void setContentView(int resId) {
        createdView = layoutInflater.inflate(resId, null, false);//这里root就无法传了，因为这个不inflate，后续find方法则无法工作
    }

    @Override
    public View getView() {
        View view = super.getView();
        if (view == null) {
            ZLog.d(this.getClass().getSimpleName(), "get View return null.");
            view = createdView;
        }
        return view;
    }

    /**
     * 通过资源id获得页面中的组件
     *
     * @param resId
     * @return
     */
    public View findViewById(int resId) {
        if (createdView != null) {
            return createdView.findViewById(resId);
        }
        return null;
    }

    public void setContentView(View view) {
        this.createdView = view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ZLog.d(this.getClass().getSimpleName(), "onCreateView");
        ViewParent viewParent = createdView.getParent();
        if (viewParent != null && viewParent instanceof ViewGroup) {
            ((ViewGroup) viewParent).removeView(createdView);
        }
//        createdView = createContentView();
        return createdView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ZLog.d(this.getClass().getSimpleName(), "onViewCreated");
//        ViewUtils.inject(this, createdView);
//        isViewCreated = true;
    }


    @Override
    public void onStart() {
        super.onStart();
        ZLog.d(this.getClass().getSimpleName(), "onStart");
//        MobclickAgent.onPageStart(this.getClass().getName()); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onResume() {
        super.onResume();
        ZLog.d(this.getClass().getSimpleName(), "onResume");
//        MobclickAgent.onPageEnd(this.getClass().getName());
    }

    /**
     * Fragment需要加载的view,注意这里只会调用一次，初始化的时候调用
     *
     * @return
     */
    public abstract View createContentView();

    /**
     * 当前页面被切换走的时候调用
     */
    public abstract void onPageTurnBackground();

    /**
     * viewpage页面切换中
     */
    public abstract void onPageScrolling(float positionOffset, int positionOffsetPixels);

    /**
     * 加载布局 初始化数据
     */
    public abstract void startLoadView();


    /**
     * 刷新数据 tab切换时 如果实时刷新
     *
     * @param isAutoRefresh true表示滑动或者切换tab的refreshData ，false表示人为调用的refreshData
     */
    public abstract void refreshData(boolean isAutoRefresh);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        //该方法在onCreateView之前被调用
        ZLog.d(this.getClass().getSimpleName(), "setUserVisibleHint isVisibleToUser " + isVisibleToUser);
        if (isVisibleToUser) {
            onPageResume(true);
        }
    }

    /**
     * @param isSnapResume true表示滑动或者切换tab的onPageResume ，false表示activity切换导致的onPageResume
     */
    public void onPageResume(boolean isSnapResume) {
        if (!isInited) {
            isInited = true;
            //如果 onCreate 先执行了 就直接将view添加进入
            if (isAlreadyOncreate()) {
                startCreateView();
            }
        } else {
            refreshData(true);
        }
    }

    /**
     * fragment 嵌套fragment 解决 childFragmentManager： java.lang.IllegalStateException: No activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void startActivity(Class cls) {
        startActivity(cls, null);
    }


    public void startActivity(Class cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }


    public void startActivityForResult(Class cls, int requestCode) {
        startActivityForResult(cls, requestCode, null);
    }

    public void startActivityForResult(Class cls, int requestCode, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    public void showLoading() {
        showLoading(null);
    }

    public void showLoading(String text) {

        if (mLoading == null) {
            if (mContext == null) {
                mContext = ZooerApp.getAppSelf();
            }
            mLoading = new LoadingDialog(mContext);
        }
        if (mLoading.isShowing()) {
            dismissLoading();
        }
        mLoading.show(text);
    }

    public void dismissLoading() {
        if (mLoading != null) {
            mLoading.dismiss();
        }
    }

    public boolean isShowing() {
        if (mLoading == null) return false;
        return mLoading.isShowing();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeNetWorkListener();
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

        if (mViewStub == null) {
            mErrorView = LayoutInflater.from(mContext).inflate(R.layout.layout_error_stub, null);
            mViewStub = (ViewStub) mErrorView.findViewById(R.id.framlib_viewStub);
            rootView.addView(mErrorView);
        }

        if (!mErrorUtils.isShowing()) {
            mErrorUtils.init(rootView, mViewStub).
                    setErrorText(error).
                    setErrorIco(resId).
                    setErrorIconParams(ViewUtils.dip2px(mContext, 64), ViewUtils.dip2px(mContext, 64))
//                    setBackgroudColor(Color.parseColor("#f5f4f0"))
                    .show();
        } else {
            mErrorUtils.setErrorText(error).
                    setErrorIco(resId).
                    show();
        }
    }

    public void showError(String error, int resId) {
        showError(mViewStub, error, resId);
    }

    public void hideErrorView(int visible) {
        if (null != mErrorView) {
            mErrorView.setVisibility(visible);
        }
    }

    public void hiddenError() {
        if (mErrorUtils == null) return;
        if (mErrorUtils.isShowing()) {
            mErrorUtils.hidden();
        }
    }

}
