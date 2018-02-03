package com.czy.chat;

import android.app.Application;

import com.czy.presenter.business.InitBusiness;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:57
 * 说明：
 */
public class ChatApplication extends Application {

    private static final String TAG = "ChatApplication";

    public static String identifier = "";

    @Override
    public void onCreate() {
        super.onCreate();
        InitBusiness.init(this);
    }

}
