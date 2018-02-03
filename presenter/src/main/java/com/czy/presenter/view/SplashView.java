package com.czy.presenter.view;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:50
 * 说明：
 */
public interface SplashView {

    /**
     * 判断用户是否需要登录TLS
     */
    boolean needLoginTls();

    /**
     * 跳转到TLS登录界面
     */
    void navToLoginTls();

    /**
     * 登录IM服务器
     */
    void loginImServer();

}
