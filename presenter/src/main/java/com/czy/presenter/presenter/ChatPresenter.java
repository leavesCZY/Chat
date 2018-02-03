package com.czy.presenter.presenter;

import android.support.annotation.Nullable;

import com.czy.presenter.event.MessageEvent;
import com.czy.presenter.event.RefreshEvent;
import com.czy.presenter.log.Logger;
import com.czy.presenter.view.ChatView;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMMessageDraft;

import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:46
 * 说明：聊天界面逻辑
 */
public class ChatPresenter implements Observer {

    private static final String TAG = "ChatPresenter";

    private ChatView chatView;

    private TIMConversation conversation;

    //用于标记是否正在获取消息
    private boolean isGettingMessage = false;

    private static final int SHOW_LAST_MESSAGE_NUMBER = 20;

    public ChatPresenter(ChatView chatView, String identifier, TIMConversationType conversationType) {
        this.chatView = chatView;
        this.conversation = TIMManager.getInstance().getConversation(conversationType, identifier);
    }

    /**
     * 开启聊天界面逻辑
     */
    public void start() {
        MessageEvent.getInstance().addObserver(this);
        RefreshEvent.getInstance().addObserver(this);
        getMessage(null);
        TIMConversationExt timConversationExt = new TIMConversationExt(conversation);
        if (chatView != null && timConversationExt.hasDraft()) {
            chatView.showDraft(timConversationExt.getDraft());
        }
    }

    /**
     * 中止聊天界面逻辑
     */
    public void stop() {
        MessageEvent.getInstance().deleteObserver(this);
        RefreshEvent.getInstance().deleteObserver(this);
        chatView = null;
    }

    /**
     * 发送消息
     *
     * @param message 发送的消息
     */
    public void sendMessage(final TIMMessage message) {
        /*
         * 每条消息均位于四种状态其中之一：发送中、发送成功、发送失败、被删除
         * 可以根据message.status()方法来获取
         * 一开始message处于发送中状态，可以根据此来处理UI状态
         */
        //先进行发送操作
        conversation.sendMessage(message, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int code, String desc) {
                if (chatView != null) {
                    chatView.onSendMessageFail(code, desc, message);
                }
            }

            @Override
            public void onSuccess(TIMMessage msg) {
                //此时消息发送成功，message.status()消息状态已在SDK中修改,此时传入null提示更新UI即可
                MessageEvent.getInstance().onNewMessage(null);
            }
        });
        //此时message处在发送中状态，可以根据此来更新UI
        MessageEvent.getInstance().onNewMessage(message);
        /*
         * 要注意的是，以上两个方法的执行顺序不能改变，一开始我是后执行发送操作的
         * 可是此时sendMessage(TIMMessage message)方法传入的message并不包含发送对象与会话类型等信息
         * 无法用于update(Observable observable, Object data)中的消息类型判断
         * 因为这个琢磨了很久，后来调换了位置，发现后台将信息补全了
         */
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent) {
            if (chatView != null) {
                if (data == null) {
                    TIMMessage message = null;
                    chatView.showMessage(message);
                } else if (data instanceof TIMMessage) {
                    TIMMessage message = (TIMMessage) data;
                    if (message.getConversation().getPeer().equals(conversation.getPeer())
                            && message.getConversation().getType() == conversation.getType()) {
                        chatView.showMessage(message);
                        //当前聊天界面已读上报，用于多终端登录时未读消息数同步
                        readMessages();
                    }
                }
            }
        } else if (observable instanceof RefreshEvent) {
            if (chatView != null) {
                chatView.clearAllMessage();
                getMessage(null);
            }
        }
    }

    /**
     * 获取消息
     *
     * @param message 从该消息开始向前获取，如果为null，则表示从最新消息开始向前获取
     */
    public void getMessage(@Nullable TIMMessage message) {
        if (!isGettingMessage) {
            isGettingMessage = true;
            TIMConversationExt timConversationExt = new TIMConversationExt(conversation);
            timConversationExt.getMessage(SHOW_LAST_MESSAGE_NUMBER, message, new TIMValueCallBack<List<TIMMessage>>() {
                @Override
                public void onError(int i, String s) {
                    isGettingMessage = false;
                    Logger.e(TAG, "getMessage onError: " + i + " " + s);
                }

                @Override
                public void onSuccess(List<TIMMessage> timMessages) {
                    if (chatView != null) {
                        Collections.reverse(timMessages);
                        chatView.showMessage(timMessages);
                        isGettingMessage = false;
                        readMessages();
                    }
                }
            });
        }
    }

    /**
     * 设置会话为已读
     */
    private void readMessages() {
        new TIMConversationExt(conversation).setReadMessage(null, null);
    }

    /**
     * 保存草稿
     *
     * @param message 消息数据
     */
    public void saveDraft(TIMMessage message) {
        TIMConversationExt timConversationExt = new TIMConversationExt(conversation);
        timConversationExt.setDraft(null);
        if (message != null && message.getElementCount() > 0) {
            TIMMessageDraft draft = new TIMMessageDraft();
            for (int i = 0; i < message.getElementCount(); i++) {
                draft.addElem(message.getElement(i));
            }
            timConversationExt.setDraft(draft);
        }
    }

}
