package hello.leavesC.presenter.event;

import hello.leavesC.presenter.event.base.BaseEvent;

/**
 * 作者：叶应是叶
 * 时间：2018/10/3 12:57
 * 描述：
 */
public class GroupProfileActionEvent extends BaseEvent {

    public static final int QUIT_GROUP_SUCCESS = 10;

    public static final int MODIFY_PROFILE_SUCCESS = 20;

    public static final int INVITE_GROUP_SUCCESS = 30;

    public static final int CREATE_GROUP_SUCCESS = 40;

    public GroupProfileActionEvent(int action) {
        super(action);
    }

}