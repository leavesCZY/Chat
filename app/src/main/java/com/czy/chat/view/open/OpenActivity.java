package com.czy.chat.view.open;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

import com.czy.chat.ChatApplication;
import com.czy.chat.R;
import com.czy.chat.view.MainActivity;
import com.czy.chat.view.base.BaseActivity;
import com.czy.presenter.business.LoginBusiness;
import com.czy.presenter.event.FriendEvent;
import com.czy.presenter.event.GroupEvent;
import com.czy.presenter.event.MessageEvent;
import com.czy.presenter.event.RefreshEvent;
import com.czy.presenter.log.Logger;
import com.czy.presenter.presenter.SplashPresenter;
import com.czy.presenter.view.SplashView;
import com.czy.tls.service.TlsService;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:17
 * 说明：开屏界面
 */
public class OpenActivity extends BaseActivity implements SplashView {

    private static final int REQUEST_CODE_LOGIN = 10;

    private static final int REQUEST_CODE_REGISTER = 20;

    private static final int REQUEST_CODE_MAIN = 30;

    public static final String IDENTIFIER = "identifier";

    private LinearLayout ll_login_register;

    private static final String TAG = "OpenActivity";

    private static final int REQUEST_CODE_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        ll_login_register = (LinearLayout) findViewById(R.id.ll_login_register);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
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
            new SplashPresenter(this).start();
        }
    }

    /**
     * 判断用户是否需要登录TLS
     */
    @Override
    public boolean needLoginTls() {
        return TlsService.getInstance(this).needLogin(TlsService.getInstance(this).getLastUserIdentifier());
    }

    /**
     * 跳转到TLS开屏界面
     */
    @Override
    public void navToLoginTls() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_open_login:
                        LoginActivity.navigation(OpenActivity.this, REQUEST_CODE_LOGIN, TlsService.getInstance(OpenActivity.this).getLastUserIdentifier());
                        break;
                    case R.id.btn_open_register:
                        RegisterActivity.navigation(OpenActivity.this, REQUEST_CODE_REGISTER);
                        break;
                }
            }
        };
        Button btn_open_login = (Button) findViewById(R.id.btn_open_login);
        Button btn_open_register = (Button) findViewById(R.id.btn_open_register);
        btn_open_login.setOnClickListener(clickListener);
        btn_open_register.setOnClickListener(clickListener);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(600);
        ll_login_register.setVisibility(View.VISIBLE);
        ll_login_register.startAnimation(alphaAnimation);
    }

    /**
     * 登录IM服务器
     */
    @Override
    public void loginImServer() {
        //登录之前要先初始化群和好友关系链缓存
        TIMUserConfig userConfig = new TIMUserConfig();
        userConfig = FriendEvent.getInstance().init(userConfig);
        userConfig = GroupEvent.getInstance().init(userConfig);
        userConfig = MessageEvent.getInstance().init(userConfig);
        RefreshEvent.getInstance().init(userConfig);
        userConfig.setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                Logger.e(TAG, "被其他终端踢下线");
                if (!isDestroyed()) {
                    showToast("在其他设备上登录了本账号，请重新登录");
                    TlsService.getInstance(OpenActivity.this).clearUserInfo();
                    startActivity(OpenActivity.class);
                }
            }

            @Override
            public void onUserSigExpired() {
                Logger.e(TAG, "用户签名过期了，需要刷新userSig重新登录SDK");
            }
        });
        userConfig.setConnectionListener(new TIMConnListener() {
            @Override
            public void onConnected() {
                Logger.e(TAG, "onConnected");
            }

            @Override
            public void onDisconnected(int i, String s) {
                Logger.e(TAG, "onDisconnected: " + i + " " + s);
            }

            @Override
            public void onWifiNeedAuth(String s) {
                Logger.e(TAG, "onWifiNeedAuth:" + s);
            }
        });
        TIMManager.getInstance().setUserConfig(userConfig);
        final TlsService tlsService = TlsService.getInstance(this);
        LoginBusiness.loginImServer(tlsService.getLastUserIdentifier(), tlsService.getUserSignature(tlsService.getLastUserIdentifier()), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                showToast("登录失败");
                navToLoginTls();
            }

            @Override
            public void onSuccess() {
                showToast("登录成功");
                ChatApplication.identifier = tlsService.getLastUserIdentifier();
                ll_login_register.setVisibility(View.INVISIBLE);
                startActivityForResult(MainActivity.class, REQUEST_CODE_MAIN);
                finish();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        navToLoginTls();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_LOGIN: {
                if (resultCode == RESULT_OK) {
                    ll_login_register.setVisibility(View.GONE);
                    loginImServer();
                }
                break;
            }
            case REQUEST_CODE_REGISTER: {
                if (resultCode == RESULT_OK) {
                    ll_login_register.setVisibility(View.GONE);
                    LoginActivity.navigation(this, REQUEST_CODE_LOGIN, data.getStringExtra(OpenActivity.IDENTIFIER));
                }
                break;
            }
            case REQUEST_CODE_MAIN: {
                if (resultCode == RESULT_OK) {
                    finish();
                    return;
                }
                break;
            }
        }
    }

}
