package hello.leavesC.chat.view.base;

import androidx.lifecycle.ViewModel;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

    protected List<ViewModel> initViewModelList() {
        return null;
    }

    private void initViewModelEvent() {
        List<ViewModel> viewModelList = initViewModelList();
        if (viewModelList != null && viewModelList.size() > 0) {
            observeEvent(viewModelList);
        } else {
            ViewModel viewModel = initViewModel();
            if (viewModel != null) {
                List<ViewModel> modelList = new ArrayList<>();
                modelList.add(viewModel);
                observeEvent(modelList);
            }
        }
    }

    private void observeEvent(List<ViewModel> viewModelList) {
        for (ViewModel viewModel : viewModelList) {
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