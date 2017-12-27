package com.nokia.vlm.view;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * @创建者 DK-house
 * @创建时间 2017/11/14 23:49
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/11/14
 * @更新描述 ${TODO}
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {


        //Glide 加载图片简单用法
        Glide.with(context).load(path).into(imageView);

    }


}
