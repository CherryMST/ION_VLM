package com.nokia.vlm.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nokia.vlm.R;
import com.nokia.vlm.entity.BaseAssetItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 DK-house
 * @创建时间 2017/11/19 20:01
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/11/19
 * @更新描述 ${TODO}
 */

public class SelectResultAdapter extends BaseAdapter {

    protected Context mContext;
    private List<BaseAssetItemInfo> mInfos = new ArrayList<>();

    public SelectResultAdapter(Context context, List<BaseAssetItemInfo> infos) {
        mContext = context;
        mInfos = infos;
    }

    @Override
    public int getCount() {
        return mInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mInfos.get(position);

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
            convertView = inflater.inflate(R.layout.item_asset_in_fragment, null);
            holder = new Holder();
            holder.mTvArea = (TextView) convertView.findViewById(R.id.tv_area);
            holder.mTvDescription = (TextView) convertView.findViewById(R.id.tv_description);
            holder.mTvCurrentUser = (TextView) convertView.findViewById(R.id.tv_current_user);
            holder.mTvDepartment = (TextView) convertView.findViewById(R.id.tv_department);
            holder.mTvCondition = (ImageView) convertView.findViewById(R.id.img_work_condition);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        BaseAssetItemInfo itemInfo = mInfos.get(position);
        holder.mTvArea.setText("Area: ".concat(itemInfo.getArea()));
        holder.mTvDescription.setText("Description: ".concat(itemInfo.getDescription()));
        holder.mTvCurrentUser.setText("Current User: ".concat(itemInfo.getCurrentuser()));
        holder.mTvDepartment.setText("Department: ".concat(itemInfo.getDepartment()));
        switch (itemInfo.getWorkingcondition()) {
            case "In Service":
                holder.mTvCondition.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_inuse));
                break;
            case "In Repair":
                holder.mTvCondition.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_inrepair));
                break;
        }

        return convertView;
    }

    private class Holder {
        private TextView  mTvArea;
        private TextView  mTvDescription;
        private TextView  mTvCurrentUser;
        private TextView  mTvDepartment;
        private ImageView mTvCondition;
    }
    interface MyOnItemClickListener{
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

}
