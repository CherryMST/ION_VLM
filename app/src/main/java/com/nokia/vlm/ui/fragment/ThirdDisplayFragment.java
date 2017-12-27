package com.nokia.vlm.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.nokia.vlm.R;
import com.nokia.vlm.entity.BaseAssetItemInfo;
import com.nokia.vlm.ui.adapter.SelectResultAdapter;
import com.nokia.vlm.utils.ListViewUtils;
import com.qx.framelib.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/8 15:26
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/8
 * @更新描述 ${TODO}
 */

public class ThirdDisplayFragment extends BaseFragment {
    private ArrayList<BaseAssetItemInfo> mListForDisplay=new ArrayList<>();
    private ListView                     mListView;

    @Override
    public View createContentView() {
        return layoutInflater.inflate(R.layout.fragment_third_display, null);
    }

    @Override
    public void onPageTurnBackground() {

    }

    @Override
    public void onPageScrolling(float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void startLoadView() {
        Bundle bundle = getArguments();
        if (null != bundle) {
            mListForDisplay = bundle.getParcelableArrayList("ListForDisplay");
        }
        initFragmentView();
    }

    private void initFragmentView() {
        mListView = (ListView) findViewById(R.id.listView);
        SelectResultAdapter adapter = new SelectResultAdapter(getActivity(), mListForDisplay);
        mListView.setAdapter(adapter);
        ListViewUtils.setListViewHeightBasedOnChildren(mListView);
    }

    @Override
    public void refreshData(boolean isAutoRefresh) {

    }
}
