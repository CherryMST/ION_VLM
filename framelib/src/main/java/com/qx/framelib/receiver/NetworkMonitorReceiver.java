package com.qx.framelib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.qx.framelib.manager.SystemEventManager;
import com.qx.framelib.utlis.ZLog;


public class NetworkMonitorReceiver extends BroadcastReceiver {

    public NetworkMonitorReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            ZLog.d("NetWorkManager", "receive");
            NetworkInfo activeNetInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            SystemEventManager.getInstance().onConnectivityChanged(activeNetInfo);
        }

    }

}
