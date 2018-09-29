package hello.leavesC.presenter.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import hello.leavesC.presenter.event.RegisterEvent;
import hello.leavesC.sdk.Constants;
import tencent.tls.platform.TLSAccountHelper;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * 作者：叶应是叶
 * 时间：2018/9/29 22:36
 * 描述：
 */
public class RegisterViewModel extends AndroidViewModel {

    private TLSAccountHelper accountHelper;

    private MutableLiveData<RegisterEvent> registerEventLiveData;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        accountHelper = TLSAccountHelper.getInstance().init(application,
                Constants.SDK_APP_ID, Constants.ACCOUNT_TYPE, Constants.APP_VERSION);
        registerEventLiveData = new MutableLiveData<>();
    }

    /**
     * 根据用户名与密码来注册
     *
     * @param identify 用户名
     * @param password 密码
     */
    public void register(String identify, String password) {
        int action = accountHelper.TLSStrAccReg(identify, password, new TLSStrAccRegListener() {
            @Override
            public void OnStrAccRegSuccess(TLSUserInfo tlsUserInfo) {
                RegisterEvent registerEvent = new RegisterEvent(RegisterEvent.REG_SUCCESS);
                registerEvent.setIdentifier(tlsUserInfo.identifier);
                registerEventLiveData.setValue(registerEvent);
            }

            @Override
            public void OnStrAccRegFail(TLSErrInfo tlsErrInfo) {
                RegisterEvent registerEvent = new RegisterEvent(RegisterEvent.REG_FAIL);
                registerEvent.setErrorCode(tlsErrInfo.ErrCode);
                registerEvent.setErrorMsg(tlsErrInfo.Msg);
                registerEventLiveData.setValue(registerEvent);
            }

            @Override
            public void OnStrAccRegTimeout(TLSErrInfo tlsErrInfo) {
                RegisterEvent registerEvent = new RegisterEvent(RegisterEvent.REG_FAIL);
                registerEvent.setErrorCode(tlsErrInfo.ErrCode);
                registerEvent.setErrorMsg(tlsErrInfo.Msg);
                registerEventLiveData.setValue(registerEvent);
            }
        });
        if (action == TLSErrInfo.INPUT_INVALID) {
            RegisterEvent registerEvent = new RegisterEvent(RegisterEvent.FORMAT_INVALID);
            registerEvent.setErrorMsg("格式有误，请重新输入");
            registerEventLiveData.setValue(registerEvent);
        }
    }

    public MutableLiveData<RegisterEvent> getRegisterEventLiveData() {
        return registerEventLiveData;
    }

}
