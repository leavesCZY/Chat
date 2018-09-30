package hello.leavesC.chat.view.open;

import android.os.Bundle;
import android.widget.EditText;

import hello.leavesC.chat.R;
import hello.leavesC.chat.view.base.BaseActivity;
import hello.leavesC.presenter.event.RegisterEvent;
import hello.leavesC.presenter.viewModel.RegisterViewModel;
import hello.leavesC.presenter.viewModel.base.BaseViewModel;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:17
 * 说明：注册页面
 */
public class RegisterActivity extends BaseActivity {

    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        setToolbarTitle("注册");
        findViewById(R.id.btn_register).setOnClickListener(v -> {
            EditText et_registerIdentifier = findViewById(R.id.et_registerIdentifier);
            EditText et_registerPassword = findViewById(R.id.et_registerPassword);
            String identifier = et_registerIdentifier.getText().toString();
            String password = et_registerPassword.getText().toString();
            registerViewModel.register(identifier, password);
        });
    }

    @Override
    protected BaseViewModel initViewModel() {
        registerViewModel = new RegisterViewModel(getApplication());
        registerViewModel.getRegisterEventLiveData().observe(this, this::handleRegisterEvent);
        return registerViewModel;
    }

    private void handleRegisterEvent(RegisterEvent registerEvent) {
        switch (registerEvent.getAction()) {
            case RegisterEvent.REG_SUCCESS: {
                LoginActivity.navToLogin(this, registerEvent.getIdentifier());
                finish();
                break;
            }
        }
    }

}