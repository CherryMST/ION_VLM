package com.nokia.vlm.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class FixedWebView extends WebView {
    //这些机型有问题或这些机型特定版本的rom有问题，会导致硬件渲染问题，先简单过滤
    private static ArrayList<String> specialPhone = new ArrayList<String>();
    static {
        specialPhone.add("HUAWEI_HUAWEI U9508");
        specialPhone.add("Xiaomi_MI 2SC");
    }
    /**
     * @param context
     * @param attrs
     */
    public FixedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        String mark = android.os.Build.MANUFACTURER + "_"+ android.os.Build.MODEL;
        //强制关闭加速，或者特殊机型 时使用软件加速,特殊机型后台配置更合理
        if (specialPhone.contains(mark) ) {
            closeHardWare();
        }
        setBackgroundColor(0);
        //以下是为了解决sx的安全检测加的
        try {
            Class<?> clazz = getClass();
            Method method = clazz.getMethod("removeJavascriptInterface", String.class);
            if (method != null)
                method.invoke(this, "searchBoxJavaBridge_");
        } catch (Exception e1) {
        }
    }

    public  void setLayerType(View view, int type) {
        if (view == null) {
            return;
        }
        if (android.os.Build.VERSION.SDK_INT > 10) {
            Method m;
            try {
                m = view.getClass().getMethod("setLayerType", Integer.TYPE,
                        Paint.class);
                m.invoke(view, type, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**开启硬件加速，这是个尽人事听天命的方法，能不能生效谁用谁知道*/
    public void openHardware(){
        setLayerType(this, 2);
    }

    /**关闭硬件加速，这是个能解决很多兼容性问题，但是会带来流畅度问题的方法*/
    public void closeHardWare(){
        setLayerType(this, 1);
    }
    /**
     * @param context
     */
    public FixedWebView(Context context) {
        super(context);
        String mark = android.os.Build.MANUFACTURER + "_"+ android.os.Build.MODEL;
        //强制关闭加速，或者特殊机型 时使用软件加速,特殊机型后台配置更合理
        if (specialPhone.contains(mark) ) {
            closeHardWare();
        }
        setBackgroundColor(0);
    }
    
    private ScrollListener scrollListener;
    
    @Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(scrollListener != null) {
			scrollListener.onSChanged(l, t, oldl, oldt);
		}
	}
	
	public void setScrollListener(ScrollListener listener) {
		this.scrollListener = listener;
	}
	
	public interface ScrollListener {
		public void onSChanged(int l, int t, int oldl, int oldt);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
	    if (ev.getAction() == MotionEvent.ACTION_MOVE) {
	        onScrollChanged(getScrollX(), getScrollY(), getScrollX(), getScrollY());
	    }
	    return super.onTouchEvent(ev);

    }



}