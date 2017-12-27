package com.nokia.vlm.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.qx.framelib.dialog.WrapBottomSheetDialog;
import com.nokia.vlm.R;


/**
 * 键盘
 */

public class KeyBoardDialog extends WrapBottomSheetDialog implements View.OnClickListener {
    private Context mContext;
    protected ImageView btnClose;
    protected EditText pwd1;
    protected EditText pwd2;
    protected EditText pwd3;
    protected EditText pwd4;
    protected EditText pwd5;
    protected EditText pwd6;
    protected Button btnFindPayPwd;
    protected Button num1;
    protected Button num2;
    protected Button num3;
    protected Button num4;
    protected Button num5;
    protected Button num6;
    protected Button num7;
    protected Button num8;
    protected Button num9;
    protected ImageButton btnDel;
    protected Button num0;
    protected TextView btnOk;
    protected TextView txtTitle;

    private EditText[] pwds;
    StringBuffer sb = new StringBuffer();

    private OnInputPasswordListener listener;


    public KeyBoardDialog setOnInputPasswordListener(OnInputPasswordListener listener) {
        this.listener = listener;
        return this;
    }

    private KeyBoardDialog(@NonNull Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pay_password, null);
        setContentView(view);
        initView(view);

        pwds = new EditText[]{pwd1, pwd2, pwd3, pwd4, pwd5, pwd6};
        this.mContext = context;
    }

    public static KeyBoardDialog init(Context context) {
        return new KeyBoardDialog(context);
    }


    public KeyBoardDialog setOnDialogCancelListener(@Nullable OnCancelListener listener) {
        super.setOnCancelListener(listener);
        return this;
    }

    public KeyBoardDialog setOnDialogDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.num_0:
            case R.id.num_1:
            case R.id.num_2:
            case R.id.num_3:
            case R.id.num_4:
            case R.id.num_5:
            case R.id.num_6:
            case R.id.num_7:
            case R.id.num_8:
            case R.id.num_9:
                if (sb.length() >= 6) return;
                String password = ((Button) view).getText().toString();
                setPassword(password);
                break;
            case R.id.btn_del: //删除
                if (sb.length() <= 0) return;
                delPassword();
                break;
            case R.id.btn_ok: //完成
                if (sb.length() == 6) {

                    if (listener != null) {
                        listener.onInputPassword(this, sb.toString());
                    }
                }
                break;
            case R.id.btn_close:
                dismiss();
                break;

            case R.id.btn_find_pay_pwd:
//                Intent intent = new Intent(mContext, ForgetPayPwdActivity.class);
//                mContext.startActivity(intent);
                break;

        }
    }

    private void initView(View rootView) {
        pwd1 = (EditText) rootView.findViewById(R.id.pwd1);
        pwd2 = (EditText) rootView.findViewById(R.id.pwd2);
        pwd3 = (EditText) rootView.findViewById(R.id.pwd3);
        pwd4 = (EditText) rootView.findViewById(R.id.pwd4);
        pwd5 = (EditText) rootView.findViewById(R.id.pwd5);
        pwd6 = (EditText) rootView.findViewById(R.id.pwd6);
        btnFindPayPwd = (Button) rootView.findViewById(R.id.btn_find_pay_pwd);
        btnFindPayPwd.setOnClickListener(KeyBoardDialog.this);
        num1 = (Button) rootView.findViewById(R.id.num_1);
        num1.setOnClickListener(KeyBoardDialog.this);
        num2 = (Button) rootView.findViewById(R.id.num_2);
        num2.setOnClickListener(KeyBoardDialog.this);
        num3 = (Button) rootView.findViewById(R.id.num_3);
        num3.setOnClickListener(KeyBoardDialog.this);
        num4 = (Button) rootView.findViewById(R.id.num_4);
        num4.setOnClickListener(KeyBoardDialog.this);
        num5 = (Button) rootView.findViewById(R.id.num_5);
        num5.setOnClickListener(KeyBoardDialog.this);
        num6 = (Button) rootView.findViewById(R.id.num_6);
        num6.setOnClickListener(KeyBoardDialog.this);
        num7 = (Button) rootView.findViewById(R.id.num_7);
        num7.setOnClickListener(KeyBoardDialog.this);
        num8 = (Button) rootView.findViewById(R.id.num_8);
        num8.setOnClickListener(KeyBoardDialog.this);
        num9 = (Button) rootView.findViewById(R.id.num_9);
        num9.setOnClickListener(KeyBoardDialog.this);
        btnDel = (ImageButton) rootView.findViewById(R.id.btn_del);
        btnDel.setOnClickListener(KeyBoardDialog.this);
        num0 = (Button) rootView.findViewById(R.id.num_0);
        num0.setOnClickListener(KeyBoardDialog.this);
        btnOk = (TextView) rootView.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(KeyBoardDialog.this);
        rootView.findViewById(R.id.btn_close).setOnClickListener(KeyBoardDialog.this);
        txtTitle = (TextView) rootView.findViewById(R.id.txt_title);
        txtTitle.setText("输入支付密码");
    }

    private void setPassword(String password) {
        int len = sb.length();
        pwds[len].setText(password);
        sb.append(password);

        if (sb.length() == 6) {
//            btnOk.setEnabled(true);
            if (listener != null) {
                listener.onInputPassword(this, sb.toString());
            }

        }
    }

    private void delPassword() {
        int len = sb.length();
        pwds[len - 1].setText(""); //删除密码
        sb.deleteCharAt(len - 1);

        if (btnOk.isEnabled())
            btnOk.setEnabled(false);
    }

    public interface OnInputPasswordListener {
        void onInputPassword(KeyBoardDialog dialog, String password);
    }

}
