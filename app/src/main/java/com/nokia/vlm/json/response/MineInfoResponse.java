package com.nokia.vlm.json.response;


import com.nokia.vlm.entity.LeftMenuInfo;

/**
 * Created by LiShang on 2017/5/8.
 */
public class MineInfoResponse extends BaseResponseJson {

    public LeftMenuInfo info;

    @Override
    protected void parseCustom(String jsonStr) {
        info = new LeftMenuInfo();
        info.parse(contentJson);
    }
}
