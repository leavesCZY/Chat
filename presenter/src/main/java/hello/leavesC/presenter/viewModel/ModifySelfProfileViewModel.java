package hello.leavesC.presenter.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendAllowType;
import com.tencent.imsdk.TIMFriendGenderType;
import com.tencent.imsdk.TIMFriendshipManager;

import hello.leavesC.presenter.event.ModifySelfProfileActionEvent;
import hello.leavesC.presenter.viewModel.base.BaseViewModel;

/**
 * 作者：叶应是叶
 * 时间：2018/10/1 14:09
 * 描述：
 */
public class ModifySelfProfileViewModel extends BaseViewModel {

    private MutableLiveData<ModifySelfProfileActionEvent> modifySuccessLiveData;

    public ModifySelfProfileViewModel() {
        modifySuccessLiveData = new MutableLiveData<>();
    }

    private TIMCallBack callBack = new TIMCallBack() {
        @Override
        public void onError(int i, String s) {
            dismissLoadingDialog();
            showToast(s);
        }

        @Override
        public void onSuccess() {
            dismissLoadingDialog();
            showToast("修改成功");
            modifySuccessLiveData.setValue(new ModifySelfProfileActionEvent(ModifySelfProfileActionEvent.MODIFY_SUCCESS));
        }
    };

    public void setNickname(@NonNull String nickname) {
        modifyProfile(nickname, null, null, null, callBack);
    }

    public void setSignature(@NonNull String signature) {
        modifyProfile(null, null, signature, null, callBack);
    }

    public void setGender(@NonNull TIMFriendGenderType genderType) {
        modifyProfile(null, genderType, null, null, callBack);
    }

    public void setAllowType(@NonNull TIMFriendAllowType allowType) {
        modifyProfile(null, null, null, allowType, callBack);
    }

    private void modifyProfile(String nickname, TIMFriendGenderType genderType,
                               String signature, TIMFriendAllowType allowType,
                               TIMCallBack callBack) {
        showLoadingDialog("正在修改");
        TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
        if (nickname != null) {
            param.setNickname(nickname);
        }
        if (genderType != null) {
            param.setGender(genderType);
        }
        if (signature != null) {
            param.setSelfSignature(signature);
        }
        if (allowType != null) {
            param.setAllowType(allowType);
        }
        TIMFriendshipManager.getInstance().modifyProfile(param, callBack);
    }

    public MutableLiveData<ModifySelfProfileActionEvent> getModifySuccessLiveData() {
        return modifySuccessLiveData;
    }

}