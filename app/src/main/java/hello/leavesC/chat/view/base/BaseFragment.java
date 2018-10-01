package hello.leavesC.chat.view.base;

import android.arch.lifecycle.ViewModel;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import hello.leavesC.common.dialog.LoadingDialog;
import hello.leavesC.common.dialog.MessageDialog;
import hello.leavesC.presenter.event.base.BaseActionEvent;
import hello.leavesC.presenter.viewModel.base.IViewModelAction;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:04
 * 说明：基类Fragment
 */
public abstract class BaseFragment extends Fragment {

    private LoadingDialog loadingDialog;

    private MessageDialog messageDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModelEvent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissLoadingDialog();
        dismissMessageDialog();
    }

    protected abstract ViewModel initViewModel();

    private void initViewModelEvent() {
        ViewModel viewModel = initViewModel();
        if (viewModel instanceof IViewModelAction) {
            IViewModelAction viewModelAction = (IViewModelAction) viewModel;
            viewModelAction.getActionLiveData().observe(this, baseActionEvent -> {
                if (baseActionEvent != null) {
                    switch (baseActionEvent.getAction()) {
                        case BaseActionEvent.SHOW_LOADING_DIALOG: {
                            showLoadingDialog(baseActionEvent.getMessage());
                            break;
                        }
                        case BaseActionEvent.DISMISS_LOADING_DIALOG: {
                            dismissLoadingDialog();
                            break;
                        }
                        case BaseActionEvent.SHOW_TOAST: {
                            showToast(baseActionEvent.getMessage());
                            break;
                        }
                    }
                }
            });
        }
    }

    protected void startActivity(Class cl) {
        startActivity(new Intent(getContext(), cl));
    }

    public void showToast(String content) {
        Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
    }

    protected void showLoadingDialog(String hintText, boolean cancelable, boolean canceledOnTouchOutside) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getContext());
        }
        loadingDialog.show(hintText, cancelable, canceledOnTouchOutside);
    }

    protected void showLoadingDialog(String hintText) {
        showLoadingDialog(hintText, false, false);
    }

    protected void showMessageDialog(String title, String message, DialogInterface.OnClickListener positiveCallback) {
        if (messageDialog == null) {
            messageDialog = new MessageDialog();
        }
        messageDialog.show(title, message, positiveCallback, getActivity().getSupportFragmentManager());
    }

    protected void showConfirmMessageDialog(String title, String message, DialogInterface.OnClickListener positiveCallback) {
        if (messageDialog == null) {
            messageDialog = new MessageDialog();
        }
        messageDialog.showConfirm(title, message, positiveCallback, getActivity().getSupportFragmentManager());
    }

    protected void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    protected void dismissMessageDialog() {
        if (messageDialog != null && loadingDialog.isShowing()) {
            messageDialog.dismiss();
        }
    }

}