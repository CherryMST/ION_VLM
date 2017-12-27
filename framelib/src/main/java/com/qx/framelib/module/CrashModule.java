package com.qx.framelib.module;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import com.qx.framelib.application.ZooerApp;
import com.qx.framelib.utlis.ZLog;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 程序运行期间的crash统计
 * Created by luohongbo on 16/3/6.
 */
public class CrashModule {

    private CustomExceptionHandler customExceptionHandler;

    public CrashModule() {
        customExceptionHandler = new CustomExceptionHandler();
    }

    public void enableCrash() {
        Thread.setDefaultUncaughtExceptionHandler(customExceptionHandler);
    }

    //异常发生的时候需要做些其他事情在这里处理
    protected void doFun(Thread thread, Throwable ex) {

    }

    public class CustomExceptionHandler implements UncaughtExceptionHandler {
        private UncaughtExceptionHandler defaultHandler;

        public CustomExceptionHandler() {
            defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        }

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {


            ex.printStackTrace();

            doFun(thread, ex);

            String data = null;
            Writer writer = null;
            PrintWriter printWriter = null;
            try {
                writer = new StringWriter();
                printWriter = new PrintWriter(writer);
                ex.printStackTrace(printWriter);
                data = writer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            }

            if (ZLog.isDebug) {
                Date dt = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                String timestamp = df.format(dt);
                final StringBuffer sb = new StringBuffer();
                sb.append("MODEL:" + Build.MODEL + "\r\n");
                sb.append("PRODUCT:" + Build.PRODUCT + "\r\n");
                sb.append("DEVICE:" + Build.DEVICE + "\r\n");
                sb.append("DISPLAY:" + Build.DISPLAY + "\r\n");
                sb.append("CURRENT_VERSION:" + getAppVersionName() + "\r\n");
                sb.append(data);
                String filename = "Exception_" + timestamp + ".log";
                ZLog.f(sb.toString(), filename);
            }

            Intent intent = new Intent(ZooerApp.INTENT_ACTION_EXIT_APP);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            ZooerApp.getAppSelf().sendBroadcast(intent);

            defaultHandler.uncaughtException(thread, ex);
        }
    }


    public static String getAppVersionName() {
        try {
            String packageName = ZooerApp.getAppSelf().getPackageName();
            return ZooerApp.getAppSelf().getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

}
