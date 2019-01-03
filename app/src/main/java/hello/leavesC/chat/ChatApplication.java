package hello.leavesC.chat;

import android.app.Application;

import hello.leavesC.chat.business.InitBusiness;

/**
 * 作者：leavesC
 * 时间：2017/11/29 20:57
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class ChatApplication extends Application {

    public static String identifier = "";

    @Override
    public void onCreate() {
        super.onCreate();
        InitBusiness.init(this);
    }

}