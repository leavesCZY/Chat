package hello.leavesC.presenter.event;

/**
 * 作者：叶应是叶
 * 时间：2018/9/30 22:16
 * 描述：
 */
public class LoginEvent extends BaseEvent {

    public static final int LOGIN_IM_SERVER_SUCCESS = 30;

    private String identifier;

    public LoginEvent(int action) {
        super(action);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}