package hello.leavesC.presenter.viewModel.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import hello.leavesC.presenter.event.BaseActionEvent;

/**
 * 作者：叶应是叶
 * 时间：2018/9/30 22:24
 * 描述：
 */
public class BaseViewModel extends AndroidViewModel {

    private MutableLiveData<BaseActionEvent> actionLiveData;

    public BaseViewModel() {
        super(null);
        actionLiveData = new MutableLiveData<>();
    }

    public BaseViewModel(@NonNull Application application) {
        super(application);
        actionLiveData = new MutableLiveData<>();
    }

    protected void showLoadingDialog() {
        showLoadingDialog(null);
    }

    protected void showLoadingDialog(String message) {
        BaseActionEvent baseActionEvent = new BaseActionEvent(BaseActionEvent.SHOW_LOADING_DIALOG);
        baseActionEvent.setMessage(message);
        actionLiveData.setValue(baseActionEvent);
    }

    protected void dismissLoadingDialog() {
        actionLiveData.setValue(new BaseActionEvent(BaseActionEvent.DISMISS_LOADING_DIALOG));
    }

    protected void showToast(String message) {
        BaseActionEvent baseActionEvent = new BaseActionEvent(BaseActionEvent.SHOW_TOAST);
        baseActionEvent.setMessage(message);
        actionLiveData.setValue(baseActionEvent);
    }

    public MutableLiveData<BaseActionEvent> getActionLiveData() {
        return actionLiveData;
    }

}