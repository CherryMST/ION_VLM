package com.nokia.vlm.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.qx.framelib.activity.BaseActivity;
import com.nokia.vlm.R;
import com.nokia.vlm.manager.SettingManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by DingCuiLin on 2016/6/13.
 */

public class GuideActivity extends BaseActivity {

    private List<View> list = new ArrayList<>();
    private ViewPager viewPager;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewPager = new ViewPager(this);
        viewPager.setBackgroundColor(Color.parseColor("#9fca3c"));
        super.setContentView(viewPager);

        initView();

        viewPager.setOnTouchListener(new View.OnTouchListener() {

            private float downX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (viewPager.getCurrentItem() == list.size() - 1) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        downX = event.getX();
                    }
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    float moveX = event.getX();
                    if (downX - moveX > 10 && isFirst) {
//                        goSlpash();
                    }
                }
                return false;
            }
        });

    }

    private synchronized void goSlpash() {
        isFirst = false;
        startActivity(new Intent(GuideActivity.this, SplashActivity.class));
        SettingManager.getInstance().setIsFirstLaunch(false);
        finish();
    }

    private void initView() {
        View view1 = new View(this);
        view1.setBackgroundResource(R.drawable.guide_one);
        View view2 = new View(this);
        view2.setBackgroundResource(R.drawable.guide_two);
        View view3 = new View(this);
        view3.setBackgroundResource(R.drawable.guide_three);
        View view4 = new View(this);
        view4.setBackgroundResource(R.drawable.guide_four);
        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSlpash();
            }
        });


        list.add(view1);
        list.add(view2);
        list.add(view3);
        list.add(view4);

        viewPager.setAdapter(new MyAdapter());
    }

    class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView(list.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));//添加页卡
            return list.get(position);
        }
    }
}
