package com.nokia.vlm.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.nokia.vlm.R;
import com.nokia.vlm.callback.CodeCaptureCallBack;
import com.nokia.vlm.dao.CodeCaptureEngine;
import com.nokia.vlm.entity.BaseAssetItemInfo;
import com.nokia.vlm.entity.CardAsset;
import com.nokia.vlm.entity.CodeCaptureInfo;
import com.nokia.vlm.entity.ComputerAsset;
import com.nokia.vlm.entity.MapListInfo;
import com.nokia.vlm.entity.PluggableAsset;
import com.nokia.vlm.entity.ShelfAsset;
import com.nokia.vlm.entity.TestEquipAsset;
import com.nokia.vlm.ui.adapter.AssetFragmentsAdapter;
import com.nokia.vlm.ui.fragment.HomeFragment;
import com.nokia.vlm.utils.PXUtils;
import com.nokia.vlm.view.ScrollableTabLayout;
import com.qx.framelib.activity.BaseTitle2Activity;
import com.qx.framelib.utlis.ZLog;
import com.qx.framelib.view.TitleBar2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @创建者 DK-house
 * @创建时间 2017/12/8 15:51
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/8
 * @更新描述 ${TODO}
 */

public class AssetActivity extends BaseTitle2Activity implements CodeCaptureCallBack {
    private ViewPager             mViewPager;
    private ScrollableTabLayout   mTabLayout;
    private TabLayout.Tab         mFrstTab;
    private TabLayout.Tab         mSecondTab;
    private TabLayout.Tab         mThirdTab;
    private AssetFragmentsAdapter mFragmentPagerAdapter;

    private CodeCaptureEngine codeCaptureEngine = new CodeCaptureEngine();
    private HashMap<String, List<HashMap<String, String>>> mListMap;
    private List<BaseAssetItemInfo>                        mListForDisplay;
    private MapListInfo                                    mTableMapInfo;
    public  HashMap<String, List<HashMap<String, String>>> mTableHashMap;
    public List<String> mKeyList = new ArrayList<>();
    private CodeCaptureInfo mCaptureInfo;
    private CodeCaptureInfo mCaptureInfoForDBSearch;
    private CodeCaptureInfo mCaptureInfoForAssetSearch;
    private boolean         mIsToSeachInDB;
    private boolean         mIsToSeachInAsset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_variety_assets);

        setStatusBarEnald(true);
        initView();
        initData();
        initTitle();
        initViewPager();
        initListener();
        initRequest();
        registers();
    }

    private void initData() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (null != bundle) {
            //            String          key         = bundle.getString("key");//ey代表传递过来的表名
            ArrayList<String> keyList = bundle.getStringArrayList(QRCodeCaptureActivity.QR_RESULT_TABLENAME);//key代表传递过来的表名
            String            key     = null;
            if (null != keyList && keyList.size() > 1) {
                mKeyList = keyList;
            } else if (null != keyList && keyList.size() == 1) {
                key = keyList.get(0);
            }
            mKeyList.add(key);
            mCaptureInfo = bundle.getParcelable(HomeFragment.ALL_ASSET);
            mIsToSeachInDB = bundle.getBoolean(HomeFragment.DATA_FROM_DB, false);
            mIsToSeachInAsset = bundle.getBoolean(HomeFragment.DATA_FROM_TARGET_ASSET, false);

            MapListInfo mapListInfo = (MapListInfo) bundle.getSerializable(QRCodeCaptureActivity.QR_RESULT_MAP_LIST);
            if (null != mCaptureInfo) {
                this.mTableHashMap = mCaptureInfo.getTableHashMap();
            }
            if (null != mapListInfo) {//优先多个结果的展示
                //TODO---key是所有表的名字,要用时进行解析
                mListMap = mapListInfo.getMapList();//ListMap用于Tab为各个表名的时候,根据表名展示符合当前搜索条件的各个表中的数据.
                if (mListMap.size() > 0) {
                    mListForDisplay = new ArrayList<>();
                    for (String tableName : QRResultActivity.tables) {
                        List<BaseAssetItemInfo> assetItemInfos = getValuesFromBundle(tableName, mListMap);
                        if (null != assetItemInfos && assetItemInfos.size() > 0) {
                            mListForDisplay.addAll(assetItemInfos);
                        }
                    }
                }
            } else if (null != mCaptureInfo && null != key) {
                //TODO---mListForDisplay
                //某个确定的表中的符合搜索条件的记录
                mListForDisplay = getValuesFromBundle(key, this.mTableHashMap);

            }


        }


    }


    private void parseResultWithParam(Map<String, List<HashMap<String, String>>> listMap, String[] params) {
        String[] keyArray = {};
        for (String tableName : QRResultActivity.tables) {
            //list中每个map就是一条记录
            List<HashMap<String, String>> dataList = listMap.get(tableName);

            switch (tableName) {
                case "TestEquip_Asset":
                    keyArray = TestEquipAsset.keyArray;
                    break;
                case "Card_Asset":
                    keyArray = CardAsset.keyArray;
                    break;
                case "Pluggable_Asset":
                    keyArray = PluggableAsset.keyArray;
                    break;
                case "Shelf_Asset":
                    keyArray = ShelfAsset.keyArray;
                    break;
                case "Computer_Asset":
                    keyArray = ComputerAsset.keyArray;
                    break;
            }

            for (HashMap<String, String> map : dataList) {

            }
        }
    }


    //根据key值决定用哪个keyArray来获取其中的各个value值
    private List<BaseAssetItemInfo> getValuesFromBundle(String key, HashMap<String, List<HashMap<String, String>>> tableHashMap) {
        List<HashMap<String, String>> table    = tableHashMap.get(key);
        String[]                      keyArray = {};
        switch (key) {
            case "TestEquip_Asset":
                keyArray = TestEquipAsset.keyArray;
                break;
            case "Card_Asset":
                keyArray = CardAsset.keyArray;
                break;
            case "Pluggable_Asset":
                keyArray = PluggableAsset.keyArray;
                break;
            case "Shelf_Asset":
                keyArray = ShelfAsset.keyArray;
                break;
            case "Computer_Asset":
                break;
        }

        //            "area",
        //            "description",
        //            "currentuser",
        //            "department",
        //              "workingcondition"
        return getListForDisplay(table, keyArray);
    }

    //三个tab分别为 all inUse inRepair 时,需要用的数据,返回的是all对应的Fragment需要展示的所有数据
    private List<BaseAssetItemInfo> getListForDisplay(List<HashMap<String, String>> mapList, String[] keyArray) {
        List<BaseAssetItemInfo> assetItemInfos = new ArrayList<>();
        if (null == mapList) {
            return null;
        }
        for (HashMap<String, String> map : mapList) {
            BaseAssetItemInfo info          = new BaseAssetItemInfo();
            boolean           hasSetArea    = false;
            boolean           hasSetDesc    = false;
            boolean           hasSetCUser   = false;
            boolean           hasSetDep     = false;
            boolean           hasSetWorking = false;
            boolean           hasQR         = false;
            for (int i = 0; i < keyArray.length; i++) {
                List<String> tempList = new ArrayList<>();
                tempList.addAll(map.keySet());
                for (String temp : tempList) {
                    if (keyArray[i].equals(temp)) {
                        switch (keyArray[i]) {
                            case "area":
                                info.setArea(map.get(keyArray[i]));
                                hasSetArea = true;
                                break;
                            case "description":
                                info.setDescription(map.get(keyArray[i]));
                                hasSetDesc = true;
                                break;
                            case "currentuser":
                                info.setCurrentuser(map.get(keyArray[i]));
                                hasSetCUser = true;
                                break;
                            case "department":
                                info.setDepartment(map.get(keyArray[i]));
                                hasSetDep = true;
                                break;
                            case "workingcondition":
                                info.setWorkingcondition(map.get(keyArray[i]));
                                hasSetWorking = true;
                                break;
                            case "qr":
                                info.setQr(map.get(keyArray[i]));
                                hasQR = true;
                                break;
                        }
                    }

                }
            }
            if (hasSetArea && hasSetDesc && hasSetCUser && hasSetDep && hasSetWorking && hasQR) {
                ZLog.d("mapinfo", map.toString());
                assetItemInfos.add(info);
            }
            hasQR = false;
            hasSetArea = false;
            hasSetDesc = false;
            hasSetCUser = false;
            hasSetDep = false;
            hasSetWorking = false;
        }
        return assetItemInfos;
    }

    private void initRequest() {

    }

    private void initListener() {

    }

    private void initView() {
        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (ScrollableTabLayout) findViewById(R.id.tabLayout);

        //        initViewPager();
    }

    private void initViewPager() {
        mFragmentPagerAdapter = new AssetFragmentsAdapter(getSupportFragmentManager(), mListForDisplay);
        mViewPager.setAdapter(mFragmentPagerAdapter);

        //将TabLayout与ViewPager绑定在一起
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        mFrstTab = mTabLayout.getTabAt(0);
        mSecondTab = mTabLayout.getTabAt(1);
        mThirdTab = mTabLayout.getTabAt(2);

        //设置Tab的图标
        mFrstTab.setIcon(R.drawable.tab_all);
        //        mSecondTab.setIcon(R.drawable.tab_inuse);
        mSecondTab.setText("In Service");
        //        mThirdTab.setIcon(R.drawable.tab_inrepair);
        mThirdTab.setText("In Repair");
    }

    private void registers() {
        codeCaptureEngine.register(this);
    }

    private void initTitle() {
        setTitleLayoutColor(getResources().getColor(R.color.color_nokia_blue));
        setTitleViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssetActivity.this, SearchBySelectionActivity.class);
                Bundle bundle = new Bundle();
                if (null != mCaptureInfo) {
                    bundle.putParcelable(HomeFragment.ALL_ASSET,mCaptureInfo);
                    if (mIsToSeachInDB) {
                        bundle.putBoolean(HomeFragment.DATA_FROM_DB, true);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (mIsToSeachInAsset) {
                        bundle.putBoolean(HomeFragment.DATA_FROM_TARGET_ASSET, true);
                        ArrayList<String> arrayList = new ArrayList<>();
                        arrayList.addAll(mKeyList);
                        bundle.putStringArrayList(QRCodeCaptureActivity.QR_RESULT_TABLENAME, arrayList);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }

            }
        });
        setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSyncAll();
                startActivity(QRCodeCaptureActivity.class);
            }
        });

        setLeftDrawble(null, TitleBar2.DrawableIndex.LEFT);
        setLeftBackgroudResource(R.drawable.back_white, PXUtils.dip2px(AssetActivity.this, 14), PXUtils.dip2px(AssetActivity.this, 26));
        setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitleBackgroundResource(R.drawable.icon_search, PXUtils.dip2px(AssetActivity.this, 30));
        setRightBackgroudResource(R.drawable.icon_home_top_scan, PXUtils.dip2px(AssetActivity.this, 25), PXUtils.dip2px(AssetActivity.this, 25));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisters();
    }

    private void unregisters() {

    }

    @Override
    public void codeCaptureMsg(int code, String msg, CodeCaptureInfo responseJson) {
        if (codeCaptureEngine != null) {
            codeCaptureEngine.unregister(this);
        }
    }

    private void requestSyncAll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                codeCaptureEngine.getCodeCapture();//请求syncall接口
            }
        }).start();
    }
}
