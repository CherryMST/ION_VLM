package com.nokia.vlm.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qx.framelib.utlis.ZLog;
import com.nokia.vlm.R;
import com.nokia.vlm.ui.adapter.holder.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 适配
 */
public abstract class RecyclerBaseAdapter2<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public LayoutInflater mInflater;
    public Context mContext;
    private int layoutResId; //资源id
    protected List<T> mDatas; //数据集合
    protected OnItemClickListener listener;

    private static final int ITEM_TYPE_NULL = R.layout.item_list_data_null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public RecyclerBaseAdapter2(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

            }
        });
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void setDatas(List<T> list) {
        mDatas = new ArrayList<>(list);
    }

    public void setDatas(T[] t) {
        mDatas = new ArrayList<>(Arrays.asList(t));
    }

    public void addDatas(List<T> list) {
        if (mDatas == null) {
            mDatas = new ArrayList<>(list);
        } else {
            mDatas.addAll(list);
        }
    }

    public void addItemData(T data) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.add(data);
    }

    public void notifyAddItemData(T data) {
        addItemData(data);
        notifyDataSetChanged();
    }

    public void notifySetDatas(List<T> list) {
        setDatas(list);
        if (mDatas != null && mDatas.size() > 0) {
            notifyDataSetChanged();
        }
    }

    public void notifyAddDatas(List<T> list) {
        addDatas(list);
        notifyDataSetChanged();
    }

    public void addTopDatas(List<T> list) {
        if (mDatas == null) {
            mDatas = list;
        } else {
            mDatas.addAll(0, list);
        }

    }

    public void notifyAddTopDatas(List<T> list) {
        addTopDatas(list);
        notifyDataSetChanged();
    }

    public void removeItemData(int position) {
        if (mDatas != null && position >= 0) {
            mDatas.remove(position);
        }
    }

    public void notifyRemoveItemData(int position) {
        removeItemData(position);
        if (mDatas != null && position >= 0) {
            notifyItemRemoved(position);
        }
    }

    public void clearAllData() {
        if (mDatas != null) {
            mDatas.removeAll(mDatas);
        }
    }

    public void notifyClearAllData() {
        clearAllData();
        if (mDatas != null) {
            notifyDataSetChanged();
        }
    }

    public T getItemData(int position) {
        if (mDatas != null && position >= 0) {
            return mDatas.get(position);
        }
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ZLog.d(getClass().getSimpleName(), "onCreateViewHolder");

        if (viewType == ITEM_TYPE_NULL) {
            layoutResId = ITEM_TYPE_NULL;
        } else {
            layoutResId = createContentView(viewType);
        }

        View view = mInflater.inflate(layoutResId, parent, false);
        BaseViewHolder holder = new BaseViewHolder(mContext, view);
        holder.setViewType(viewType);
        initView(holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ZLog.d(getClass().getSimpleName(), "onBindViewHolder position:" + position);
        BaseViewHolder mHolder = (BaseViewHolder) holder;

//        if (mDatas == null || mDatas.isEmpty()) {
//            return;
//        } else {
            bindItemViewHolder(mHolder, position);
//        }
    }

    @Override
    public int getItemCount() {
        if (mDatas == null || mDatas.isEmpty())
            return 1;
        else {
            return mDatas.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas == null || mDatas.isEmpty()) {
            return ITEM_TYPE_NULL;
        } else
            return super.getItemViewType(position);
    }

    //绑定item
    protected abstract void bindItemViewHolder(BaseViewHolder holder, int position);

    protected abstract int createContentView(int viewType);

    public void initView(final BaseViewHolder holder) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {

                    if (getDatas() == null || getDatas().isEmpty()) {
                        listener.onItemClick(holder, holder.getLayoutPosition(), view, null);
                    } else {
                        listener.onItemClick(holder, holder.getLayoutPosition(), view, getItemData(holder.getLayoutPosition()));
                    }
                }
            }
        });
    }

    public void initView(BaseViewHolder holder, int viewType) {
        initView(holder);
    }


    public interface OnItemClickListener<T> {
        public void onItemClick(RecyclerView.ViewHolder holder, int position, View view, T data);
    }


}
