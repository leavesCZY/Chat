package hello.leavesC.presenter.viewModel;

import android.app.Application;
import androidx.lifecycle.MediatorLiveData;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;

import hello.leavesC.presenter.event.SplashActionEvent;
import hello.leavesC.presenter.liveData.FriendEventLiveData;
import hello.leavesC.presenter.liveData.GroupEventLiveData;
import hello.leavesC.presenter.liveData.MessageEventLiveData;
import hello.leavesC.presenter.liveData.RefreshEventLiveData;
import hello.leavesC.presenter.model.ProfileModel;
import hello.leavesC.presenter.viewModel.base.BaseAndroidViewModel;
import hello.leavesC.sdk.Constants;
import tencent.tls.platform.TLSLoginHelper;
import tencent.tls.platform.TLSUserInfo;

/**
 * 作者：叶应是叶
 * 时间：2018/9/28 21:49
 * 描述：
 */
public class SplashViewModel extends BaseAndroidViewModel {

    private static final String TAG = "SplashViewModel";

    private MediatorLiveData<SplashActionEvent> eventLiveData = new MediatorLiveData<>();

    private TLSLoginHelper loginHelper;

    private MediatorLiveData<ProfileModel> navToLoginLiveData = new MediatorLiveData<>();

    public SplashViewModel(@NonNull Application application) {
        super(application);
        loginHelper = TLSLoginHelper.getInstance().init(application,
                Constants.SDK_APP_ID, Constants.ACCOUNT_TYPE, Constants.APP_VERSION);
    }

    public void start() {
        TLSUserInfo lastUserInfo = loginHelper.getLastUserInfo();
        if (lastUserInfo == null) {
            eventLiveData.setValue(new SplashActionEvent(SplashActionEvent.LOGIN_OR_REGISTER));
            return;
        }
        String identifier = lastUserInfo.identifier;
        if (TextUtils.isEmpty(identifier)) {
            eventLiveData.setValue(new SplashActionEvent(SplashActionEvent.LOGIN_OR_REGISTER));
            return;
        }
        if (loginHelper.needLogin(identifier)) {
            ProfileModel profileModel = new ProfileModel();
            profileModel.setIdentifier(identifier);
            navToLoginLiveData.setValue(profileModel);
        } else {
            loginImServer(identifier);
        }
    }

    private void loginImServer(String identifier) {
        //登录之前要先初始化群和好友关系链缓存
        TIMUserConfig userConfig = new TIMUserConfig();
        userConfig = FriendEventLiveData.getInstance().init(userConfig);
        userConfig = GroupEventLiveData.getInstance().init(userConfig);
        userConfig = MessageEventLiveData.getInstance().init(userConfig);
        userConfig = RefreshEventLiveData.getInstance().init(userConfig);
        TIMManager.getInstance().setUserConfig(userConfig);
        TIMManager.getInstance().login(identifier, loginHelper.getUserSig(identifier), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                showToast(s);
                eventLiveData.setValue(new SplashActionEvent(SplashActionEvent.LOGIN_OR_REGISTER));
            }

            @Override
            public void onSuccess() {
                eventLiveData.setValue(new SplashActionEvent(SplashActionEvent.LOGIN_SUCCESS));
            }
        });
    }

    public String getLastUserIdentifier() {
        TLSUserInfo userInfo = loginHelper.getLastUserInfo();
        if (userInfo != null) {
            return userInfo.identifier;
        }
        return null;
    }

    public MediatorLiveData<SplashActionEvent> getEventLiveData() {
        return eventLiveData;
    }

    public MediatorLiveData<ProfileModel> getNavToLoginLiveData() {
        return navToLoginLiveData;
    }
}