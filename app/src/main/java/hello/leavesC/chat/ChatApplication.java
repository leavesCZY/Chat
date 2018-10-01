package hello.leavesC.chat;

import android.app.Application;

import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMSdkConfig;

import hello.leavesC.sdk.Constants;

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
        initIM();
    }

    private void initIM() {
        TIMSdkConfig sdkConfig = new TIMSdkConfig(Constants.SDK_APP_ID)
                .enableCrashReport(true)
                .setLogLevel(BuildConfig.isRelease ? TIMLogLevel.OFF : TIMLogLevel.WARN)
                .enableLogPrint(!BuildConfig.isRelease)
                .setLogPath("/chat/leavesC/logs/");
        TIMManager.getInstance().init(getApplicationContext(), sdkConfig);
    }

}