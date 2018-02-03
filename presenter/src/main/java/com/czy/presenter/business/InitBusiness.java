package com.czy.presenter.business;

import android.content.Context;

import com.czy.sdk.Constants;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMSdkConfig;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:42
 * 说明：初始化IM_SDK，登录前调用，在多进程的情况下，只在一个进程进行SDK的初始化
 */
public class InitBusiness {

    private InitBusiness() {
    }

    public static void init(Context context) {
        TIMSdkConfig sdkConfig = new TIMSdkConfig(Constants.SDK_APP_ID);
        //设置是否开启Bugly的crash上报功能
        sdkConfig.enableCrashReport(true);
        //设置是否把日志输出到控制台
        sdkConfig.enableLogPrint(true);
        //设置日志等级
        sdkConfig.setLogLevel(TIMLogLevel.WARN);
        //初始化IM_SDK
        TIMManager.getInstance().init(context, sdkConfig);
    }

}
