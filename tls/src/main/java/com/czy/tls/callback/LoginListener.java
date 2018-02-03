package com.czy.tls.callback;

/**
 * 作者：叶应是叶
 * 时间：2017/11/27 23:28
 * 说明：登录接口
 */
public interface LoginListener {

    //登陆成功时回调
    void onLoginSuccess();

    //登录失败时回调
    void onLoginFail(String error);

    //需要输入图片验证码时回调
    void onLoginNeedImageCode(byte[] imageBytes);

    //重新请求图片验证码时回调
    void onRequestImageCodeAgain(byte[] imageBytes);

    //登录超时时回调
    void onLoginTimeout();

}