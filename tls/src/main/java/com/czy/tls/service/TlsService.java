package com.czy.tls.service;

import android.content.Context;

import com.czy.sdk.Constants;

import tencent.tls.platform.TLSAccountHelper;
import tencent.tls.platform.TLSLoginHelper;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * 作者：叶应是叶
 * 时间：2017/11/27 23:30
 * 说明：TLS管理
 */
public class TlsService {

    private static TlsService tlsService;

    private TLSLoginHelper loginHelper;

    private TLSAccountHelper accountHelper;

    private TlsService(Context context) {
        loginHelper = TLSLoginHelper.getInstance().init(context.getApplicationContext(), Constants.SDK_APP_ID, Constants.ACCOUNT_TYPE, Constants.APP_VERSION);
        accountHelper = TLSAccountHelper.getInstance().init(context.getApplicationContext(), Constants.SDK_APP_ID, Constants.ACCOUNT_TYPE, Constants.APP_VERSION);
    }

    public static TlsService getInstance(Context context) {
        if (tlsService == null) {
            synchronized (TlsService.class) {
                if (tlsService == null) {
                    tlsService = new TlsService(context);
                }
            }
        }
        return tlsService;
    }

    /**
     * 根据字符串账号名与密码来注册
     *
     * @param userName 用户名
     * @param password 密码
     * @param listener 回调函数
     * @return 结果码
     */
    int register(String userName, String password, TLSStrAccRegListener listener) {
        return accountHelper.TLSStrAccReg(userName, password, listener);
    }

    /**
     * 根据用户名与密码登录
     *
     * @param userName 用户名
     * @param password 密码
     * @param listener 回调函数
     */
    void login(String userName, String password, TLSPwdLoginListener listener) {
        loginHelper.TLSPwdLogin(userName, password.getBytes(), listener);
    }

    /**
     * @param identify 用户帐号
     * @return 用户帐号签名，由私钥加密获得
     */
    public String getUserSignature(String identify) {
        return loginHelper.getUserSig(identify);
    }

    /**
     * 获取最后登录用户的信息
     */
    private TLSUserInfo getLastUserInfo() {
        return loginHelper.getLastUserInfo();
    }

    /**
     * 获取最后登录用户的账号
     */
    public String getLastUserIdentifier() {
        TLSUserInfo userInfo = getLastUserInfo();
        if (userInfo != null) {
            return userInfo.identifier;
        }
        return null;
    }

    /**
     * 删除用户信息
     */
    public void clearUserInfo() {
        loginHelper.clearUserInfo(getLastUserIdentifier());
    }

    /**
     * 判断是否需要登录
     *
     * @param identifier 用户账号
     * @return 是否需要登录
     */
    public boolean needLogin(String identifier) {
        return loginHelper.needLogin(identifier);
    }

    /**
     * 判断输入的字符与图片验证码是否相符
     *
     * @param imageCode 输入的验证码
     * @param listener  回调函数
     */
    void verifyImageCode(String imageCode, TLSPwdLoginListener listener) {
        loginHelper.TLSPwdLoginVerifyImgcode(imageCode, listener);
    }

}