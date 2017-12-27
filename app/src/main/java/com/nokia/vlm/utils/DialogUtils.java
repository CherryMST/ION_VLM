package com.nokia.vlm.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;

import com.qx.framelib.activity.BaseActivity;
import com.qx.framelib.utlis.TextUtil;
import com.nokia.vlm.manager.UserInfoManager;
import com.nokia.vlm.ui.QXApp;
import com.nokia.vlm.ui.ZooerConstants;
import com.nokia.vlm.view.OneBtnDialog;
import com.nokia.vlm.view.TwoBtnDialog;
import com.nokia.vlm.view.TwoBtnEditDialog;


/**
 * dialog 工具
 */
public class DialogUtils {

    private static final float COMMON_DIALOG_WIDTH_PERCENT = 0.867f;

    /***
     * 显示统一的两个button的dialog
     *
     * @param dialogInfo
     */
    public static void show2BtnDialog(final ZooerConstants.TwoBtnDialogInfo dialogInfo) {
        Activity context = QXApp.getCurrentActivity();
        if (context == null || context.isFinishing()) {
            return;
        }

        final TwoBtnDialog dialog = new TwoBtnDialog(context);
        dialog.setCancelable(true);
        if (dialogInfo.isOnTouchCancal) {
            dialog.setCanceledOnTouchOutside(true);
        } else {
            dialog.setCanceledOnTouchOutside(false);
        }


        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                dialogInfo.onCancel();
            }

        });

        if (!dialogInfo.hasTitle) { //是否显示标题
            dialog.noTitle();
        }

        dialog.setTitle(dialogInfo.titleRes); //设置标题
        dialog.setContentMsg(dialogInfo.contentRes); //设置内容
        dialog.setLeftBtnMsg(dialogInfo.lBtnTxtRes); //左按钮
        dialog.setRightBtnMsg(dialogInfo.rBtnTxtRes); //右按钮
        if (dialogInfo.rBtnTextColorResId != 0) //右按钮颜色
            dialog.setRightBtnMsgColor(dialogInfo.rBtnTextColorResId);
        if (dialogInfo.lBtnTextColorResId != 0) //左按钮颜色
            dialog.setLeftBtnMsgColor(dialogInfo.lBtnTextColorResId);

        dialog.setOnClickListener(new TwoBtnDialog.OnClickListener() {
            @Override
            public void onLeftClick() {
                if (dialogInfo != null) {

                    if (dialogInfo.isClickDismiss) {
                        dialog.dismiss();
                    }

                    dialogInfo.onLeftBtnClick();
                }
            }

            @Override
            public void onRightClick() {
                if (dialogInfo != null) {

                    if (dialogInfo.isClickDismiss) {
                        dialog.dismiss();
                    }

                    dialogInfo.onRightBtnClick();
                }
            }
        });

//        TwoButtonDialogView dialogView = new TwoButtonDialogView(context);
//        dialogView.setHasTitle(dialogInfo.hasTitle);
//        dialogView.setTitleAndMsg(dialogInfo.titleRes, dialogInfo.contentRes);
//        if (dialogInfo.extraMsgView != null) {
//            dialogView.setMsgVisible(View.GONE);
//            dialogView.addExtraMsgView(dialogInfo.extraMsgView);
//        }
//        View.OnClickListener leftClickListenr = new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (dialogInfo != null) {
//                    try {
//
//                        if (dialogInfo.isClickDismiss) {
//                            dialog.dismiss();
//                        }
//                        dialogInfo.onLeftBtnClick();
//                    } catch (Throwable e) {
//
//                    }
//                }
//            }
//        };
//        View.OnClickListener rightClickListenr = new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (dialogInfo != null) {
//                    if (dialogInfo.isClickDismiss) {
//                        dialog.dismiss();
//                    }
//                    dialogInfo.onRightBtnClick();
//                }
//            }
//        };
//        dialogView.setButton(dialogInfo.lBtnTxtRes,
//                dialogInfo.rBtnTxtRes, leftClickListenr, rightClickListenr);
//
//        if (dialogInfo.rBtnTextColorResId != 0 && dialogInfo.rBtnBackgroundResId != 0) {
//            dialogView.setButtonStyle(false, dialogInfo.rBtnTextColorResId, dialogInfo.rBtnBackgroundResId);
//        }
//
//        dialog.addContentView(dialogView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        dialog.setOwnerActivity(context);
//
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        android.view.WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        Display d = window.getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
        params.width = (int) (d.getWidth() * COMMON_DIALOG_WIDTH_PERCENT); // 宽度设置为屏幕百分比
        window.setAttributes(params); // 设置生效
//
        if (!context.isFinishing()) {
            dialog.show();
            if (dialogInfo.blockCaller == false && context instanceof BaseActivity) {
                ((BaseActivity) context).setDialog(dialog);
            }
        }
    }


    public static void showEdit2BtnDialog(final ZooerConstants.TwoBtnDialogInfo2 dialogInfo) {
        Activity context = QXApp.getCurrentActivity();
        if (context == null || context.isFinishing()) {
            return;
        }

        final TwoBtnEditDialog dialog = new TwoBtnEditDialog(context);
        dialog.setCancelable(true);
        if (dialogInfo.isOnTouchCancal) {
            dialog.setCanceledOnTouchOutside(true);
        } else {
            dialog.setCanceledOnTouchOutside(false);
        }


        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                dialogInfo.onCancell();
            }

        });

        if (!dialogInfo.hasTitle) { //是否显示标题
            dialog.noTitle();
        }

        dialog.setTitle(dialogInfo.titleRes); //设置标题
        dialog.setContentMsg(dialogInfo.contentRes); //设置内容
        dialog.setLeftBtnMsg(dialogInfo.lBtnTxtRes); //左按钮
        dialog.setRightBtnMsg(dialogInfo.rBtnTxtRes); //右按钮
        if (dialogInfo.rBtnTextColorResId != 0) //右按钮颜色
            dialog.setRightBtnMsgColor(dialogInfo.rBtnTextColorResId);

        dialog.setOnClickListener(new TwoBtnEditDialog.OnClickListener() {
            @Override
            public void onLeftClick() {
                if (dialogInfo != null) {

                    if (dialogInfo.isClickDismiss) {
                        dialog.dismiss();
                    }

                    dialogInfo.onLeftBtnClick();
                }
            }

            @Override
            public void onRightClick() {
                if (dialogInfo != null) {

                    if (dialogInfo.isClickDismiss) {
                        dialog.dismiss();
                    }
                    if (!TextUtil.isEmpty(dialog.editText.getText().toString().trim())) {
                        dialogInfo.onRightBtnClick(dialog.editText.getText().toString().trim());
                    }

                }
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        android.view.WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        Display d = window.getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
        params.width = (int) (d.getWidth() * COMMON_DIALOG_WIDTH_PERCENT); // 宽度设置为屏幕百分比
        window.setAttributes(params); // 设置生效

        if (!context.isFinishing()) {
            dialog.show();
            if (dialogInfo.blockCaller == false && context instanceof BaseActivity) {
                ((BaseActivity) context).setDialog(dialog);
            }
        }
    }


    public static void showOneBtnDialog(final ZooerConstants.OneBtnDialogInfo dialogInfo) {
        Activity context = QXApp.getCurrentActivity();
        if (context == null || context.isFinishing()) {
            return;
        }

        final OneBtnDialog dialog = new OneBtnDialog(context);
        dialog.setCancelable(true);
        if (dialogInfo.isOnTouchCancal) {
            dialog.setCanceledOnTouchOutside(true);
        } else {
            dialog.setCanceledOnTouchOutside(false);
        }


        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                dialogInfo.onCancell();
            }

        });

        if (!dialogInfo.hasTitle) { //是否显示标题
            dialog.noTitle();
        }

        dialog.setTitle(dialogInfo.titleRes); //设置标题
        dialog.setContentMsg(dialogInfo.contentRes); //设置内容
        dialog.setLeftBtnMsg(dialogInfo.btnTxtRes); //左按钮
        dialog.setOnClickListener(new OneBtnDialog.OnClickListener() {
            @Override
            public void onLeftClick() {
                dialog.dismiss();
                dialogInfo.onBtnClick();
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        android.view.WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        Display d = window.getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
        params.width = (int) (d.getWidth() * COMMON_DIALOG_WIDTH_PERCENT); // 宽度设置为屏幕百分比
        window.setAttributes(params); // 设置生效

        if (!context.isFinishing()) {
            dialog.show();
            if (dialogInfo.blockCaller == false && context instanceof BaseActivity) {
                ((BaseActivity) context).setDialog(dialog);
            }
        }
    }


    /***
     * 显示统一的两个button的dialog
     *
     * @param dialogInfo
     */
    public static void showDialog(final ZooerConstants.TwoBtnDialogInfo dialogInfo, FragmentManager fm) {
        Activity context = QXApp.getCurrentActivity();
        if (context == null || context.isFinishing()) {
            return;
        }
        final com.nokia.vlm.dialog.TwoBtnDialog dialog = new com.nokia.vlm.dialog.TwoBtnDialog();
        //点击返回按钮是否关闭
        dialog.setCancelable(dialogInfo.isBackDismiss);

        dialog.setDialogInfo(dialogInfo);
        if (!context.isFinishing()) {
            dialog.show(fm, String.valueOf(context.hashCode()));
        }
    }

    /**
     * 退出登录
     *
     * @param dialogInfo
     * @param fm
     */
    public static void loginout(ZooerConstants.TwoBtnDialogInfo dialogInfo, FragmentManager fm) {
        dialogInfo.titleRes = "提示";
        dialogInfo.contentRes = "是否退出?";
        dialogInfo.lBtnTxtRes = "取消";
        dialogInfo.rBtnTxtRes = "退出";
        showDialog(dialogInfo, fm);
    }

    public static void nopaypwd(ZooerConstants.TwoBtnDialogInfo dialogInfo, FragmentManager fm) {
        dialogInfo.titleRes = "提示";
        dialogInfo.contentRes = "未设置支付密码";
        dialogInfo.rBtnTxtRes = "设置";
        dialogInfo.rBtnTextColorResId = Color.parseColor("#fe4040");
        dialogInfo.isOnlyOneBtn = true;
        showDialog(dialogInfo, fm);
    }


    /**
     * 访客vip 查看
     *
     * @param dialogInfo
     * @param fm
     */
    public static void vistorToast(ZooerConstants.TwoBtnDialogInfo dialogInfo, FragmentManager fm) {
        dialogInfo.titleRes = "提示";
        dialogInfo.contentRes = "只有VIP才能查看访客记录";
        dialogInfo.lBtnTxtRes = UserInfoManager.getInstance().getUserInfo().sex == 1 ? "朕知道了" : "本宫知道了";
        dialogInfo.rBtnTxtRes = "成为会员";
        dialogInfo.rBtnTextColorResId = Color.parseColor("#04a5e9");
        dialogInfo.isBackDismiss = false;
        dialogInfo.isOnTouchCancal = false;
        showDialog(dialogInfo, fm);
    }

    public static void clearMessage(ZooerConstants.TwoBtnDialogInfo dialogInfo, FragmentManager fm) {
        dialogInfo.titleRes = "提示";
        dialogInfo.contentRes = "是否清空消息?";
        dialogInfo.lBtnTxtRes = "取消";
        dialogInfo.rBtnTxtRes = "清空";
        showDialog(dialogInfo, fm);
    }

}
