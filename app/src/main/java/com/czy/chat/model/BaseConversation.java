package com.czy.chat.model;

import android.support.annotation.DrawableRes;
import android.text.SpannableString;

import com.czy.chat.R;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.ext.message.TIMConversationExt;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:51
 * 说明：会话-基类
 */
public abstract class BaseConversation {

    //会话对象ID
    String peer;

    //会话类型
    TIMConversationType conversationType;

    //会话
    TIMConversation timConversation;

    //会话对象称呼（备注-》昵称-》ID）
    String name;

    //最后一条消息
    BaseMessage lastMessage;

    //最新一条消息的摘要
    SpannableString lastMessageSummary;

    //会话最新一条消息的接收或发送时间
    long lastMessageTime;

    //未读消息数量
    long unreadMessageNumber;

    //会话头像
    String avatarUrl;

    BaseConversation(TIMConversation timConversation) {
        this.timConversation = timConversation;
        init();
    }

    protected abstract void init();

    //刷新未读消息计数
    public void freshenUnreadMessageNumber() {
        unreadMessageNumber = timConversation == null ? 0 : new TIMConversationExt(timConversation).getUnreadMessageNum();
    }

    //获取默认头像图片资源ID
    @DrawableRes
    public int getDefaultAvatar() {
        switch (conversationType) {
            case C2C:
                return R.drawable.avatar_friend;
            case Group:
                return R.drawable.avatar_group;
            case System:
                return R.drawable.avatar_system;
            default:
                return 0;
        }
    }

    public String getPeer() {
        return peer;
    }

    public void setPeer(String peer) {
        this.peer = peer;
    }

    public TIMConversationType getConversationType() {
        return conversationType;
    }

    public void setConversationType(TIMConversationType conversationType) {
        this.conversationType = conversationType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(BaseMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public SpannableString getLastMessageSummary() {
        return lastMessageSummary;
    }

    public void setLastMessageSummary(SpannableString lastMessageSummary) {
        this.lastMessageSummary = lastMessageSummary;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public long getUnreadMessageNumber() {
        return unreadMessageNumber;
    }

    public void setUnreadMessageNumber(long unreadMessageNumber) {
        this.unreadMessageNumber = unreadMessageNumber;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public TIMConversation getTimConversation() {
        return timConversation;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BaseConversation that = (BaseConversation) obj;
        return that.getPeer().equals(getPeer()) && that.getConversationType() == getConversationType();
    }

}
