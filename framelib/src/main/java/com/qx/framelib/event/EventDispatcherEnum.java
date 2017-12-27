package com.qx.framelib.event;


/**
 * 事件汇总.
 * --------add by xiaofenglou,注意，事件只能递增，不允许将新的事件插入中间-------
 * -----------------------------------------------------------------------------------------------------------
 */
public class EventDispatcherEnum {

    public static final int UI_EVENT_BEGIN = 1000;
    //网络连接
    public static final int UI_EVENT_NETWORK_CONNECT = UI_EVENT_BEGIN + 1;
    //网络断开连接
    public static final int UI_EVENT_NETWORK_DISCONNECT = UI_EVENT_NETWORK_CONNECT + 1;


    public static final int UI_EVENT_END = 2000;
}
