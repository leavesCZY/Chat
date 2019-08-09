package hello.leavesC.chat.business;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMSdkConfig;

import hello.leavesC.chat.BuildConfig;
import hello.leavesC.sdk.Constants;

/**
 * 作者：叶应是叶
 * 时间：2018/10/3 17:44
 * 描述：
 */
public class InitBusiness {

    public static void init(Application application) {
        initIM(application);
        initLeakCanary(application);
    }

    private static void initIM(Context context) {
        TIMSdkConfig sdkConfig = new TIMSdkConfig(Constants.SDK_APP_ID)
                .enableCrashReport(false)
                .setLogLevel(BuildConfig.isRelease ? TIMLogLevel.OFF : TIMLogLevel.WARN)
                .enableLogPrint(!BuildConfig.isRelease)
                .setLogPath("/chat/leavesC/logs/");
        TIMManager.getInstance().init(context, sdkConfig);
    }

    private static void initLeakCanary(Application application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            return;
        }
        LeakCanary.install(application);
    }

    private static boolean isMainProcess(Context context) {
        return context.getPackageName().equals(getCurrentProcessName(context));
    }

    private static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        String processName = null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
                if (process.pid == pid) {
                    processName = process.processName;
                }
            }
        }
        return processName;
    }

}