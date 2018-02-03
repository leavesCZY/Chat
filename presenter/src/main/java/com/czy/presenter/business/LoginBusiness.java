package com.czy.presenter.business;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:42
 * 说明：登录与注销
 */
public class LoginBusiness {

    private LoginBusiness() {
    }

    /**
     * 登录IM服务器
     *
     * @param identify      用户账号
     * @param userSignature 用户签名
     * @param callBack      回调函数
     */
    public static void loginImServer(String identify, String userSignature, TIMCallBack callBack) {
        TIMManager.getInstance().login(identify, userSignature, callBack);
    }

    /**
     * 注销
     *
     * @param callBack 回调函数
     */
    public static void logoutImServer(TIMCallBack callBack) {
        TIMManager.getInstance().logout(callBack);
    }

}
