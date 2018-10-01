package hello.leavesC.presenter.event.base;

/**
 * 作者：叶应是叶
 * 时间：2018/10/1 10:02
 * 描述：
 */
public class BaseCallbackEvent extends BaseEvent {

    private int errorCode;

    private String message;

    public BaseCallbackEvent(int action) {
        super(action);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}