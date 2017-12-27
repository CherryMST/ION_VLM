package com.nokia.vlm.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.nokia.vlm.R;
import com.nokia.vlm.entity.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 */
public class SelectionExpandGridViewAdapter extends BaseAdapter {
    public static final int NAME = 1002;
    private List<Bean> mBeanList;


    private int[]          res;
    private LayoutInflater inflater;
    private Context        myContext;

    private int          mPosition =-1;


    public SelectionExpandGridViewAdapter(Context myContext, int[] res) {
        this.res = res;
        this.myContext = myContext;
        this.inflater = LayoutInflater.from(myContext);
    }

    public SelectionExpandGridViewAdapter(Context myContext, List<Bean> beanList) {
        this.mBeanList = beanList;
        this.myContext = myContext;
        this.inflater = LayoutInflater.from(myContext);

    }

    public int[] getRes() {
        return res;
    }

    @Override
    public int getCount() {
        if (mBeanList!=null){
            return mBeanList.size();
        }
       return 0;
    }

    @Override
    public Object getItem(int position) {
        return mBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView ==null){
           convertView  = inflater.inflate(R.layout.item_search_expand, null);
            holder = new ViewHolder();
            holder.initView(convertView);
            convertView.setTag(holder);
        }else {
            holder  = (ViewHolder) convertView.getTag();
        }
        if(mBeanList.get(position).isChecked()){//状态选中
            holder.itemCB.setChecked(true);
        }else{
            holder.itemCB.setChecked(false);
        }


        int drawable = mBeanList.get(position).getDrawable();
        holder.itemCB.setButtonDrawable(myContext.getResources().getDrawable(drawable));
        holder.itemCB.setTag(drawable);
        holder.itemCB.setTag(drawable,mBeanList.get(position).getName());//key是个资源id,value是name
        return convertView;
    }

    class  ViewHolder{
        public CheckBox itemCB;
        public void initView(View convertView){
            itemCB = (CheckBox) convertView.findViewById(R.id.cb_search_selection_item);
        }
    }


}
