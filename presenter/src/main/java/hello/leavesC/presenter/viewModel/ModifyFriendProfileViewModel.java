package hello.leavesC.presenter.viewModel;

import android.support.annotation.NonNull;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMDelFriendType;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;

import java.util.Collections;
import java.util.List;

import hello.leavesC.presenter.viewModel.base.BaseViewModel;

/**
 * 作者：叶应是叶
 * 时间：2018/10/3 9:05
 * 描述：
 */
public class ModifyFriendProfileViewModel extends BaseViewModel {

    public void modifyFriendRemark(@NonNull String identifier, @NonNull String remark) {
        showLoadingDialog("正在修改");
        TIMFriendshipManagerExt.ModifySnsProfileParam param = new TIMFriendshipManagerExt.ModifySnsProfileParam(identifier);
        param.setRemark(remark);
        TIMFriendshipManagerExt.getInstance().modifySnsProfile(param, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                dismissLoadingDialog();
                showToast(s);
            }

            @Override
            public void onSuccess() {
                dismissLoadingDialog();
                showToast("修改成功");
                finish();
            }
        });
    }

    public void deleteFriend(@NonNull String identifier) {
        showLoadingDialog("正在删除");
        TIMFriendshipManagerExt.DeleteFriendParam param = new TIMFriendshipManagerExt.DeleteFriendParam();
        //双向删除好友
        param.setType(TIMDelFriendType.TIM_FRIEND_DEL_BOTH);
        param.setUsers(Collections.singletonList(identifier));
        TIMFriendshipManagerExt.getInstance().delFriend(param, new TIMValueCallBack<List<TIMFriendResult>>() {
            @Override
            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                dismissLoadingDialog();
                showToast("已删除好友");
                finish();
            }

            @Override
            public void onError(int i, String s) {
                showToast(s);
            }
        });
    }

}