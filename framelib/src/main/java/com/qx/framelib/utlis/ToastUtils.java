package com.qx.framelib.utlis;

import android.text.TextUtils;
import android.widget.Toast;

import com.qx.framelib.application.ZooerApp;


/**
 * Created by ZhaoWei on 2016/4/19.
 */
public class ToastUtils {
    private static ToastUtils instance;
    private Toast toast;
    private long showTime; //显示的时间
    private int duration; //显示多久

    public static ToastUtils getInstance() {
        if (instance == null) {
            instance = new ToastUtils();
        }
        return instance;
    }

    public void show(String text, int duration) {
        if (toast == null) {
            toast = Toast.makeText(ZooerApp.getAppSelf(), text, duration);
            toast.show();
            showTime = System.currentTimeMillis();
            return;
        }
        long currenttime = System.currentTimeMillis();
        if (currenttime - showTime < toast.getDuration()) { //说明还没显示完
            toast.cancel();
        }
        toast.setText(text);
        toast.setDuration(duration);
        toast.show();
        showTime = System.currentTimeMillis();
    }

    public void show(String text) {
        if (!TextUtil.isEmpty(text)) {
            show(text, Toast.LENGTH_SHORT);
        }
    }


    public static boolean toastTel(String tel) {
        if (TextUtils.isEmpty(tel)) {
            getInstance().show("亲，请先输入您的手机号！");
            return false;
        }

        if (tel.length() != 11) {
            getInstance().show("亲！请输入正确的手机号哦！");
            return false;
        }
        return true;
    }

    public static boolean toastCode(String code) {
        if (TextUtils.isEmpty(code) || code.length() != 6) {
            getInstance().show("亲！请输入正确的验证码！");
            return false;
        }
        return true;
    }

    public static boolean toastPassword(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            getInstance().show("亲！请输入密码！");
            return false;
        }

//        if (ValidatorUtils.isPassword(pwd)) {
//            getInstance().show("亲！请输入您的密码!(6-20位数字+字母)");
//        }

        int len = pwd.length();
        if (len < 6 || len > 16) {
            getInstance().show("亲！请输入6-16位的数字或字母！");
            return false;
        }
        return true;
    }

    public static boolean toastPassword(String pwd1, String pwd2) {
        if (!toastPassword(pwd1)) {
            return false;
        }

        if (TextUtils.isEmpty(pwd2) || !pwd1.equals(pwd2)) {
            getInstance().show("亲！两次输入的密码不一致");
            return false;
        }
        return true;
    }

}
