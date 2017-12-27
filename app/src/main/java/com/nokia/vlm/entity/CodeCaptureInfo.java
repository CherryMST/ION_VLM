package com.nokia.vlm.entity;


import android.os.Parcel;
import android.os.Parcelable;

import com.nokia.vlm.json.response.BaseResponseJson;
import com.nokia.vlm.utils.MapDataUtils;

import java.util.HashMap;
import java.util.List;

public class CodeCaptureInfo extends BaseResponseJson implements Parcelable {
    //hashmap的keySet用于搜索时的模糊匹配

    private HashMap<String, List<HashMap<String, String>>> mTableHashMap = new HashMap<>();

    public String getJsonStr() {
        return mJsonStr;
    }

    private String mJsonStr;

    public HashMap<String, List<HashMap<String, String>>> getTableHashMap() {
        return mTableHashMap;
    }

    @Override
    protected void parseCustom(String jsonStr) {
        if (null == jsonStr) {
            return;
        }
        mJsonStr=jsonStr;
        mTableHashMap= MapDataUtils.parseJsonToMap4Tables(jsonStr);

    }


    public CodeCaptureInfo() {

    }

    //配合MainActivity中离线测试代码使用.
    public CodeCaptureInfo(HashMap<String, List<HashMap<String, String>>> tableHashMap) {
        mTableHashMap = tableHashMap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.mTableHashMap);
    }

    protected CodeCaptureInfo(Parcel in) {
        this.mTableHashMap = (HashMap<String, List<HashMap<String, String>>>) in.readSerializable();
    }

    public static final Parcelable.Creator<CodeCaptureInfo> CREATOR = new Parcelable.Creator<CodeCaptureInfo>() {
        @Override
        public CodeCaptureInfo createFromParcel(Parcel source) {
            return new CodeCaptureInfo(source);
        }

        @Override
        public CodeCaptureInfo[] newArray(int size) {
            return new CodeCaptureInfo[size];
        }
    };
}
