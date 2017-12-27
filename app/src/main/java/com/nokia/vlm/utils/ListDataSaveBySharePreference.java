package com.nokia.vlm.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * @创建者 DK-house
 * @创建时间 2017/11/16 2:01
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/11/16
 * @更新描述 ${TODO}
 */

public class ListDataSaveBySharePreference {

    public static SharedPreferences        preferences;
    public static SharedPreferences.Editor editor;

    public static SharedPreferences getPreferences() {
        return preferences;
    }

    public static SharedPreferences.Editor getEditor() {
        return editor;
    }
    public ListDataSaveBySharePreference(Context mContext, String preferenceName) {
        preferences = mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * 保存List
     * @param tag
     * @param datalist
     */
    public static <T> void setDataList(String tag, ArrayList<T> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
//        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();

    }

    /**
     * 获取List
     * @param tag
     * @return
     */
    public static <T> ArrayList<T> getDataList(String tag) {
        ArrayList<T> datalist=new ArrayList<T>();
        String strJson = preferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<ArrayList<T>>() {
        }.getType());
        return datalist;

    }
}
