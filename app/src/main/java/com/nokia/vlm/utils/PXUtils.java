package com.nokia.vlm.utils;

import android.content.Context;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/12 16:05
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/12
 * @更新描述 ${TODO}
 */

public class PXUtils {
    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

}
