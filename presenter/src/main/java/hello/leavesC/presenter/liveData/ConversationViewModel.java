package hello.leavesC.presenter.liveData;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MediatorLiveData;

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

import hello.leavesC.presenter.event.FriendActionEvent;
import hello.leavesC.presenter.event.GroupActionEvent;
import hello.leavesC.presenter.event.MessageActionEvent;
import hello.leavesC.presenter.event.RefreshActionEvent;
import hello.leavesC.presenter.event.base.ConversationActionEvent;
import hello.leavesC.presenter.log.Logger;
import hello.leavesC.presenter.model.ConversationModel;
import hello.leavesC.presenter.viewModel.base.BaseViewModel;

/**
 * 作者：叶应是叶
 * 时间：2018/10/1 22:15
 * 描述：
 */
public class ConversationViewModel extends BaseViewModel {

    private static final String TAG = "ConversationViewModel";

    private FriendEventLiveData friendEventLiveData;

    private GroupEventLiveData groupEventLiveData;

    private MessageEventLiveData messageEventLiveData;

    private RefreshEventLiveData refreshEventLiveData;

    private MediatorLiveData<TIMMessage> updateC2CMessageLiveData = new MediatorLiveData<>();

    private MediatorLiveData<TIMMessage> updateGroupMessageLiveData = new MediatorLiveData<>();

    private MediatorLiveData<TIMMessage> updateSystemMessageLiveData = new MediatorLiveData<>();

    private MediatorLiveData<TIMGroupCacheInfo> updateGroupInfoLiveData = new MediatorLiveData<>();

    private MediatorLiveData<ConversationModel> removeConversationLiveData = new MediatorLiveData<>();

    private MediatorLiveData<List<String>> updateFriendProfileLiveData = new MediatorLiveData<>();

    private MediatorLiveData<ConversationActionEvent> actionEventLiveData = new MediatorLiveData<>();

    private MediatorLiveData<List<TIMConversation>> initConversationLiveData = new MediatorLiveData<>();

    public ConversationViewModel() {
        friendEventLiveData = FriendEventLiveData.getInstance();
        groupEventLiveData = GroupEventLiveData.getInstance();
        messageEventLiveData = MessageEventLiveData.getInstance();
        refreshEventLiveData = RefreshEventLiveData.getInstance();
    }

    public void start(LifecycleOwner lifecycleOwner) {
        friendEventLiveData.observe(lifecycleOwner, this::handleFriendEvent);
        groupEventLiveData.observe(lifecycleOwner, this::handleGroupEvent);
        messageEventLiveData.observe(lifecycleOwner, this::handleMessageEvent);
        refreshEventLiveData.observe(lifecycleOwner, this::handlerRefreshEvent);
    }

    private void handlerRefreshEvent(RefreshActionEvent refreshActionEvent) {
        getConversation();
    }

    private void handleMessageEvent(MessageActionEvent messageActionEvent) {
        Logger.e(TAG, "update MessageEvent");
        TIMMessage message = messageActionEvent.getMessage();
        if (message == null) {
            Logger.e(TAG, "update MessageEvent update data == null");
            getConversation();
        } else {
            Logger.e(TAG, "MessageEvent update data != null");
            TIMConversationType conversationType = message.getConversation().getType();
            switch (conversationType) {
                case C2C:
                    updateC2CMessageLiveData.setValue(message);
                    break;
                case Group:
                    updateGroupMessageLiveData.setValue(message);
                    break;
                case System:
                    updateSystemMessageLiveData.setValue(message);
                    break;
                case Invalid:
                    break;
            }
        }
    }

    private void handleGroupEvent(GroupActionEvent groupActionEvent) {
        switch (groupActionEvent.getAction()) {
            case GroupActionEvent.ADD: {
                updateGroupInfoLiveData.setValue(groupActionEvent.getGroupCacheInfo());
                break;
            }
            case GroupActionEvent.DELETE: {
                removeConversationLiveData.setValue(new ConversationModel(groupActionEvent.getGroupId(), TIMConversationType.Group));
                break;
            }
            case GroupActionEvent.GROUP_PROFILE_UPDATE: {
                break;
            }
            case GroupActionEvent.JOIN: {
                break;
            }
            case GroupActionEvent.QUIT: {
                break;
            }
            case GroupActionEvent.MEMBER_PROFILE_UPDATE: {
                break;
            }
        }
    }

    private void handleFriendEvent(FriendActionEvent friendActionEvent) {
        switch (friendActionEvent.getAction()) {
            case FriendActionEvent.DELETE_FRIEND: {
                List<String> identifierList = friendActionEvent.getIdentifierList();
                for (String identifier : identifierList) {
                    deleteConversation(identifier, TIMConversationType.C2C);
                }
                break;
            }
            case FriendActionEvent.PROFILE_UPDATE: {
                List<TIMUserProfile> userProfileList = friendActionEvent.getUserProfileList();
                List<String> identifierList = new ArrayList<>();
                for (TIMUserProfile userProfile : userProfileList) {
                    identifierList.add(userProfile.getIdentifier());
                }
                updateFriendProfileLiveData.setValue(identifierList);
                break;
            }
            case FriendActionEvent.ADD_FRIEND:
            case FriendActionEvent.ADD_REQUEST:
            case FriendActionEvent.READ_MESSAGE: {
                actionEventLiveData.setValue(new ConversationActionEvent(ConversationActionEvent.UPDATE_FRIENDSHIP_MESSAGE));
                break;
            }
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
        initConversationLiveData.setValue(conversationList);
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
                                updateC2CMessageLiveData.setValue(message);
                                break;
                            case Group:
                                updateGroupMessageLiveData.setValue(message);
                                break;
                            case System:
                                updateSystemMessageLiveData.setValue(message);
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
            removeConversationLiveData.setValue(new ConversationModel(identifier, type));
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

    public MediatorLiveData<TIMMessage> getUpdateC2CMessageLiveData() {
        return updateC2CMessageLiveData;
    }

    public MediatorLiveData<TIMMessage> getUpdateGroupMessageLiveData() {
        return updateGroupMessageLiveData;
    }

    public MediatorLiveData<TIMMessage> getUpdateSystemMessageLiveData() {
        return updateSystemMessageLiveData;
    }

    public MediatorLiveData<TIMGroupCacheInfo> getUpdateGroupInfoLiveData() {
        return updateGroupInfoLiveData;
    }

    public MediatorLiveData<ConversationModel> getRemoveConversationLiveData() {
        return removeConversationLiveData;
    }

    public MediatorLiveData<List<String>> getUpdateFriendProfileLiveData() {
        return updateFriendProfileLiveData;
    }

    public MediatorLiveData<ConversationActionEvent> getActionEventLiveData() {
        return actionEventLiveData;
    }

    public MediatorLiveData<List<TIMConversation>> getInitConversationLiveData() {
        return initConversationLiveData;
    }

}