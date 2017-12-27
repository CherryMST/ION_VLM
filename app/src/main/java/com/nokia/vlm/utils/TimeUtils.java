package com.nokia.vlm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeUtils {

    //字符串转时间戳
    public static String getTime(String timeString){
        String timeStamp = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date d;
        try{
            d = sdf.parse(timeString);
            long l = d.getTime() / 1000;
            timeStamp = String.valueOf(l);
        } catch(ParseException e){
            e.printStackTrace();
        }
        return timeStamp;
    }

    //时间戳转字符串
    public static String getStrTime(String timeStamp){
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        long l;
        if (timeStamp.length() == 10) {
            l = Long.valueOf(timeStamp) * 1000;
        } else {
            l = Long.valueOf(timeStamp);
        }

        timeString = sdf.format(new Date(l));//单位秒
        return timeString;
    }

    //根据时间戳获得多久以前
    public static String getTimeAgo(long timeStamp){
        if (timeStamp == 0) return "";
        try {
            long now = new Date().getTime() / 1000;
            long diff = 0;
            String temp = "";
            diff = now - timeStamp ;

            if (diff != 0) {
                long months = diff / (60 * 60 * 24 * 30);
                long days = diff / (60 * 60 * 24);
                long hours = (diff - days * (60 * 60 * 24)) / (60 * 60);
                long minutes = (diff - days * (60 * 60 * 24) - hours * (60 * 60)) / 60;
                long second = diff - days * (60 * 60 * 24) - hours * (60 * 60) - minutes * 60;
                if (months > 0) {
                    temp = months + "月前";
                } else if (days > 0) {
                    temp = days + "天前";
                } else if (hours > 0) {
                    temp = hours + "小时前";
                } else if (minutes > 0) {
                    temp = minutes + "分钟前";
                } else {
                    temp = second + "秒前";
                }
            } else {
                temp = "刚刚";
            }
            return temp;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //根据时间戳获还多久结束
    public static String getTimeFinish(long timeStamp){
        if (timeStamp == 0) return "";
        try {
            long now = new Date().getTime()/1000;
            long diff = 0;
            StringBuffer temp = new StringBuffer();
            diff = timeStamp - now;

            if (diff > 0) {
                long days = diff / (60 * 60 * 24);
                long hours = (diff - days * (60 * 60 * 24)) / (60 * 60);
                long minutes = (diff - days * (60 * 60 * 24) - hours * (60 * 60)) / 60;
                long second = diff - days * (60 * 60 * 24) - hours * (60 * 60) - minutes * 60;
                if (days > 0) {
                    temp.append(days + "天") ;
                }
                if (hours > 0) {
                    temp.append(hours + "时") ;
                }
                if (minutes > 0) {
                    temp.append(minutes + "分");
                }
                if (second > 0){
                    temp.append(second + "秒");
                }
            } else {
                temp.append("已结束");
            }
            return temp.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
