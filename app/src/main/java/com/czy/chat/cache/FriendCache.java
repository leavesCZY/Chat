package com.czy.chat.cache;

import com.czy.chat.model.FriendProfile;
import com.czy.chat.utils.FriendProfileComparator;
import com.czy.presenter.event.FriendEvent;
import com.czy.presenter.event.RefreshEvent;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.ext.sns.TIMFriendshipProxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:54
 * 说明：好友数据缓存，每当好友链发生变化时，缓存数据会相应改变
 */
public class FriendCache extends Observable implements Observer {

    private static final String TAG = "FriendCache";

    private Map<String, FriendProfile> friendMap;

    private static FriendCache sInstance;

    private FriendCache() {
        friendMap = new HashMap<>();
        FriendEvent.getInstance().addObserver(this);
        RefreshEvent.getInstance().addObserver(this);
        refresh();
    }

    public static FriendCache getInstance() {
        if (sInstance == null) {
            synchronized (FriendCache.class) {
                if (sInstance == null) {
                    sInstance = new FriendCache();
                }
            }
        }
        return sInstance;
    }

    @Override
    public synchronized void update(Observable observable, Object data) {
        if (observable instanceof FriendEvent) {
            if (data instanceof FriendEvent.Notify) {
                FriendEvent.Notify notify = (FriendEvent.Notify) data;
                switch (notify.notifyType) {
                    case ADD_FRIEND:
                    case DELETE_FRIEND:
                    case PROFILE_UPDATE:
                    case ADD_REQUEST:
                    case READ_MESSAGE:
                        refresh();
                        break;
                }
            }
        } else if (observable instanceof RefreshEvent) {
            refresh();
        }
    }

    /**
     * 刷新好友缓存
     */
    private synchronized void refresh() {
        friendMap.clear();
        List<TIMUserProfile> userProfileList = TIMFriendshipProxy.getInstance().getFriends();
        if (userProfileList != null) {
            for (TIMUserProfile userProfile : userProfileList) {
                friendMap.put(userProfile.getIdentifier(), new FriendProfile(userProfile));
            }
        }
        setChanged();
        notifyObservers();
    }

    /**
     * 获取好友列表
     */
    public synchronized List<FriendProfile> getFriendProfileList() {
        List<FriendProfile> friendProfileList = new ArrayList<>();
        for (String key : friendMap.keySet()) {
            friendProfileList.add(friendMap.get(key));
        }
        Collections.sort(friendProfileList, new FriendProfileComparator());
        return friendProfileList;
    }

    /**
     * 判断是否是好友
     */
    public synchronized boolean isFriend(String identifier) {
        for (String key : friendMap.keySet()) {
            if (key.equals(identifier)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取好友资料
     */
    public synchronized FriendProfile getProfile(String identifier) {
        for (String key : friendMap.keySet()) {
            if (key.equals(identifier)) {
                return friendMap.get(key);
            }
        }
        return null;
    }

    /**
     * 获取对好友的称呼
     */
    public synchronized String getFriendName(String identifier) {
        FriendProfile friendProfile = getProfile(identifier);
        return friendProfile == null ? identifier : friendProfile.getName();
    }

    /**
     * 清除数据
     */
    public synchronized void clear() {
        FriendEvent.getInstance().deleteObserver(this);
        RefreshEvent.getInstance().deleteObserver(this);
        friendMap.clear();
        friendMap = null;
        sInstance = null;
    }

}