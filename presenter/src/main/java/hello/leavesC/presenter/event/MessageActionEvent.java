package hello.leavesC.presenter.event;

import com.tencent.imsdk.TIMMessage;

import hello.leavesC.presenter.event.base.BaseEvent;

/**
 * 作者：叶应是叶
 * 时间：2018/10/1 16:51
 * 描述：
 */
public class MessageActionEvent extends BaseEvent {

    public static final int NEW_MESSAGE = 10;

    private TIMMessage message;

    public MessageActionEvent(int action) {
        super(action);
    }

    public TIMMessage getMessage() {
        return message;
    }

    public void setMessage(TIMMessage message) {
        this.message = message;
    }
}
