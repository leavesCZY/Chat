package hello.leavesC.chat.view.open;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.widget.EditText;

import hello.leavesC.chat.R;
import hello.leavesC.chat.view.base.BaseActivity;
import hello.leavesC.presenter.model.ProfileModel;
import hello.leavesC.presenter.viewModel.RegisterViewModel;

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
    protected ViewModel initViewModel() {
        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        registerViewModel.getRegSuccessLiveData().observe(this, this::handleRegisterEvent);
        return registerViewModel;
    }

    private void handleRegisterEvent(ProfileModel profileModel) {
        LoginActivity.navToLogin(this, profileModel.getIdentifier());
        finish();
    }

}