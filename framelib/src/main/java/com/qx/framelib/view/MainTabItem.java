package com.qx.framelib.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qixiang.framelib.R;


/**
 * Created by ZhaoWei on 2016/4/20.
 */
public class MainTabItem extends LinearLayout {

    private ImageView tab_ico;

    public ImageView getTab_ico() {
        return tab_ico;
    }

    public TextView getTab_txt() {
        return tab_txt;
    }

    private TextView tab_txt;

    public MainTabItem(Context context) {
        this(context, null);
    }

    public MainTabItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainTabItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_main_bottom_tab, this);
        setGravity(Gravity.CENTER);
        tab_ico = (ImageView) super.findViewById(R.id.tab_ico);
        tab_txt = (TextView) super.findViewById(R.id.tab_txt);
    }


    public void setIcon(int resId) {
        tab_ico.setImageResource(resId);
    }

    public void setText(String text) {
        tab_txt.setText(text);
    }

    public void setTextColor(int color) {
        tab_txt.setTextColor(color);
    }

    public void setTextColor(ColorStateList color) {
        tab_txt.setTextColor(color);
    }

    public void setTextSize(int size) {
        tab_txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
    }

    public void setIcoSize(int width, int height) {
        LayoutParams params = (LayoutParams) tab_ico.getLayoutParams();
        params.width = width;
        params.height = height;
        tab_ico.setLayoutParams(params);
    }

}
