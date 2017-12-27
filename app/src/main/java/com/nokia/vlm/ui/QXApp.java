package com.nokia.vlm.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Message;
import android.os.RemoteException;

import com.amap.api.maps.MapsInitializer;
import com.nokia.vlm.event.QXEventDispatcherEnum;
import com.qx.framelib.application.ZooerApp;
import com.qx.framelib.event.listener.UIEventListener;



public class QXApp extends ZooerApp implements UIEventListener {
    public static String api_host_debug = "http://135.252.218.106:20200";


    @Override
    public void onCreate() {
        super.onCreate();
        super.api_host = api_host_debug;

        //ShareSDK 分享
//        initUIEventListener();

        try {
            MapsInitializer.initialize(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private void initUIEventListener() {
        QXApp.getAppSelf().getEventController().addUIEventListener(QXEventDispatcherEnum.UI_EVENT_TOKEN_NO_USE, this);
    }

    private void removeUIEventListener() {
        QXApp.getAppSelf().getEventController().removeUIEventListener(QXEventDispatcherEnum.UI_EVENT_TOKEN_NO_USE, this);
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
//        removeUIEventListener();
//        IMKitUserManager.getInstane().removeCallBack();
    }


    @Override
    public void handleUIEvent(Message msg) {

//        if (msg.what == QXEventDispatcherEnum.UI_EVENT_TOKEN_NO_USE) { //跳转到登陆页面
//            UserInfoManager.getInstance().delUserInfo();
//            //清空别名
//            JPushUtils.setAlias("null");
//            //MessageManager.getInstanse().clearAll();
//
//            Intent intent = new Intent();
//            intent.setClass(ZooerApp.getAppSelf(), LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Bundle bundle = new Bundle();
//            bundle.putString(LoginActivity.KEY_PAGE, LoginActivity.BEFORE_PAGE);
//            intent.putExtras(bundle);
//
//            startActivity(intent);
//        }

    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }



}
