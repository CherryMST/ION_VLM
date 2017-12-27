package com.nokia.vlm.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.nokia.vlm.entity.BaseAssetItemInfo;
import com.nokia.vlm.ui.fragment.FirstDisplayFragment;
import com.nokia.vlm.ui.fragment.SecondDisplayFragment;
import com.nokia.vlm.ui.fragment.ThirdDisplayFragment;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/14 10:28
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/14
 * @更新描述 ${TODO}
 */

public class AssetFragmentsAdapter extends FragmentPagerAdapter {
    String[] params = new String[]{"In Service", "In Repair"};
    private List<BaseAssetItemInfo> mLisForDisplay;
    private SparseArray<SoftReference<Fragment>> viewCache = new SparseArray<SoftReference<Fragment>>();
    private List<List<BaseAssetItemInfo>> mListForTabs;

    public AssetFragmentsAdapter(FragmentManager fm) {
        super(fm);
    }

    public AssetFragmentsAdapter(FragmentManager fm, List<BaseAssetItemInfo> listForDisplay) {
        super(fm);
        mLisForDisplay = listForDisplay;
        mListForTabs = parseResultWithParamByList(listForDisplay, params);
    }

    @Override
    public Fragment getItem(int position) {
        /*if (position == 1) {
            return new SecondDisplayFragment();
        } else if (position == 2) {
            return new ThirdDisplayFragment();
        }
        return new FirstDisplayFragment();*/

        SoftReference<Fragment> viewWeakReference = viewCache.get(position);
        Fragment                view              = null;
        if (viewWeakReference != null && viewWeakReference.get() != null) {
            view = viewWeakReference.get();
        }
        if (view == null) {
            view = getViewByType(position);
            viewCache.put(position, new SoftReference<Fragment>(view));
        }
        return view;
    }

    @Override
    public int getCount() {
        return params.length + 1;
    }

    private Fragment getViewByType(int type) {
        Bundle bundle = new Bundle();
        switch (type) {
            case 0: //first tab
                ArrayList<BaseAssetItemInfo> baseAssetItemInfos = new ArrayList<>();
                FirstDisplayFragment firstDisplayFragment;
                if (null != mLisForDisplay && mLisForDisplay.size() > 0) {
                    baseAssetItemInfos.addAll(mLisForDisplay);
                    firstDisplayFragment = new FirstDisplayFragment();
                    bundle.putParcelableArrayList("ListForDisplay", baseAssetItemInfos);
                    firstDisplayFragment.setArguments(bundle);
                } else {
                    firstDisplayFragment = new FirstDisplayFragment();
                }
                return firstDisplayFragment;
            case 1: //second tab
                ArrayList<BaseAssetItemInfo> infos1 = new ArrayList<>();
                SecondDisplayFragment secondDisplayFragment;
                if (null != mListForTabs && mListForTabs.size() > 0) {
                    infos1.addAll(mListForTabs.get(0));
                    secondDisplayFragment = new SecondDisplayFragment();
                    bundle.putParcelableArrayList("ListForDisplay", infos1);
                    secondDisplayFragment.setArguments(bundle);
                } else {
                    secondDisplayFragment = new SecondDisplayFragment();
                }
                return secondDisplayFragment;

            case 2: //third tab
                ArrayList<BaseAssetItemInfo> infos2 = new ArrayList<>();
                ThirdDisplayFragment thirdDisplayFragment;
                if (null != mListForTabs && mListForTabs.size() > 0) {
                    infos2.addAll(mListForTabs.get(1));
                    thirdDisplayFragment = new ThirdDisplayFragment();
                    bundle.putParcelableArrayList("ListForDisplay", infos2);
                    thirdDisplayFragment.setArguments(bundle);
                } else {
                    thirdDisplayFragment = new ThirdDisplayFragment();
                }
                return thirdDisplayFragment;
            default:
                return new FirstDisplayFragment();
        }

    }


    private List<List<BaseAssetItemInfo>> parseResultWithParamByList(List<BaseAssetItemInfo> listForDisplay, String[] params) {
        List<List<BaseAssetItemInfo>> lists = new ArrayList<>(params.length);
        for (int j = 0; j < params.length; j++) {
            String                  param          = params[j];
            List<BaseAssetItemInfo> assetItemInfos = new ArrayList<>();
            if (null != listForDisplay && listForDisplay.size() > 0) {
                for (int i = 0; i < listForDisplay.size(); i++) {
                    BaseAssetItemInfo info = listForDisplay.get(i);
                    if (param.equals(info.getWorkingcondition())) {
                        assetItemInfos.add(info);
                    }
                }

                lists.add(j, assetItemInfos);
            }
        }
        return lists;
    }
}
