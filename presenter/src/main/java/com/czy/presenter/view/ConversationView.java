package com.czy.presenter.view;

import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;

import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:48
 * 说明：
 */
public interface ConversationView {

    /**
     * 初始化界面或刷新界面
     */
    void initConversation(List<TIMConversation> conversationList);

    /**
     * 更新单聊会话消息
     *
     * @param message 最新一条消息
     */
    void updateC2CMessage(TIMMessage message);

    /**
     * 更新群聊会话消息
     *
     * @param message 最新一条消息
     */
    void updateGroupMessage(TIMMessage message);

    /**
     * 更新系统会话消息
     *
     * @param message 最新一条消息
     */
    void updateSystemMessage(TIMMessage message);

    /**
     * 更新好友关系链消息
     */
    void updateFriendshipMessage();

    /**
     * 更新群组关系链消息
     */
    void updateGroupInfo(TIMGroupCacheInfo info);

    /**
     * 更新好友资料
     *
     * @param identifierList 好友ID
     */
    void updateFriendProfile(List<String> identifierList);

    /**
     * 删除会话
     */
    void removeConversation(TIMConversationType type, String identifier);

    /**
     * 刷新
     */
    void refresh();

}
