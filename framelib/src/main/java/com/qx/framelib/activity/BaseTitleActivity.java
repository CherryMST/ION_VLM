package com.qx.framelib.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.qixiang.framelib.R;
import com.qx.framelib.utlis.LollipopUtils;
import com.qx.framelib.view.TitleBar;


/**
 * Created by ZhaoWei on 2016/6/27.
 */
public class BaseTitleActivity extends BaseActivity {

    private ViewGroup rootView;
    protected TitleBar titleView;
    private ViewStub mViewStub;
    protected boolean isCheckNetWork = true; //是否检查网络异常

    public void setCheckNetWork(boolean checkNetWork) {
        isCheckNetWork = checkNetWork;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        rootView = (ViewGroup) mActivity.getLayoutInflater().inflate(R.layout.layout_main, null);

        titleView = (TitleBar) rootView.findViewById(R.id.titlebar);
        mViewStub = (ViewStub) rootView.findViewById(R.id.framlib_viewStub);

        ViewGroup contentView = (ViewGroup) rootView.findViewById(R.id.layout_main_content);

        contentView.addView(view, params);

        super.setContentView(rootView);
        if (isStatusBarEnald)
            LollipopUtils.setStatusbarHeight(mActivity, titleView);
        initTitleBack();
    }

    private void initTitleBack() {
        titleView.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        titleView.setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        titleView.setTitle(titleId);
    }

    @Override
    public void setTitleColor(int textColor) {
        super.setTitleColor(textColor);
        titleView.setTitleColor(textColor);
    }

    public void setLeftOnClickListener(View.OnClickListener onClickListener) {
        titleView.setLeftClickListener(onClickListener);
    }

    public void setRightOnClickListener(View.OnClickListener onClickListener) {
        titleView.setRightClickListener(onClickListener);
    }

    public void setLeftBackgroudResource(int res) {
        titleView.setLeftBackgroudResource(res);
    }

    public void setLeftText(String res) {
        titleView.setLeftText(res);
    }

    public void setLeftDrawble(Drawable drawble, TitleBar.DrawableIndex index) {

        titleView.setLeftDrawble(drawble, index);
    }


    public void setRightBackgroudResource(int res) {
        titleView.setRightBackgroudResource(res);
    }

    public void setRightText(String res) {
        titleView.setRightText(res);
    }

    public void setRightDrawble(Drawable drawble, TitleBar.DrawableIndex index) {

        titleView.setRightDrawble(drawble, index);
    }

    public void setLeftVisibility(int visibility) {
        titleView.setLeftVisibility(visibility);
    }

    public void setRightVisibility(int visibility) {
        titleView.setRightVisibility(visibility);
    }

    public void setbackgroudColor(int color) {
        titleView.setBackgroundColor(color);
    }

    public void setRightTextColor(int color) {
        titleView.setRightTextColor(color);
    }

    public void setLeftTextColor(int color) {
        titleView.setLeftTextColor(color);
    }

    public void setTitleViewVisible(int visibility) {
        titleView.setVisibility(visibility);
    }


//    public void showLoading() {
//
//        ImageView loading_icon = (ImageView) rootView.findViewById(R.id.loading_icon);
//        AnimationDrawable animationDrawable = (AnimationDrawable) loading_icon.getDrawable();
//        animationDrawable.start();
//
//        rootView.findViewById(R.id.ll_loading_view).setVisibility(View.VISIBLE);
//        rootView.findViewById(R.id.layout_main_content).setVisibility(View.GONE);
//
//    }

//    @Override
//    public void showLoading(String text) {
//        rootView.findViewById(R.id.ll_loading_view).setVisibility(View.VISIBLE);
//        rootView.findViewById(R.id.layout_main_content).setVisibility(View.GONE);
//        ImageView loading_icon = (ImageView) rootView.findViewById(R.id.loading_icon);
//        TextView loading_txt = (TextView) rootView.findViewById(R.id.loading_txt);
//        loading_txt.setText(text);
//        AnimationDrawable animationDrawable = (AnimationDrawable) loading_icon.getDrawable();
//        animationDrawable.start();
//    }

//    public void dismissLoading() {
//        rootView.findViewById(R.id.ll_loading_view).setVisibility(View.GONE);
//        rootView.findViewById(R.id.layout_main_content).setVisibility(View.VISIBLE);
//        ImageView loading_icon = (ImageView) rootView.findViewById(R.id.loading_icon);
//        AnimationDrawable animationDrawable = (AnimationDrawable) loading_icon.getDrawable();
//        animationDrawable.stop();
//    }


    public void showError(String error, int resId) {
        super.showError(mViewStub, error, resId);
    }

    @Override
    public void openNetWork() {
        super.openNetWork();
        if (isCheckNetWork)
            hiddenError();
    }

    @Override
    public void closeNetWork() {
        super.closeNetWork();
        if (isCheckNetWork)
            showError("亲,网络开小差中...", R.drawable.icon_wifi_error);
    }
}
