package com.nokia.vlm.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nokia.vlm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 */
public class HomeGridViewAdapter extends BaseAdapter {
    private String[] names;
    private int[] res;
    private LayoutInflater inflater;
    private Context myContext;
    private List<String> list = new ArrayList<>();

    private String[] tables={"TestInstrument","More"};


    private ClickCallback mClickCallback;

    public void setClickCallback(ClickCallback clickCallback) {
        mClickCallback = clickCallback;
    }
    public HomeGridViewAdapter(Context myContext,int[] res,ClickCallback clickCallback) {
        for (String str : tables) {
            this.list.add(str);
        }
        this.res = res;
        this.mClickCallback = clickCallback;
        this.myContext = myContext;
        this.inflater = LayoutInflater.from(myContext);
    }



    @Override
    public int getCount() {
        if (list!=null){
            return list.size();
        }
       return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView ==null){
           convertView  = inflater.inflate(R.layout.item_home_tab, null);
            holder = new ViewHolder();
            holder.initView(convertView);
            convertView.setTag(holder);
        }else {
            holder  = (ViewHolder) convertView.getTag();
        }
        holder.item_img.setImageDrawable(myContext.getResources().getDrawable(res[position]));
        holder.item_img.setTag(res[position]);
        holder.item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickCallback.onGridViewClick(v);
            }
        });
        return convertView;
    }

    class  ViewHolder{
        public ImageView item_img;
        public void initView(View convertView){
            item_img = (ImageView) convertView.findViewById(R.id.item_img);
        }
    }
    public interface ClickCallback {
        void onGridViewClick(View view);
    }


}
