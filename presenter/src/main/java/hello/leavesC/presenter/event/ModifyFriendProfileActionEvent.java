package hello.leavesC.presenter.event;

import hello.leavesC.presenter.event.base.BaseEvent;

/**
 * 作者：叶应是叶
 * 时间：2018/10/3 9:06
 * 描述：
 */
public class ModifyFriendProfileActionEvent extends BaseEvent {

    public static final int MODIFY_SUCCESS = 10;

    public static final int DELETE_SUCCESS = 20;

    private String identifier;

    public ModifyFriendProfileActionEvent(int action) {
        super(action);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}