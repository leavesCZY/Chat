package hello.leavesC.presenter.viewModel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMMessageDraft;

import java.util.Collections;
import java.util.List;

import hello.leavesC.presenter.event.ChatActionEvent;
import hello.leavesC.presenter.event.MessageActionEvent;
import hello.leavesC.presenter.event.RefreshActionEvent;
import hello.leavesC.presenter.liveData.MessageEventLiveData;
import hello.leavesC.presenter.liveData.RefreshEventLiveData;
import hello.leavesC.presenter.log.Logger;
import hello.leavesC.presenter.viewModel.base.BaseViewModel;

/**
 * 作者：叶应是叶
 * 时间：2018/10/1 19:40
 * 描述：
 */
public class ChatViewModel extends BaseViewModel {

    private static final String TAG = "ChatViewModel";

    private TIMConversation conversation;

    //用于标记是否正在获取消息
    private volatile boolean isGettingMessage = false;

    private static final int SHOW_LAST_MESSAGE_NUMBER = 20;

    private MessageEventLiveData messageEventLiveData;

    private RefreshEventLiveData refreshEventLiveData;

    private MediatorLiveData<TIMMessageDraft> messageDraftLiveData = new MediatorLiveData<>();

    private MediatorLiveData<ChatActionEvent> chatActionLiveData = new MediatorLiveData<>();

    private MediatorLiveData<TIMMessage> showMessageLiveData = new MediatorLiveData<>();

    private MediatorLiveData<List<TIMMessage>> showListMessageLiveData = new MediatorLiveData<>();

    public ChatViewModel() {
        messageEventLiveData = MessageEventLiveData.getInstance();
        refreshEventLiveData = RefreshEventLiveData.getInstance();
    }

    public void start(String identifier, TIMConversationType conversationType, LifecycleOwner lifecycleOwner) {
        conversation = TIMManager.getInstance().getConversation(conversationType, identifier);
        messageEventLiveData.observe(lifecycleOwner, this::handleMessageActionEvent);
        refreshEventLiveData.observe(lifecycleOwner, this::handleRefreshActionEvent);
        getMessage(null);
        TIMConversationExt timConversationExt = new TIMConversationExt(conversation);
        if (timConversationExt.hasDraft()) {
            messageDraftLiveData.setValue(timConversationExt.getDraft());
        }
    }

    private void handleMessageActionEvent(MessageActionEvent messageActionEvent) {
        switch (messageActionEvent.getAction()) {
            case MessageActionEvent.NEW_MESSAGE: {
                TIMMessage message = messageActionEvent.getMessage();
                if (message == null) {
                    showMessageLiveData.setValue(null);
                } else if (message.getConversation().getPeer().equals(conversation.getPeer())
                        && message.getConversation().getType() == conversation.getType()) {
                    showMessageLiveData.setValue(message);
                    //当前聊天界面已读上报，用于多终端登录时未读消息数同步
                    readMessages();
                }
                break;
            }
        }
    }

    private void handleRefreshActionEvent(RefreshActionEvent refreshActionEvent) {
        chatActionLiveData.setValue(new ChatActionEvent(ChatActionEvent.CLEAN_MESSAGE));
        getMessage(null);
    }

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
                showToast(desc);
                chatActionLiveData.setValue(new ChatActionEvent(ChatActionEvent.SEND_MESSAGE_FAIL));
            }

            @Override
            public void onSuccess(TIMMessage msg) {
                //此时消息发送成功，message.status()消息状态已在SDK中修改,此时传入null提示更新UI即可
                messageEventLiveData.onNewMessage(null);
            }
        });
        //此时message处在发送中状态，可以根据此来更新UI
        messageEventLiveData.onNewMessage(message);
        /*
         * 要注意的是，以上两个方法的执行顺序不能改变，一开始我是后执行发送操作的
         * 可是此时sendMessage(TIMMessage message)方法传入的message并不包含发送对象与会话类型等信息
         * 无法用于update(Observable observable, Object data)中的消息类型判断
         * 因为这个琢磨了很久，后来调换了位置，发现后台将信息补全了
         */
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
                    Collections.reverse(timMessages);
                    showListMessageLiveData.setValue(timMessages);
                    readMessages();
                    isGettingMessage = false;
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
     */
    public void saveDraft(String draft) {
        TIMConversationExt timConversationExt = new TIMConversationExt(conversation);
        if (TextUtils.isEmpty(draft)) {
            timConversationExt.setDraft(null);
        } else {
            TIMTextElem textElem = new TIMTextElem();
            textElem.setText(draft);
            TIMMessageDraft messageDraft = new TIMMessageDraft();
            messageDraft.addElem(textElem);
            timConversationExt.setDraft(messageDraft);
        }
    }

    public MediatorLiveData<TIMMessageDraft> getMessageDraftLiveData() {
        return messageDraftLiveData;
    }

    public MediatorLiveData<ChatActionEvent> getChatActionLiveData() {
        return chatActionLiveData;
    }

    public MediatorLiveData<TIMMessage> getShowMessageLiveData() {
        return showMessageLiveData;
    }

    public MediatorLiveData<List<TIMMessage>> getShowListMessageLiveData() {
        return showListMessageLiveData;
    }

}