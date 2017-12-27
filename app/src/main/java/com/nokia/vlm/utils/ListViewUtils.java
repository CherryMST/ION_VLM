package com.nokia.vlm.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/14 16:24
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/14
 * @更新描述 ${TODO}
 */

public class ListViewUtils {
    /**
     * 动态设置ListView的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        //listView为什么为空????

        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);

            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            // listView.getDividerHeight()获取子项间分隔符占用的高度
            // params.height最后得到整个ListView完整显示需要的高度
            listView.setLayoutParams(params);
        }
    }
}