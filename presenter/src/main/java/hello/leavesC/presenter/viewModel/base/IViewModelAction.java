package hello.leavesC.presenter.viewModel.base;

import android.arch.lifecycle.MutableLiveData;

import hello.leavesC.presenter.event.base.BaseActionEvent;

/**
 * 作者：叶应是叶
 * 时间：2018/10/1 13:20
 * 描述：
 */
public interface IViewModelAction {

    void showLoadingDialog();

    void showLoadingDialog(String message);

    void dismissLoadingDialog();

    void showToast(String message);

    void finish();

    void finishWithResultOk();

    MutableLiveData<BaseActionEvent> getActionLiveData();

}