package com.nokia.vlm.utils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.qx.framelib.utlis.ZLog;

import java.io.Serializable;

public class IntentDataUtils {

    /**
     * 获取intent中指定key的string值。这里之所以要进行封装是因为系统的接口没有对intent中出现的unparcel异常进行捕获
     *
     * @param intent
     * @param key
     * @return
     */
    public static String getStringExtra(Intent intent, String key) {
        if (intent == null) {
            return null;
        }
        try {
            return intent.getStringExtra(key);
        } catch (Throwable throwable) {//对发生的异常进行捕获
        }
        return null;
    }

    /**
     * 获得intent里面的bundle对象，获取时会做一下可读性测试，如果unparcel()失败会返回一个空对象以免后续使用一样出现了问题。如果你获得的bundle后面需要设置classloader的话，不要使用这个方法
     * @param intent
     * @return
     */
    public static Bundle getExtras(Intent intent) {
        if (intent == null) {
            return null;
        }
        try {
            Bundle b = intent.getExtras();
            //这里测试一下看是否能够成功读取，如果无法反系列化，会出现异常，这个bundle不再可用
            return b;
        } catch (Throwable throwable) {
            if(throwable instanceof RuntimeException && !TextUtils.isEmpty(throwable.toString()) && throwable.toString().indexOf("ClassNotFound")>=0)
            {
                ZLog.d("IntentDataUtils", "get intent extras fail." + throwable);
                return null;
            }
        }
        return null;
    }

    public static Serializable getSerializableExtra(Intent intent, String key) {
        if (intent == null) {
            return null;
        }
        try {
            return intent.getSerializableExtra(key);
        } catch (Throwable throwable) {
        }
        return null;
    }

    public static boolean getBooleanExtra(Intent intent, String key, boolean defaultVal) {
        if (intent == null) {
            return defaultVal;
        }
        try {
            return intent.getBooleanExtra(key, defaultVal);
        } catch (Throwable throwable) {
        }
        return defaultVal;
    }

    public static int getIntExtra(Intent intent, String key, int defaultVal) {
        if (intent == null) {
            return defaultVal;
        }
        try {
            return intent.getIntExtra(key, defaultVal);
        } catch (Throwable throwable) {
        }
        return defaultVal;
    }


}
