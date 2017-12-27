package com.nokia.vlm.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.qx.framelib.activity.BaseActivity;
import com.qx.framelib.utlis.HandlerUtils;
import com.qx.framelib.utlis.TextUtil;
import com.qx.framelib.utlis.ZLog;
import com.nokia.vlm.R;
import com.nokia.vlm.manager.SettingManager;
import com.nokia.vlm.manager.UserInfoManager;


/**
 * 欢迎页
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isFirstLaunch = SettingManager.getInstance().IsFirstLaunch();
        if (isFirstLaunch) {
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
            finish();
            return;
        }

        Drawable drawable = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.splash));
        getWindow().setBackgroundDrawable(drawable);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (!NetWorkUtil.isNetworkActive()) {
//            showNetWorkDialog();
//            return;
//        }
        jumpNextActivity();
    }

    private void jumpNextActivity() {
        HandlerUtils.getMainZooerHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!TextUtil.isEmpty(UserInfoManager.getInstance().getToken())) {
                    ZLog.d("UserInfoMananger", "getToken非空");
                    if (UserInfoManager.getInstance().getUserInfo().status == 0) {
                        //
                    } else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        //Intent intent = new Intent(LoginActivity.this, RecommendUserActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    ZLog.d("UserInfoMananger", "getUserInfo.status :" + UserInfoManager.getInstance().getUserInfo().status);
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }

            }
        }, 2 * 1000);


    }


}
