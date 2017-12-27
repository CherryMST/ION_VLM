package com.nokia.vlm.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nokia.vlm.R;
import com.nokia.vlm.entity.BaseAssetItemInfo;
import com.nokia.vlm.entity.SerializableMap;
import com.nokia.vlm.ui.activity.AssetActivity;
import com.nokia.vlm.ui.activity.QRCodeCaptureActivity;
import com.nokia.vlm.ui.activity.QRResultActivity;
import com.nokia.vlm.ui.adapter.SelectResultAdapter;
import com.nokia.vlm.utils.ListViewUtils;
import com.nokia.vlm.utils.MapDataUtils;
import com.qx.framelib.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.nokia.vlm.ui.activity.QRCodeCaptureActivity.QR_RESULT_MAP;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/14 10:32
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/14
 * @更新描述 ${TODO}
 */

public class FirstDisplayFragment extends BaseFragment {

    private List<String[]> mStrArrayList4SearchAndUpdate;
    private List<BaseAssetItemInfo> mListForDisplay = new ArrayList<>();
    private ListView                                       mListView;
    private AssetActivity                                  mParentActivity;
    private HashMap<String, List<HashMap<String, String>>> mTableMap;
    private String                                         mSearchKey;

    public FirstDisplayFragment() {
    }


    @Override
    public View createContentView() {
        return layoutInflater.inflate(R.layout.fragment_fist_display, null);
    }

    @Override
    public void onPageTurnBackground() {

    }

    @Override
    public void onPageScrolling(float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void startLoadView() {
        mParentActivity = (AssetActivity) getActivity();
        Bundle bundle = getArguments();
        if (null != bundle) {
            mListForDisplay = bundle.getParcelableArrayList("ListForDisplay");
        }
        initFragmentView();
        initData();
    }

    private void initData() {
        this.mTableMap = mParentActivity.mTableHashMap;
        mStrArrayList4SearchAndUpdate = new ArrayList<>();
    }

    private void initFragmentView() {
        mListView = (ListView) findViewById(R.id.listView);

        SelectResultAdapter adapter = new SelectResultAdapter(getActivity(), mListForDisplay);
        mListView.setAdapter(adapter);
        ListViewUtils.setListViewHeightBasedOnChildren(mListView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchKey = mListForDisplay.get(position).getQr();

                HashMap<String, String> result4Search = MapDataUtils.getResult4Search(mTableMap, mSearchKey, mStrArrayList4SearchAndUpdate);

                Intent                intent          = new Intent(getActivity(), QRResultActivity.class);
                Bundle                bundle          = new Bundle();
                final SerializableMap serializableMap = new SerializableMap();
                serializableMap.setMap(result4Search);
//                bundle.putBoolean(QR_RESULT_NOT_NULL, true);
                bundle.putSerializable(QR_RESULT_MAP, serializableMap);
                ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.add(MapDataUtils.mResultTableName);//MapDataUtils.mResultName 专为 精准查询准备
                bundle.putStringArrayList(QRCodeCaptureActivity.QR_RESULT_TABLENAME,arrayList);
                intent.putExtras(bundle);
                startActivity(intent, bundle);
            }
        });


    }

    @Override
    public void refreshData(boolean isAutoRefresh) {

    }


}
