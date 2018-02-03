package com.czy.chat.model;

import android.text.SpannableString;

import com.czy.chat.cache.FriendCache;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageStatus;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:51
 * 说明：消息-基类
 */
public abstract class BaseMessage {

    //消息体
    protected TIMMessage message;

    //发送者账号
    private String sender;

    //发送者称呼
    private String senderName;

    //消息状态
    private TIMMessageStatus messageStatus;

    //是否系统消息
    private boolean isSystemMessage;

    BaseMessage(TIMMessage message) {
        this.message = message;
        init();
    }

    private void init() {
        sender = message.getSender() == null ? "" : message.getSender();
        FriendProfile friendProfile = FriendCache.getInstance().getProfile(sender);
        senderName = friendProfile == null ? sender : friendProfile.getName();
        messageStatus = message.status();
    }

    //获取消息摘要
    public abstract SpannableString getMessageSummary();

    //获取消息
    public TIMMessage getMessage() {
        return message;
    }

    //消息是否自己发送的
    public boolean isSelf() {
        return message.isSelf();
    }

    //获取消息发送时间
    public long getMessageTime() {
        return message == null ? 0 : message.timestamp();
    }

    //消息ID
    public String getMsgId() {
        return message == null ? "" : message.getMsgId();
    }

    public String getSender() {
        return sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public TIMMessageStatus getMessageStatus() {
        return messageStatus;
    }

    public boolean isSystemMessage() {
        return isSystemMessage;
    }

    void setSystemMessage(boolean systemMessage) {
        isSystemMessage = systemMessage;
    }

}
