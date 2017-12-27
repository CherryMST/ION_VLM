package com.qx.framelib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qixiang.framelib.R;
import com.qx.framelib.utlis.ViewUtils;


//import com.zhy.autolayout.utils.AutoUtils;


/**
 * Created by ZhaoWei on 2016/4/19.
 */
public class ErrorPageView extends RelativeLayout {

    public static final int ERROR_NETWORK = 1;
    public static final int ERROR_DATA = 2;

    private Context context;
    private ImageView error_ico;
    private TextView error_txt;

    public ErrorPageView(Context context) {
        this(context, null);
    }

    public ErrorPageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ErrorPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        inflate(context, R.layout.layout_error, this);

        error_ico = (ImageView) findViewById(R.id.error_ico);
        error_txt = (TextView) findViewById(R.id.error_txt);
    }

    public void setErrorIco(int redId) {
        error_ico.setImageResource(redId);
    }

    public void setErrorText(String txt) {
        error_txt.setText(txt);
    }

    public void setErrorTextColor(int color) {
        error_txt.setTextColor(color);
    }

    /**
     * @param size 单位 sp
     */
    public void setErrorTextSize(float size) {
        error_txt.setTextSize(size);
    }

    public void setErrorIconParams(int width, int height) {
        LayoutParams params = (LayoutParams) error_ico.getLayoutParams();
        params.width = width;
        params.height = height;
        error_ico.setLayoutParams(params);
//        AutoUtils.auto(this);
    }

    /**
     * 没有网络
     */
    public void NetWorkError() {
        setErrorIco(R.drawable.icon_wifi_error);
        setErrorText("亲，网络开小差中...");
        setErrorIconParams(ViewUtils.dip2px(context, 64), ViewUtils.dip2px(context, 64));
        setErrorTextSize(14);
    }
}
