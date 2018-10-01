package hello.leavesC.chat.cache;

import android.arch.lifecycle.LiveData;

import com.tencent.imsdk.ext.group.TIMGroupAssistant;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hello.leavesC.chat.model.GroupProfile;
import hello.leavesC.presenter.event.GroupActionEvent;
import hello.leavesC.presenter.event.RefreshActionEvent;
import hello.leavesC.presenter.liveData.GroupEventLiveData;
import hello.leavesC.presenter.liveData.RefreshEventLiveData;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:54
 * 说明：群组数据缓存，公开群，私有群，聊天室
 * 每当群组链发生变化时，缓存数据均会相应改变
 */
public class GroupCache extends LiveData<Map<String, List<GroupProfile>>> {

    private Map<String, List<GroupProfile>> groupMap;

    private static GroupCache sInstance;

    public static final String PUBLIC_GROUP = "Public";

    public static final String PRIVATE_GROUP = "Private";

    public static final String CHAT_ROOM = "ChatRoom";

    private static final String TAG = "GroupCache";

    private GroupEventLiveData groupEventLiveData;

    private RefreshEventLiveData refreshEventLiveData;

    private GroupCache() {
        groupMap = new HashMap<>();
        groupMap.put(PUBLIC_GROUP, new ArrayList<>());
        groupMap.put(PRIVATE_GROUP, new ArrayList<>());
        groupMap.put(CHAT_ROOM, new ArrayList<>());
        groupEventLiveData = GroupEventLiveData.getInstance();
        refreshEventLiveData = RefreshEventLiveData.getInstance();
        groupEventLiveData.observeForever(this::handleGroupEvent);
        refreshEventLiveData.observeForever(this::handleRefreshEvent);
        refresh();
    }

    private void handleGroupEvent(GroupActionEvent groupActionEvent) {
        switch (groupActionEvent.getAction()) {
            case GroupActionEvent.ADD:
                refresh();
                break;
            case GroupActionEvent.DELETE:
                refresh();
                break;
            case GroupActionEvent.GROUP_PROFILE_UPDATE:
                refresh();
                break;
            case GroupActionEvent.JOIN:
                refresh();
                break;
            case GroupActionEvent.QUIT:
                refresh();
                break;
            case GroupActionEvent.MEMBER_PROFILE_UPDATE:
                refresh();
                break;
        }
    }

    private void handleRefreshEvent(RefreshActionEvent refreshActionEvent) {
        refresh();
    }

    public static GroupCache getInstance() {
        if (sInstance == null) {
            synchronized (GroupCache.class) {
                if (sInstance == null) {
                    sInstance = new GroupCache();
                }
            }
        }
        return sInstance;
    }

    /**
     * 刷新群组缓存
     */
    private synchronized void refresh() {
        for (String key : groupMap.keySet()) {
            groupMap.get(key).clear();
        }
        List<TIMGroupCacheInfo> groupCacheInfoList = TIMGroupAssistant.getInstance().getGroups(null);
        if (groupCacheInfoList != null && groupCacheInfoList.size() > 0) {
            List<GroupProfile> groupProfileList;
            for (TIMGroupCacheInfo groupCacheInfo : groupCacheInfoList) {
                groupProfileList = groupMap.get(groupCacheInfo.getGroupInfo().getGroupType());
                if (groupProfileList == null) {
                    continue;
                }
                groupProfileList.add(new GroupProfile(groupCacheInfo));
            }
        }
        setValue(groupMap);
    }

    /**
     * 判断是否群内成员
     */
    public synchronized boolean isInGroup(String groupId) {
        for (String key : groupMap.keySet()) {
            for (GroupProfile groupProfile : groupMap.get(key)) {
                if (groupProfile.getIdentifier().equals(groupId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否指定群组的群主
     */
    public synchronized boolean isGroupOwner(String groupId, String identifier) {
        for (String key : groupMap.keySet()) {
            for (GroupProfile groupProfile : groupMap.get(key)) {
                if (groupProfile.getIdentifier().equals(groupId) && groupProfile.getOwner().equals(identifier)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取所有群
     */
    public synchronized List<GroupProfile> getAllGroup() {
        List<GroupProfile> profileArrayList = new ArrayList<>();
        for (String key : groupMap.keySet()) {
            profileArrayList.addAll(groupMap.get(key));
        }
        return profileArrayList;
    }

    /**
     * 通过群ID获取群名称
     */
    public String getGroupName(String groupId) {
        for (String key : groupMap.keySet()) {
            for (GroupProfile groupProfile : groupMap.get(key)) {
                if (groupProfile.getIdentifier().equals(groupId)) {
                    return groupProfile.getName();
                }
            }
        }
        return groupId;
    }

    /**
     * 通过群ID获取群资料
     */
    public GroupProfile getGroupProfile(String type, String groupId) {
        for (GroupProfile groupProfile : groupMap.get(type)) {
            if (groupProfile.getIdentifier().equals(groupId)) {
                return groupProfile;
            }
        }
        return null;
    }

    /**
     * 通过群ID获取群资料
     */
    public GroupProfile getGroupProfile(String groupId) {
        for (String key : groupMap.keySet()) {
            for (GroupProfile groupProfile : groupMap.get(key)) {
                if (groupProfile.getIdentifier().equals(groupId)) {
                    return groupProfile;
                }
            }
        }
        return null;
    }

    /**
     * 清除数据
     */
    public synchronized void clear() {
        groupEventLiveData.removeObserver(this::handleGroupEvent);
        refreshEventLiveData.removeObserver(this::handleRefreshEvent);
        groupMap.clear();
        groupMap = null;
        sInstance = null;
    }

}