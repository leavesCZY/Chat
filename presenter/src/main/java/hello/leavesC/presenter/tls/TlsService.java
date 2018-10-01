package hello.leavesC.presenter.tls;

import android.content.Context;

import hello.leavesC.sdk.Constants;
import tencent.tls.platform.TLSLoginHelper;
import tencent.tls.platform.TLSUserInfo;

/**
 * 作者：叶应是叶
 * 时间：2017/11/27 23:30
 * 说明：TLS管理
 */
public class TlsService {

    private static TlsService tlsService;

    private TLSLoginHelper loginHelper;

    private TlsService(Context context) {
        loginHelper = TLSLoginHelper.getInstance().init(context.getApplicationContext(), Constants.SDK_APP_ID, Constants.ACCOUNT_TYPE, Constants.APP_VERSION);
    }

    public static TlsService getInstance(Context context) {
        if (tlsService == null) {
            synchronized (TlsService.class) {
                if (tlsService == null) {
                    tlsService = new TlsService(context);
                }
            }
        }
        return tlsService;
    }

    /**
     * 获取最后登录用户的信息
     */
    private TLSUserInfo getLastUserInfo() {
        return loginHelper.getLastUserInfo();
    }

    /**
     * 获取最后登录用户的账号
     */
    public String getLastUserIdentifier() {
        TLSUserInfo userInfo = getLastUserInfo();
        if (userInfo != null) {
            return userInfo.identifier;
        }
        return null;
    }

    /**
     * 删除用户信息
     */
    public void clearUserInfo() {
        loginHelper.clearUserInfo(getLastUserIdentifier());
    }

}