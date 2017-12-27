package com.qx.framelib.utlis;

import android.app.Activity;
import android.view.View;
import android.view.ViewStub;

import com.qixiang.framelib.R;
import com.qx.framelib.view.ErrorPageView;


/**
 * Created by ZhaoWei on 2016/7/29.
 * 异常页面
 */
public class ErrorPageUtils {

    private ErrorPageView errorView;

    public ErrorPageUtils init(Activity activity, ViewStub stub) {
        if (errorView == null) {
            stub.inflate();
            errorView = (ErrorPageView) activity.findViewById(R.id.framlib_error_page);
        }
        return this;
    }

    public ErrorPageUtils init(View rootView, ViewStub stub) {
        if (errorView == null) {
            if (stub != null) {
                stub.inflate();
                errorView = (ErrorPageView) rootView.findViewById(R.id.framlib_error_page);
            }

        }
        return this;
    }

    public ErrorPageUtils show() {
        if (errorView != null)
            errorView.setVisibility(View.VISIBLE);
        return this;
    }

    public ErrorPageUtils hidden() {
        if (errorView != null)
            errorView.setVisibility(View.GONE);
        return this;
    }

    public boolean isShowing() {
        if (errorView == null) return false;
        return errorView.getVisibility() == View.VISIBLE;
    }

    public ErrorPageUtils setErrorIco(int redId) {
        if (errorView != null)
            errorView.setErrorIco(redId);
        return this;
    }

    public ErrorPageUtils setErrorText(String txt) {
        if (errorView != null)
            errorView.setErrorText(txt);
        return this;
    }

    public ErrorPageUtils setErrorTextColor(int color) {
        if (errorView != null)
            errorView.setErrorTextColor(color);
        return this;
    }

    /**
     * @param size 单位 sp
     */
    public ErrorPageUtils setErrorTextSize(float size) {
        if (errorView != null)
            errorView.setErrorTextSize(size);
        return this;
    }

    public ErrorPageUtils setErrorIconParams(int width, int height) {
        if (errorView != null)
            errorView.setErrorIconParams(width, height);
        return this;
    }

    public ErrorPageUtils setBackgroudColor(int color) {
        if (errorView != null)
            errorView.setBackgroundColor(color);
        return this;
    }

    public ErrorPageUtils NetWorkError() {
        if (errorView != null)
            errorView.NetWorkError();
        return this;
    }

}
