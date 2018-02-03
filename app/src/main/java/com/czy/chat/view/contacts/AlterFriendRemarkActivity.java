package com.czy.chat.view.contacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.czy.chat.R;
import com.czy.chat.cache.FriendCache;
import com.czy.chat.view.base.BaseActivity;
import com.czy.presenter.listener.CallBackListener;
import com.czy.presenter.manager.FriendManager;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:12
 * 说明：修改好友备注
 */
public class AlterFriendRemarkActivity extends BaseActivity {

    private static final String IDENTIFIER = "identifier";

    private String identifier;

    private String original;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_friend_remark);
        identifier = getIntent().getStringExtra(IDENTIFIER);
        original = FriendCache.getInstance().getProfile(identifier).getRemark();
        initView();
    }

    private void initView() {
        initToolbar(R.id.toolbar_alterFriendRemark, "设置备注");
        final Button btn_alterFriendRemark = (Button) findViewById(R.id.btn_alterFriendRemark);
        final EditText et_alterFriendRemark = (EditText) findViewById(R.id.et_alterFriendRemark);
        btn_alterFriendRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog("正在修改备注", false, false);
                String remark = et_alterFriendRemark.getText().toString().trim();
                FriendManager.setFriendRemark(identifier, remark, new CallBackListener() {
                    @Override
                    public void onError(int code, String desc) {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        showMessageDialog("修改备注失败", "code:" + code + " desc:" + desc, null);
                    }

                    @Override
                    public void onSuccess() {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        dismissLoadingDialog();
                        showToast("备注修改成功");
                        finish();
                    }
                });
            }
        });
        et_alterFriendRemark.setText(original);
        et_alterFriendRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().equals(original)) {
                    btn_alterFriendRemark.setEnabled(false);
                } else {
                    btn_alterFriendRemark.setEnabled(true);
                }
            }
        });
    }

    public static void navigation(Context context, String identifier) {
        Intent intent = new Intent(context, AlterFriendRemarkActivity.class);
        intent.putExtra(IDENTIFIER, identifier);
        context.startActivity(intent);
    }

}
