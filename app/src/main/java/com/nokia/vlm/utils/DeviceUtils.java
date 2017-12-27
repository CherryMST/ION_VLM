package com.nokia.vlm.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import com.qx.framelib.application.ZooerApp;
import com.qx.framelib.utlis.FileUtils;
import com.qx.framelib.utlis.TextUtil;
import com.qx.framelib.utlis.ZLog;
import com.nokia.vlm.ui.Global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DeviceUtils {

    private static String zooer_guid = "";


    public static int currentDeviceWidth;
    public static int currentDeviceHeight;
    public static float currentDensity;

    // 是否支持多点触摸？(sdk>=7 && 屏幕支持多点)
    public static boolean IS_SURPORT_MUTITOUCH_GESTURER = true;

    // 是否是高质量手机, 决定图片上传策略
    private static boolean IS_HIGH_QUALITY_DEVICE = true;

    public static String model;

    public static void init(Context context)
    {
        int currentOrientation = context.getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            currentDeviceWidth = context.getResources().getDisplayMetrics().widthPixels;
            currentDeviceHeight = context.getResources().getDisplayMetrics().heightPixels;
        } else if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            currentDeviceWidth = context.getResources().getDisplayMetrics().heightPixels;
            currentDeviceHeight = context.getResources().getDisplayMetrics().widthPixels;
        } else {
            int _currentDeviceWidth = context.getResources().getDisplayMetrics().widthPixels;
            int _currentDeviceHeight = context.getResources().getDisplayMetrics().heightPixels;
            currentDeviceWidth = Math.min(_currentDeviceWidth, _currentDeviceHeight);
            currentDeviceHeight = Math.max(_currentDeviceWidth, _currentDeviceHeight);
        }
        currentDensity = context.getResources().getDisplayMetrics().density;

        int temp = (currentDeviceWidth > currentDeviceHeight) ? currentDeviceWidth : currentDeviceHeight;
        if (temp < 800 || currentDensity <= 1) {
            IS_HIGH_QUALITY_DEVICE = false;
        }

        if (Build.VERSION.SDK_INT < 7 || !isSupportMultiTouch()) {
            IS_SURPORT_MUTITOUCH_GESTURER = false;
        }
        genZooerGuid();

    }

    /***
     * 当前machine是否支持多点触摸
     *
     * @return
     */
    public static boolean isSupportMultiTouch()
    {
        boolean condition1 = false;
        boolean condition2 = false;
        // Not checking for getX(int), getY(int) etc 'cause I'm lazy
        Method methods[] = MotionEvent.class.getDeclaredMethods();
        for (Method m : methods)
        {
            if (m.getName().equals("getPointerCount"))
                condition1 = true;
            if (m.getName().equals("getPointerId"))
                condition2 = true;
        }
        if (Build.VERSION.SDK_INT >= 7 || (condition1 && condition2))
            return true; // 支持多点触摸
        else
            return false;
    }


    public static String getModel(){
        if(TextUtils.isEmpty(model)){
            model = Build.MODEL;
        }
        return model;
    }


    public static String genZooerGuid()
    {
        if (!TextUtil.isEmpty(zooer_guid))
        {
            return zooer_guid;
        }
        zooer_guid = readOrWriteGUID(ZooerApp.getAppSelf());
        return zooer_guid;
    }

    private static boolean illegal_guid(File file)
    {
        if( !file.exists())
            return false;
        if(file.length() < 8 || file.length() >256)
            return false;
        byte[] buf = new byte[256];
        int read = 0;

        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        if( fis == null)
            return false;
        try {
            read = fis.read(buf, 0, 256);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            fis.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if( read < 8)
            return false;
        return true;
    }

    public static String readOrWriteGUID(Context context )
    {
        File sd  =  FileUtils.getSDCard();

        File sd_guid =  null;
        File in_guid =   new File( context.getApplicationInfo().dataDir+"/.z_guid");
        if( sd != null )
        {
            sd_guid  = new File( sd.getAbsolutePath()+"/.z_guid");
        }

        byte[] buf = new byte[256];

        if( sd_guid != null  )
        {
            boolean bSdExist = sd_guid.exists();
            boolean bInExit  = in_guid.exists();

            if( bSdExist )
            {
                if( sd_guid.length() < 8)
                {
                    bSdExist = false;
                    sd_guid.delete();
                }

                if( bSdExist )
                {
                    bSdExist = illegal_guid(sd_guid);
                    if(!bSdExist)
                    {
                        sd_guid.delete();
                        ZLog.d("GUID", "rm illegal sd guid!!!");
                        bSdExist = false;
                    }
                }
            }
            if( bInExit)
            {
                if( in_guid.length() < 8)
                {
                    bInExit = false;
                    in_guid.delete();
                }

                if( bInExit )
                {
                    bInExit = illegal_guid(in_guid);
                    if(!bInExit)
                    {
                        in_guid.delete();
                        ZLog.d("GUID", "rm illegal in guid!!!");
                        bInExit = false;
                    }
                }
            }

            try {
                if( bSdExist &&  !bInExit)
                {
                    FileUtils.copyRawFile_s(sd_guid, in_guid);
                }
                else if( bInExit && !bSdExist)
                {
                    FileUtils.copyRawFile_s(in_guid, sd_guid);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

            if( bInExit  )
            {

                ZLog.d("GUID", "using guid in !");
                int read = FileUtils.readFromFile(in_guid,buf,255);
                if(read > 0)
                    return new String(buf,0,read);
            }
            if( bSdExist )
            {
                ZLog.d("GUID", "using guid sd !");
                int read = FileUtils.readFromFile(sd_guid,buf,255);
                if( read > 0)
                    return new String(buf,0,read);
            }
            //else
            {

                String hash = java.util.UUID.randomUUID().toString()+"_"+ System.currentTimeMillis()+"_"+ Global.getAppVersionCode();

                ZLog.d("GUID", "create hash:"+hash);

                try
                {
                    in_guid.createNewFile();
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try
                {
                    sd_guid.createNewFile();
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                FileUtils.write2File(hash.getBytes(), in_guid.getAbsolutePath());
                FileUtils.write2File(hash.getBytes(), sd_guid.getAbsolutePath());

                return hash;
            }
        }
        else
        {
            boolean bInExit  = in_guid.exists();

            if( bInExit  )
            {
                int read = FileUtils.readFromFile(in_guid,buf,255);
                if( read > 0)
                    return new String(buf,0,read);
            }

            {
                String hash = java.util.UUID.randomUUID().toString()+"_"+ System.currentTimeMillis()+"_"+ Global.getAppVersionCode();;

                ZLog.d("GUID", "create hash:"+hash);

                try
                {
                    in_guid.createNewFile();
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                FileUtils.write2File(hash.getBytes(), in_guid.getAbsolutePath());
                return hash;
            }
        }
    }



    public static String getImei() {
        int result = -1;
        try {
            result = ZooerApp.getAppSelf().checkCallingOrSelfPermission(android.Manifest.permission.READ_PHONE_STATE);
        } catch (Exception e) {
        }
        if(result != PackageManager.PERMISSION_GRANTED){
            return "000000000000000";
        }
        try {
            TelephonyManager tm = (TelephonyManager) ZooerApp.getAppSelf().getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Throwable e) {
            return "000000000000000";
        }

    }

    public static String getImsi() {
        int result = ZooerApp.getAppSelf().checkCallingOrSelfPermission(android.Manifest.permission.READ_PHONE_STATE);
        if(result != PackageManager.PERMISSION_GRANTED){
            return "000000";
        }
        try {
            final TelephonyManager tm = (TelephonyManager) ZooerApp.getAppSelf().getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSubscriberId();
        } catch (Exception e) {
            return "000000";
        }

    }

    public static String getMacAddress() {
        try {
            final WifiManager wifiManager = (WifiManager) ZooerApp.getAppSelf().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = null;
            info = wifiManager.getConnectionInfo();
            if (info != null) {
                return info.getMacAddress();
            } else {
                return "";
            }
        }
        catch (Throwable ex)
        {
            ex.printStackTrace();
        }
        return "";
    }

    public static String getAndroidIdInPhone() {
        return Settings.Secure.getString(ZooerApp.getAppSelf().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 50;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Throwable e1)
        {
            e1.printStackTrace();
            if( Global.isDev())
            {
                ZLog.d("DeviceUtils", "getStatusBarHeight "+e1.toString());
            }
        }
        return statusBarHeight;
    }

    /**
     * 获取屏幕的宽度和高度
     * @param activity
     * @return
     */
    public static Point computerScreen(Activity activity) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int height = mDisplayMetrics.heightPixels;
        int	width = mDisplayMetrics.widthPixels;
        if (width < height) {
            int size = width;
            width = height;
            height = size;
        }

        return new Point(width, height);
    }
}
