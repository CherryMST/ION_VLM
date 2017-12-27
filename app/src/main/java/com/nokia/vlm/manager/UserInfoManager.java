package com.nokia.vlm.manager;


import android.text.TextUtils;

import com.qx.framelib.utlis.ZLog;
import com.nokia.vlm.db.table.ZooerProfileTable;
import com.nokia.vlm.entity.UserInfo;


/**
 * 用户资料信息管理
 * 默认只会保存一条用户记录
 */
public class UserInfoManager {

    public UserInfo userInfo;
    private ZooerProfileTable table;

    private static UserInfoManager instance;

    private UserInfoManager() {
        table = new ZooerProfileTable();
    }

    public synchronized static UserInfoManager getInstance() {
        if (instance == null) {
            instance = new UserInfoManager();
        }
        return instance;
    }

    public synchronized String getToken() {

        if (userInfo == null) {
            userInfo = table.getLastUserInfo();
        }
        if (userInfo != null) {
            ZLog.d("UserInfoMananger getToken :::" + userInfo.token);
        }
        ZLog.d("UserInfoMananger getToken ");
        return userInfo != null ? userInfo.token : "";
    }


    //用户资料的删除,用户点击退出账户的时候调用
    public synchronized boolean delUserInfo() {
        boolean del = table.delUserInfo(userInfo);
        userInfo = null;

        ZLog.d("UserInfoMananger delUserInfo " + del);
        return del;
    }

    //用于用户资料的更新,不存在记录就删除，存在更新
    public synchronized boolean updateUserInfo(UserInfo newUserInfo) {
        if (newUserInfo == null) {
            return false;
        }
        boolean updated = table.updateUserInfo(newUserInfo);
        if (!updated) {
            updated = table.addUserInfo(newUserInfo);
        }
        this.userInfo = newUserInfo;


        ZLog.d("UserInfoMananger updateUserInfo " + userInfo.token);
        return updated;
    }

    public synchronized UserInfo getUserInfo() {
        if (userInfo == null) {
            userInfo = table.getLastUserInfo();
        }
        ZLog.d("UserInfoMananger getUserInfo ", ""  + userInfo.token);
        return userInfo;
    }

    //用户是否登陆
    public synchronized boolean isLogin() {
        String token = getToken();
        return !TextUtils.isEmpty(token);
    }

    /*public synchronized String getServiceTel() {
        if (userInfo == null) {
            userInfo = table.getLastUserInfo();
        }
        return userInfo != null ? userInfo.server_tel : "";
    }*/


//    public synchronized String getMobile(){
//        if (userInfo == null) {
//            userInfo = table.getLastUserInfo();
//        }
//        return userInfo != null ? userInfo.mobile : "";
//    }

//    public synchronized String getRongYunToken() {
//        if (userInfo == null) {
//            userInfo = table.getLastUserInfo();
//        }
//        return userInfo != null ? userInfo.rongytoken : "";
//    }

    /**
     * 是否设置支付密码
     * @return
     */
//    public synchronized boolean hasPayPwd() {
//        if (userInfo == null) {
//            userInfo = table.getLastUserInfo();
//        }
//        return userInfo.paypsw_status == 1;
//    }

}
