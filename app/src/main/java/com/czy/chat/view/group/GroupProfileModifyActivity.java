package com.czy.chat.view.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.czy.chat.R;
import com.czy.chat.cache.GroupCache;
import com.czy.chat.model.GroupProfile;
import com.czy.chat.view.base.BaseActivity;
import com.czy.presenter.listener.CallBackListener;
import com.czy.presenter.manager.GroupProfileManager;

/**
 * 作者：叶应是叶
 * 时间：2018/1/14 10:45
 * 说明：修改群组资料的界面
 */
public class GroupProfileModifyActivity extends BaseActivity {

    public static final int ALTER_GROUP_NAME = 1;

    public static final int ALTER_GROUP_INTRODUCTION = 2;

    public static final int ALTER_GROUP_NOTIFICATION = 3;

    public static final String KEY_GROUP_ID = "groupId";

    public static final String KEY_CODE = "KeyCode";

    public String groupId;

    public int code;

    private Button btn_groupProfileAlter_completed;

    private EditText et_groupProfileAlter;

    private String origin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile_modify);
        groupId = getIntent().getStringExtra(KEY_GROUP_ID);
        code = getIntent().getIntExtra(KEY_CODE, 0);
        GroupProfile groupProfile = GroupCache.getInstance().getGroupProfile(groupId);
        if (groupProfile == null) {
            finish();
            return;
        }
        btn_groupProfileAlter_completed = (Button) findViewById(R.id.btn_groupProfileAlter_completed);
        et_groupProfileAlter = (EditText) findViewById(R.id.et_groupProfileAlter);
        switch (code) {
            case ALTER_GROUP_NAME: {
                initToolbar(R.id.toolbar_groupProfileAlter, "修改群名");
                origin = groupProfile.getName();
                break;
            }
            case ALTER_GROUP_INTRODUCTION: {
                initToolbar(R.id.toolbar_groupProfileAlter, "修改群简介");
                origin = groupProfile.getIntroduction();
                break;
            }
            case ALTER_GROUP_NOTIFICATION: {
                initToolbar(R.id.toolbar_groupProfileAlter, "修改群公告");
                origin = groupProfile.getNotification();
                break;
            }
            default: {
                finish();
                return;
            }
        }
        et_groupProfileAlter.setText(origin);
        et_groupProfileAlter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(origin)) {
                    btn_groupProfileAlter_completed.setEnabled(false);
                } else {
                    btn_groupProfileAlter_completed.setEnabled(true);
                }
            }
        });
        btn_groupProfileAlter_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog("正在修改");
                String result = et_groupProfileAlter.getText().toString().trim();
                CallBackListener callBackListener = new CallBackListener() {
                    @Override
                    public void onSuccess() {
                        if (!isDestroyed() && !isFinishing()) {
                            dismissLoadingDialog();
                            showToast("修改成功");
                            finish();
                        }
                    }

                    @Override
                    public void onError(int code, String desc) {
                        if (!isDestroyed() && !isFinishing()) {
                            dismissLoadingDialog();
                            showToast("修改失败，" + desc + " code:" + code);
                        }
                    }
                };
                switch (code) {
                    case ALTER_GROUP_NAME: {
                        GroupProfileManager.modifyGroupName(groupId, result, callBackListener);
                        break;
                    }
                    case ALTER_GROUP_INTRODUCTION: {
                        GroupProfileManager.modifyGroupIntroduction(groupId, result, callBackListener);
                        break;
                    }
                    case ALTER_GROUP_NOTIFICATION: {
                        GroupProfileManager.modifyGroupNotification(groupId, result, callBackListener);
                        break;
                    }
                }
            }
        });
    }

    public static void navigation(Context context, String groupId, int code) {
        Intent intent = new Intent(context, GroupProfileModifyActivity.class);
        intent.putExtra(KEY_GROUP_ID, groupId);
        intent.putExtra(KEY_CODE, code);
        context.startActivity(intent);
    }

}
