package com.nokia.vlm.ui.fragment;

import android.view.View;

import com.nokia.vlm.R;
import com.qx.framelib.fragment.BaseFragment;
import com.qx.framelib.utlis.ToastUtils;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/8 15:26
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/8
 * @更新描述 ${TODO}
 */

public class AssetFragment extends BaseFragment {
    @Override
    public View createContentView() {
        return layoutInflater.inflate(R.layout.fragment_asset, null);
    }

    @Override
    public void onPageTurnBackground() {

    }

    @Override
    public void onPageScrolling(float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void startLoadView() {
        setCheckNetWork(false);
        initView(rootView);
    }

    private void initView(View rootView) {
        ToastUtils.getInstance().show("Fragment 跳转成功!");
    }

    @Override
    public void refreshData(boolean isAutoRefresh) {

    }
}
