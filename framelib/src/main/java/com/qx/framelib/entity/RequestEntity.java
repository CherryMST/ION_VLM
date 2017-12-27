package com.qx.framelib.entity;

import java.io.File;

/**
 * Created by luohongbo on 16/1/9.
 */
public class RequestEntity {

    public int seq;//当前请求序列号
    public String requestBody;//请求内容
    public String url;//请求地址
    public String tag; //请求tag

    public long startTime; //请求开始时间
    public File file; //上传文件
    public boolean isUpload; //是否是上传文件  默认：false
}
