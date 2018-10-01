package hello.leavesC.presenter.event;

import com.tencent.imsdk.TIMSNSChangeInfo;
import com.tencent.imsdk.TIMUserProfile;

import java.util.List;

import hello.leavesC.presenter.event.base.BaseEvent;

/**
 * 作者：叶应是叶
 * 时间：2018/10/1 16:15
 * 描述：
 */
public class FriendActionEvent extends BaseEvent {

    //主动添加好友或被添加
    public static final int ADD_FRIEND = 10;

    //主动删除好友或被删除
    public static final int DELETE_FRIEND = 20;

    //好友资料变更
    public static final int PROFILE_UPDATE = 30;

    //有人请求添加好友
    public static final int ADD_REQUEST = 40;

    //关系链通知已读（主动通知）
    public static final int READ_MESSAGE = 50;

    private List<TIMUserProfile> userProfileList;

    private List<String> identifierList;

    private List<TIMSNSChangeInfo> changeInfoList;

    public FriendActionEvent(int action) {
        super(action);
    }

    public List<TIMUserProfile> getUserProfileList() {
        return userProfileList;
    }

    public void setUserProfileList(List<TIMUserProfile> userProfileList) {
        this.userProfileList = userProfileList;
    }

    public List<String> getIdentifierList() {
        return identifierList;
    }

    public void setIdentifierList(List<String> identifierList) {
        this.identifierList = identifierList;
    }

    public List<TIMSNSChangeInfo> getChangeInfoList() {
        return changeInfoList;
    }

    public void setChangeInfoList(List<TIMSNSChangeInfo> changeInfoList) {
        this.changeInfoList = changeInfoList;
    }

}