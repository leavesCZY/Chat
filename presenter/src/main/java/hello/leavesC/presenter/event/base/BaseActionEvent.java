package hello.leavesC.presenter.event.base;

/**
 * 作者：叶应是叶
 * 时间：2018/9/30 22:28
 * 描述：
 */
public class BaseActionEvent extends BaseEvent {

    public static final int SHOW_LOADING_DIALOG = 10;

    public static final int DISMISS_LOADING_DIALOG = 20;

    public static final int SHOW_TOAST = 30;

    public static final int FINISH = 40;

    public static final int FINISH_WITH_RESULT_OK = 50;

    private String message;

    public BaseActionEvent(int action) {
        super(action);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}