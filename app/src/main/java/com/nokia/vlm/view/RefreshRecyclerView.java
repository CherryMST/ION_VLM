package com.nokia.vlm.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.qx.framelib.utlis.ViewUtils;
import com.nokia.vlm.R;
import com.nokia.vlm.ui.adapter.holder.BaseViewHolder;
import com.nokia.vlm.view.listenter.OnRecyclerLoadMoreListener;
import com.nokia.vlm.view.listenter.OnRecylerItemClickListener;


/**
 * 支持刷新 只支持线性 底部上来加载
 */
public class RefreshRecyclerView extends RecyclerView {

    private Context mContext;
    private RecyclerWrapperAdapter wrapperAdapter;
    private View headerView;
    private View footerView;
    private OnRecylerItemClickListener itemClickListener;
    private OnRecyclerLoadMoreListener loadMoreListener;
    private boolean isLoadingData; //是否在加载数据
    private boolean isLoadMoreEnable; //是否去用加载更多
    //item 点击使用 手势控制器
    private GestureDetectorCompat mGestureDetector;
    private boolean isFooterEnable = true; //是否用底部

    /**
     * 标记加载更多的position
     */
    private int mLoadMorePosition;

    private BaseViewHolder footViewHolder;

    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        setItemAnimator(null);
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
    }

    public void setFooterView(View footerView) {
        this.footerView = footerView;
        footerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtils.dip2px(mContext, 45)));
        footerView.setVisibility(GONE);

        footViewHolder = new BaseViewHolder(mContext, footerView);
    }

    public void setFooterView(int id) {
        setFooterView(LayoutInflater.from(mContext).inflate(id, null));
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        isLoadMoreEnable = loadMoreEnable;
    }

    public boolean isLoadMoreEnable() {
        return isLoadMoreEnable;
    }

    public void isFooterEnable(boolean isFooterEnable) {
        this.isFooterEnable = isFooterEnable;
    }

    @Override
    public void setAdapter(Adapter adapter) {

        wrapperAdapter = new RecyclerWrapperAdapter(adapter);
        if (headerView != null) {
            wrapperAdapter.addHeaderView(headerView);
        }
        if (footerView != null) {
//            isLoadMoreEnable = true;
            wrapperAdapter.addFootView(footerView);
        } else {
            if (isFooterEnable) {
//                isLoadMoreEnable = true;
                setFooterView(R.layout.recycler_load_more_layout);
                wrapperAdapter.addFootView(footerView);
            }

        }

        super.swapAdapter(wrapperAdapter, true);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        if (state == RecyclerView.SCROLL_STATE_IDLE && loadMoreListener != null && !isLoadingData && isLoadMoreEnable) {
            LayoutManager layoutManager = getLayoutManager();
//            int lastVisibleItemPosition = 0;
////            if (!(layoutManager instanceof LinearLayoutManager)) {  //非线性
////                return;
////            }
//            if ((layoutManager instanceof LinearLayoutManager)) {
//                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
//            } else if (layoutManager instanceof GridLayoutManager) {
//                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
//            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
////                lastVisibleItemPosition = ((StaggeredGridLayoutManager) layoutManager).fin
//            } else {
//                return;
//            }

            int lastVisibleItemPosition = getLastVisiblePosition();

            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1) {
                if (footerView != null) {
                    footerView.setVisibility(VISIBLE);
                }
                // 加载更多
                isLoadingData = true;
                mLoadMorePosition = lastVisibleItemPosition;
                loadMoreListener.onLoadMore();
            }
        }
    }

    public void loadMoreEnd() {
        isLoadingData = false;
        if (footerView != null)
            footerView.setVisibility(GONE);
    }

    public void setOnRecyclerLoadMoreListener(OnRecyclerLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (mGestureDetector != null) //将手势控制器与　recyclerView　关联
            mGestureDetector.onTouchEvent(e);
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mGestureDetector != null)//将手势控制器与　recyclerView　关联
            mGestureDetector.onTouchEvent(e);
        return super.onTouchEvent(e);
    }

    public void setOnItemClickListener(OnRecylerItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        mGestureDetector = new GestureDetectorCompat(mContext, new ItemTouchHelperGestureListener(this));
    }

    public void notifyMoreFinish(boolean hasMore) {
        setLoadMoreEnable(hasMore);
        loadMoreEnd();
//        getAdapter().notifyItemRemoved(mLoadMorePosition);
//        getAdapter().notifyItemRangeChanged(0, mLoadMorePosition);
        getAdapter().notifyDataSetChanged();
    }

    /**
     * 移除
     *
     * @param postion
     */
    public void notifyRemovePostion(int postion) {
        getAdapter().notifyItemRemoved(postion);
    }

    public void notifyDataSetChanged() {
        getAdapter().notifyDataSetChanged();
    }

    public void notifyItemChanged(int postion) {
        getAdapter().notifyItemChanged(postion);
    }

    /**
     * 获取第一条展示的位置
     *
     * @return
     */
    private int getFirstVisiblePosition() {
        int position;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        } else if (getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMinPositions(lastPositions);
        } else {
            position = 0;
        }
        return position;
    }

    /**
     * 获得当前展示最小的position
     *
     * @param positions
     * @return
     */
    private int getMinPositions(int[] positions) {
        int size = positions.length;
        int minPosition = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            minPosition = Math.min(minPosition, positions[i]);
        }
        return minPosition;
    }

    /**
     * 获取最后一条展示的位置
     *
     * @return
     */
    private int getLastVisiblePosition() {
        int position;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    /**
     * 获得最大的位置
     *
     * @param positions
     * @return
     */
    private int getMaxPosition(int[] positions) {
        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxPosition = Math.max(maxPosition, positions[i]);
        }
        return maxPosition;
    }

    /**
     * 切换layoutManager
     * <p>
     * 为了保证切换之后页面上还是停留在当前展示的位置，记录下切换之前的第一条展示位置，切换完成之后滚动到该位置
     * 另外切换之后必须要重新刷新下当前已经缓存的itemView，否则会出现布局错乱（俩种模式下的item布局不同），
     * RecyclerView提供了swapAdapter来进行切换adapter并清理老的itemView cache
     *
     * @param layoutManager
     */
    public void switchLayoutManager(LayoutManager layoutManager) {
        int firstVisiblePosition = getFirstVisiblePosition();
//        getLayoutManager().removeAllViews();
        setLayoutManager(layoutManager);
        //super.swapAdapter(mAutoLoadAdapter, true);
        getLayoutManager().scrollToPosition(firstVisiblePosition);
    }

    /**
     * Created by ZhaoWei on 2016/7/27.
     * 用于添加头部 底部
     */
    private class RecyclerWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int BASE_ITEM_TYPE_HEADER = 100000;
        private static final int BASE_ITEM_TYPE_FOOTER = 200000;

        private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
        private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

        private RecyclerView.Adapter mInnerAdapter;

        public RecyclerWrapperAdapter(RecyclerView.Adapter adapter) {
            mInnerAdapter = adapter;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mHeaderViews.get(viewType) != null) {
                ViewHolder holder = new BaseViewHolder(parent.getContext(), mHeaderViews.get(viewType));
                return holder;

            } else if (mFootViews.get(viewType) != null) {
                ViewHolder holder = new BaseViewHolder(parent.getContext(), mFootViews.get(viewType));
                return holder;
            }
            return mInnerAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeaderViewPos(position)) {
                return mHeaderViews.keyAt(position);
            } else if (isFooterViewPos(position)) {
                return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
            }
            return mInnerAdapter.getItemViewType(position - getHeadersCount());
        }

        private int getRealItemCount() {
            return mInnerAdapter.getItemCount();
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (isHeaderViewPos(position)) {
                return;
            }
            if (isFooterViewPos(position)) {
                return;
            }
            mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
        }

        @Override
        public int getItemCount() {
            return getHeadersCount() + getFootersCount() + getRealItemCount();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
                @Override
                public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                    int viewType = getItemViewType(position);
                    if (mHeaderViews.get(viewType) != null) {
                        return layoutManager.getSpanCount();
                    } else if (mFootViews.get(viewType) != null) {
                        return layoutManager.getSpanCount();
                    }
                    if (oldLookup != null)
                        return oldLookup.getSpanSize(position);
                    return 1;
                }
            });
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            mInnerAdapter.onViewAttachedToWindow(holder);
            int position = holder.getLayoutPosition();
            if (isHeaderViewPos(position) || isFooterViewPos(position)) {
                WrapperUtils.setFullSpan(holder);
            }
        }

        private boolean isHeaderViewPos(int position) {
            return position < getHeadersCount();
        }

        private boolean isFooterViewPos(int position) {
            return position >= getHeadersCount() + getRealItemCount();
        }


        public void addHeaderView(View view) {
            mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
        }

        public void addFootView(View view) {
            mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
        }

        public int getHeadersCount() {
            return mHeaderViews.size();
        }

        public int getFootersCount() {
            return mFootViews.size();
        }


    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        private RecyclerView recyclerView;

        public ItemTouchHelperGestureListener(RefreshRecyclerView refreshRecyclerView) {
            this.recyclerView = refreshRecyclerView;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(vh);
                }
            }
            return true;
        }

        //长点击事件，本例不需要不处理
        //@Override
        //public void onLongPress(MotionEvent e) {
        //    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
        //    if (child!=null) {
        //        RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
        //        onItemLongClick(vh);
        //    }
        //}

        //public abstract void onItemLongClick(RecyclerView.ViewHolder vh);
    }


    public static class WrapperUtils {
        public interface SpanSizeCallback {
            int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position);
        }

        public static void onAttachedToRecyclerView(RecyclerView.Adapter innerAdapter, RecyclerView recyclerView, final SpanSizeCallback callback) {
            innerAdapter.onAttachedToRecyclerView(recyclerView);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return callback.getSpanSize(gridLayoutManager, spanSizeLookup, position);
                    }
                });
                gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
            }
        }

        public static void setFullSpan(RecyclerView.ViewHolder holder) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams) {

                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

                p.setFullSpan(true);
            }
        }
    }


}
