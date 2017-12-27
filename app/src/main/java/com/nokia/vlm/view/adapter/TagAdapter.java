package com.nokia.vlm.view.adapter;

import android.view.View;

import com.nokia.vlm.view.FlexboxListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class TagAdapter<T> {

    private List<T> mTagDatas;
    private OnDataChangedListener mOnDataChangedListener;

    public TagAdapter(List<T> mData) {
        this.mTagDatas = mData;
    }

    public TagAdapter(T[] t) {
        mTagDatas = new ArrayList<>(Arrays.asList(t));
    }

    public interface OnDataChangedListener {
        void onChanged();

        void onItemChanged(int position);

        void onItemRemove(int position);

        void onAddLastItem(boolean isCheck);
    }

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        mOnDataChangedListener = listener;
    }

    public int getCount() {
        return mTagDatas == null ? 0 : mTagDatas.size();
    }

    public void notifyDataChanged() {
        mOnDataChangedListener.onChanged();
    }

    public void notifyItemChanged(int position) {
        mOnDataChangedListener.onItemChanged(position);
    }

    public void notifyItemRemoved(int position) {
        mOnDataChangedListener.onItemRemove(position);
    }

    public void addLastItem(T t) {
        addLastItem(t, false);
    }

    public void addLastItem(T t, boolean isCheck) {
        if (mTagDatas != null)
            mTagDatas.add(t);
        mOnDataChangedListener.onAddLastItem(isCheck);
    }

    public T getItem(int position) {
        if (mTagDatas == null) return null;
        return mTagDatas.get(position);
    }

    public void removeItem(int position) {
        if (mTagDatas == null) return;
        mTagDatas.remove(position);
    }

    public abstract View getView(FlexboxListView parent, int position, T t);


}
