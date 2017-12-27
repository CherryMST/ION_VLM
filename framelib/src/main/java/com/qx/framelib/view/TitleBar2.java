package com.qx.framelib.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qixiang.framelib.R;


/**
 * Created by ZhaoWei on 2016/6/27.
 * 标题栏
 */
public class TitleBar2 extends RelativeLayout {

    private Button left;
    private TextView title;
    private Button right;

    public enum DrawableIndex {
        LEFT, TOP, RIGHT, BOTTOM, Gravity
    }


    public TitleBar2(Context context) {
        this(context, null);
    }

    public TitleBar2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_title_bar2, this);

        left = (Button) findViewById(R.id.left);
        title = (TextView) findViewById(R.id.title);
        right = (Button) findViewById(R.id.right);


        Drawable drawable = getResources().getDrawable(R.drawable.back_white);
        drawable.setBounds(0, 0, 21, 38);

        setLeftDrawble(drawable, TitleBar2.DrawableIndex.LEFT);

    }

    public void setTitle(CharSequence str) {
        title.setText(str);
    }

    public void setTitleClickable(boolean clickable){
        title.setClickable(clickable);
    }

    public void setTitle(int res) {
        title.setText(res);
    }

    public void setTitleVisibility(int visibility){
        title.setVisibility(visibility);
    }

    public void setTitleBackgroundResource(int res) {
        title.setVisibility(VISIBLE);
        title.setBackgroundResource(res);
        LinearLayout.LayoutParams lp;
        lp= (LinearLayout.LayoutParams) title.getLayoutParams();
        lp.width=getResources().getDrawable(res).getMinimumWidth();
        lp.height=getResources().getDrawable(res).getMinimumHeight();
        title.setLayoutParams(lp);
    }
    public void setTitleBackgroundResource(int res,int height) {
        title.setVisibility(VISIBLE);
        title.setBackgroundResource(res);
        LinearLayout.LayoutParams lp;
        lp= (LinearLayout.LayoutParams) title.getLayoutParams();
        lp.width=getResources().getDrawable(res).getMinimumWidth();
        lp.height=height;
        title.setLayoutParams(lp);
    }

    public void setTitleBackgroundResource(int res,int width,int height) {
        title.setVisibility(VISIBLE);
        title.setBackgroundResource(res);
        LinearLayout.LayoutParams lp;
        lp= (LinearLayout.LayoutParams) title.getLayoutParams();
        lp.width=width;
        lp.height=height;
        title.setLayoutParams(lp);
    }

    public void setTitleColor(int color) {
        title.setTextColor(color);
    }

    public void setTitleViewOnClickListener(View.OnClickListener onClickListener){
        title.setOnClickListener(onClickListener);

    }

    public void setLeftBackgroudResource(int res) {
        setLeftVisibility(VISIBLE);
        left.setBackgroundResource(res);
        LinearLayout.LayoutParams lp;
        lp= (LinearLayout.LayoutParams) left.getLayoutParams();
        lp.width=getResources().getDrawable(res).getMinimumWidth();
        lp.height=getResources().getDrawable(res).getMinimumHeight();
        left.setLayoutParams(lp);
    }

    public void setLeftBackgroudResource(int res,int width,int height) {
        setLeftVisibility(VISIBLE);
        left.setBackgroundResource(res);
        LinearLayout.LayoutParams lp;
        lp= (LinearLayout.LayoutParams) left.getLayoutParams();
        lp.width=width;
        lp.height=height;
        left.setLayoutParams(lp);
    }

    public void setRightBackgroudResource(int res,int width,int height) {
        setRightVisibility(VISIBLE);
        right.setBackgroundResource(res);
        LinearLayout.LayoutParams lp;
        lp= (LinearLayout.LayoutParams) right.getLayoutParams();
        lp.width=width;
        lp.height=height;
        right.setLayoutParams(lp);
    }


    public void setLeftText(String res) {
        setLeftVisibility(VISIBLE);
        left.setText(res);
    }

    public void setLeftDrawble(Drawable drawble, DrawableIndex index) {
        setLeftVisibility(VISIBLE);
        switch (index) {
            case LEFT:
                left.setCompoundDrawables(drawble, null, null, null);
                return;
            case TOP:
                left.setCompoundDrawables(null, drawble, null, null);
                return;
            case RIGHT:
                left.setCompoundDrawables(null, null, drawble, null);
                return;
            case BOTTOM:
                left.setCompoundDrawables(null, null, null, drawble);
                return;
        }
    }


    public void setRightBackgroudResource(int res) {
        setRightVisibility(VISIBLE);
        right.setBackgroundResource(res);
        LinearLayout.LayoutParams lp;
        lp= (LinearLayout.LayoutParams) right.getLayoutParams();
        lp.width=getResources().getDrawable(res).getMinimumWidth();
        lp.height=getResources().getDrawable(res).getMinimumHeight();
        right.setLayoutParams(lp);
    }

    public void setRightText(String res) {
        setRightVisibility(VISIBLE);
        right.setText(res);
    }



    public void setRightDrawble(Drawable drawble, DrawableIndex index) {
        setRightVisibility(VISIBLE);
        switch (index) {
            case LEFT:
                right.setCompoundDrawables(drawble, null, null, null);
                return;
            case TOP:
                right.setCompoundDrawables(null, drawble, null, null);
                return;
            case RIGHT:
                right.setCompoundDrawables(null, null, drawble, null);
                right.setPadding(10,0,16,0);
                return;
            case BOTTOM:
                right.setCompoundDrawables(null, null, null, drawble);
                return;
        }
    }

    public void setLeftVisibility(int visibility) {
        left.setVisibility(visibility);
    }

    public void setRightVisibility(int visibility) {
        right.setVisibility(visibility);
    }

    public void setLeftClickListener(OnClickListener onClickListener) {
        left.setOnClickListener(onClickListener);
    }

    public void setRightClickListener(OnClickListener onClickListener) {
        right.setOnClickListener(onClickListener);
    }

    public void setRightTextColor(int color) {
        setRightVisibility(VISIBLE);
        right.setTextColor(color);
    }

    public void setLeftTextColor(int color) {
        setLeftVisibility(VISIBLE);
        left.setTextColor(color);
    }

}
