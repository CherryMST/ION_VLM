package com.nokia.vlm.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.nokia.vlm.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/14 14:36
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/14
 * @更新描述 ${TODO}
 */

public class SearchSelectionsAdapter extends BaseAdapter {
    public static final int DRAWABLE=1001;
    public int mPosition = -1;
    protected Context mContext;
//    private List<Drawable>    mSelectionDrawbleList        = new ArrayList<>();
    private int[] mSelectionDrawbleList={};
    private List<RadioButton> mSelectionButtonsList;
    private List<View> mHolderRadioButtons = new ArrayList<>();
    private SearchSelectionsClick mClickCallback;
    HashSet<View> set = new HashSet<>();

    public List<RadioButton> getSelectionButtonsList() {
        return mSelectionButtonsList;
    }


    public List<View> getHolderRadioButtons() {
        return mHolderRadioButtons;
    }


    public SearchSelectionsAdapter(Context context, int[] drawableList, SearchSelectionsClick clickCallback,List<RadioButton> radioButtons) {
        mContext = context;
        mClickCallback = clickCallback;
        mSelectionDrawbleList = drawableList;
        mSelectionButtonsList = radioButtons;
    }

    @Override
    public int getCount() {
        return mSelectionButtonsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSelectionButtonsList.get(position);
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
            convertView = inflater.inflate(R.layout.item_search_selections_list, null);
            holder = new Holder();
//            holder.mRadioButton = (RadioButton) convertView.findViewById(R.id.tv_search_selection_item);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

//        mHolderRadioButtons.add(convertView);
        Drawable drawable = mContext.getResources().getDrawable(mSelectionDrawbleList[position]);
//        holder.mRadioButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null,drawable,null,null);
        holder.mRadioButton.setBackgroundDrawable(drawable);
        holder.mRadioButton.setTag(mSelectionDrawbleList[position]);
        holder.mRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mClickCallback.onSearchSelectionsClick(buttonView,isChecked);

            }
        });
        if (mPosition != position) {
            holder.mRadioButton.setChecked(false);
        } else {
            holder.mRadioButton.setChecked(true);
        }
        return convertView;
    }

    private class Holder {
        private RadioButton mRadioButton;
    }

    public interface SearchSelectionsClick{
        void onSearchSelectionsClick(View view, boolean isChecked);
    }

}
