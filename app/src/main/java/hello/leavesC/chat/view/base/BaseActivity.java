package hello.leavesC.chat.view.base;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import hello.leavesC.chat.R;
import hello.leavesC.ui.dialog.LoadingDialog;
import hello.leavesC.ui.dialog.MessageDialog;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:04
 * 说明：基类Activity
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private LoadingDialog loadingDialog;

    private MessageDialog messageDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoadingDialog();
        dismissMessageDialog();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_still, R.anim.slide_out_right);
    }

    protected void initToolbar(@IdRes int toolbarId, String toolbarTitle, boolean displayHomeAsUpEnabled) {
        Toolbar toolbar = (Toolbar) findViewById(toolbarId);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(toolbarTitle);
                if (displayHomeAsUpEnabled) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onToolbarBack();
                        }
                    });
                }
            }
        }
    }

    protected void initToolbar(@IdRes int toolbarId, @StringRes int toolbarTitleRes, boolean displayHomeAsUpEnabled) {
        String toolbarTitle = getString(toolbarTitleRes);
        initToolbar(toolbarId, toolbarTitle, displayHomeAsUpEnabled);
    }

    protected void initToolbar(@IdRes int toolbarId, @StringRes int toolbarTitleRes) {
        String toolbarTitle = getString(toolbarTitleRes);
        initToolbar(toolbarId, toolbarTitle, true);
    }

    protected void initToolbar(@IdRes int toolbarId, String toolbarTitle) {
        initToolbar(toolbarId, toolbarTitle, true);
    }

    protected void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    protected void initToolbar(@IdRes int toolbarId) {
        initToolbar(toolbarId, "", true);
    }

    protected void onToolbarBack() {
        finish();
    }

    protected void showLoadingDialog(String hintText, boolean cancelable, boolean canceledOnTouchOutside) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.show(hintText, cancelable, canceledOnTouchOutside);
    }

    protected void showLoadingDialog(String hintText) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.show(hintText, false, false);
    }

    protected void showMessageDialog(String title, String message, DialogInterface.OnClickListener positiveCallback) {
        if (messageDialog == null) {
            messageDialog = new MessageDialog();
        }
        messageDialog.show(title, message, positiveCallback, getSupportFragmentManager());
    }

    protected void showConfirmMessageDialog(String title, String message, DialogInterface.OnClickListener positiveCallback) {
        if (messageDialog == null) {
            messageDialog = new MessageDialog();
        }
        messageDialog.showConfirm(title, message, positiveCallback, getSupportFragmentManager());
    }

    protected void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    protected void dismissMessageDialog() {
        if (messageDialog != null) {
            messageDialog.dismiss();
        }
    }

    protected BaseActivity getContext() {
        return BaseActivity.this;
    }

    protected void startActivity(Class cl) {
        startActivity(new Intent(this, cl));
    }

    public void startActivityForResult(Class cl, int requestCode) {
        startActivityForResult(new Intent(this, cl), requestCode);
    }

    protected void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    protected boolean isFinishingOrDestroyed() {
        return isFinishing() || isDestroyed();
    }

}