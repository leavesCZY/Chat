package com.czy.presenter.event;

import com.czy.presenter.log.Logger;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupSettings;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.ext.group.TIMGroupAssistantListener;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMUserConfigGroupExt;

import java.util.List;
import java.util.Observable;


/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:44
 * 说明：如果开启了群信息本地存储，可以通过setGroupAssistantListener方法设置监听感知群事件，
 * 当有对应事件发生时会进行回调，从而实现群相关数据缓存
 */
public class GroupEvent extends Observable {

    private static final String TAG = "GroupEvent";

    public enum NotifyType {
        ADD,                    //添加群
        DELETE,                 //删除群
        GROUP_PROFILE_UPDATE,   //群信息更新
        JOIN,                   //有新成员加入群
        QUIT,                   //有群成员退出
        MEMBER_PROFILE_UPDATE   //群成员资料刷新
    }

    public class Notify {

        public final NotifyType notifyType;

        public final Object data;

        Notify(NotifyType notifyType, Object data) {
            this.notifyType = notifyType;
            this.data = data;
        }

    }

    private static GroupEvent sInstance;

    private GroupEvent() {

    }

    public static GroupEvent getInstance() {
        if (sInstance == null) {
            synchronized (GroupEvent.class) {
                if (sInstance == null) {
                    sInstance = new GroupEvent();
                }
            }
        }
        return sInstance;
    }

    public TIMUserConfig init(TIMUserConfig userConfig) {
        TIMGroupSettings groupSettings = new TIMGroupSettings();
        userConfig.setGroupSettings(groupSettings);
        TIMUserConfigGroupExt userConfigGroupExt = new TIMUserConfigGroupExt(userConfig);
        //设置是否开启群组资料本地储存
        userConfigGroupExt.enableGroupStorage(true);
        //设置群组资料变更事件监听器
        userConfigGroupExt.setGroupAssistantListener(groupAssistantListener);
        return userConfigGroupExt;
    }

    private TIMGroupAssistantListener groupAssistantListener = new TIMGroupAssistantListener() {

        /**
         * 加入群的通知回调
         *
         * @param groupCacheInfo 加入的群的缓存群资料
         */
        @Override
        public void onGroupAdd(TIMGroupCacheInfo groupCacheInfo) {
            Logger.e(TAG, "onGroupAdd: " + groupCacheInfo.getGroupInfo().getGroupId());
            setChanged();
            notifyObservers(new Notify(NotifyType.ADD, groupCacheInfo));
        }

        /**
         * 被解散的群的通知回调
         *
         * @param groupId 被解散的群的群ID
         */
        @Override
        public void onGroupDelete(String groupId) {
            Logger.e(TAG, "onGroupDelete: " + groupId);
            setChanged();
            notifyObservers(new Notify(NotifyType.DELETE, groupId));
        }

        /**
         * 群缓存资料更新的通知回调
         *
         * @param groupCacheInfo 更新后的群缓存资料信息
         */
        @Override
        public void onGroupUpdate(TIMGroupCacheInfo groupCacheInfo) {
            Logger.e(TAG, "onGroupUpdate: " + groupCacheInfo.getGroupInfo().getGroupId());
            setChanged();
            notifyObservers(new Notify(NotifyType.GROUP_PROFILE_UPDATE, groupCacheInfo));
        }

        /**
         * 有新用户加群时的通知回调
         *
         * @param groupId        群ID
         * @param memberInfoList 加群的用户的群内资料列表
         */
        @Override
        public void onMemberJoin(String groupId, List<TIMGroupMemberInfo> memberInfoList) {
            for (TIMGroupMemberInfo info : memberInfoList) {
                Logger.e(TAG, "onMemberJoin:" + groupId + " " + info.getUser());
            }
            setChanged();
            notifyObservers(new Notify(NotifyType.JOIN, memberInfoList));
        }

        /**
         * 有群成员退群时的通知回调
         *
         * @param groupId        群ID
         * @param identifierList 退群的成员的identifier列表
         */
        @Override
        public void onMemberQuit(String groupId, List<String> identifierList) {
            for (String id : identifierList) {
                Logger.e(TAG, "onMemberQuit: " + groupId + " " + id);
            }
            setChanged();
            notifyObservers(new Notify(NotifyType.QUIT, identifierList));
        }

        /**
         * 群成员信息更新的通知回调
         *
         * @param groupId        群ID
         * @param memberInfoList 更新后的群成员群内资料列表
         */
        @Override
        public void onMemberUpdate(String groupId, List<TIMGroupMemberInfo> memberInfoList) {
            for (TIMGroupMemberInfo info : memberInfoList) {
                Logger.e(TAG, "onMemberUpdate: " + groupId + " " + info.getUser());
            }
            setChanged();
            notifyObservers(new Notify(NotifyType.MEMBER_PROFILE_UPDATE, memberInfoList));
        }
    };

    public void clean() {
        sInstance = null;
    }

}
