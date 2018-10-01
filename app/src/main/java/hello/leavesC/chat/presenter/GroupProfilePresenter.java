package hello.leavesC.chat.presenter;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import hello.leavesC.chat.cache.GroupCache;
import hello.leavesC.chat.model.GroupProfile;

/**
 * 作者：叶应是叶
 * 时间：2018/1/14 14:11
 * 说明：群组资料中介管理
 */
public class GroupProfilePresenter extends Observable implements Observer {

    private String groupId;

    public GroupProfilePresenter(String groupId) {
        this.groupId = groupId;
        GroupCache.getInstance().observeForever(this::handle);
    }

    private void handle(Map<String, List<GroupProfile>> stringListMap) {
        GroupProfile groupProfile = GroupCache.getInstance().getGroupProfile(groupId);
        setChanged();
        notifyObservers(groupProfile);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public void clean() {
        GroupCache.getInstance().removeObserver(this::handle);
    }

}
