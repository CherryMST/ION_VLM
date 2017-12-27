package com.nokia.vlm.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.nokia.vlm.R;
import com.nokia.vlm.ui.ZooerConstants;


public class TwoBtnDialog extends DialogFragment implements View.OnClickListener {
    private static final float COMMON_DIALOG_WIDTH_PERCENT = 0.867f;

    public TextView titleView;
    public TextView contentView;
    public Button btn1, btn2;
    protected View titleLine;
    protected View btnLine;
    private ZooerConstants.TwoBtnDialogInfo dialogInfo;
    private OnClickListener listener;

    public TwoBtnDialog() {
        super();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View           view     = inflater.inflate(R.layout.layout_two_dialog, null);
        builder.setView(view);

        initView(view);
        initInfo();

        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        initDialog();
        Window window = getDialog().getWindow();
//        View view = inflater.inflate(R.layout.layout_two_dialog, ((ViewGroup) window.findViewById(android.R.id.content)), false);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        //window.setLayout((int) (ViewUtils.getScreenWidth() * 0.87), ViewUtils.dip2px(getContext(), 170));


        return  super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initInfo() {

        if (dialogInfo == null) return;
        getDialog().setCanceledOnTouchOutside(dialogInfo.isOnTouchCancal);


        //是否显示标题
        setTitleEnaled(dialogInfo.hasTitle);

        //是否只有一个btn
        setOnlyOneBtn(dialogInfo.isOnlyOneBtn);
        setTitle(dialogInfo.titleRes); //设置标题
        setContentMsg(dialogInfo.contentRes); //设置内容
        setLeftBtnMsg(dialogInfo.lBtnTxtRes); //左按钮
        setRightBtnMsg(dialogInfo.rBtnTxtRes); //右按钮
        if (dialogInfo.rBtnTextColorResId != 0) //右按钮颜色
            setRightBtnMsgColor(dialogInfo.rBtnTextColorResId);


    }

    private void initDialog() {
        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

//        Window window = dialog.getWindow();
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        android.view.WindowManager.LayoutParams params = window.getAttributes();
//        params.gravity = Gravity.CENTER;
//        Display d = window.getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
//        params.width = (int) (d.getWidth() * COMMON_DIALOG_WIDTH_PERCENT); // 宽度设置为屏幕百分比
//        window.setAttributes(params); // 设置生效

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (dialogInfo != null) {
                    dialogInfo.onCancel();
                }
            }
        });


        setOnClickListener(new OnClickListener() {
            @Override
            public void onLeftClick() {
                if (dialogInfo != null) {

                    if (dialogInfo.isClickDismiss) {
                        dismiss();
                    }

                    dialogInfo.onLeftBtnClick();
                }
            }

            @Override
            public void onRightClick() {
                if (dialogInfo != null) {

                    if (dialogInfo.isClickDismiss) {
                        dismiss();
                    }

                    dialogInfo.onRightBtnClick();
                }
            }
        });

    }

    private void initView(View rootView) {
        titleView = (TextView) rootView.findViewById(R.id.title);
        contentView = (TextView) rootView.findViewById(R.id.content);
        btn1 = (Button) rootView.findViewById(R.id.btn_1);
        btn2 = (Button) rootView.findViewById(R.id.btn_2);
        titleLine = rootView.findViewById(R.id.line);
        btnLine = rootView.findViewById(R.id.btn_line);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

    }

    public void setTitleEnaled(boolean isEnaled) {
        if (isEnaled) {
            titleView.setVisibility(View.VISIBLE);
            titleLine.setVisibility(View.VISIBLE);
        } else {
            titleView.setVisibility(View.GONE);
            titleLine.setVisibility(View.GONE);

        }

    }

    public void setTitle(CharSequence title) {
        titleView.setText(title);
    }

    public void setTitileColor(int color) {
        titleView.setTextColor(color);
    }

    public void setTitleSize(int size) {
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

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

    public void setRightBtnMsg(CharSequence text) {
        btn2.setText(text);
    }

    public void setRightBtnMsgColor(int color) {
        btn2.setTextColor(color);
    }

    public void setRightBtnMsgSize(int size) {
        btn2.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setRightBtnBg(int bg) {
        btn2.setBackgroundResource(bg);
    }


    public void setOnlyOneBtn(boolean isOnly) {
        if (isOnly) { //只有一个btn
            btn1.setVisibility(View.GONE);
            btnLine.setVisibility(View.GONE);
        }
    }

    public void setDialogInfo(ZooerConstants.TwoBtnDialogInfo info) {
        this.dialogInfo = info;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                if (listener != null) {
                    listener.onLeftClick();
                }
                break;
            case R.id.btn_2:
                if (listener != null) {
                    listener.onRightClick();
                }
                break;
        }
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onLeftClick();

        void onRightClick();
    }


}
