package com.nokia.vlm.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/14 23:21
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/14
 * @更新描述 ${TODO}
 */

public class MapListInfo implements Serializable{
    private HashMap<String, List<HashMap<String, String>>> mMapList;
    public MapListInfo() {

    }

    public MapListInfo(HashMap<String, List<HashMap<String, String>>> mapList) {
        mMapList = mapList;
    }

    public HashMap<String, List<HashMap<String, String>>> getMapList() {
        return mMapList;
    }

    public void setMapList(HashMap<String, List<HashMap<String, String>>> mapList) {
        mMapList = mapList;
    }
}
