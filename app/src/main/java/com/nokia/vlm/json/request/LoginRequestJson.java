package com.nokia.vlm.json.request;


public class LoginRequestJson extends BaseRequestJson {

    public String uin;//用户名
    public String a1;// 密码
    public int client_type = 1;//  客户端类型（1android,2ios，3web）
    public String platform_id;//平台类型（11）

}
