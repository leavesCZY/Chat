package hello.leavesC.presenter.event;

/**
 * 作者：叶应是叶
 * 时间：2018/9/30 23:39
 * 描述：
 */
public class SplashEvent extends BaseEvent {

    public static final int LOGIN_OR_REGISTER = 10;

    public static final int NAV_TO_LOGIN = 20;

    public static final int LOGIN_SUCCESS = 30;

    private String identifier;

    public SplashEvent(int action) {
        super(action);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}