package com.czy.presenter.presenter;

import com.czy.presenter.event.FriendEvent;
import com.czy.presenter.event.GroupEvent;
import com.czy.presenter.event.MessageEvent;
import com.czy.presenter.event.RefreshEvent;
import com.czy.presenter.log.Logger;
import com.czy.presenter.view.ConversationView;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMManagerExt;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:47
 * 说明：会话界面逻辑
 */
public class ConversationPresenter implements Observer {

    private static final String TAG = "ConversationPresenter";

    private ConversationView conversationView;

    public ConversationPresenter(ConversationView conversationView) {
        this.conversationView = conversationView;
        FriendEvent.getInstance().addObserver(this);
        GroupEvent.getInstance().addObserver(this);
        MessageEvent.getInstance().addObserver(this);
        RefreshEvent.getInstance().addObserver(this);
    }

    @Override
    public synchronized void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent) {
            Logger.e(TAG, "update MessageEvent");
            if (data == null) {
                Logger.e(TAG, "update MessageEvent update data == null");
//                conversationView.refresh();
                getConversation();
            } else if (data instanceof TIMMessage) {
                Logger.e(TAG, "MessageEvent update data != null");
                TIMMessage message = (TIMMessage) data;
                TIMConversationType conversationType = message.getConversation().getType();
                switch (conversationType) {
                    case C2C:
                        conversationView.updateC2CMessage(message);
                        break;
                    case Group:
                        conversationView.updateGroupMessage(message);
                        break;
                    case System:
                        conversationView.updateSystemMessage(message);
                        break;
                    case Invalid:
                        break;
                }
            }
        } else if (observable instanceof FriendEvent) {
            FriendEvent.Notify notify = (FriendEvent.Notify) data;
            Logger.e(TAG, "update FriendEvent: " + notify.notifyType);
            switch (notify.notifyType) {
                case PROFILE_UPDATE: {
                    List<TIMUserProfile> userProfileList = (List<TIMUserProfile>) notify.data;
                    List<String> identifierList = new ArrayList<>();
                    for (TIMUserProfile userProfile : userProfileList) {
                        identifierList.add(userProfile.getIdentifier());
                    }
                    conversationView.updateFriendProfile(identifierList);
                    break;
                }
                case DELETE_FRIEND: {
                    List<String> identifierList = (List<String>) notify.data;
                    for (String identifier : identifierList) {
                        deleteConversation(identifier, TIMConversationType.C2C);
                    }
                    break;
                }
                case ADD_FRIEND:
                case ADD_REQUEST:
                case READ_MESSAGE:
                    conversationView.updateFriendshipMessage();
                    break;
            }
        } else if (observable instanceof GroupEvent) {
            GroupEvent.Notify notify = (GroupEvent.Notify) data;
            Logger.e(TAG, "update GroupEvent: " + notify.notifyType);
            switch (notify.notifyType) {
                case ADD:
                    conversationView.updateGroupInfo((TIMGroupCacheInfo) notify.data);
                    break;
                case DELETE:
                    conversationView.removeConversation(TIMConversationType.Group, (String) notify.data);
                    break;
                case GROUP_PROFILE_UPDATE:
                    break;
                case JOIN:
                    break;
                case QUIT:
                    break;
                case MEMBER_PROFILE_UPDATE:
                    break;
            }
        } else if (observable instanceof RefreshEvent) {
            Logger.e(TAG, "RefreshEvent");
            getConversation();
        }
    }

    public synchronized void getConversation() {
        List<TIMConversation> allConversationList = TIMManagerExt.getInstance().getConversationList();
        List<TIMConversation> conversationList = new ArrayList<>();
        for (TIMConversation conversation : allConversationList) {
            switch (conversation.getType()) {
                case C2C: {
                    conversationList.add(conversation);
                    break;
                }
                case Group: {
                    conversationList.add(conversation);
                    break;
                }
                case System: {
                    conversationList.add(conversation);
                    break;
                }
                case Invalid: {
                    break;
                }
                default:
                    break;
            }
        }
        conversationView.initConversation(conversationList);
        for (TIMConversation conversation : conversationList) {
            TIMConversationExt conversationExt = new TIMConversationExt(conversation);
            //  获取会话漫游消息，如果本地消息是连续的则不会通过网络获取
            //  如果本地消息不连续则会通过网络获取断层消息
            //  几个参数的意思分别是：
            //1.从指定的消息往前要获取的消息条数
            //2.指定要获取消息的起点，当传入null时从最新消息开始获取
            //3.回调函数
            conversationExt.getMessage(1, null, new TIMValueCallBack<List<TIMMessage>>() {
                @Override
                public void onError(int i, String s) {
                    Logger.e(TAG, "onError :" + i + " " + s);
                }

                @Override
                public void onSuccess(List<TIMMessage> messageList) {
                    if (messageList.size() > 0) {
                        TIMMessage message = messageList.get(0);
                        TIMConversationType conversationType = message.getConversation().getType();
                        switch (conversationType) {
                            case C2C:
                                conversationView.updateC2CMessage(message);
                                break;
                            case Group:
                                conversationView.updateGroupMessage(message);
                                break;
                            case System:
                                conversationView.updateSystemMessage(message);
                                break;
                            case Invalid:
                                break;
                        }
                    }
                }
            });
        }
    }

    /**
     * 删除会话
     *
     * @param identifier 会话对象id
     * @param type       会话类型
     */
    public synchronized void deleteConversation(String identifier, TIMConversationType type) {
        if (TIMManagerExt.getInstance().deleteConversationAndLocalMsgs(type, identifier)) {
            readAllMessage(identifier, type);
            conversationView.removeConversation(type, identifier);
        }
    }

    /**
     * 将此会话的所有消息标记为已读
     *
     * @param peer 单聊对象账号
     */
    private void readAllMessage(String peer, TIMConversationType conversationType) {
        TIMConversation conversation = TIMManager.getInstance().getConversation(conversationType, peer);
        TIMConversationExt conExt = new TIMConversationExt(conversation);
        conExt.setReadMessage(null, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                Logger.e(TAG, "readAllMessage onError:" + code + " " + desc);
            }

            @Override
            public void onSuccess() {
                Logger.e(TAG, "readAllMessage onSuccess");
            }
        });
    }

    public void clean() {
        FriendEvent.getInstance().deleteObserver(this);
        GroupEvent.getInstance().deleteObserver(this);
        MessageEvent.getInstance().deleteObserver(this);
        RefreshEvent.getInstance().deleteObserver(this);
        this.conversationView = null;
    }

}
