package com.nokia.vlm.view;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nokia.vlm.R;



public class OneBtnDialog extends Dialog implements View.OnClickListener {
    public Context context;
    //public TextView titleView;
    public TextView contentView;
    public Button btn1;

    private OnClickListener listener;

    public OneBtnDialog(Context context) {
        this(context, R.style.theme_dialog_alert);
    }

    public OneBtnDialog(Context context, int themeResId) {
        super(context, themeResId);

        super.setContentView(R.layout.layout_one_dialog);
        this.context = context;
        initView();
    }

    private void initView() {
        //titleView = (TextView) super.findViewById(R.id.title);
        contentView = (TextView) super.findViewById(R.id.content);
        btn1 = (Button) super.findViewById(R.id.btn_1);

        btn1.setOnClickListener(this);

    }

    public void noTitle() {
        //titleView.setVisibility(View.GONE);
    }
/*
    @Override
    public void setTitle(CharSequence title) {
//        super.setTitle(title);
        titleView.setText(title);
    }

    public void setTitileColor(int color) {
        titleView.setTextColor(color);
    }

    public void setTitleSize(int size) {
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }*/

    public void setContentMsg(CharSequence title) {
//        super.setTitle(title);
        contentView.setText(title);
    }

    public void setContentMsgColor(int color) {
        contentView.setTextColor(color);
    }

    public void setContentMsgSize(int size) {
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setLeftBtnMsg(CharSequence text) {
        btn1.setText(text);
    }

    public void setLeftBtnMsgColor(int color) {
        btn1.setTextColor(color);
    }

    public void setLeftBtnMsgSize(int size) {
        btn1.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setLeftBtnBg(int bg) {
        btn1.setBackgroundResource(bg);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
//                dismiss();
                if (listener != null) {
                    listener.onLeftClick();
                }
                break;
        }
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        public void onLeftClick();

    }

}
