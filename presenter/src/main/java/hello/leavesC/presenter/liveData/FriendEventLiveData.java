package hello.leavesC.presenter.liveData;

import android.arch.lifecycle.LiveData;

import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMFriendshipSettings;
import com.tencent.imsdk.TIMSNSChangeInfo;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.ext.sns.TIMFriendshipProxyListener;
import com.tencent.imsdk.ext.sns.TIMUserConfigSnsExt;

import java.util.List;

import hello.leavesC.presenter.event.FriendActionEvent;
import hello.leavesC.presenter.log.Logger;

/**
 * 作者：叶应是叶
 * 时间：2018/10/1 20:56
 * 描述：通过TIMFriendshipProxy提供的接口从内存中同步获取好友关系链资料
 */
public class FriendEventLiveData extends LiveData<FriendActionEvent> {

    private static final String TAG = "FriendEventLiveData";

    private TIMFriendshipProxyListener friendshipProxyListener = new TIMFriendshipProxyListener() {

        /**
         * 添加好友通知（被添加或主动添加）
         *
         * @param userProfileList 好友列表
         */
        @Override
        public void OnAddFriends(List<TIMUserProfile> userProfileList) {
            for (TIMUserProfile userProfile : userProfileList) {
                Logger.e(TAG, "OnAddFriends: " + userProfile.getIdentifier());
            }
            FriendActionEvent friendActionEvent = new FriendActionEvent(FriendActionEvent.ADD_FRIEND);
            friendActionEvent.setUserProfileList(userProfileList);
            setValue(friendActionEvent);
        }

        /**
         * 删除好友通知(被删除或主动删除)
         *
         * @param identifierList 用户identityList列表
         */
        @Override
        public void OnDelFriends(List<String> identifierList) {
            for (String id : identifierList) {
                Logger.e(TAG, "OnDelFriends: " + id);
            }
            FriendActionEvent friendActionEvent = new FriendActionEvent(FriendActionEvent.DELETE_FRIEND);
            friendActionEvent.setIdentifierList(identifierList);
            setValue(friendActionEvent);
        }

        /**
         * 好友资料更新通知
         *
         * @param userProfileList 资料列表
         */
        @Override
        public void OnFriendProfileUpdate(List<TIMUserProfile> userProfileList) {
            for (TIMUserProfile userProfile : userProfileList) {
                Logger.e(TAG, "OnFriendProfileUpdate: " + userProfile.getIdentifier());
            }
            FriendActionEvent friendActionEvent = new FriendActionEvent(FriendActionEvent.PROFILE_UPDATE);
            friendActionEvent.setUserProfileList(userProfileList);
            setValue(friendActionEvent);
        }

        /**
         * 好友申请通知
         *
         * @param changeInfoList 申请者列表
         */
        @Override
        public void OnAddFriendReqs(List<TIMSNSChangeInfo> changeInfoList) {
            for (TIMSNSChangeInfo info : changeInfoList) {
                Logger.e(TAG, "OnAddFriendReqs: " + info.getIdentifier());
            }
            FriendActionEvent friendActionEvent = new FriendActionEvent(FriendActionEvent.ADD_REQUEST);
            friendActionEvent.setChangeInfoList(changeInfoList);
            setValue(friendActionEvent);
        }
    };

    private FriendEventLiveData() {

    }

    private static class SingletonHolder {
        private final static FriendEventLiveData instance = new FriendEventLiveData();
    }

    public static FriendEventLiveData getInstance() {
        return SingletonHolder.instance;
    }

    public TIMUserConfig init(TIMUserConfig userConfig) {
        TIMFriendshipSettings friendshipSettings = new TIMFriendshipSettings();
        //设置关系链默认拉取资料标识
        long flags = 0;
        flags |= TIMFriendshipManager.TIM_PROFILE_FLAG_NICK
                | TIMFriendshipManager.TIM_PROFILE_FLAG_FACE_URL
                | TIMFriendshipManager.TIM_PROFILE_FLAG_REMARK
                | TIMFriendshipManager.TIM_PROFILE_FLAG_SELF_SIGNATURE
                | TIMFriendshipManager.TIM_PROFILE_FLAG_GENDER;
        friendshipSettings.setFlags(flags);
        userConfig.setFriendshipSettings(friendshipSettings);
        //用户配置扩展类（好友关系链扩展）
        TIMUserConfigSnsExt userConfigSnsExt = new TIMUserConfigSnsExt(userConfig);
        //设置是否开启关系链本地储存
        userConfigSnsExt.enableFriendshipStorage(true);
        //设置关系链变更事件监听器
        userConfigSnsExt.setFriendshipProxyListener(friendshipProxyListener);
        return userConfigSnsExt;
    }

    /**
     * 好友关系链消息已读通知
     */
    public void onFriendshipMessageRead() {
        setValue(new FriendActionEvent(FriendActionEvent.READ_MESSAGE));
    }

    @Override
    protected void onActive() {
        super.onActive();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
    }

}