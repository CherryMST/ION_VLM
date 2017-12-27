package com.qx.framelib.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.qx.framelib.utlis.ViewUtils;
import com.qx.framelib.utlis.ZLog;

/**
 * Created by ZhaoWei on 2016/9/9.
 * 解决 滑动关闭 再次显示只有背景
 */

public class WrapBottomSheetDialog extends BottomSheetDialog {
    public WrapBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public WrapBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
    }

    protected WrapBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onStart() {
        super.onStart();

        ZLog.d("BottomSheetDialog", "onStart");

        FrameLayout bottomSheet = (FrameLayout) findViewById(android.support.design.R.id.design_bottom_sheet);
        BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);

    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int screenHeight = ViewUtils.getScreenHeight();
        int statusBarHeight = ViewUtils.getStatusBarHeight();
        int dialogHeight = screenHeight - statusBarHeight;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
    }

}
