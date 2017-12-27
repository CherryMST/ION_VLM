package com.nokia.vlm.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.nokia.vlm.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashSet;
import java.util.List;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/25 20:10
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/25
 * @更新描述 ${TODO}
 */

public class SearchBySelectionRecycleViewAdapter extends CommonAdapter<Integer> {
    public HashSet<CheckBox> getCheckBoxSet() {
        return mCheckBoxSet;
    }

    public void setCheckBoxSet(HashSet<CheckBox> checkBoxSet) {
        mCheckBoxSet = checkBoxSet;
    }

    private HashSet<CheckBox> mCheckBoxSet = new HashSet<>();
    private SearchSelectionsClick onClickCallBack;

    public SearchBySelectionRecycleViewAdapter(Context context, int layoutId, List<Integer> datas, SearchSelectionsClick clickCallback) {
        super(context, layoutId, datas);
        this.onClickCallBack = clickCallback;
    }

    @Override
    protected void convert(final ViewHolder holder, Integer integer, final int position) {
        CheckBox button = holder.getView(R.id.tv_search_selection_item);
        if(position <= (mDatas.size()-1) && mCheckBoxSet.size()<mDatas.size()){
            mCheckBoxSet.add(button);
        }

        button.setBackgroundDrawable(mContext.getResources().getDrawable(integer));
        button.setTag(integer);
        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onClickCallBack.onSearchSelectionsClick(buttonView,isChecked,position);
            }
        });

    }

    public interface SearchSelectionsClick{
        void onSearchSelectionsClick(View view, boolean isChecked, int position);
    }

}
