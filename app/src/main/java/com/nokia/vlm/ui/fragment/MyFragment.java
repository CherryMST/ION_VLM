package com.nokia.vlm.ui.fragment;

import android.view.View;
import android.widget.ImageView;

import com.nokia.vlm.R;
import com.nokia.vlm.callback.MyClickCallback;
import com.nokia.vlm.ui.activity.MainActivity;
import com.nokia.vlm.utils.PXUtils;
import com.qx.framelib.fragment.BaseFragment;
import com.qx.framelib.utlis.ToastUtils;
import com.qx.framelib.view.TitleBar2;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/8 15:26
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/8
 * @更新描述 ${TODO}
 */

public class MyFragment extends BaseFragment implements MyClickCallback, View.OnClickListener {

    private ImageView mIvAssetManagement;
    private ImageView mIvFeedback;

    @Override
    public View createContentView() {
        return layoutInflater.inflate(R.layout.fragment_my, null);
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
        initListener();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initTitle() {
        MainActivity activity = (MainActivity) getActivity();
        activity.setTitleVisibility(View.INVISIBLE);
        activity.setTitleViewClickable(false);
        activity.setRightDrawble(null, TitleBar2.DrawableIndex.RIGHT);
        activity.setRightBackgroudResource(R.drawable.icon_setting, PXUtils.dip2px(getActivity(), 27), PXUtils.dip2px(getActivity(), 27));
    }

    private void initListener() {
        mIvAssetManagement.setOnClickListener(this);
        mIvFeedback.setOnClickListener(this);

    }


    private void initView(View rootView) {
        mIvAssetManagement = (ImageView) rootView.findViewById(R.id.iv_asset_management);
        mIvFeedback = (ImageView) rootView.findViewById(R.id.iv_feedback);
    }

    private void initData() {
    }


    @Override
    public void refreshData(boolean isAutoRefresh) {

    }

    @Override
    public void onMyClick(View view) {
        ToastUtils.getInstance().show("页面跳转");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_asset_management:

                break;
            case R.id.iv_feedback:

                break;
        }
    }

}
