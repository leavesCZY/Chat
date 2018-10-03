package hello.leavesC.presenter.event;

import com.tencent.imsdk.TIMUserProfile;

import hello.leavesC.presenter.event.base.BaseEvent;

/**
 * 作者：叶应是叶
 * 时间：2018/10/3 13:47
 * 描述：
 */
public class SearchUserActionEvent extends BaseEvent {

    public static final int ADD_FRIEND_SUCCESS = 10;

    public static final int SEARCH_USER_SUCCESS = 20;

    public static final int SEARCH_USER_FAIL = 30;

    private TIMUserProfile userProfile;

    public SearchUserActionEvent(int action) {
        super(action);
    }

    public TIMUserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(TIMUserProfile userProfile) {
        this.userProfile = userProfile;
    }

}