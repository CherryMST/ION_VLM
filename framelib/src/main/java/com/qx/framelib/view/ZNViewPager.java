package com.qx.framelib.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ViewPage可能不需要滑动
 *
 * @author luohongbo
 */
public class ZNViewPager extends ViewPager {

    private boolean scroolEnable = true;

    public ZNViewPager(Context context) {
        super(context);
    }

    public ZNViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public void setScrollEnable(boolean scrollEnable) {
        this.scroolEnable = scrollEnable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (!scroolEnable) {
            return false;
        }
        try {
            return super.onTouchEvent(arg0);
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {


        if (!scroolEnable) {
            return false;
        }
        return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }
}
