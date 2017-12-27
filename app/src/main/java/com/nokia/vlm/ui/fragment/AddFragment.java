package com.nokia.vlm.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nokia.vlm.R;
import com.nokia.vlm.entity.CodeCaptureInfo;
import com.nokia.vlm.ui.activity.AddNewOneActivity;
import com.nokia.vlm.ui.activity.MainActivity;
import com.nokia.vlm.ui.activity.QRCodeCaptureActivity;
import com.nokia.vlm.utils.PXUtils;
import com.nokia.vlm.view.HomeGridViewAdapter;
import com.nokia.vlm.view.NoScrollGridView;
import com.qx.framelib.fragment.BaseFragment;

import java.util.ArrayList;

import static com.nokia.vlm.ui.fragment.HomeFragment.ALL_ASSET;
import static com.nokia.vlm.ui.fragment.HomeFragment.TESTEQUIP_ASSET;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/8 15:26
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/8
 * @更新描述 ${TODO}
 */

public class AddFragment extends BaseFragment implements HomeGridViewAdapter.ClickCallback {
    public static final String ADD_NEW_ONE = "ADD_NEW_ONE";
    private NoScrollGridView mGridView;
    private int[] res = {R.drawable.grid_test_instrument, R.drawable.grid_more};

    @Override
    public View createContentView() {
        return layoutInflater.inflate(R.layout.fragment_add, null);
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
        initData();
    }


    private void initView(View rootView) {
        mGridView = (NoScrollGridView) rootView.findViewById(R.id.fragment_add_gridView);
    }

    private void initData() {
        initGridView();
    }

    private void initTile() {
        MainActivity activity = (MainActivity) getActivity();
        activity.setRightVisibility(View.GONE);
        activity.setTitleVisibility(View.VISIBLE);
        activity.setTitleViewClickable(true);
        activity.setTitleBackgroundResource(R.drawable.icon_search, PXUtils.dip2px(getActivity(), 30));
    }

    private void initGridView() {
        HomeGridViewAdapter gridViewAdapter = new HomeGridViewAdapter(getActivity(), res,this);
        mGridView.setAdapter(gridViewAdapter);
    }

    @Override
    public void refreshData(boolean isAutoRefresh) {

    }

    @Override
    public void onGridViewClick(View view) {

        Bundle bundle = new Bundle();

        //带着对应按钮的数据
        if (view instanceof ImageView) {
            int tag = getDrawableId((ImageView) view);
            switch (tag) {
                case R.drawable.grid_test_instrument:
                    CodeCaptureInfo responseJson = ((MainActivity) getActivity()).getResponseJson();
                    Intent intent = new Intent(getActivity(), AddNewOneActivity.class);
                    //                    bundle.putString("key", TESTEQUIP_ASSET);
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(TESTEQUIP_ASSET);
                    bundle.putStringArrayList(QRCodeCaptureActivity.QR_RESULT_TABLENAME, arrayList);
                    bundle.putBoolean(ADD_NEW_ONE, true);
                    bundle.putParcelable(ALL_ASSET, responseJson);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent, bundle);

                    /**
                     * QR_RESULT_MAP
                     QR_RESULT_KEY
                     QR_RESULT_TABLENAME
                     *
                     * */
                    break;
            }

        }
    }

    private int getDrawableId(ImageView iv) {
        return (Integer) iv.getTag();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
