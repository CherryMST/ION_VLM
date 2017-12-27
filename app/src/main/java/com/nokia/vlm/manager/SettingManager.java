package com.nokia.vlm.manager;


import com.nokia.vlm.db.table.ZooerSettingTable;
import com.nokia.vlm.ui.QXApp;

import java.util.concurrent.ConcurrentHashMap;

public class SettingManager {

    private ZooerSettingTable setting;

    private volatile static SettingManager instance;

    private ConcurrentHashMap<String, String> mData = new ConcurrentHashMap<String, String>();

    private SettingManager() {
        setting = new ZooerSettingTable(QXApp.getAppSelf());
        setting.getAll(mData);
    }

    public synchronized static SettingManager getInstance() {
        if (instance == null) {
            instance = new SettingManager();
        }
        return instance;
    }

    public void setIsFirstLaunch(boolean isFirstLaunch) {
        set(IS_FIRST_LAUNCH, isFirstLaunch);
    }

    public boolean IsFirstLaunch() {
        return getBoolean(IS_FIRST_LAUNCH, true);
    }

    public void setJPushAlias(String uid, boolean isAlias) {
        set(uid, IS_SET_JPUSH_ALIAS, isAlias);
    }

    public boolean IsSetJPushAlias(String uid) {
        return getBoolean(uid, IS_SET_JPUSH_ALIAS, false);
    }

    public void setNewUrl(String newUrl) {
        set(NEW_URL, newUrl);
    }

    public String getNewUrl() {
        return getString(NEW_URL, "");
    }

    public String getCurWithdrawCard() {
        return getString(CUR_WITHDRAW_CARD, "");
    }

    public void setCurWithdrawCard(String withdrawCard) {
        set(CUR_WITHDRAW_CARD, withdrawCard);
    }


    public void setLoginUsername(String username) {
        set(LOGIN_USERNAME, username);
    }

    public String getLoginUsername() {
        return getString(LOGIN_USERNAME, null);
    }

    /*public void setLocationCityInfo(PositionEntity info) {
        byte[] bytes = info.encodeUserInfo();
        boolean update = setting.update("", "_" + LOCATION_CITY_INFO, "", bytes);
        if (!update) {
            setting.add("", "_" + LOCATION_CITY_INFO, "", bytes);
        }
    }

    public PositionEntity getLocationCityInfo() {
        byte[] bytes = setting.getBlob("", "_" + LOCATION_CITY_INFO);
        if (bytes == null) {
            return new PositionEntity();
        } else {
            return PositionEntity.decodeUserInfo(bytes);
        }
    }*/

    public void setFriendNewCount(int count) {
        set(FRIEND_NEW_COUNT, count);
    }

    public int getFriendNetCount() {
        return getInt(FRIEND_NEW_COUNT, 0);
    }

    public boolean getBoolean(String uqid, String key, boolean defValue) {
        try {
            return Boolean.parseBoolean(get(uqid + "_" + key, defValue));
        } catch (NumberFormatException e) {
            //e.printStackTrace();
        }
        return defValue;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return getBoolean("", key, defValue);
    }

    public float getFloat(String uqid, String key, float defValue) {
        try {
            return Float.valueOf(get(uqid + "_" + key, defValue));
        } catch (NumberFormatException e) {
            //e.printStackTrace();
        }
        return defValue;
    }

    public float getFloat(String key, float defValue) {
        return getFloat("", key, defValue);
    }

    public int getInt(String uqid, String key, int defValue) {
        try {
            return Integer.valueOf(get(uqid + "_" + key, defValue));
        } catch (NumberFormatException e) {
            //e.printStackTrace();
        }
        return defValue;
    }

    public int getInt(String key, int defValue) {
        return getInt("", key, defValue);
    }

    public byte getByte(String uqid, String key, byte defValue) {
        try {
            return Byte.valueOf(get(uqid + "_" + key, defValue));
        } catch (NumberFormatException e) {
            //e.printStackTrace();
        }
        return defValue;
    }

    public byte getByte(String key, byte defValue) {
        return getByte("", key, defValue);
    }

    public long getLong(String uqid, String key, long defValue) {
        try {
            return Long.valueOf(get(uqid + "_" + key, defValue));
        } catch (NumberFormatException e) {
            //e.printStackTrace();
        }
        return defValue;
    }

    public long getLong(String key, long defValue) {
        return getLong("", key, defValue);
    }

    public String getString(String uqid, String key, String defValue) {
        return get(uqid + "_" + key, defValue);
    }

    public String getString(String key, String defValue) {
        return getString("", key, defValue);
    }

    public String get(String key, Object defValue) {
        if (mData.containsKey(key)) {
            return mData.get(key);
        } else {
            return String.valueOf(defValue);
        }
    }

    public boolean set(String uqid, String key, Object value) {
        mData.put(uqid + "_" + key, String.valueOf(value));
        boolean updated = setting.update(uqid, key, String.valueOf(value));
        if (!updated) {
            return setting.add(uqid, key, String.valueOf(value));
        }
        return false;
    }

    public boolean set(String key, Object value) {
        return set("", key, value);
    }

    //---------------------------------配置信息-----------------------------------------------//
    //用户是否首次启动
    public static final String IS_FIRST_LAUNCH = "is_first_launch";
    //登陆用户
    public static final String LOGIN_USERNAME = "login_username";
    //定位城市信息
    public static final String LOCATION_CITY_INFO = "location_city_info";

    //是否设置极光别名
    public static final String IS_SET_JPUSH_ALIAS = "is_set_JPush_Alias";
    //新朋友申请消息个数
    public static final String FRIEND_NEW_COUNT = "friend_new_count";

    //最新的域名
    public static final String NEW_URL = "new_url";


    public static final String CUR_WITHDRAW_CARD = "cur_withdraw_card";//当前提现账号


}
