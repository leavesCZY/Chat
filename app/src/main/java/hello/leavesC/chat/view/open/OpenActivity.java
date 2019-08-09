package hello.leavesC.chat.view.open;

import android.Manifest;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import hello.leavesC.chat.ChatApplication;
import hello.leavesC.chat.R;
import hello.leavesC.chat.view.MainActivity;
import hello.leavesC.chat.view.base.BaseActivity;
import hello.leavesC.presenter.event.SplashActionEvent;
import hello.leavesC.presenter.viewModel.SplashViewModel;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:17
 * 说明：开屏界面
 */
public class OpenActivity extends BaseActivity {

    private LinearLayout ll_loginRegister;

    private static final int REQUEST_CODE_PERMISSION = 100;

    private SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        initView();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    showToast("请授予权限");
                    finish();
                    return;
                }
            }
            splashViewModel.start();
        }
    }

    @Override
    protected ViewModel initViewModel() {
        splashViewModel = ViewModelProviders.of(this).get(SplashViewModel.class);
        splashViewModel.getEventLiveData().observe(this, this::handleEvent);
        splashViewModel.getNavToLoginLiveData().observe(this, profileModel -> {
            LoginActivity.navToLogin(OpenActivity.this, splashViewModel.getLastUserIdentifier());
            finish();
        });
        return splashViewModel;
    }

    private void handleEvent(SplashActionEvent splashActionEvent) {
        switch (splashActionEvent.getAction()) {
            case SplashActionEvent.LOGIN_OR_REGISTER: {
                showLoginRegister();
                break;
            }
            case SplashActionEvent.LOGIN_SUCCESS: {
                ChatApplication.identifier = splashViewModel.getLastUserIdentifier();
                startActivity(MainActivity.class);
                finish();
                break;
            }
        }
    }

    private void showLoginRegister() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(800);
        ll_loginRegister.setVisibility(View.VISIBLE);
        ll_loginRegister.startAnimation(alphaAnimation);
    }

    private void initView() {
        View.OnClickListener clickListener = v -> {
            switch (v.getId()) {
                case R.id.btn_openLogin:
                    LoginActivity.navToLogin(OpenActivity.this, splashViewModel.getLastUserIdentifier());
                    finish();
                    break;
                case R.id.btn_openRegister:
                    startActivity(RegisterActivity.class);
                    finish();
                    break;
            }
        };
        findViewById(R.id.btn_openLogin).setOnClickListener(clickListener);
        findViewById(R.id.btn_openRegister).setOnClickListener(clickListener);
        ll_loginRegister = findViewById(R.id.ll_loginRegister);
        ll_loginRegister.setVisibility(View.INVISIBLE);
    }

}