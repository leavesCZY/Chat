package com.czy.chat.presenter;

import com.czy.chat.cache.GroupCache;
import com.czy.chat.model.GroupProfile;

import java.util.Observable;
import java.util.Observer;

/**
 * 作者：叶应是叶
 * 时间：2018/1/14 14:11
 * 说明：群组资料中介管理
 */
public class GroupProfilePresenter extends Observable implements Observer {

    private String groupId;

    public GroupProfilePresenter(String groupId) {
        this.groupId = groupId;
        GroupCache.getInstance().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        GroupProfile groupProfile = GroupCache.getInstance().getGroupProfile(groupId);
        setChanged();
        notifyObservers(groupProfile);
    }

    public void clean() {
        GroupCache.getInstance().deleteObserver(this);
    }

}
