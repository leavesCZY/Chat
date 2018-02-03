package com.czy.presenter.event;

import com.czy.presenter.log.Logger;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.ext.message.TIMUserConfigMsgExt;

import java.util.List;
import java.util.Observable;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:44
 * 说明：消息通知事件
 */
public class MessageEvent extends Observable {

    private static final String TAG = "MessageEvent";

    private static MessageEvent sInstance;

    private TIMMessageListener messageListener = new TIMMessageListener() {
        @Override
        public boolean onNewMessages(List<TIMMessage> messageList) {
            for (TIMMessage message : messageList) {
                Logger.e(TAG, "MessageType: " + message.getElement(0).getType());
                setChanged();
                notifyObservers(message);
            }
            return false;
        }
    };

    private MessageEvent() {
        TIMManager.getInstance().addMessageListener(messageListener);
    }

    public static MessageEvent getInstance() {
        if (sInstance == null) {
            synchronized (MessageEvent.class) {
                if (sInstance == null) {
                    sInstance = new MessageEvent();
                }
            }
        }
        return sInstance;
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
        setChanged();
        notifyObservers(message);
    }

    /**
     * 清理消息监听
     */
    public void clean() {
        TIMManager.getInstance().removeMessageListener(messageListener);
        sInstance = null;
    }

}
