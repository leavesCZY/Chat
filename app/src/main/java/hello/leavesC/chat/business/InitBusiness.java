package hello.leavesC.chat.business;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMSdkConfig;

import hello.leavesC.chat.BuildConfig;
import hello.leavesC.chat.view.MainActivity;
import hello.leavesC.sdk.Constants;

/**
 * 作者：叶应是叶
 * 时间：2018/10/3 17:44
 * 描述：
 */
public class InitBusiness {

    public static void init(Application application) {
        initBugly(application);
        initIM(application);
        initLeakCanary(application);
    }

    private static void initBugly(Context context) {
        Beta.autoInit = true;
        Beta.autoCheckUpgrade = true;
        Beta.upgradeCheckPeriod = 60 * 1000;
        Beta.initDelay = 10 * 1000;
        Beta.showInterruptedStrategy = true;
        Beta.canShowUpgradeActs.add(MainActivity.class);
        Beta.canShowApkInfo = true;
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(isMainProcess(context));
        CrashReport.setIsDevelopmentDevice(context, BuildConfig.DEBUG);
        Bugly.init(context, String.valueOf(Constants.SDK_APP_ID), !BuildConfig.isRelease, strategy);
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