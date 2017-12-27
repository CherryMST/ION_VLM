package com.nokia.vlm.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.qx.framelib.utlis.ViewUtils;
import com.qx.framelib.utlis.ZLog;
import com.nokia.vlm.R;

import java.util.ArrayList;
import java.util.List;



/**
 * 首页自动带手动广告
 *
 */
public class AuctionPageBanner extends RelativeLayout {

    private Context context = null;
    private LayoutInflater inflater = null;

    public static final int HOME_BANNER_TYPE = 1;

    private List<String> adInfoList = new ArrayList<String>();

    private static final int MSG_PLAY = 888;
    private static final int PLAY_TIME = 5000; // 5秒换一张
    private boolean playing = false;

    private HorizonScrollLayout mHorizonScrollLayout = null;
    private LinearLayout pointsLy = null;

    private static final String bannerSolt = "07_00";

    private boolean isCanClick = true;

    // 用于大图轮播banner自动播放的Handler
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_PLAY:
                    if (playing) {
                        mHorizonScrollLayout.displayNextScreen();
                    }
                    if (handler != null) {
                        handler.sendEmptyMessageDelayed(MSG_PLAY, PLAY_TIME);
                    }
                    break;
                default:
                    break;
            }
        }

    };

    public AuctionPageBanner(Context context) {
        super(context);
        init(context);
    }

    public AuctionPageBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AuctionPageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        if (isInEditMode())
            return;
        this.context = context;
        inflater = LayoutInflater.from(context);
        initBanner();
    }

    private void initBanner() {
        removeAllViews();
        View baseView = inflater.inflate(R.layout.layout_home_list_banner, null);
        this.addView(baseView);

        mHorizonScrollLayout = (HorizonScrollLayout) findViewById(R.id.images);
//        mHorizonScrollLayout.isDismissParentTouchEvent = true;
        mHorizonScrollLayout.setEnableOverScroll(false);
        mHorizonScrollLayout.setLockAllWhenTouch(true);
        mHorizonScrollLayout.setScrollSlop(1.75f);
        mHorizonScrollLayout.setOnTouchScrollListener(new HorizonScrollLayout.OnTouchScrollListener() {
            @Override
            public void onScrollStateChanged(int scrollState, int currentScreem) {
                // 当手滚动广告位是, 不播放
                if (HorizonScrollLayout.OnTouchScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState) {
                    stopPlay();
                } else if (HorizonScrollLayout.OnTouchScrollListener.SCROLL_STATE_IDLE == scrollState) {
                    startPlay();
                }
            }

            @Override
            public void onScroll(View view, float leftX, float screemWidth) {
            }

            @Override
            public void onScreenChange(int displayScreem) {
//				BusinessSTManager.getInstance().logReport(STConst.ST_PAGE_GAME_POPULAR,
//						parentActivity.getActivityPrePageId(), bannerSolt+(displayScreem+1), STConstAction.ACTION_BROSWER, STConst.ST_USER_ACTION_EXTRADATA_TYPE_DEFAULT, null);
                if (adInfoList != null && adInfoList.size() > 0) {
                    updateDots(displayScreem);
                }
            }
        });
        pointsLy = (LinearLayout) findViewById(R.id.points);
    }

    // 刷新大图banner
    public void refreshBanner(String[] adList) {
        stopPlay();
        if (adList != null && adList.length > 0) {
            adInfoList.clear();
            String item;
            for (int i = 0; i < adList.length; i++) {
                item = adList[i];
                adInfoList.add(item);

            }
            if (adInfoList.size() > 0) {
                if (getChildCount() == 0) {
                    initBanner();
                }
                refreshBanner();
            } else {
//                setBackgroundColor(getResources().getColor(R.color.colorAccent));
                removeAllViews();
            }
        } else {
//            setBackgroundColor(getResources().getColor(R.color.colorAccent));
            removeAllViews();
        }
    }

    private void refreshBanner() {
//		View view = null;
//		TextView textView = null;
        ImageView imageView = null;
        mHorizonScrollLayout.removeAllViews();
        for (int i = 0; i < adInfoList.size(); i++) {
            ZLog.d("refreshBanner adInfoList i = " + i);
            final String card = adInfoList.get(i);
//			view = inflater.inflate(R.layout.banner_big_image, null);
//			textView = (TextView) view.findViewById(R.id.default_pic_txt);
//			textView.setText(card.getContent());
            imageView = new ImageView(context);
            ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ScaleType.CENTER_INSIDE);

            Glide.with(imageView.getContext()).load(card).fitCenter().into(imageView);

//			final String adAction = card.getAdAction();
//			final String adActionContent = card.getContent();
//			final int actionType = card.getActionType();
//			final int bannerIndex = (i+1);
            final int curPostion = i;
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, curPostion);
                    }

                    /*Intent intent = new Intent(context, CommodityDetailActivity.class);
                    intent.putExtra("id", adInfoList.get(curPostion).id);
                    context.startActivity(intent);*/


//                    try {
//                        if (!TextUtils.isEmpty(card.toUrl) && card.toUrl.startsWith("ybb://")) {
//                            Intent intent = new Intent();
//                            intent.setClass(context, LinkProxyActivity.class);
//                            intent.setData(Uri.parse(card.toUrl));
//                            context.startActivity(intent);
//                        } else {
//                            Toast.makeText(context, "参数错误,ybb://", Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
            });
            mHorizonScrollLayout.addView(imageView);

            if (!isCanClick) {
                imageView.setClickable(false);
            } else {
                imageView.setClickable(true);
            }

        }
        updateDots(0);
        if (adInfoList.size() > 1) {
            mHorizonScrollLayout.setCircle(true);
            startPlay();
        } else {
            mHorizonScrollLayout.setCircle(false);
            stopPlay();
        }
    }

    public void setCanClick(boolean isCanClick) {
        this.isCanClick = isCanClick;
    }

    // 开始大图轮播banner的播放
    private void startPlay() {
        playing = true;
        if (handler != null) {
            handler.removeMessages(MSG_PLAY);
            handler.sendEmptyMessageDelayed(MSG_PLAY, PLAY_TIME);
        }
    }

    // 停止大图轮播banner的播放
    private void stopPlay() {
        playing = false;
        if (handler != null) {
            handler.removeMessages(MSG_PLAY);
        }
    }

    public void recycle() {
        if (handler != null) {
            handler.removeMessages(MSG_PLAY);
            handler = null;
        }
    }

    // 更新大图轮播banner下面的点点
    private void updateDots(int current) {
//        pointsLy.removeAllViews();
//        if (adInfoList.size() < 2) {
//            return;
//        }
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.leftMargin = ViewUtils.getSpValueInt(2);
//        params.rightMargin = ViewUtils.getSpValueInt(2);
////		params.height = ViewUtils.getSpValueInt(8);
////		params.width = ViewUtils.getSpValueInt(8);
//        ImageView dot = null;
//        for (int i = 0; i < adInfoList.size(); i++) {
//            dot = new ImageView(context);
//            dot.setLayoutParams(params);
//            if (current == i) {
//                dot.setImageResource(R.drawable.dot_highlight);
//            } else {
//                dot.setImageResource(R.drawable.dot);
//            }
//            pointsLy.addView(dot);
//        }

        //如果少于2张图就不画点
        if (adInfoList.size() < 2) {
            return;
        }

        if (pointsLy.getChildCount() == 0) {
            pointsLy.removeAllViews();
            //初始化点
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = ViewUtils.getSpValueInt(2);
            params.rightMargin = ViewUtils.getSpValueInt(2);
            ImageView dot = null;
            for (int i = 0; i < adInfoList.size(); i++) {
                dot = new ImageView(context);
                dot.setLayoutParams(params);
                dot.setImageResource(R.drawable.dot);
                pointsLy.addView(dot);
            }
        }

        for (int i = 0; i < adInfoList.size() && i < pointsLy.getChildCount(); i++) {
            ImageView img = (ImageView) pointsLy.getChildAt(i);
            if (current == i) {
                img.setImageResource(R.drawable.dot_highlight);
            } else {
                img.setImageResource(R.drawable.dot);
            }
        }

    }

    public void onPause() {
        stopPlay();
    }

    public void onResume() {
        if (adInfoList.size() > 1) {
            startPlay();
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int postion);
    }

}
