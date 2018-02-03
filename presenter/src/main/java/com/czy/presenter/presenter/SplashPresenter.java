package com.czy.presenter.presenter;

import android.os.Handler;

import com.czy.presenter.view.SplashView;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:47
 * 说明：
 */
public class SplashPresenter {

    private SplashView splashView;

    public SplashPresenter(SplashView splashView) {
        this.splashView = splashView;
    }

    /**
     * 加载页面逻辑
     */
    public void start() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (splashView.needLoginTls()) {
                    splashView.navToLoginTls();
                } else {
                    splashView.loginImServer();
                }
            }
        }, 1000);
    }

}
