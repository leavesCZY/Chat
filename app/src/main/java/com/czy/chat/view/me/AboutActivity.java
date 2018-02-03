package com.czy.chat.view.me;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.czy.chat.R;
import com.czy.chat.view.base.BaseActivity;
import com.czy.presenter.listener.CallBackListener;
import com.czy.presenter.manager.GroupManager;
import com.czy.ui.common.OptionView;

/**
 * 作者：叶应是叶
 * 时间：2018/1/21 19:28
 * 说明：关于
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView() {
        initToolbar(R.id.toolbar_about, "关于");
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ov_summary: {
                        AppIntroductionActivity.navigation(AboutActivity.this, "应用介绍", getString(R.string.about_summary));
                        break;
                    }
                    case R.id.ov_changelog: {
                        AppIntroductionActivity.navigation(AboutActivity.this, "更新日志", getString(R.string.about_changelog));
                        break;
                    }
                    case R.id.ov_gitHub: {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/leavesC/Chat")));
                        break;
                    }
                    case R.id.ov_reward: {
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        if (clipboardManager != null) {
                            ClipData clip = ClipData.newPlainText("Hello", "#吱口令#长按复制此条消息，打开支付宝给我转账cN9ccz98uq");
                            clipboardManager.setPrimaryClip(clip);
                            try {
                                startActivity(getPackageManager().getLaunchIntentForPackage("com.eg.android.AlipayGphone"));
                            } catch (Exception e) {
                                showToast("启动支付宝失败 " + e.getMessage());
                            }
                        }
                        break;
                    }
                    case R.id.ov_contact: {
                        AppIntroductionActivity.navigation(AboutActivity.this, "联系方式", getString(R.string.about_contact));
                        break;
                    }
                    case R.id.ov_joinGroup: {
                        showMessageDialog(null, "是否加入开发者聊天群？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    showLoadingDialog("正在申请加入开发者聊天群");
                                    GroupManager.applyJoinGroup("@TGS#2VYICXBF3", "兴趣所致", new CallBackListener() {
                                        @Override
                                        public void onSuccess() {
                                            if (!isFinishingOrDestroyed()) {
                                                dismissLoadingDialog();
                                                showToast("已加入开发者聊天群");
                                            }
                                        }

                                        @Override
                                        public void onError(int code, String desc) {
                                            if (!isFinishingOrDestroyed()) {
                                                dismissLoadingDialog();
                                                showToast("Error: " + code + " " + desc);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        break;
                    }
                }
            }
        };
        OptionView ov_summary = (OptionView) findViewById(R.id.ov_summary);
        OptionView ov_changelog = (OptionView) findViewById(R.id.ov_changelog);
        OptionView ov_gitHub = (OptionView) findViewById(R.id.ov_gitHub);
        OptionView ov_reward = (OptionView) findViewById(R.id.ov_reward);
        OptionView ov_contact = (OptionView) findViewById(R.id.ov_contact);
        OptionView ov_joinGroup = (OptionView) findViewById(R.id.ov_joinGroup);
        ov_summary.setOnClickListener(clickListener);
        ov_changelog.setOnClickListener(clickListener);
        ov_gitHub.setOnClickListener(clickListener);
        ov_reward.setOnClickListener(clickListener);
        ov_contact.setOnClickListener(clickListener);
        ov_joinGroup.setOnClickListener(clickListener);
    }

}
