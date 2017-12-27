/*
 * FixMultiViewPager 2016-12-26
 * Copyright (c) 2016 suzeyu Co.Ltd. All right reserved
 */
package com.nokia.vlm.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * ClassDescription : 对多点触控场景时, {@link android.support.v4.view.ViewPager#onInterceptTouchEvent(MotionEvent)}中
 *                  pointerIndex = -1. 发生IllegalArgumentException: pointerIndex out of range 处理
 */
public class DragPhotoViewPager extends ViewPager {
    private static final String TAG = DragPhotoViewPager.class.getSimpleName();

    public DragPhotoViewPager(Context context) {
        super(context);
    }

    public DragPhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            Log.w(TAG, "onInterceptTouchEvent() ", ex);
            ex.printStackTrace();
        }
        return false;
    }

}
