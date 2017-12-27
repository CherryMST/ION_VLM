package com.nokia.vlm.ui;

import android.content.pm.PackageManager;

import com.qx.framelib.utlis.ChannelUtil;
import com.nokia.vlm.utils.DeviceUtils;


/**
 * 一些全局的东西放在这里
 * 
 * @author luohongbo
 *
 */
public class Global {
	
	public static AppStatus app_status = AppStatus.DEV;//开始期间都是开发版本，发布和灰度再改
	
//	public static final String QQ_APPID = "1105043569";
//	public static final String QQ_APPKEY = "";
//
//    public static final String WX_APPID = "wx85a292bbab3bfdc6";
//    public static final String WX_APPKEY = "b0ec55c14084a6c37b9967f06594d2ce";
//
//	public static final String WB_APPID = "2746050804";
//	public static final String WB_APPKEY = "3f210738460a2b630add849cbfd3a7ca";
//
//    public static final String EASEMOB_APPKEY = "zooer2016#zooermall2016";

    private static volatile boolean hasInit = false;
	
	
	static enum AppStatus{
		
		DEV,GRAY,OFFICIAL
	}
	
	public static boolean isDev()
	{
		return app_status == AppStatus.DEV;
	}
	
	
	public static boolean isGray()
	{
		return app_status == AppStatus.GRAY;
	}
	
	public static boolean isOfficial()
	{
		return app_status == AppStatus.OFFICIAL;
	}

    public static void init()
    {
        if (!hasInit)
        {
            DeviceUtils.init(QXApp.getAppSelf());
            ChannelUtil.genChannel();
            hasInit = true;
        }

    }


    public static String getAppVersionName()
    {
        try {
            String packageName = QXApp.getAppSelf().getPackageName();
            return QXApp.getAppSelf().getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static int getAppVersionCode() {
        String packageName = QXApp.getAppSelf().getPackageName();
        try {
            return QXApp.getAppSelf().getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getChannelID()
    {
       return ChannelUtil.genChannel();
    }

}
