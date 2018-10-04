package hello.leavesC.presenter.event;

import hello.leavesC.presenter.event.base.BaseEvent;

/**
 * 作者：叶应是叶
 * 时间：2018/10/4 9:04
 * 描述：
 */
public class GlobalActionEvent extends BaseEvent {

    public static final int FORCE_OFFLINE = 10;

    public static final int USER_SIGN_EXPIRED = 20;

    public GlobalActionEvent(int action) {
        super(action);
    }

}
