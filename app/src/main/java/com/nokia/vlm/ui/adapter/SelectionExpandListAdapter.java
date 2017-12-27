package com.nokia.vlm.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.nokia.vlm.R;
import com.nokia.vlm.entity.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/14 14:36
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/14
 * @更新描述 ${TODO}
 */

public class SelectionExpandListAdapter extends BaseAdapter {
    private String[] mNames;
    protected Context mContext;
    private int mPosition;

    private int[] mSelectionDrawbleList ={};


    private List<Bean> mSelectionBeanList = new ArrayList<>();
    private List<CheckBox> mSelectionCheboxList = new ArrayList<>();

    public List<CheckBox> getSelectionCheboxList() {
        return mSelectionCheboxList;
    }

    public SelectionExpandListAdapter(Context context, List<Bean> beans) {
        super();
        mContext = context;
        mSelectionBeanList = beans;
    }

    public int[] getSelectionDrawbleList() {
        return mSelectionDrawbleList;
    }

    @Override
    public int getCount() {
        return mSelectionBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSelectionBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_search_expand, null);
            holder = new Holder();
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.cb_search_selection_item);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if(mSelectionBeanList.get(position).isChecked()){//状态选中
            holder.mCheckBox.setChecked(true);
        }else{
            holder.mCheckBox.setChecked(false);
        }

        int drawable = mSelectionBeanList.get(position).getDrawable();
        holder.mCheckBox.setButtonDrawable(mContext.getResources().getDrawable(drawable));
        holder.mCheckBox.setTag(mContext.getResources().getDrawable(drawable));
        holder.mCheckBox.setTag(drawable,mSelectionBeanList.get(position).getName());


        return convertView;
    }

    private class Holder {
        private CheckBox mCheckBox;
    }


}
