package hello.leavesC.presenter.viewModel;

import android.arch.lifecycle.MediatorLiveData;
import android.os.Handler;
import android.text.TextUtils;

import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMAddFriendRequest;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;

import java.util.ArrayList;
import java.util.List;

import hello.leavesC.presenter.event.SearchUserActionEvent;
import hello.leavesC.presenter.viewModel.base.BaseViewModel;

/**
 * 作者：叶应是叶
 * 时间：2018/10/3 13:39
 * 描述：
 */
public class SearchUserViewModel extends BaseViewModel {

    private MediatorLiveData<SearchUserActionEvent> searchUserLiveData = new MediatorLiveData<>();

    public void searchUser(String identifier) {
        showLoadingDialog("正在搜索");
        List<String> identifierList = new ArrayList<>();
        identifierList.add(identifier);
        TIMFriendshipManager.getInstance().getUsersProfile(identifierList, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {
                dismissLoadingDialog();
                showToast(s);
                searchUserLiveData.setValue(new SearchUserActionEvent(SearchUserActionEvent.SEARCH_USER_FAIL));
            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                dismissLoadingDialog();
                SearchUserActionEvent searchUserActionEvent = new SearchUserActionEvent(SearchUserActionEvent.SEARCH_USER_SUCCESS);
                searchUserActionEvent.setUserProfile(timUserProfiles.get(0));
                searchUserLiveData.setValue(searchUserActionEvent);
            }
        });
    }

    public void addFriend(String identifier) {
        showLoadingDialog("正在添加好友");
        List<TIMAddFriendRequest> friendRequestList = new ArrayList<>();
        TIMAddFriendRequest friendRequest = new TIMAddFriendRequest(identifier);
        friendRequestList.add(friendRequest);
        TIMFriendshipManagerExt.getInstance().addFriend(friendRequestList, new TIMValueCallBack<List<TIMFriendResult>>() {
            @Override
            public void onError(int i, String s) {
                dismissLoadingDialog();
                showToast(s);
            }

            @Override
            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                String message = null;
                switch (timFriendResults.get(0).getStatus()) {
                    case TIM_FRIEND_STATUS_SUCC:
                        //添加好友成功后，等待一段时间以让好友数据缓存刷新
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dismissLoadingDialog();
                                showToast("添加好友成功");
                                searchUserLiveData.setValue(new SearchUserActionEvent(SearchUserActionEvent.ADD_FRIEND_SUCCESS));
                            }
                        }, 1500);
                        break;
                    case TIM_ADD_FRIEND_STATUS_PENDING:
                        message = "等待对方通过添加请求";
                        break;
                    case TIM_ADD_FRIEND_STATUS_ALREADY_FRIEND:
                        message = "对方已经是好友";
                        break;
                    case TIM_ADD_FRIEND_STATUS_FRIEND_SIDE_FORBID_ADD:
                        message = "对方禁止添加好友";
                        break;
                    case TIM_ADD_FRIEND_STATUS_IN_OTHER_SIDE_BLACK_LIST:
                        message = "已被对方列入黑名单中";
                        break;
                    case TIM_ADD_FRIEND_STATUS_IN_SELF_BLACK_LIST:
                        message = "对方在黑名单中";
                        break;
                    case TIM_ADD_FRIEND_STATUS_SELF_FRIEND_FULL:
                        message = "好友数量已满";
                        break;
                    case TIM_FRIEND_STATUS_UNKNOWN:
                    default:
                        message = "未知错误";
                        break;
                }
                if (!TextUtils.isEmpty(message)) {
                    dismissLoadingDialog();
                    showToast(message);
                }
            }
        });
    }

    public MediatorLiveData<SearchUserActionEvent> getSearchUserLiveData() {
        return searchUserLiveData;
    }
}