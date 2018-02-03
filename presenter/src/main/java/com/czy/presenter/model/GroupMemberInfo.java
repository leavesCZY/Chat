package com.czy.presenter.model;

import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupMemberRoleType;

/**
 * 作者：叶应是叶
 * 时间：2018/1/22 22:06
 * 说明：
 */
public class GroupMemberInfo {

    private String identifier;

    private long joinTime;

    private TIMGroupMemberRoleType roleType;

    public GroupMemberInfo(TIMGroupMemberInfo groupMemberInfo) {
        this.identifier = groupMemberInfo.getUser();
        this.joinTime = groupMemberInfo.getJoinTime();
        this.roleType = groupMemberInfo.getRole();
    }

    public GroupMemberInfo(String identifier, long joinTime, TIMGroupMemberRoleType roleType) {
        this.identifier = identifier;
        this.joinTime = joinTime;
        this.roleType = roleType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }

    public TIMGroupMemberRoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(TIMGroupMemberRoleType roleType) {
        this.roleType = roleType;
    }

}
