package com.qx.framelib.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.qx.framelib.application.ZooerApp;
import com.qx.framelib.utlis.ToastUtils;


public class NetWorkUtil {


    public static final byte GROUP_NETTYPE_2g = 1;
    public static final byte GROUP_NETTYPE_3g = 2;
    public static final byte GROUP_NETTYPE_WIFI = 3;
    public static final byte GROUP_NETTYPE_UNKNOWN = 4;
    public static final byte GROUP_NETTYPE_4g = 5;

    /* 中国移动 */
    public static final int OPERATOR_CHINA_MOBILE = 0;
    /* 中国联通 */
    public static final int OPERATOR_CHINA_UNICOM = 1;
    /* 中国电信 */
    public static final int OPERATOR_CHINA_TELECOM = 2;
    /* 未知运营商 */
    public static final int OPERATOR_UNKNOWN = -1;

    private static boolean isNetworkActive = true;

    // 接入点名称
    private static NetInfo netInfo = new NetInfo();

    private static boolean isHotSpotWifi = false;

    public static boolean isNetworkActive() {
        if (netInfo.apn == APN.UN_DETECT) {
            refreshNetwork();
        }
        return isNetworkActive;
    }

    /* 判断是否是wap类网络 */
    public static boolean isWap() {
        String host = Proxy.getDefaultHost();
        return !TextUtils.isEmpty(host);
    }

    /**
     * 获取网络类型是wifi，3g，还是2g
     *
     * @return
     */
    public static int getGroupNetType() {
        if (isWifi()) {
            return GROUP_NETTYPE_WIFI;
        } else if (is4G()) {
            return GROUP_NETTYPE_4g;
        } else if (is3G()) {
            return GROUP_NETTYPE_3g;
        } else if (is2G()) {
            return GROUP_NETTYPE_2g;
        } else {
            return GROUP_NETTYPE_UNKNOWN;
        }
    }

    /**
     * 获取网络类型描述符
     */
    public static String getGroupNetTypeDesc() {
        if (isWifi()) {
            return "WIFI";
        } else if (is4G()) {
            return "4G";
        } else if (is3G()) {
            return "3G";
        } else if (is2G()) {
            return "2G";
        } else {
            return "UNKNOWN";
        }
    }

    /* 判断是否是Wifi类网络 */
    public static boolean isWifi() {
        final APN apn = getApn();
        return apn == APN.WIFI;
    }

    public static boolean is2G() {
        final APN apn = getApn();
        return apn == APN.CMNET || apn == APN.CMWAP || apn == APN.UNINET || apn == APN.UNIWAP;
    }

    public static boolean is3G() {
        final APN apn = getApn();
        return apn == APN.CTWAP || apn == APN.CTNET || apn == APN.WAP3G || apn == APN.NET3G;
    }

    public static boolean is4G() {
        final APN apn = getApn();
        return apn == APN.WAP4G || apn == APN.NET4G;
    }

    public static NetInfo getNetInfo() {
        if (netInfo.apn == APN.UN_DETECT) {
            refreshNetwork();
        }
        return netInfo;
    }

    public static APN getApn() {
        final NetInfo netInfo = getNetInfo();
        return netInfo.apn;
    }

    /**
     * 网络连接变化的时候需要重新刷新一下当前的apn
     */
    public static void refreshNetwork() {
        netInfo = getNetInfo(ZooerApp.getAppSelf());
    }

    /* 获取接入点名称 Access Point Name , context不能为null */
    private static NetInfo getNetInfo(Context context) {
        NetInfo result = new NetInfo();
        NetworkInfo info = null;
        ConnectivityManager connManager = null;
        try {
            connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // OPPO的机器上这个地方会抛异常crash+
            if (connManager != null)
                info = connManager.getActiveNetworkInfo();
            if (info == null || !info.isAvailable()) {
                isNetworkActive = false;
                result.apn = APN.NO_NETWORK;
                return result;
            }
        } catch (Throwable e) {
            //增加error捕获，部分酷派机器上可能没有getActiveNetworkInfo方法
        }

        isNetworkActive = true;
        if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {// wifi网络判定
            result.apn = APN.WIFI;

            WifiManager wifiManager = (WifiManager) ZooerApp.getAppSelf().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                try {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo != null) {
                        result.bssid = wifiInfo.getBSSID();
                        result.ssid = wifiInfo.getSSID();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            return result;
        } else {
            return getMobileNetInfo(context);
        }
    }

    /* 获取移动网络接入点类型 , context不能为null */
    private static NetInfo getMobileNetInfo(Context context) {
        NetInfo result = new NetInfo();
        final boolean isWap = isWap();
        result.isWap = isWap;                                        // iswap赋值

        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String networkOperator = telManager.getNetworkOperator();
        result.networkOperator = networkOperator;                    // networkOperator赋值
        final int networkType = telManager.getNetworkType();
        result.networkType = networkType;                            // networkType赋值

        final int operator = getSimOperator(networkOperator);
        switch (operator) {
            case OPERATOR_CHINA_MOBILE: // 中国移动
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        if (isWap) {
                            result.apn = APN.WAP4G;
                        } else {
                            result.apn = APN.NET4G;
                        }
                        return result;
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        if (isWap) {
                            result.apn = APN.CMWAP;
                        } else {
                            result.apn = APN.CMNET;
                        }
                        return result;
                    default:
                        if (isWap) {
                            result.apn = APN.UNKNOW_WAP;
                        } else {
                            result.apn = APN.UNKNOWN;
                        }
                        return result;
                }
            case OPERATOR_CHINA_UNICOM: // 中国联通
                // 先判断是2g还是3g网络
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        if (isWap) {
                            result.apn = APN.WAP4G;
                        } else {
                            result.apn = APN.NET4G;
                        }
                        return result;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:    // 联通3g
                    case 15:                                    // TelephonyManager.NETWORK_TYPE_HSPAP api 13+
                        if (isWap) {
                            result.apn = APN.WAP3G;
                        } else {
                            result.apn = APN.NET3G;
                        }
                        return result;
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE: // 联通2g
                        if (isWap) {
                            result.apn = APN.UNIWAP;
                        } else {
                            result.apn = APN.UNINET;
                        }
                        return result;
                    default:
                        if (isWap) {
                            result.apn = APN.UNKNOW_WAP;
                        } else {
                            result.apn = APN.UNKNOWN;
                        }
                        return result;
                }
            case OPERATOR_CHINA_TELECOM: // 中国电信
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        if (isWap) {
                            result.apn = APN.WAP4G;
                        } else {
                            result.apn = APN.NET4G;
                        }
                        return result;
                    default:
                        if (isWap) {
                            result.apn = APN.CTWAP;
                        } else {
                            result.apn = APN.CTNET;
                        }
                        return result;
                }
            default:
                if (isWap) {
                    result.apn = APN.UNKNOW_WAP;
                } else {
                    result.apn = APN.UNKNOWN;
                }
                return result;
        }
    }

    /* 获取移动网络运营商 , context不能为null */
    public static int getSimOperator(String networkOperator) {
        if (!TextUtils.isEmpty(networkOperator)) {
            if (networkOperator.equals("46000") || networkOperator.equals("46002") || networkOperator.equals("46007")) {
                return OPERATOR_CHINA_MOBILE;
            } else if (networkOperator.equals("46001")) {
                return OPERATOR_CHINA_UNICOM;
            } else if (networkOperator.equals("46003")) {
                return OPERATOR_CHINA_TELECOM;
            }
        }
        return OPERATOR_UNKNOWN;
    }

    public static void setIsHotWifi(boolean isHotWifi) {
        isHotSpotWifi = isHotWifi;
    }

    public static boolean getIsHotWifi() {
        return isHotSpotWifi;
    }

    /**
     * 检查当前飞行模式是否打开
     *
     * @param context
     * @return
     */
    public static boolean IsAirModeOn(Context context) {
        return (android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.AIRPLANE_MODE_ON, 0) == 1 ? true : false);
    }

    /**
     * 获取wifi 下信号强度
     *
     * @return
     */
    public static int getWifiSignalStrength() {
        WifiManager wifiManager = (WifiManager) ZooerApp.getAppSelf().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            try {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo != null) {
                    return WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5);
                }
            } catch (Throwable e) {
            }
        }

        return -1;
    }


    public static boolean hasNetWork() {
        if (NetWorkUtil.isNetworkActive()) {
            return true;
        } else {
            ToastUtils.getInstance().show("无网络", Toast.LENGTH_SHORT);
            return false;
        }
    }

    /**
     * 获取wifi mac 地址
     *
     * @param context
     * @return
     */
    public static String getWifiMac(Context context) {

        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiMan.getConnectionInfo();
        String mac = info.getBSSID();// 获得网络既 wifi 的MAC地址
//        info.getMacAddress(); 获取的是手机的wifi
        return mac;
    }

    public static String getLocalMac(Context context) {
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiMan.getConnectionInfo();
//        String mac = info.getBSSID();// 获得网络既 wifi 的MAC地址
        String mac = info.getMacAddress(); //获取的是手机的wifi
        return mac;
    }

}
