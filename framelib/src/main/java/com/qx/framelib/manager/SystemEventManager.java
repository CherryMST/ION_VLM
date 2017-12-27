package com.qx.framelib.manager;

import android.content.Context;
import android.net.NetworkInfo;

import com.qx.framelib.net.APN;
import com.qx.framelib.net.NetWorkUtil;
import com.qx.framelib.utlis.ZLog;


public class SystemEventManager {

    private static SystemEventManager mInstance = null;

    private NetworkMonitorManager mNetworkMonitor;


    private SystemEventManager() {
        mNetworkMonitor = new NetworkMonitorManager();
    }

    public synchronized static SystemEventManager getInstance() {
        if (mInstance == null) {
            mInstance = new SystemEventManager();
        }
        return mInstance;
    }

    public void registerNetWorkListener(NetworkMonitorManager.ConnectivityChangeListener listener) {
        mNetworkMonitor.register(listener);
    }

    public void unregisterNetWorkListener(NetworkMonitorManager.ConnectivityChangeListener listener) {
        mNetworkMonitor.unregister(listener);
    }


    /**
     * 网络变化处理
     *
     * @param activeNetInfo
     */
    public void onConnectivityChanged(NetworkInfo activeNetInfo) {
        if (activeNetInfo != null) {
            APN apn1 = NetWorkUtil.getApn();
            ZLog.d("onConnectivityChanged", "apn1:" + apn1);
            NetWorkUtil.refreshNetwork();
            APN apn2 = NetWorkUtil.getApn();
            ZLog.d("onConnectivityChanged", "apn1:" + apn2);
            if (apn1 != apn2) {
                if (apn1 == APN.NO_NETWORK) {
                    ZLog.d("NetWorkManager", "apn1APN.NO_NETWORK");
                    mNetworkMonitor.notifyConnected(apn2);
                } else if (apn2 == APN.NO_NETWORK) {
                    ZLog.d("NetWorkManager", "apn2APN.NO_NETWORK");
                    mNetworkMonitor.notifyDisconnected(apn1);
                } else {
                    ZLog.d("NetWorkManager", "apn1apn2APN.NO_NETWORK");
                    mNetworkMonitor.notifyChanged(apn1, apn2);
                }
            }
        }
    }


    public void init(Context context) {
        mNetworkMonitor.init(context);
    }
}
