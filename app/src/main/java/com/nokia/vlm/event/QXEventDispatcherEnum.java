package com.nokia.vlm.event;


import com.qx.framelib.event.EventDispatcherEnum;

public class QXEventDispatcherEnum extends EventDispatcherEnum {

    private static final int UI_EVENT_BEGIN = 1002;

    public static final int UI_EVENT_TOKEN_NO_USE = UI_EVENT_BEGIN + 1;

    //推送消息
    public static final int UI_EVENT_JPUSH_MESSAGE = UI_EVENT_TOKEN_NO_USE + 1;


    //接受到消息
    public static final int UI_EVENT_PUSH_MESSAGE = UI_EVENT_JPUSH_MESSAGE + 1;
    //消息读完了
    public static final int UI_EVENT_READ_END_MESSAGE = UI_EVENT_PUSH_MESSAGE + 1;

    //刷新消息
    public static final int UI_EVENT_RESHFER_MESSAGE = UI_EVENT_READ_END_MESSAGE + 1;

    public static final int UI_EVENT_SELF_UPDATE_DOWNLOAD_ING = UI_EVENT_RESHFER_MESSAGE + 1;
    public static final int UI_EVENT_SELF_UPDATE_DOWNLOAD_START = UI_EVENT_SELF_UPDATE_DOWNLOAD_ING + 1;
    public static final int UI_EVENT_SELF_UPDATE_DOWNLOAD_END = UI_EVENT_SELF_UPDATE_DOWNLOAD_START + 1;

    //添加新的朋友
    public static final int UI_EVENT_ADD_NEW_FRIEND = UI_EVENT_SELF_UPDATE_DOWNLOAD_END + 1;

    //添加新的朋友完成
    public static final int UI_EVENT_ADD_NEW_FRIEND_END = UI_EVENT_ADD_NEW_FRIEND + 1;

    //融云 成功
    public static final int UI_EVENT_RONGYUN_SUCCESS = UI_EVENT_ADD_NEW_FRIEND_END + 1;
    //融云token 失效
    public static final int UI_EVENT_RONGYUN_INCORRECT = UI_EVENT_RONGYUN_SUCCESS + 1;
    //融云 异常
    public static final int UI_EVENT_RONGYUN_ERROR = UI_EVENT_RONGYUN_INCORRECT + 1;
    //余额更改
    public static final int UI_EVENT_MOENY_CHANGE = UI_EVENT_RONGYUN_ERROR + 1;
    //微信支付成功
    public static final int UI_EVENT_WX_PAY_FINISH = UI_EVENT_MOENY_CHANGE + 1;
    //支付宝支付结束
    public static final int UI_EVENT_ALIPAY_FINISH = UI_EVENT_WX_PAY_FINISH + 1;

    //拍卖
    public static final int UI_EVENT_GO_PAIMAI = UI_EVENT_ALIPAY_FINISH + 1;

    //部落
    public static final int UI_EVENT_GO_BULUO = UI_EVENT_GO_PAIMAI + 1;
    //我的部落
    public static final int UI_EVENT_GO_MY_BULUO = UI_EVENT_GO_BULUO + 1;

    public static final int UI_EVENT_CREAT_AUCTION_FINISH = UI_EVENT_GO_MY_BULUO + 1;

    public static final int UI_EVENT_REG_FINISH = UI_EVENT_CREAT_AUCTION_FINISH + 1;

    //退出部落
    public static final int UI_EVENT_EXIT_TRIBE = UI_EVENT_REG_FINISH + 1;


}
