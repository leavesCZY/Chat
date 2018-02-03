package com.czy.tls.service;

import android.content.Context;

import com.czy.tls.callback.LoginListener;

import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * 作者：叶应是叶
 * 时间：2017/11/27 23:29
 * 说明：账号登录服务
 */
public class LoginService {

    private TlsService tlsService;

    private LoginListener loginListener;

    private TLSPwdLoginListener tlsPwdLoginListener = new TLSPwdLoginListener() {
        @Override
        public void OnPwdLoginSuccess(TLSUserInfo tlsUserInfo) {
            loginListener.onLoginSuccess();
        }

        @Override
        public void OnPwdLoginReaskImgcodeSuccess(byte[] bytes) {
            loginListener.onRequestImageCodeAgain(bytes);
        }

        @Override
        public void OnPwdLoginNeedImgcode(byte[] bytes, TLSErrInfo tlsErrInfo) {
            loginListener.onLoginNeedImageCode(bytes);
        }

        @Override
        public void OnPwdLoginFail(TLSErrInfo tlsErrInfo) {
            loginListener.onLoginFail(tlsErrInfo.Msg);
        }

        @Override
        public void OnPwdLoginTimeout(TLSErrInfo tlsErrInfo) {
            loginListener.onLoginTimeout();
        }
    };

    public LoginService(Context context) {
        this.tlsService = TlsService.getInstance(context);
    }

    public void login(String userName, String password, LoginListener loginListener) {
        this.loginListener = loginListener;
        this.tlsService.login(userName, password, tlsPwdLoginListener);
    }

    public void verifyImageCode(String code) {
        tlsService.verifyImageCode(code, tlsPwdLoginListener);
    }

}
