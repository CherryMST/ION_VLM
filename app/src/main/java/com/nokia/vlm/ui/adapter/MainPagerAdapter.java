package com.nokia.vlm.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nokia.vlm.R;
import com.nokia.vlm.ui.QXApp;
import com.nokia.vlm.ui.fragment.AddFragment;
import com.nokia.vlm.ui.fragment.HomeFragment;
import com.nokia.vlm.ui.fragment.MyFragment;
import com.qx.framelib.utlis.ZLog;
import com.qx.framelib.view.MainTabItem;

import java.lang.ref.SoftReference;


/**
 * Created by ZhaoWei on 2016/7/25.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private SparseArray<SoftReference<Fragment>> viewCache = new SparseArray<SoftReference<Fragment>>();


    private String[] titles = {"0", "1", "2"};
    private int[] res = {R.drawable.main_tab_home_selector, R.drawable.tab_add,  R.drawable.main_tab_mine_selector};

    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ZLog.d("MainPageAdapter", "instantiateItem POS: " + position);
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ZLog.d("MainPageAdapter", "destroyItem POS: " + position);
//        super.destroyItem(container, position, object);
    }

    private Fragment getViewByType(int type) {
        switch (type) {
            case 0: //首页
                return new HomeFragment();
            case 1: //Add操作
                return new AddFragment();
            case 2: //我的
                return new MyFragment();
            default:
                return null;
        }
    }

    @Override
    public Fragment getItem(int position) {
        SoftReference<Fragment> viewWeakReference = viewCache.get(position);
        Fragment view = null;
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
        return titles.length;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

//    public CharSequence getPageTitle(int position) {
//        return super.getPageTitle(position);
//    }

    public View getPageTab(int position) {
        MainTabItem tab = new MainTabItem(QXApp.getAppSelf());
//        tab.setText(titles[position]);
        tab.getTab_txt().setVisibility(View.GONE);
        tab.setIcon(res[position]);
        tab.setOrientation(LinearLayout.VERTICAL);
        return tab;
    }

}