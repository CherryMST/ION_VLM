package com.nokia.vlm.utils;

import android.content.Context;
import android.content.Intent;

import com.nokia.vlm.entity.SelfUpdateInfo;
import com.nokia.vlm.ui.QXApp;
import com.nokia.vlm.ui.ZooerConstants;
import com.nokia.vlm.update.UpdateService;
import com.qx.framelib.utlis.TextUtil;
import com.qx.framelib.utlis.ZLog;


public class SelfUpdateDialogUtils {

    public static final int SELF_UPDATE_COMMON = 1; //普通升级
    public static final int SELF_UPDATE_FORCE = 2; //强制升级

    private static int current_update_type = 1;


    /**
     * 普通升级 俩按钮
     *
     * @param info
     */
    public static void commonUpdate(final Context context, final SelfUpdateInfo info) {


        ZLog.d("SelfUpdateDialogUtils", "commonUpdate Dialog ");
        current_update_type = SELF_UPDATE_COMMON;
        ZooerConstants.TwoBtnDialogInfo dialog = new ZooerConstants.TwoBtnDialogInfo() {
            @Override
            public void onLeftBtnClick() {

                if (info != null) {
                    if (!TextUtil.isEmpty(info.down_url)) {
                        startService(info.down_url);
                    }
                }
            }

            @Override
            public void onRightBtnClick() {
            }

            @Override
            public void onCancel() {
            }
        };
        dialog.titleRes = "提示";
        if (!TextUtil.isEmpty(info.desc)) {
            dialog.contentRes = info.desc;
        } else {
            dialog.contentRes = "有新的版本更新";
        }
        dialog.lBtnTxtRes = "升级";
        dialog.rBtnTxtRes = "取消";
        dialog.isOnTouchCancal = false;
        DialogUtils.show2BtnDialog(dialog);

    }


    private static void startService(String url) {
        Intent intent = new Intent(QXApp.getAppSelf(), UpdateService.class);
        intent.putExtra("url", url);
        intent.putExtra("current_update_type", current_update_type);
        QXApp.getAppSelf().startService(intent);
    }


}
