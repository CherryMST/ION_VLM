package com.nokia.vlm.json.response;


import com.nokia.vlm.entity.SelfUpdateInfo;

/**
 * Created by dcl on 2016/12/12.
 */

public class SeltUpdateResponseJson extends BaseResponseJson {

    public SelfUpdateInfo selfUpdateInfo;

    @Override
    protected void parseCustom(String jsonStr) {
        if (contentJson == null) return;

        SelfUpdateInfo selfUpdateInfo = new SelfUpdateInfo();
        selfUpdateInfo.parse(contentJson);
        this.selfUpdateInfo = selfUpdateInfo;
    }

}
