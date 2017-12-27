package com.nokia.vlm.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nokia.vlm.R;
import com.nokia.vlm.callback.AddTestEquipAssetItemCallback;
import com.nokia.vlm.entity.CodeCaptureInfo;
import com.nokia.vlm.ui.activity.AssetActivity;
import com.nokia.vlm.ui.activity.MainActivity;
import com.nokia.vlm.ui.activity.QRCodeCaptureActivity;
import com.nokia.vlm.utils.PXUtils;
import com.nokia.vlm.view.GlideImageLoader;
import com.nokia.vlm.view.HomeGridViewAdapter;
import com.nokia.vlm.view.NoScrollGridView;
import com.qx.framelib.fragment.BaseFragment;
import com.qx.framelib.utlis.ZLog;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Administrator on 2017/2/4.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener, AddTestEquipAssetItemCallback, HomeGridViewAdapter.ClickCallback {
    public static final String DATA_FROM_DB   = "dataFromDb";
    public static final String DATA_FROM_TARGET_ASSET   = "dataFromTargetAsset";
    public static final String RESULT_RESULT_ITEM   = "resultItem";
    public static final String SHAREPREFERENCE_FLAG = "Capture";
    public static final int    SYNCALL              = 100;
    private int[] res = {R.drawable.grid_test_instrument, R.drawable.grid_more};

    private CodeCaptureInfo mResponseJson;
    private             HashMap<String, List<HashMap<String, String>>> mTableHashMap   = new HashMap<>();
    public static final String                                         ALL_ASSET       = "All_Asset";
    public static final String                                         TESTEQUIP_ASSET = "TestEquip_Asset";
    public static final String                                         CARD_ASSET      = "Card_Asset";
    public static final String                                         PLUGGABLE_ASSET = "Pluggable_Asset";
    public static final String                                         SHELF_ASSET     = "Shelf_Asset";
    public static final String                                         COMPUTER_ASSET  = "Computer_Asset";

    protected Banner           mBanner;
    protected NoScrollGridView mGridView;
    private boolean isFirst = true;

    @Override
    public View createContentView() {
        return layoutInflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void onPageTurnBackground() {

    }

    @Override
    public void onPageScrolling(float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirst) {
            isFirst = false;
        } else {
            //initLBS();
        }


    }

    @Override
    public void startLoadView() {
        setCheckNetWork(false);
        initView(rootView);

        initListener();
        registers();
        initData();


    }

    private void initListener() {

    }


    private void initTile() {
        MainActivity activity = (MainActivity) getActivity();
        activity.setRightVisibility(View.GONE);
        activity.setTitleVisibility(View.VISIBLE);
        activity.setTitleViewClickable(true);
        activity.setTitleBackgroundResource(R.drawable.icon_search, PXUtils.dip2px(getActivity(), 30));
    }



    private void initData() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mResponseJson = mainActivity.getResponseJson();
        ZLog.d("getResponseJson()");

        initImageBanner();
        initGridView();


    }


    private void initImageBanner() {
        Integer[] images = {R.drawable.banner_1, R.drawable.banner_2, R.drawable.banner_3, R.drawable.banner_4};
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(Arrays.asList(images));
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }

    @Override
    public void refreshData(boolean isAutoRefresh) {
        if (isAutoRefresh) {
            ZLog.d("HomeFragment", "refresh");
        }
    }


    private void initView(View rootView) {
        mBanner = (Banner) rootView.findViewById(R.id.banner);
        mGridView = (NoScrollGridView) rootView.findViewById(R.id.gridView);

    }

    private void registers() {

    }

    private void unregisters() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisters();
    }

    @Override
    public void onClick(View v) {

    }


    private void initGridView() {

        HomeGridViewAdapter gridViewAdapter = new HomeGridViewAdapter(getActivity(),res, this);
        mGridView.setAdapter(gridViewAdapter);

    }

    private int getDrawableId(ImageView iv) {
        return (Integer) iv.getTag();
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
                    Intent intent = new Intent(getActivity(), AssetActivity.class);
//                    bundle.putString("key", TESTEQUIP_ASSET);
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(TESTEQUIP_ASSET);
                    bundle.putStringArrayList(QRCodeCaptureActivity.QR_RESULT_TABLENAME, arrayList);
                    bundle.putParcelable(HomeFragment.ALL_ASSET, responseJson);
                    bundle.putBoolean(HomeFragment.DATA_FROM_TARGET_ASSET,true);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent, bundle);
                    break;
            }

        }

    }

}
