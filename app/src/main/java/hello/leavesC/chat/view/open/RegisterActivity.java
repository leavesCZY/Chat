package hello.leavesC.chat.view.open;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import hello.leavesC.chat.R;
import hello.leavesC.chat.view.base.BaseActivity;
import hello.leavesC.presenter.event.RegisterEvent;
import hello.leavesC.presenter.viewModel.RegisterViewModel;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:17
 * 说明：注册页面
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_register_identifier;

    private EditText et_register_password;

    private RegisterViewModel registerViewModel;

    public static void navigation(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initViewModel();
    }

    private void initView() {
        setToolbarTitle("注册");
        et_register_identifier = findViewById(R.id.et_register_identifier);
        et_register_password = findViewById(R.id.et_register_password);
        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
    }

    private void initViewModel() {
        registerViewModel = new RegisterViewModel(getApplication());
        registerViewModel.getRegisterEventLiveData().observe(this, this::handleRegisterEvent);
    }

    private void handleRegisterEvent(RegisterEvent registerEvent) {
        switch (registerEvent.getAction()) {
            case RegisterEvent.REG_SUCCESS: {
                dismissLoadingDialog();
                showToast("注册成功");
                Intent intent = new Intent();
                intent.putExtra(OpenActivity.IDENTIFIER, registerEvent.getIdentifier());
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
            case RegisterEvent.REG_FAIL:
            case RegisterEvent.FORMAT_INVALID: {
                dismissLoadingDialog();
                showToast(registerEvent.getErrorMsg());
                break;
            }
        }
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
                showLoadingDialog("正在注册...");
                registerViewModel.register(identifier, password);
                break;
        }
    }

}