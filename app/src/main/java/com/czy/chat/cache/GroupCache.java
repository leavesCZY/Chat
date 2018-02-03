package com.czy.chat.cache;

import com.czy.chat.model.GroupProfile;
import com.czy.presenter.event.GroupEvent;
import com.czy.presenter.event.RefreshEvent;
import com.tencent.imsdk.ext.group.TIMGroupAssistant;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:54
 * 说明：群组数据缓存，公开群，私有群，聊天室
 * 每当群组链发生变化时，缓存数据均会相应改变
 */
public class GroupCache extends Observable implements Observer {

    private Map<String, List<GroupProfile>> groupMap;

    private static GroupCache sInstance;

    public static final String PUBLIC_GROUP = "Public";

    public static final String PRIVATE_GROUP = "Private";

    public static final String CHAT_ROOM = "ChatRoom";

    private static final String TAG = "GroupCache";

    private GroupCache() {
        groupMap = new HashMap<>();
        groupMap.put(PUBLIC_GROUP, new ArrayList<GroupProfile>());
        groupMap.put(PRIVATE_GROUP, new ArrayList<GroupProfile>());
        groupMap.put(CHAT_ROOM, new ArrayList<GroupProfile>());
        GroupEvent.getInstance().addObserver(this);
        RefreshEvent.getInstance().addObserver(this);
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

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof GroupEvent) {
            if (data instanceof GroupEvent.Notify) {
                GroupEvent.Notify notify = (GroupEvent.Notify) data;
                switch (notify.notifyType) {
                    case ADD:
                        refresh();
                        break;
                    case DELETE:
                        refresh();
                        break;
                    case GROUP_PROFILE_UPDATE:
                        refresh();
                        break;
                    case JOIN:
                        refresh();
                        break;
                    case QUIT:
                        refresh();
                        break;
                    case MEMBER_PROFILE_UPDATE:
                        refresh();
                        break;
                }
            }
        } else if (observable instanceof RefreshEvent) {
            refresh();
        }
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
        setChanged();
        notifyObservers();
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
            for (GroupProfile groupProfile : groupMap.get(key)) {
                profileArrayList.add(groupProfile);
            }
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
        GroupEvent.getInstance().deleteObserver(this);
        RefreshEvent.getInstance().deleteObserver(this);
        groupMap.clear();
        groupMap = null;
        sInstance = null;
    }

}