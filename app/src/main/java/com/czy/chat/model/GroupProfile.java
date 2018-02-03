package com.czy.chat.model;

import android.text.TextUtils;

import com.czy.chat.view.MessageFactory;
import com.czy.chat.R;
import com.tencent.imsdk.TIMGroupAddOpt;
import com.tencent.imsdk.TIMGroupMemberRoleType;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.ext.group.TIMGroupBasicSelfInfo;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:53
 * 说明：群组资料
 */
public class GroupProfile extends BaseProfile {

    private TIMGroupDetailInfo groupDetailInfo;

    private TIMGroupBasicSelfInfo groupBasicSelfInfo;

    public GroupProfile(TIMGroupCacheInfo groupCacheInfo) {
        this(groupCacheInfo.getGroupInfo(), groupCacheInfo.getSelfInfo());
    }

    public GroupProfile(TIMGroupDetailInfo groupDetailInfo, TIMGroupBasicSelfInfo groupBasicSelfInfo) {
        this.groupDetailInfo = groupDetailInfo;
        this.groupBasicSelfInfo = groupBasicSelfInfo;
    }

    @Override
    public String getIdentifier() {
        return groupDetailInfo.getGroupId();
    }

    @Override
    public String getName() {
        return TextUtils.isEmpty(groupDetailInfo.getGroupName()) ? groupDetailInfo.getGroupId() : groupDetailInfo.getGroupName();
    }

    @Override
    public int getDefaultAvatarResource() {
        return R.drawable.avatar_group;
    }

    @Override
    public String getAvatarUrl() {
        return groupDetailInfo.getFaceUrl();
    }

    public String getOwner() {
        return groupDetailInfo.getGroupOwner();
    }

    public long getCreateTime() {
        return groupDetailInfo.getCreateTime();
    }

    public long getMemberNumber() {
        return groupDetailInfo.getMemberNum();
    }

    public String getIntroduction() {
        return groupDetailInfo.getGroupIntroduction();
    }

    public String getNotification() {
        return groupDetailInfo.getGroupNotification();
    }

    public BaseMessage getLastMessage() {
        return MessageFactory.getMessage(groupDetailInfo.getLastMsg());
    }

    public TIMGroupMemberRoleType getRole() {
        return groupBasicSelfInfo.getRole();
    }

    public TIMGroupReceiveMessageOpt getMessageOption() {
        return groupBasicSelfInfo.getRecvMsgOption();
    }

    public TIMGroupAddOpt getGroupAddOption() {
        return groupDetailInfo.getGroupAddOpt();
    }

    public TIMGroupDetailInfo getGroupDetailInfo() {
        return groupDetailInfo;
    }

    public TIMGroupBasicSelfInfo getGroupBasicSelfInfo() {
        return groupBasicSelfInfo;
    }

}
