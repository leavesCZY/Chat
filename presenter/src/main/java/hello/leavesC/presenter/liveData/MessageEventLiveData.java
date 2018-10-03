package hello.leavesC.presenter.liveData;

import android.arch.lifecycle.LiveData;

import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.ext.message.TIMUserConfigMsgExt;

import hello.leavesC.presenter.event.MessageActionEvent;
import hello.leavesC.presenter.log.Logger;

/**
 * 作者：叶应是叶
 * 时间：2018/10/1 16:53
 * 描述：消息通知事件
 */
public class MessageEventLiveData extends LiveData<MessageActionEvent> {

    private static final String TAG = "MessageEventLiveData";

    private TIMMessageListener messageListener = messageList -> {
        for (TIMMessage message : messageList) {
            Logger.e(TAG, "MessageType: " + message.getElement(0).getType());
            MessageActionEvent messageActionEvent = new MessageActionEvent(MessageActionEvent.NEW_MESSAGE);
            messageActionEvent.setMessage(message);
            setValue(messageActionEvent);
        }
        return false;
    };

    private MessageEventLiveData() {
        TIMManager.getInstance().addMessageListener(messageListener);
    }

    private static class SingletonHolder {
        private final static MessageEventLiveData instance = new MessageEventLiveData();
    }

    public static MessageEventLiveData getInstance() {
        return MessageEventLiveData.SingletonHolder.instance;
    }

    public TIMUserConfig init(TIMUserConfig userConfig) {
        TIMUserConfigMsgExt userConfigMsgExt = new TIMUserConfigMsgExt(userConfig);
        //设置是否开启已读上报功能，默认开启
        userConfigMsgExt.enableAutoReport(false);
        //设置是否开启最近联系人功能，默认开启
        userConfigMsgExt.enableRecentContact(true);
        //设置是否开启最近联系人消息通知，默认开启
        userConfigMsgExt.enableRecentContactNotify(true);
        //设置是否开启消息本地储存
        userConfigMsgExt.enableStorage(true);
        return userConfigMsgExt;
    }

    /**
     * 主动通知新消息
     */
    public void onNewMessage(TIMMessage message) {
        MessageActionEvent messageActionEvent = new MessageActionEvent(MessageActionEvent.NEW_MESSAGE);
        messageActionEvent.setMessage(message);
        setValue(messageActionEvent);
    }

    @Override
    protected void onActive() {
        super.onActive();
        TIMManager.getInstance().addMessageListener(messageListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        TIMManager.getInstance().addMessageListener(messageListener);
    }

}