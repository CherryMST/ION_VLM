package com.nokia.vlm.ui;

import android.content.DialogInterface;
import android.view.View;

public interface ZooerConstants {


    /**
     * 登录的身份类型： 0 没有身份 , 1 手Q, 2 微信, 3 微信跳转code
     */
    public static enum IdentityType {
        NONE, MOBILEQ, WX, WXCODE
    }

    // 对话框标题与内容信息的封装
    abstract class  DialogInfo {
        public String titleRes; // 对话框标题
        public String contentRes; // 对话框内容

        public boolean blockCaller; // true 模态对话框 false 非模态对话框, 即是否要阻塞调用者所在线程
        public boolean isOnlyOneBtn;
    }

    // 两个按钮对话框信息的封装
    abstract class TwoBtnDialogInfo extends DialogInfo {
        public String lBtnTxtRes; // 对话框左按钮文案
        public String rBtnTxtRes; // 对话框右按钮文案
        public boolean hasTitle = true;// 是否有title，默认有
        //右边按钮的样式和文字颜色可能修改
        public int rBtnTextColorResId;
        public int rBtnBackgroundResId;

        public int lBtnTextColorResId;

        public boolean isClickDismiss = true; //点击是否关闭 ，默认关闭

        public View extraMsgView = null;
        public boolean isOnTouchCancal = true; //点击是否关闭
        public boolean isBackDismiss = true;

        public abstract void onLeftBtnClick();

        public abstract void onRightBtnClick();

        public abstract void onCancel();
    }

    // 带edittext的两个按钮对话框信息的封装
    abstract class TwoBtnDialogInfo2 extends DialogInfo {
        public String lBtnTxtRes; // 对话框左按钮文案
        public String rBtnTxtRes; // 对话框右按钮文案
        public boolean hasTitle = true;// 是否有title，默认有
        //右边按钮的样式和文字颜色可能修改
        public int rBtnTextColorResId;
        public int rBtnBackgroundResId;

        public boolean isClickDismiss = true; //点击是否关闭 ，默认关闭

        public View extraMsgView = null;
        public boolean isOnTouchCancal = true; //点击是否关闭

        public abstract void onLeftBtnClick();

        public abstract void onRightBtnClick(String text);

        public abstract void onCancell();
    }

    abstract class ListDialogInfo extends DialogInfo {
        public boolean hasTitle = true;// 是否有title，默认有
        public CharSequence[] items;
        public int pos;//list dialog打开后定位到第几个

        public abstract void onClick(DialogInterface dialog, int position);
    }

    // 一个按钮对话框信息的封装
    abstract class OneBtnDialogInfo extends DialogInfo {
        public boolean hasTitle = true;// 是否有title，默认有

        public String btnTxtRes;

        public int rBtnTextColorResId;
        public int rBtnBackgroundResId;
        public boolean isOnTouchCancal = true; //点击是否关闭

        public abstract void onBtnClick();

        public abstract void onCancell();
    }

    public static class ApiPath {
        //我的积分
        public static final String GET_RECORD_LIST = "user/user/my_score";
        //确认收货
        public static final String CONFIRM_RECEI = "user/user/order_confirm";
        //订单详情
        public static final String GET_ORDER_DETAILS = "user/user/order_info";
        //我的订单
        public static final String GET_ORDER_LIST = "user/user/my_orders";
        //支付后跳转界面
        public static final String GET_ORDER_INFO = "user/order/order_info";
        //创建订单
        public static final String CREATE_ORDER = "user/order/create_order";
        //我的收藏
        public static final String GET_MY_COLLECTION = "user/user/my_coll";
        //设为默认地址
        public static final String SET_DEFAULT_ADDRESS = "user/address/set_default";
        //删除收货地址
        public static final String DEL_ADDRESS = "user/address/delete";
        //添加收货地址
        public static final String ADD_ADDRESS = "user/address/add";
        //获取地区省份数据
        public static final String GET_PROVINCE_LIST_PATH = "public/area/getProvinceList";
        //获取父级地区数据
        public static final String GET_AREA_BY_PARENT_PATH = "public/area/getAreasByParent";
        //获取下级地区数据
        public static final String GET_REGION = "public/area/getAreasByParent";
        //收货地址列表
        public static final String GET_ADDRESS_LIST = "user/address";
        //错误返回
        public static final int ERROR_CODE = 10001;
    }


}
