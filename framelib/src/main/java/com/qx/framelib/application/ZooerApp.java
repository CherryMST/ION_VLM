package com.qx.framelib.application;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.qx.framelib.event.EventController;
import com.qx.framelib.event.EventDispatcher;
import com.qx.framelib.manager.SystemEventManager;
import com.qx.framelib.module.CrashModule;
import com.qx.framelib.net.NetWorkManager;
import com.qx.framelib.utlis.ZLog;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

//import com.facebook.common.logging.FLog;
//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.facebook.imagepipeline.core.ImagePipelineConfig;
//import com.facebook.imagepipeline.listener.RequestListener;
//import com.facebook.imagepipeline.listener.RequestLoggingListener;

//import com.zhy.autolayout.config.AutoLayoutConifg;


/**
 * Created by ZhaoWei on 2016/4/18.
 */
public class ZooerApp extends MultiDexApplication {
    private static final String TAG = "ZooerApp";
    //程序发生crash后的广播
    public static final String INTENT_ACTION_EXIT_APP = "com.nokia.vlm.action.EXIT_APP";

    public static String api_host = "http://135.252.218.106:20200";

    private static ZooerApp        self;
    private static Activity        mCurrentActivity;
    private        EventDispatcher mEventDispatcher;
    private        EventController mEventController;


    @Override
    public void onCreate() {
        super.onCreate();

        self = this;

        mEventController = EventController.getInstance();
        mEventDispatcher = EventDispatcher.getInstance(mEventController);

        init();
    }


    private void init() {

//        //配置 fackbook
//        FLog.setMinimumLoggingLevel(FLog.VERBOSE);
//        Set<RequestListener> listeners = new HashSet<>();
//        listeners.add(new RequestLoggingListener());
//        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
//                .setRequestListeners(listeners)
//                .setBitmapsConfig(Bitmap.Config.ARGB_8888)
//                .build();
//        Fresco.initialize(this, config);


        //异常捕获
        CrashModule crashModule = new CrashModule();
        crashModule.enableCrash();

        OkHttpUtils.getInstance().setConnectTimeout(30, TimeUnit.SECONDS);//离线测试为1s,正常测试为30s
        OkHttpUtils.getInstance().setReadTimeout(30, TimeUnit.SECONDS);

        SystemEventManager.getInstance().init(ZooerApp.getAppSelf());
        NetWorkManager.getInstance().register();


//        AutoLayoutConifg.getInstance().useDeviceSize().init(this);
    }


    public static ZooerApp getAppSelf() {
        return self;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        mCurrentActivity = currentActivity;
    }

    public static Activity getCurrentActivity() {
        if (mCurrentActivity != null) {
            return mCurrentActivity;
        }
        return null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
        ZLog.d(TAG, "onTerminate");
        NetWorkManager.getInstance().unregister();
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
        ZLog.d(TAG, "onLowMemory");
    }


    /**
     * Get the event dispatcher.
     *
     * @return The event dispaccctcher.
     */
    public EventDispatcher getEventDispatcher() {
        return mEventDispatcher;
    }

    /**
     * Get the controller.
     *
     * @return The controller.
     */
    public EventController getEventController() {
        return mEventController;
    }

}
