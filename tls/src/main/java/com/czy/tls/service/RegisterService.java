package com.czy.tls.service;

import android.content.Context;

import com.czy.tls.callback.RegisterListener;

import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * 作者：叶应是叶
 * 时间：2017/11/27 23:29
 * 说明：账号注册服务
 */
public class RegisterService {

    private TlsService tlsService;

    private RegisterListener registerListener;

    private TLSStrAccRegListener tlsStrAccRegListener = new TLSStrAccRegListener() {
        @Override
        public void OnStrAccRegSuccess(TLSUserInfo tlsUserInfo) {
            registerListener.onRegisterSuccess(tlsUserInfo.identifier);
        }

        @Override
        public void OnStrAccRegFail(TLSErrInfo tlsErrInfo) {
            registerListener.onRegisterFail(tlsErrInfo.Msg);
        }

        @Override
        public void OnStrAccRegTimeout(TLSErrInfo tlsErrInfo) {
            registerListener.onRegisterTimeout();
        }
    };

    public RegisterService(Context context) {
        tlsService = TlsService.getInstance(context);
    }

    public void register(String userName, String password, RegisterListener registerListener) {
        this.registerListener = registerListener;
        if (tlsService.register(userName, password, tlsStrAccRegListener) == TLSErrInfo.INPUT_INVALID) {
            this.registerListener.onFormatInvalid();
        }
    }

}
