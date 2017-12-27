package com.nokia.vlm.ui.adapter;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qx.framelib.utlis.HandlerUtils;
import com.qx.framelib.utlis.ViewUtils;
import com.qx.framelib.utlis.ZLog;
import com.nokia.vlm.R;
import com.nokia.vlm.entity.Message;
import com.nokia.vlm.event.QXEventDispatcherEnum;
import com.nokia.vlm.manager.MessageManager;
import com.nokia.vlm.ui.QXApp;
import com.nokia.vlm.ui.adapter.holder.BaseViewHolder;

/**
 * 消息
 */
public class MessageAdapter extends RecyclerBaseAdapter<Message> {


    public MessageAdapter(Context context) {
        super(context);
    }


    @Override
    protected void bindItemViewHolder(BaseViewHolder holder, final int position) {
        final Message message = mDatas.get(position);

        holder.setText(R.id.tv_date, initTime(message.addtime)).
                setText(R.id.tv_desc, message.content).
                setText(R.id.tv_title, message.title).
                setVisible(R.id.img_hot, message.isRead != 1).
                itemView.scrollTo(0, 0);

    }

    private String initTime(long addtime) {
        long currenttime = System.currentTimeMillis() / 1000;
        long offtime = currenttime - addtime;
        if (offtime >= 2592000) {//一个月前
            return "1个月前";
        } else if (offtime >= 86400) { //一天前
            return (offtime / 86400) + "天前";
        } else if (offtime >= 3600) {//一小时前
            return (offtime / 3600) + "小时前";
        } else if (offtime >= 60) {//一分钟前
            return (offtime / 60) + "分钟前";
        } else {
            return "1分钟内";
        }
    }

    @Override
    protected int createContentView(int viewType) {
        return R.layout.item_message;
    }

    @Override
    public void initView(BaseViewHolder holder) {
        super.initView(holder);

//        //侧滑
//        swpieMenu(holder);
        //点击
        onclick(holder);

    }

    private void swpieMenu(BaseViewHolder holder) {
        RelativeLayout rl_item = holder.findViewById(R.id.rl_item);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rl_item.getLayoutParams();
        params.width = ViewUtils.getScreenWidth();
        rl_item.setLayoutParams(params);

        final HorizontalScrollView horizontalScroll = (HorizontalScrollView) holder.itemView;
        final Button del = holder.findViewById(R.id.btn_del);
        horizontalScroll.setOnTouchListener(new View.OnTouchListener() {
            private int downX;
            private int moveX;
            private boolean isMove;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {//按下
                    downX = (int) event.getX();
                    ZLog.d("ACTION_DOWN:" + downX);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (!isMove) {
                        moveX = (int) event.getX();
                        isMove = true;
                        ZLog.d("ACTION_MOVE:" + moveX);
                    }

                } else if (event.getAction() == MotionEvent.ACTION_UP) {//抬起
                    isMove = false;
                    int x = (int) event.getX();
                    ZLog.d("ACTION_UP:" + x);
                    int offX = del.getWidth() / 2;

                    if (moveX - x >= offX) {
                        horizontalScroll.scrollBy(offX * 2, 0);
                    } else {
                        horizontalScroll.scrollTo(0, 0);
                    }

                }
                return false;
            }
        });
    }

    private void onclick(final BaseViewHolder holder) {
        if (mDatas == null || mDatas.isEmpty()) return;
        holder.setOnClickListener(R.id.rl_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();

                Message message = getItemData(position);

//                String type = message.type;
//                String itemId = message.itemId;
//                if ("101".equals(type)) { //诊单详情
//
//                    if (!TextUtil.isEmpty(itemId)) {
//
//                        try {
//
////                            int id = Integer.parseInt(itemId);
////                            Bundle bundle = new Bundle();
////                            bundle.putInt("id", id);
////
////                            ((BaseActivity) mContext).startActivity(OrderDetailActivity.class, bundle);
//
//                        } catch (Exception e) {
//
//                        }
//
//
//                    }
//
//                }

                if (message.isRead == 1) return;

                message.isRead = 1;
                MessageManager.getInstanse().updateMessage(message);
                MessageManager.getInstanse().updateMessage(position, message);
                mDatas.set(position, message);
//                notifyDataSetChanged();
                holder.setVisible(R.id.img_hot, message.isRead != 1);

                pushMessage();
//                if (fragment != null) {
//                    fragment.clickItem();
//                }

            }
        });
//                .setOnClickListener(R.id.btn_del, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int postion = (int) holder.itemView.getTag();
//                Message msg = mDatas.get(postion);
//
//
//                boolean isDel = MessageManager.getInstanse().delMessageById(msg.id);
//                ZLog.d("消息删除:" + isDel);
//                if (isDel) {
//                    notifyRemoveItemData(postion);
//                    if (msg.isRead == 0) { //未读消息
////                        if (fragment != null) {
////                            fragment.clickItem();
////                        }
//                    }
////                    fragment.delItem();
//                }
//            }
//        });

    }

    private synchronized void pushMessage() {
        HandlerUtils.getMainZooerHandler().post(new Runnable() {
            @Override
            public void run() {

                int count = MessageManager.getInstanse().getUnReadCount();
                if (count <= 0) {
                    QXApp.getAppSelf().getEventDispatcher().obtainMessage(QXEventDispatcherEnum.UI_EVENT_READ_END_MESSAGE).sendToTarget();
                }

            }
        });
    }

}
