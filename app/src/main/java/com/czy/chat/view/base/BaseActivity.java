package com.czy.chat.view.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.czy.ui.dialog.LoadingDialog;
import com.czy.ui.dialog.MessageDialog;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:04
 * 说明：基类Activity
 */
public class BaseActivity extends AppCompatActivity {

    private LoadingDialog loadingDialog;

    private MessageDialog messageDialog;

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoadingDialog();
        dismissMessageDialog();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
