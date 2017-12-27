package com.nokia.vlm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class ScrollBannerWrapper extends FrameLayout {

    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;

    public ScrollBannerWrapper(Context context) {
        super(context);
    }

    public ScrollBannerWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollBannerWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance = Math.abs(curX - xLast);
                yDistance = Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                if (xDistance < yDistance) {
                    isIntercept = true;
                }
        }
        return isIntercept;
    }
}
