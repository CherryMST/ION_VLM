package com.nokia.vlm.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nokia.vlm.R;
import com.nokia.vlm.entity.LeftMenuInfo;
import com.nokia.vlm.entity.UserInfo;
import com.nokia.vlm.event.QXEventDispatcherEnum;
import com.nokia.vlm.manager.UserInfoManager;
import com.nokia.vlm.ui.QXApp;

/**
 * Created by LiShang on 2017/4/24.
 * 主菜单
 */

public class MainMeunView extends LinearLayout implements View.OnClickListener {


    protected ImageView leftV;
    protected TextView tvName;
    protected TextView tvFriends;
    protected TextView tvVisitors;
    protected TextView tvBanlece;
    protected ImageView imgNewFriend;
    private Context mContext;

    private DrawerLayout drawerLayout;
    private ImageView left_back;
    //private ImageView left_msg;
    private ImageView left_head;
    //private ImageView left_msg_point;
    private RelativeLayout tab1;
    private LinearLayout tab2;
    private LinearLayout tab3;
    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;
    private LinearLayout ll4;
    private LinearLayout ll5;
    private LinearLayout ll6;
    private LinearLayout ll7;
    private LinearLayout ll8;
    private LinearLayout ll9;
    private LinearLayout ll10;
    private LinearLayout ll11;
    private TextView orderNum;

    public MainMeunView(Context context) {
        this(context, null);
    }

    public MainMeunView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainMeunView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate(mContext, R.layout.menu_left_layout, this);
        initView();
        setOrientation(VERTICAL);
        setBackgroundColor(Color.WHITE);
    }

    @SuppressLint("WrongConstant")
    private void initView() {
        leftV = (ImageView) findViewById(R.id.left_v);
        left_back = (ImageView) findViewById(R.id.left_back);
        left_back.setOnClickListener(this);
        //left_msg = (ImageView) findViewById(R.id.left_message);
        //left_msg.setOnClickListener(this);
        left_head = (ImageView) findViewById(R.id.left_head);
        left_head.setOnClickListener(this);

        tab1 = (RelativeLayout) findViewById(R.id.left_tab1);
        tab1.setOnClickListener(this);
        imgNewFriend = (ImageView) findViewById(R.id.img_new_friend);

        tab2 = (LinearLayout) findViewById(R.id.left_tab2);
        tab2.setOnClickListener(this);
        tab3 = (LinearLayout) findViewById(R.id.left_tab3);
        tab3.setOnClickListener(this);
        ll1 = (LinearLayout) findViewById(R.id.left_item_ll1);
        ll1.setOnClickListener(this);
        ll2 = (LinearLayout) findViewById(R.id.left_item_ll2);
        ll2.setOnClickListener(this);
        ll3 = (LinearLayout) findViewById(R.id.left_item_ll3);
        ll3.setOnClickListener(this);
        ll4 = (LinearLayout) findViewById(R.id.left_item_ll4);
        ll4.setOnClickListener(this);
        ll5 = (LinearLayout) findViewById(R.id.left_item_ll5);
        ll5.setOnClickListener(this);
        ll6 = (LinearLayout) findViewById(R.id.left_item_ll6);
        ll6.setOnClickListener(this);
        ll7 = (LinearLayout) findViewById(R.id.left_item_ll7);
        ll7.setOnClickListener(this);
        ll8 = (LinearLayout) findViewById(R.id.left_item_ll8);
        ll8.setOnClickListener(this);
        ll9 = (LinearLayout) findViewById(R.id.left_item_ll9);
        ll9.setOnClickListener(this);
        ll10 = (LinearLayout) findViewById(R.id.left_item_ll10);
        ll10.setOnClickListener(this);
        ll11 = (LinearLayout) findViewById(R.id.left_item_ll11);
        ll11.setOnClickListener(this);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvFriends = (TextView) findViewById(R.id.tv_friends);
        tvVisitors = (TextView) findViewById(R.id.tv_visitors);
        tvBanlece = (TextView) findViewById(R.id.tv_banlece);
        orderNum = (TextView) findViewById(R.id.left_item_order_num);
        //left_msg_point = (ImageView) findViewById(R.id.left_message_point);


        UserInfo info = UserInfoManager.getInstance().getUserInfo();

        leftV.setVisibility(info.is_vip ? VISIBLE : GONE);
        tvName.setText(info.nickname);

//        Glide.with(mContext).load(bitmapTransform(GlideUtils.bitmapCropCircleTransformation(mContext), new CenterCrop(mContext)).into(left_head));
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {

            case R.id.left_back:
                if (drawerLayout != null) {
                    drawerLayout.closeDrawer(this);
                }
                break;

            case R.id.left_message:
                //intent.setClass(mContext, ConversationListActivity.class);
                //mContext.startActivity(intent);

                break;

            case R.id.left_head:
//                ToastUtils.getInstance().show("头像");


                break;

            case R.id.left_tab1:
//                ToastUtils.getInstance().show("好友");


                break;

            case R.id.left_tab2:
//                ToastUtils.getInstance().show("访客");

                break;

            case R.id.left_tab3:
//                ToastUtils.getInstance().show("余额");
                break;

            case R.id.left_item_ll1:
//                ToastUtils.getInstance().show("我的动态");
                break;

            case R.id.left_item_ll2:
//                ToastUtils.getInstance().show("我的趣币");
                break;

            case R.id.left_item_ll3:
//                ToastUtils.getInstance().show("我的收藏");
                break;

            case R.id.left_item_ll4:
//                ToastUtils.getInstance().show("我的部落");

//                intent.setClass(mContext, MyTribalActivity.class);
//                mContext.startActivity(intent);
                QXApp.getAppSelf().getEventDispatcher().sendEmptyMessage(QXEventDispatcherEnum.UI_EVENT_GO_PAIMAI);

                break;

            case R.id.left_item_ll5:
//                ToastUtils.getInstance().show("实名认证");

                break;

            case R.id.left_item_ll6:
//                ToastUtils.getInstance().show("我的拍卖");
                break;

            case R.id.left_item_ll7:
//                ToastUtils.getInstance().show("我的订单");
                break;

            case R.id.left_item_ll8:
//                ToastUtils.getInstance().show("我的礼物");
                break;

            case R.id.left_item_ll9:
//                ToastUtils.getInstance().show("推荐有奖");
                break;

            case R.id.left_item_ll10:
//                ToastUtils.getInstance().show("设置");

                break;
            case R.id.left_item_ll11:
                break;

        }
    }

    @SuppressLint("WrongConstant")
    public void notifyData(int code, String msg, LeftMenuInfo info) {

        if (code == 1) {

           /* leftV.setVisibility(info.is_vip ? VISIBLE : GONE);
            tvName.setText(info.nickname);
            tvBanlece.setText("￥" + info.balance);
            tvFriends.setText(info.friends_count);
            tvVisitors.setText(info.visitors_count);
            Glide.with(mContext).load(info.portrait).bitmapTransform(GlideUtils.bitmapCropCircleTransformation(mContext), new CenterCrop(mContext)).into(left_head);

            //更新昵称
            UserInfo user = UserInfoManager.getInstance().getUserInfo();
            user.nickname = info.nickname;
            user.portrait = info.portrait;
            UserInfoManager.getInstance().updateUserInfo(user);

            setNum(info.order_count);*/

        } else {

        }

    }

    @SuppressLint("WrongConstant")
    public void setNum(int num) {
        if (num <= 0) {
            orderNum.setVisibility(GONE);
        } else {
            orderNum.setVisibility(VISIBLE);
            orderNum.setText(String.valueOf(num));
        }
    }

    /*@SuppressLint("WrongConstant")
    public void showMsgPoint(int count) {
        left_msg_point.setVisibility(count <= 0 ? GONE : VISIBLE);
    }*/

    @SuppressLint("WrongConstant")
    public void addNewFriend(boolean isShow) {
        if (isShow) {
            imgNewFriend.setVisibility(VISIBLE);
            //添加新的朋友
            QXApp.getAppSelf().getEventDispatcher().sendEmptyMessage(QXEventDispatcherEnum.UI_EVENT_ADD_NEW_FRIEND);

        } else {
            imgNewFriend.setVisibility(GONE);
        }
    }
}
