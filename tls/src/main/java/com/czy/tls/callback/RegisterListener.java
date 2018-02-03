package com.czy.tls.callback;

/**
 * 作者：叶应是叶
 * 时间：2017/11/27 23:28
 * 说明：注册接口
 */
public interface RegisterListener {

    void onRegisterSuccess(String identifier);

    void onRegisterFail(String error);

    void onRegisterTimeout();

    void onFormatInvalid();

}
