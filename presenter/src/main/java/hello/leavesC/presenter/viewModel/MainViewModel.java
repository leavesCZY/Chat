package hello.leavesC.presenter.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;

import java.util.List;

import hello.leavesC.presenter.business.LoginBusiness;
import hello.leavesC.presenter.event.FriendEvent;
import hello.leavesC.presenter.event.GroupEvent;
import hello.leavesC.presenter.event.MessageEvent;
import hello.leavesC.presenter.event.RefreshEvent;
import hello.leavesC.presenter.log.Logger;
import hello.leavesC.sdk.Constants;
import tencent.tls.platform.TLSAccountHelper;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSLoginHelper;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSRefreshUserSigListener;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * 作者：叶应是叶
 * 时间：2018/9/28 21:49
 * 描述：
 */
public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";

    public static final int NAV_TO_LOGIN_TLS = 10;

    public static final int LOGIN_IM_SERVER_FAIL = 20;

    public static final int LOGIN_IM_SERVER_SUCCESS = 30;

    public static final int FORCE_OFFLINE = 40;

    private MediatorLiveData<Integer> actionLiveData = new MediatorLiveData<>();

    private TLSLoginHelper loginHelper;

    private TLSAccountHelper accountHelper;

    public MainViewModel(@NonNull Application application) {
        super(application);
        loginHelper = TLSLoginHelper.getInstance().init(application,
                Constants.SDK_APP_ID, Constants.ACCOUNT_TYPE, Constants.APP_VERSION);

    }

    /**
     * 根据用户名与密码来注册
     *
     * @param identify 用户名
     * @param password 密码
     * @param listener 回调函数
     * @return 结果码
     */
    public int register(String identify, String password, TLSStrAccRegListener listener) {
        return accountHelper.TLSStrAccReg(identify, password, new TLSStrAccRegListener() {
            @Override
            public void OnStrAccRegSuccess(TLSUserInfo tlsUserInfo) {

            }

            @Override
            public void OnStrAccRegFail(TLSErrInfo tlsErrInfo) {

            }

            @Override
            public void OnStrAccRegTimeout(TLSErrInfo tlsErrInfo) {

            }
        });
    }

    /**
     * 根据用户名与密码登录
     *
     * @param identify 用户名
     * @param password 密码
     * @param listener 回调函数
     */
    public void login(String identify, String password, TLSPwdLoginListener listener) {
        loginHelper.TLSPwdLogin(identify, password.getBytes(), listener);
    }

    /**
     * 判断是否需要登录
     *
     * @param identifier 用户账号
     * @return 是否需要登录
     */
    public boolean needLogin(String identifier) {
        boolean flag = loginHelper.needLogin(identifier);
        if (!flag) {
            refreshUserSig(identifier);
        }
        return flag;
    }

    public void login() {
        String identifier = getLastUserIdentifier();
        if (TextUtils.isEmpty(identifier)) {
            actionLiveData.setValue(NAV_TO_LOGIN_TLS);
        } else {
            if (loginHelper.needLogin(identifier)) {
                actionLiveData.setValue(NAV_TO_LOGIN_TLS);
            } else {
                loginImServer();
            }
        }
    }

    /**
     * 登录IM服务器
     */
    public void loginImServer() {
        //登录之前要先初始化群和好友关系链缓存
        TIMUserConfig userConfig = new TIMUserConfig();
        FriendEvent.getInstance().init(userConfig);
        GroupEvent.getInstance().init(userConfig);
        MessageEvent.getInstance().init(userConfig);
        RefreshEvent.getInstance().init(userConfig);
        userConfig.setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                actionLiveData.setValue(FORCE_OFFLINE);
            }

            @Override
            public void onUserSigExpired() {
                Logger.e(TAG, "用户签名过期了，需要刷新userSig重新登录SDK");
                refreshUserSig(getLastUserIdentifier());
            }
        });
        TIMManager.getInstance().setUserConfig(userConfig);
        String identifier = getLastUserInfo().identifier;
        LoginBusiness.loginImServer(identifier, loginHelper.getUserSig(identifier), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                actionLiveData.setValue(LOGIN_IM_SERVER_FAIL);
            }

            @Override
            public void onSuccess() {
                actionLiveData.setValue(LOGIN_IM_SERVER_SUCCESS);
            }
        });
    }

    /**
     * @param identify 用户帐号
     * @return 用户帐号签名，由私钥加密获得
     */
    public String getUserSignature(String identify) {
        return loginHelper.getUserSig(identify);
    }

    /**
     * 获取最后登录用户的信息
     */
    private TLSUserInfo getLastUserInfo() {
        return loginHelper.getLastUserInfo();
    }

    /**
     * 获取所有登录用户的信息
     */
    private List<TLSUserInfo> getAllUserInfo() {
        return loginHelper.getAllUserInfo();
    }

    /**
     * 刷新UserSig
     *
     * @param identify 用户帐号
     */
    public void refreshUserSig(String identify) {
        loginHelper.TLSRefreshUserSig(identify, new TLSRefreshUserSigListener() {
            @Override
            public void OnRefreshUserSigSuccess(TLSUserInfo tlsUserInfo) {

            }

            @Override
            public void OnRefreshUserSigFail(TLSErrInfo tlsErrInfo) {

            }

            @Override
            public void OnRefreshUserSigTimeout(TLSErrInfo tlsErrInfo) {

            }
        });
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

    /**
     * 判断输入的字符与图片验证码是否相符
     *
     * @param imageCode 输入的验证码
     * @param listener  回调函数
     */
    void verifyImageCode(String imageCode, TLSPwdLoginListener listener) {
        loginHelper.TLSPwdLoginVerifyImgcode(imageCode, listener);
    }

}
