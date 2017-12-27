package com.nokia.vlm.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nokia.vlm.R;
import com.nokia.vlm.callback.LoginCallBack;
import com.nokia.vlm.dao.LoginEngine;
import com.nokia.vlm.entity.UserInfo;
import com.nokia.vlm.manager.UserInfoManager;
import com.nokia.vlm.ui.QXApp;
import com.qx.framelib.activity.BaseTitleActivity;
import com.qx.framelib.utlis.TextUtil;
import com.qx.framelib.utlis.ToastUtils;


/**
 * Created by Administrator on 2017/2/4.
 */

public class LoginActivity extends BaseTitleActivity implements View.OnClickListener,LoginCallBack {

    protected AppCompatButton btnLogin;
    protected TextView tvRegister;
    protected TextView tvForgetPwd;
    protected EditText etMobile;
    protected EditText etPwd;
    private ImageView eye;
    private boolean isEyeOpen = false; //密码不可见
    public static LoginActivity instance;

    private LoginEngine loginEngine = new LoginEngine();

    public static final String BEFORE_PAGE = "login_out";
    public static final String KEY_PAGE = "login_out";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_login);
        setCheckNetWork(false);
        setStatusBarEnald(true);
        closeOtherActivity();
        if (!TextUtil.isEmpty(UserInfoManager.getInstance().getToken())){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
        initTopBar();
        initView();
        registers();
        instance = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisters();
    }

    private void initTopBar() {
        setTitleViewVisible(View.GONE);
        setLeftVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_btn_submit) {
            login();
            //        } else if (view.getId() == R.id.login_tv_register) {
            //
            //            startActivity(RegisterOneActivity.class);
//        } else if (view.getId() == R.id.login_tv_forget_pwd) {
//            startActivity(ForgetPwdOneActivity.class);
        } else if (view.getId() == R.id.login_btn_eye) {
            if (isEyeOpen) {
                isEyeOpen = false;
                eye.setImageResource(R.drawable.icon_close_eye);
                editPassword();
            } else {
                isEyeOpen = true;
                eye.setImageResource(R.drawable.icon_open_eye);
                editPassword();
            }
        }
    }

    private void initView() {
        btnLogin = (AppCompatButton) findViewById(R.id.login_btn_submit);
        btnLogin.setOnClickListener(LoginActivity.this);
//        tvRegister = (TextView) findViewById(R.id.login_tv_register);
//        tvForgetPwd = (TextView) findViewById(R.id.login_tv_forget_pwd);
//        tvForgetPwd.setOnClickListener(LoginActivity.this);
        etMobile = (EditText) findViewById(R.id.login_et_mobile);
        etPwd = (EditText) findViewById(R.id.login_et_password);
        eye = (ImageView) findViewById(R.id.login_btn_eye);
        eye.setVisibility(View.VISIBLE);
        eye.setOnClickListener(this);
        etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private void editPassword() {
        if(isEyeOpen){
            //显示密码
            etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else{
            //隐藏密码
            etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    private void login() {
//        String mobileStr = etMobile.getText().toString().trim();
//        String pwdStr = etPwd.getText().toString().trim();
//        if (TextUtil.isEmpty(mobileStr) || TextUtil.isEmpty(pwdStr)) return;
//        loginEngine.username = mobileStr;
//        loginEngine.password = pwdStr;
//        loginEngine.sendLogin(mobileStr, pwdStr);
//        showDialogLoading("正在登录");

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 关闭其它Activity
     */
    private void closeOtherActivity() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        String value = bundle.getString(KEY_PAGE);
        if (BEFORE_PAGE.equals(value)) { //如果是退出登录

            Intent intent = new Intent();
            intent.setAction(QXApp.INTENT_ACTION_EXIT_APP);
            String activity = this.getClass().getName();
            intent.putExtra("activity", activity);
            sendBroadcast(intent);
        }

    }

    private void registers() {
        loginEngine.register(this);
    }

    private void unregisters() {
        if (loginEngine != null) {
            loginEngine.unregister(this);
        }
    }

    @Override
    public void sendLogin(int code, String msg, UserInfo userInfo) {
        dismissDialogLoading();

        if (code == 1) {
            /*if (UserInfoManager.getInstance().getUserInfo()==null ||UserInfoManager.getInstance().getUserInfo().id==null||UserInfoManager.getInstance().getUserInfo().id != userInfo.id) {
                MessageManager.getInstanse().clearAll();
            }*/

            UserInfoManager.getInstance().updateUserInfo(userInfo);
//            JPushUtils.setAlias(userInfo.id);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

            startActivity(intent);

            finish();
        } else {
            ToastUtils.getInstance().show(msg);
        }
    }
}
