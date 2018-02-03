package com.czy.chat.view.open;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.czy.chat.R;
import com.czy.chat.view.base.BaseActivity;
import com.czy.tls.callback.LoginListener;
import com.czy.tls.service.LoginService;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:16
 * 说明：登录界面
 */
public class LoginActivity extends BaseActivity implements LoginListener {

    private EditText et_login_identifier;

    private EditText et_login_password;

    private LoginService loginService;

    private static final String NAME_USER_INFO = "userInfo";

    private static final String LAST_IDENTIFIER = "lastIdentifier";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginService = new LoginService(this);
        initView();
    }

    private void initView() {
        initToolbar(R.id.toolbar_login, "登录");
        et_login_identifier = (EditText) findViewById(R.id.et_login_identifier);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        String identifier = getIntent().getStringExtra(OpenActivity.IDENTIFIER);
        if (TextUtils.isEmpty(identifier)) {
            SharedPreferences sharedPreferences = getSharedPreferences(NAME_USER_INFO, Context.MODE_PRIVATE);
            identifier = sharedPreferences.getString(LAST_IDENTIFIER, "");
        }
        et_login_identifier.setText(identifier);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String identifier = et_login_identifier.getText().toString();
                if (identifier.length() < 5) {
                    showToast("用户名需要至少五位");
                    return;
                }
                String password = et_login_password.getText().toString();
                if (password.length() < 8) {
                    showToast("密码需要至少八位");
                    return;
                }
                showLoadingDialog("正在登录...", false, false);
                loginService.login(identifier, password, LoginActivity.this);
            }
        });
    }

    public static void navigation(Activity activity, int requestCode, String identifier) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra(OpenActivity.IDENTIFIER, identifier);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onLoginSuccess() {
        SharedPreferences sharedPreferences = getSharedPreferences(NAME_USER_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_IDENTIFIER, et_login_identifier.getText().toString());
        editor.apply();
        dismissLoadingDialog();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onLoginFail(String error) {
        dismissLoadingDialog();
        showToast("登录失败," + error);
    }

    @Override
    public void onLoginNeedImageCode(byte[] imageBytes) {
        dismissLoadingDialog();
        showToast("需要验证码");
    }

    @Override
    public void onRequestImageCodeAgain(byte[] imageBytes) {
        dismissLoadingDialog();
        showToast("重新请求验证码");
    }

    @Override
    public void onLoginTimeout() {
        dismissLoadingDialog();
        showToast("登录超时");
    }

}
