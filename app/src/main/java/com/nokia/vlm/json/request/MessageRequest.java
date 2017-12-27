package com.nokia.vlm.json.request;

/**
 * Created by DingCuiLin on 2016/8/12.
 */

public class MessageRequest extends BaseRequestJson {

    public String token;
    public String last_id;//  Y 第一次获取的第一条ID  没有则为空
}
