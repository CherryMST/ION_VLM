package com.nokia.vlm.utils;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class TestDataUtils {

    public static List<String> getPhoto() {
        List<String> list = new ArrayList<>();
        list.add("http://img4.duitang.com/uploads/item/201204/17/20120417120717_wi5xr.thumb.600_0.jpeg");
        list.add("http://cdn.duitang.com/uploads/item/201206/09/20120609170256_AHEEh.jpeg");

        list.add("http://img.funshion.com/pictures/521/312/521312.jpg");
        list.add("http://cdn.duitang.com/uploads/item/201207/11/20120711212044_zwvjr.jpeg");

        list.add("http://img.funshion.com/pictures/521/311/521311.jpg");
        list.add("http://pic.5442.com/2012/1231/07/04.jpg");

        list.add("http://img.kumi.cn/photo/85/c1/71/85c171d10f848e37.jpg");
        list.add("http://img4.duitang.com/uploads/item/201203/23/20120323165727_RsSei.jpeg");
        return list;
    }


    public static List<String> getLabel() {
        List<String> list = new ArrayList<>();
        list.add("官方");
        list.add("贵妇狗");

        list.add("度过的");
        list.add("大锅饭");

        list.add("通过");
        list.add("认股人");

        list.add("复合肥");
        list.add("杨荣华");
        return list;
    }

    public static List<LatLng> getLatLngs() {
        List<LatLng> list = new ArrayList<>();
        list.add(new LatLng(30.299482, 120.099728));
        list.add(new LatLng(30.300242, 120.100715));

        list.add(new LatLng(30.299593, 120.101616));

        list.add(new LatLng(30.301835, 120.098827));
        list.add(new LatLng(30.298241, 120.099771));
        list.add(new LatLng(30.300797, 120.099385));
        list.add(new LatLng(30.299704, 120.102668));

        return list;
    }

}
