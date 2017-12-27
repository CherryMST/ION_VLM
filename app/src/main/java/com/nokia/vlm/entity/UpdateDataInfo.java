package com.nokia.vlm.entity;


import com.nokia.vlm.json.response.BaseResponseJson;

public class UpdateDataInfo extends BaseResponseJson{
    public UpdateDataInfo(String jsonStr,String newData) {
        mJsonStr = jsonStr;
        mNewData = newData;
    }

    private String mJsonStr;
    private String mNewData;

    public void setJsonStr(String jsonStr) {
        mJsonStr = jsonStr;
    }

    public String getNewData() {
        return mNewData;
    }

    public void setNewData(String newData) {
        mNewData = newData;
    }

    public UpdateDataInfo() {

    }

    public String getJsonStr() {
        return mJsonStr;
    }



    @Override
    protected void parseCustom(String jsonStr) {
        if (null == jsonStr) {
            return;
        }
        mJsonStr=jsonStr;

    }

}
