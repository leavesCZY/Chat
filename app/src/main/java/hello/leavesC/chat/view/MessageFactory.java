package hello.leavesC.chat.view;

import com.tencent.imsdk.TIMMessage;

import hello.leavesC.chat.model.BaseMessage;
import hello.leavesC.chat.model.GroupTipsMessage;
import hello.leavesC.chat.model.SystemGroupMessage;
import hello.leavesC.chat.model.SystemProfileTipsMessage;
import hello.leavesC.chat.model.SystemSnsMessage;
import hello.leavesC.chat.model.TextMessage;

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