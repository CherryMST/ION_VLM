package com.nokia.vlm.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/11 14:42
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/11
 * @更新描述 ${TODO}
 */

public class SerializableMap implements Serializable {
    private Map<String, String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
