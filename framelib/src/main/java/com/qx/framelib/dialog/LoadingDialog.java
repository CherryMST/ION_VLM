package com.qx.framelib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import com.qixiang.framelib.R;
import com.qx.framelib.view.LoadingView;



/**
 * Created by ZhaoWei on 2016/6/30.
 * 等待dialog
 */
public class LoadingDialog extends Dialog {

    private LoadingView loading;

    public LoadingDialog(Context context) {
        this(context, R.style.theme_dialog_alert);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.layout_loading);
        setCanceledOnTouchOutside(false);
        loading = (LoadingView) findViewById(R.id.loading);
    }


    public void show(String text) {
        if (!TextUtils.isEmpty(text))
            loading.setText(text);
        super.show();
    }

    public void setLoading(String text) {
        if (!TextUtils.isEmpty(text))
            loading.setText(text);
    }

}
