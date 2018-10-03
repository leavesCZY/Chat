package hello.leavesC.chat;

import android.app.Application;

import hello.leavesC.chat.business.InitBusiness;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:57
 * 说明：
 */
public class ChatApplication extends Application {

    public static String identifier = "";

    @Override
    public void onCreate() {
        super.onCreate();
        InitBusiness.init(this);
    }

}