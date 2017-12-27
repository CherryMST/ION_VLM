package com.nokia.vlm.json.request;


public class UpdateDataRequestJson extends BaseRequestJson {

    /**
     * 类似于这种"{"TestEquip_Asset"： [row1,..],[row2...]],"Table2":[[row1],[row2]]}"
     * */
    public String data;

    public UpdateDataRequestJson() {
    }
    public UpdateDataRequestJson(String data) {
        this.data = data;
    }
}
