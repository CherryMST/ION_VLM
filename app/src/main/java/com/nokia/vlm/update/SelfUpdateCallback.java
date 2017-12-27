package com.nokia.vlm.update;

/**
 * 程序自升级回调
 */
public interface SelfUpdateCallback {

    public void onStart(String url);

    public void onProgress(String url, long totalLength, long length, double speed);

    public void onFailed(String url, int errorCode);

    public void onCancel(String url);

    public void onCompleted(String url, String savePath);
}
