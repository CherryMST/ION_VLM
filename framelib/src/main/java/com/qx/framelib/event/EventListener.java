package com.qx.framelib.event;

import android.os.Message;

/**
 * 抽象事件接口
 * @author xiaofenglou
 *
 */
public interface EventListener {
    public void handleEvent(Message msg);
}

