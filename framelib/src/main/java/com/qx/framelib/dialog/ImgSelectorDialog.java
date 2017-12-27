package com.qx.framelib.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.qixiang.framelib.R;
import com.qx.framelib.utlis.SysPhotoCropper;
import com.qx.framelib.utlis.ToastUtils;

/**
 * Created by ZhaoWei on 2016/11/10.
 * 图片选择
 */

public class ImgSelectorDialog extends WrapBottomSheetDialog implements View.OnClickListener {
    private Context mContext;
    protected Button btn1;
    protected Button btn2;
    //
    private SysPhotoCropper photoCropper;

    public ImgSelectorDialog(@NonNull Context context) {
        super(context);
        mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.layout_img_selector, null);
        setContentView(view);
        initView(view);

    }

    public void setActivityCallBack(Activity activity, SysPhotoCropper.PhotoCropCallBack photoCropCallBack) {
        photoCropper = new SysPhotoCropper(activity, photoCropCallBack);
    }

    public void setFragmentCallBack(Fragment fragment, SysPhotoCropper.PhotoCropCallBack photoCropCallBack) {
        photoCropper = new SysPhotoCropper(fragment, photoCropCallBack);
    }

    @Override
    public void onClick(View view) {
        if (photoCropper == null) {
            ToastUtils.getInstance().show("未初始化工具");
            return;
        }
        if (view.getId() == R.id.btn_1) { //拍照
            if (photoCropper != null) {
                photoCropper.cropForCamera();
            }
        } else if (view.getId() == R.id.btn_2) { //相册
            if (photoCropper != null) {
                photoCropper.cropForGallery();
            }
        }
        dismiss();
    }

    private void initView(View rootView) {
        btn1 = (Button) rootView.findViewById(R.id.btn_1);
        btn1.setOnClickListener(ImgSelectorDialog.this);
        btn2 = (Button) rootView.findViewById(R.id.btn_2);
        btn2.setOnClickListener(ImgSelectorDialog.this);
    }

    public void handlerOnActivtyResult(int requestCode, int resultCode, Intent data) {
        if (photoCropper != null) {
            photoCropper.handlerOnActivtyResult(requestCode, resultCode, data);
        }
    }

}
