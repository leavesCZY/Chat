package hello.leavesC.presenter.event;

import hello.leavesC.presenter.event.base.BaseEvent;

/**
 * 作者：叶应是叶
 * 时间：2018/9/30 23:39
 * 描述：
 */
public class SplashActionEvent extends BaseEvent {

    public static final int LOGIN_OR_REGISTER = 10;

    public static final int LOGIN_SUCCESS = 30;

    public SplashActionEvent(int action) {
        super(action);
    }

}