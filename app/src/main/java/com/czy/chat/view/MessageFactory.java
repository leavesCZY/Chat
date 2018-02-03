package com.czy.chat.view;

import com.czy.chat.model.BaseMessage;
import com.czy.chat.model.GroupTipsMessage;
import com.czy.chat.model.SystemGroupMessage;
import com.czy.chat.model.SystemProfileTipsMessage;
import com.czy.chat.model.SystemSnsMessage;
import com.czy.chat.model.TextMessage;
import com.czy.presenter.log.Logger;
import com.tencent.imsdk.TIMMessage;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:58
 * 说明：
 */
public class MessageFactory {

    private static final String TAG = "MessageFactory";

    private MessageFactory() {

    }

    public static BaseMessage getMessage(TIMMessage message) {
//        Logger.e(TAG, "getMessage Type: " + message.getElement(0).getType());
        switch (message.getElement(0).getType()) {
            case Text:
                return new TextMessage(message);
            case Face:
                break;
            case Image:
                break;
            case Sound:
                break;
            case Location:
                break;
            case File:
                break;
            case Custom:
                break;
            case GroupTips:
                return new GroupTipsMessage(message);
            case GroupSystem:
                return new SystemGroupMessage(message);
            case SNSTips:
                return new SystemSnsMessage(message);
            case ProfileTips:
                return new SystemProfileTipsMessage(message);
            case Video:
                break;
            case UGC:
                break;
        }
        return null;
    }

}