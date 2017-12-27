package com.nokia.vlm.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/15 14:05
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/15
 * @更新描述 ${TODO}
 */

public class SerializableList  implements Serializable {
    public SerializableList(List<BaseAssetItemInfo> list) {
        mList = list;
    }

    private List<BaseAssetItemInfo> mList;
    public List<BaseAssetItemInfo> getList() {
        return mList;
    }

    public void setList(List<BaseAssetItemInfo> list) {
        mList = list;
    }



}
