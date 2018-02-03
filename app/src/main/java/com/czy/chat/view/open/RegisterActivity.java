package com.czy.chat.view.open;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.czy.chat.R;
import com.czy.chat.view.base.BaseActivity;
import com.czy.tls.callback.RegisterListener;
import com.czy.tls.service.RegisterService;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:17
 * 说明：注册页面
 */
public class RegisterActivity extends BaseActivity implements RegisterListener, View.OnClickListener {

    private EditText et_register_identifier;

    private EditText et_register_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        initToolbar(R.id.toolbar_register, "注册", true);
        et_register_identifier = (EditText) findViewById(R.id.et_register_identifier);
        et_register_password = (EditText) findViewById(R.id.et_register_password);
        Button btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                String identifier = et_register_identifier.getText().toString();
                if (identifier.length() < 5) {
                    showToast("用户名至少五位");
                    return;
                }
                String password = et_register_password.getText().toString();
                if (password.length() < 8) {
                    showToast("密码至少八位");
                    return;
                }
                showLoadingDialog("正在注册...", false, false);
                new RegisterService(RegisterActivity.this).register(identifier, password, RegisterActivity.this);
                break;
        }
    }

    public static void navigation(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onRegisterSuccess(String identifier) {
        dismissLoadingDialog();
        showToast("注册成功");
        Intent intent = new Intent();
        intent.putExtra(OpenActivity.IDENTIFIER, identifier);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRegisterFail(String error) {
        dismissLoadingDialog();
        showToast("注册失败");
    }

    @Override
    public void onRegisterTimeout() {
        dismissLoadingDialog();
        showToast("注册超时");
    }

    @Override
    public void onFormatInvalid() {
        dismissLoadingDialog();
        showToast("输入参数有误");
    }

}
