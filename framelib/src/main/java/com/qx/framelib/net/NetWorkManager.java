package com.qx.framelib.net;

import android.os.Message;

import com.qx.framelib.application.ZooerApp;
import com.qx.framelib.event.EventDispatcherEnum;
import com.qx.framelib.manager.NetworkMonitorManager;
import com.qx.framelib.manager.SystemEventManager;
import com.qx.framelib.utlis.ZLog;


/**
 * Created by ZhaoWei on 2016/5/9.
 */
public class NetWorkManager implements NetworkMonitorManager.ConnectivityChangeListener {

    private static NetWorkManager instance;

    private NetWorkManager() {

    }

    public synchronized static NetWorkManager getInstance() {
        if (instance == null) {
            instance = new NetWorkManager();
        }
        return instance;
    }

    public void register() {
        SystemEventManager.getInstance().registerNetWorkListener(this);
    }

    public void unregister() {
        SystemEventManager.getInstance().unregisterNetWorkListener(this);
    }


    @Override
    public void onConnected(APN apn) {
        ZLog.d("NetWorkManager", "网络连接上了....");
        Message message = new Message();
        message.what = EventDispatcherEnum.UI_EVENT_NETWORK_CONNECT;
        ZooerApp.getAppSelf().getEventDispatcher().sendMessage(message);
    }

    @Override
    public void onDisconnected(APN apn) {
        ZLog.d("NetWorkManager", "网络断开了....");
        Message message = new Message();
        message.what = EventDispatcherEnum.UI_EVENT_NETWORK_DISCONNECT;
        ZooerApp.getAppSelf().getEventDispatcher().sendMessage(message);
    }

    @Override
    public void onConnectivityChanged(APN apn1, APN apn2) {

    }
}
