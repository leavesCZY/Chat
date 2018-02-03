package com.czy.presenter.view;

import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.ext.message.TIMMessageDraft;

import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:48
 * 说明：
 */
public interface ChatView {

    /**
     * 显示消息
     */
    void showMessage(TIMMessage message);

    /**
     * 显示多条消息
     */
    void showMessage(List<TIMMessage> messageList);

    /**
     * 显示草稿
     */
    void showDraft(TIMMessageDraft messageDraft);

    /**
     * 清除所有消息
     */
    void clearAllMessage();

    /**
     * 发送消息失败时回调
     *
     * @param code    返回码
     * @param desc    返回描述
     * @param message 发送的消息
     */
    void onSendMessageFail(int code, String desc, TIMMessage message);

}
