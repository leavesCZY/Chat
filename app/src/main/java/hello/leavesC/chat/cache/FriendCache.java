package hello.leavesC.chat.cache;

import androidx.lifecycle.LiveData;

import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.ext.sns.TIMFriendshipProxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hello.leavesC.chat.model.FriendProfile;
import hello.leavesC.chat.utils.FriendProfileComparator;
import hello.leavesC.presenter.event.FriendActionEvent;
import hello.leavesC.presenter.event.RefreshActionEvent;
import hello.leavesC.presenter.liveData.FriendEventLiveData;
import hello.leavesC.presenter.liveData.RefreshEventLiveData;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:54
 * 说明：好友数据缓存，每当好友链发生变化时，缓存数据会相应改变
 */
public class FriendCache extends LiveData<Map<String, FriendProfile>> {

    private static final String TAG = "FriendCache";

    private Map<String, FriendProfile> friendMap;

    private static FriendCache sInstance;

    private FriendEventLiveData friendEventLiveData;

    private RefreshEventLiveData refreshEventLiveData;

    private FriendCache() {
        friendMap = new HashMap<>();
        friendEventLiveData = FriendEventLiveData.getInstance();
        refreshEventLiveData = RefreshEventLiveData.getInstance();
        friendEventLiveData.observeForever(this::handleFriendEvent);
        refreshEventLiveData.observeForever(this::handleRefreshEvent);
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

    private void handleFriendEvent(FriendActionEvent friendActionEvent) {
        switch (friendActionEvent.getAction()) {
            case FriendActionEvent.ADD_FRIEND:
            case FriendActionEvent.DELETE_FRIEND:
            case FriendActionEvent.PROFILE_UPDATE:
            case FriendActionEvent.ADD_REQUEST:
            case FriendActionEvent.READ_MESSAGE:
                refresh();
                break;
        }
    }

    private void handleRefreshEvent(RefreshActionEvent refreshActionEvent) {
        switch (refreshActionEvent.getAction()) {
            case RefreshActionEvent.REFRESH: {
                refresh();
                break;
            }
        }
    }

    /**
     * 刷新好友缓存
     */
    private void refresh() {
        friendMap.clear();
        List<TIMUserProfile> userProfileList = TIMFriendshipProxy.getInstance().getFriends();
        if (userProfileList != null) {
            for (TIMUserProfile userProfile : userProfileList) {
                friendMap.put(userProfile.getIdentifier(), new FriendProfile(userProfile));
            }
        }
        setValue(friendMap);
    }

    /**
     * 获取好友列表
     */
    public List<FriendProfile> getFriendProfileList() {
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
    public boolean isFriend(String identifier) {
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
    public FriendProfile getProfile(String identifier) {
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
    public String getFriendName(String identifier) {
        FriendProfile friendProfile = getProfile(identifier);
        return friendProfile == null ? identifier : friendProfile.getName();
    }

    public void clear() {
        friendEventLiveData.removeObserver(this::handleFriendEvent);
        refreshEventLiveData.removeObserver(this::handleRefreshEvent);
        friendMap.clear();
        sInstance = null;
    }

}