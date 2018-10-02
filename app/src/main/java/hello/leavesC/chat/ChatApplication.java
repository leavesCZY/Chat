package hello.leavesC.chat;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
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
        if (isMainProcess()) {
            initIM();
        }
        initLeakCanary();
    }

    private void initIM() {
        TIMSdkConfig sdkConfig = new TIMSdkConfig(Constants.SDK_APP_ID)
                .enableCrashReport(true)
                .setLogLevel(BuildConfig.isRelease ? TIMLogLevel.OFF : TIMLogLevel.WARN)
                .enableLogPrint(!BuildConfig.isRelease)
                .setLogPath("/chat/leavesC/logs/");
        TIMManager.getInstance().init(getApplicationContext(), sdkConfig);
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    /**
     * 获取当前进程名
     */
    private String getCurrentProcessName() {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }

    /**
     * 包名判断是否为主进程
     */
    public boolean isMainProcess() {
        return getApplicationContext().getPackageName().equals(getCurrentProcessName());
    }

}