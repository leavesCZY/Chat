package hello.leavesC.presenter.viewModel;

import android.app.Application;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;

import hello.leavesC.presenter.liveData.FriendEventLiveData;
import hello.leavesC.presenter.liveData.GroupEventLiveData;
import hello.leavesC.presenter.liveData.MessageEventLiveData;
import hello.leavesC.presenter.liveData.RefreshEventLiveData;
import hello.leavesC.presenter.model.ProfileModel;
import hello.leavesC.presenter.viewModel.base.BaseAndroidViewModel;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSLoginHelper;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * 作者：叶应是叶
 * 时间：2018/9/30 22:03
 * 描述：
 */
public class LoginViewModel extends BaseAndroidViewModel {

    private static final String TAG = "LoginViewModel";

    private TLSLoginHelper loginHelper;

    private MediatorLiveData<ProfileModel> loginEventLiveData = new MediatorLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginHelper = TLSLoginHelper.getInstance();
    }

    public void login(String identifier, String password) {
        if (identifier.length() < 5) {
            showToast("用户名需要至少五位");
            return;
        }
        if (password.length() < 8) {
            showToast("密码需要至少八位");
            return;
        }
        showLoadingDialog("正在登录...");
        loginHelper.TLSPwdLogin(identifier, password.getBytes(), new TLSPwdLoginListener() {
            @Override
            public void OnPwdLoginSuccess(TLSUserInfo tlsUserInfo) {
                loginImServer(tlsUserInfo);
            }

            @Override
            public void OnPwdLoginReaskImgcodeSuccess(byte[] bytes) {

            }

            @Override
            public void OnPwdLoginNeedImgcode(byte[] bytes, TLSErrInfo tlsErrInfo) {
                dismissLoadingDialog();
                showToast("需要请求验证码");
            }

            @Override
            public void OnPwdLoginFail(TLSErrInfo tlsErrInfo) {
                dismissLoadingDialog();
                showToast(tlsErrInfo.Msg);
            }

            @Override
            public void OnPwdLoginTimeout(TLSErrInfo tlsErrInfo) {
                dismissLoadingDialog();
                showToast(tlsErrInfo.Msg);
            }
        });
    }

    private void loginImServer(final TLSUserInfo tlsUserInfo) {
        //登录之前要先初始化群和好友关系链缓存
        TIMUserConfig userConfig = new TIMUserConfig();
        userConfig = FriendEventLiveData.getInstance().init(userConfig);
        userConfig = GroupEventLiveData.getInstance().init(userConfig);
        userConfig = MessageEventLiveData.getInstance().init(userConfig);
        userConfig = RefreshEventLiveData.getInstance().init(userConfig);
        TIMManager.getInstance().setUserConfig(userConfig);
        TIMManager.getInstance().login(tlsUserInfo.identifier, loginHelper.getUserSig(tlsUserInfo.identifier), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                dismissLoadingDialog();
                showToast(s);
            }

            @Override
            public void onSuccess() {
                dismissLoadingDialog();
                ProfileModel profileModel = new ProfileModel();
                profileModel.setIdentifier(tlsUserInfo.identifier);
                loginEventLiveData.setValue(profileModel);
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

    public MediatorLiveData<ProfileModel> getLoginEventLiveData() {
        return loginEventLiveData;
    }

}