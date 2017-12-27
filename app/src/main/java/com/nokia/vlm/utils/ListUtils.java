package com.nokia.vlm.utils;

import com.qx.framelib.utlis.TextUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/6 10:50
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/6
 * @更新描述 ${TODO}
 */

public class ListUtils {
    public static  ArrayList<String> removeDuplicatedData(List<String> srcList) {
       /* List<T> newList = new ArrayList<T>();
        for (T cd:srcList) {
            if(!newList.contains(cd)){
                newList.add(cd);
            }
        }
        return newList;*/

        Set<String> set = new HashSet<>();
        ArrayList<String> newList = new ArrayList<>();
        for (String cd:srcList) {
            if(set.add(cd)){
                if (TextUtil.isEmpty(cd)) {
                    cd = "";
                }
                newList.add(cd);
            }
        }
        return newList;
    }
}
