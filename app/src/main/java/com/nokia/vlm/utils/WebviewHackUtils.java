package com.nokia.vlm.utils;

import java.lang.reflect.Field;
import java.util.HashSet;

public class WebviewHackUtils {
    //这里通过hack的做法直接将sGoogleApps置为空的set，以绕过获取信息的步骤。
    public static  void clearSGoogleApps(){
        try {
            Field sGoogleApps = Class.forName("android.webkit.WebViewClassic")
                    .getDeclaredField("sGoogleApps");
            sGoogleApps.setAccessible(true);
            sGoogleApps.set(sGoogleApps, new HashSet<String>());
        } catch (Exception e) {
        }
    }


    public static void gcConfigCallback(){
        try {
            Field sConfigCallback = Class.forName("android.webkit.BrowserFrame")
                    .getDeclaredField("sConfigCallback");
            sConfigCallback.setAccessible(true);
            sConfigCallback.set(null, null); // 这里也可能抛NPE
        } catch (Exception e) {
            // ignored
        }
    }
    /**  这里可能会引发问题，需要再多验证 - -！
     * WebViewPerfUtil这个貌似是htc rom特有的类，看leak没有使用弱引用，直接将内部mContext置为null还不够，只好强制将所有filed设为null
     LEVEL_WAKELOCK_BENCHMARK|LEVEL_WAKELOCK_NONE|LEVEL_WAKELOCK_PERFORMANCE|LEVEL_WAKELOCK_VSYNC|LEVEL_WAKELOCK_ZOOM|LOGTAG|WebSite_Enable_Perf
     |bInPerfWebSite|bInit|
     mBmCnt|mBmCpuFreqWakeLock|mBmCpuNumWakeLock|mContext|mCurrentWakeLockLevel|mPfCnt|mPfCpuFreqWakeLock|mPfCpuNumWakeLock|mVsCnt|mVsyncCpuFreqWakeLock|mVsyncCpuNumWakeLock|mZmCnt|mZmCpuFreqWakeLock|mZmCpuNumWakeLock
     *
     */
    public static void gcHtcLeak(){
        try {
            Class<?> webViewPerfUtilClass = Class.forName("android.webkit.WebViewPerfUtil");
            Field[] fields = webViewPerfUtilClass.getDeclaredFields();
            for (Field field : fields) {
                try {
                    if (field.getName().startsWith("m")) {  //有website相关的不能置为null，否则会crash
                        field.setAccessible(true);
                        field.set(null, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
