package hello.leavesC.presenter.event;

/**
 * 作者：叶应是叶
 * 时间：2018/9/29 22:39
 * 描述：
 */
public class RegisterEvent {

    public static final int REG_SUCCESS = 10;

    public static final int REG_FAIL = 20;

    public static final int FORMAT_INVALID = 30;

    private int action;

    private String identifier;

    private int errorCode;

    private String errorMsg;

    public RegisterEvent(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}