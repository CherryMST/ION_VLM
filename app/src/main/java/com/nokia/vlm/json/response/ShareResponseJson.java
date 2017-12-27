package com.nokia.vlm.json.response;

/**
 * 共用 解析类   不关注 data{} 里面的数据  只关注 code msg
 */
public class ShareResponseJson extends BaseResponseJson {
    @Override
    protected void parseCustom(String jsonStr) {

    }
}
