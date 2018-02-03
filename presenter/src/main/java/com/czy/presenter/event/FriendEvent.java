package com.czy.presenter.event;

import com.czy.presenter.log.Logger;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMFriendshipSettings;
import com.tencent.imsdk.TIMSNSChangeInfo;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.ext.sns.TIMFriendshipProxyListener;
import com.tencent.imsdk.ext.sns.TIMUserConfigSnsExt;

import java.util.List;
import java.util.Observable;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:44
 * 说明：通过TIMFriendshipProxy提供的接口从内存中同步获取好友关系链资料
 */
public class FriendEvent extends Observable {

    private static final String TAG = "FriendEvent";

    public enum NotifyType {
        ADD_FRIEND,     //主动添加好友或被添加
        DELETE_FRIEND,  //主动删除好友或被删除
        PROFILE_UPDATE, //好友资料变更
        ADD_REQUEST,    //有人请求添加好友
        READ_MESSAGE    //关系链通知已读（主动通知）
    }

    public class Notify {

        public final NotifyType notifyType;

        public final Object data;

        Notify(NotifyType notifyType, Object data) {
            this.notifyType = notifyType;
            this.data = data;
        }

    }

    private static FriendEvent sInstance;

    private FriendEvent() {

    }

    public static FriendEvent getInstance() {
        if (sInstance == null) {
            synchronized (FriendEvent.class) {
                if (sInstance == null) {
                    sInstance = new FriendEvent();
                }
            }
        }
        return sInstance;
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
            setChanged();
            notifyObservers(new Notify(NotifyType.ADD_FRIEND, userProfileList));
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
            setChanged();
            notifyObservers(new Notify(NotifyType.DELETE_FRIEND, identifierList));
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
            setChanged();
            notifyObservers(new Notify(NotifyType.PROFILE_UPDATE, userProfileList));
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
            setChanged();
            notifyObservers(new Notify(NotifyType.ADD_REQUEST, changeInfoList));
        }
    };

    /**
     * 好友关系链消息已读通知
     */
    public void OnFriendshipMessageRead() {
        setChanged();
        notifyObservers(new Notify(NotifyType.READ_MESSAGE, null));
    }

    public void clean() {
        sInstance = null;
    }

}
