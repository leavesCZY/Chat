package hello.leavesC.presenter.liveData;

import android.arch.lifecycle.LiveData;

import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupSettings;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.ext.group.TIMGroupAssistantListener;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMUserConfigGroupExt;

import java.util.List;

import hello.leavesC.presenter.event.GroupActionEvent;
import hello.leavesC.presenter.log.Logger;

/**
 * 作者：叶应是叶
 * 时间：2018/10/1 16:39
 * 描述：
 */
public class GroupEventLiveData extends LiveData<GroupActionEvent> {

    private static final String TAG = "GroupEventLiveData";

    private TIMGroupAssistantListener groupAssistantListener = new TIMGroupAssistantListener() {

        /**
         * 加入群的通知回调
         *
         * @param groupCacheInfo 加入的群的缓存群资料
         */
        @Override
        public void onGroupAdd(TIMGroupCacheInfo groupCacheInfo) {
            Logger.e(TAG, "onGroupAdd: " + groupCacheInfo.getGroupInfo().getGroupId());
            GroupActionEvent groupActionEvent = new GroupActionEvent(GroupActionEvent.ADD);
            groupActionEvent.setGroupCacheInfo(groupCacheInfo);
            setValue(groupActionEvent);
        }

        /**
         * 被解散的群的通知回调
         *
         * @param groupId 被解散的群的群ID
         */
        @Override
        public void onGroupDelete(String groupId) {
            Logger.e(TAG, "onGroupDelete: " + groupId);
            GroupActionEvent groupActionEvent = new GroupActionEvent(GroupActionEvent.DELETE);
            groupActionEvent.setGroupId(groupId);
            setValue(groupActionEvent);
        }

        /**
         * 群缓存资料更新的通知回调
         *
         * @param groupCacheInfo 更新后的群缓存资料信息
         */
        @Override
        public void onGroupUpdate(TIMGroupCacheInfo groupCacheInfo) {
            Logger.e(TAG, "onGroupUpdate: " + groupCacheInfo.getGroupInfo().getGroupId());
            GroupActionEvent groupActionEvent = new GroupActionEvent(GroupActionEvent.GROUP_PROFILE_UPDATE);
            groupActionEvent.setGroupCacheInfo(groupCacheInfo);
            setValue(groupActionEvent);
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
            GroupActionEvent groupActionEvent = new GroupActionEvent(GroupActionEvent.JOIN);
            groupActionEvent.setGroupId(groupId);
            groupActionEvent.setMemberInfoList(memberInfoList);
            setValue(groupActionEvent);
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
            GroupActionEvent groupActionEvent = new GroupActionEvent(GroupActionEvent.QUIT);
            groupActionEvent.setGroupId(groupId);
            groupActionEvent.setIdentifierList(identifierList);
            setValue(groupActionEvent);
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
            GroupActionEvent groupActionEvent = new GroupActionEvent(GroupActionEvent.MEMBER_PROFILE_UPDATE);
            groupActionEvent.setGroupId(groupId);
            groupActionEvent.setMemberInfoList(memberInfoList);
            setValue(groupActionEvent);
        }
    };

    private GroupEventLiveData() {

    }

    private static class SingletonHolder {
        private final static GroupEventLiveData instance = new GroupEventLiveData();
    }

    public static GroupEventLiveData getInstance() {
        return GroupEventLiveData.SingletonHolder.instance;
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

}