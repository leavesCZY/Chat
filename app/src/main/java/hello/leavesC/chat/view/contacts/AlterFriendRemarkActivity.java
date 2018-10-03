package hello.leavesC.chat.view.contacts;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import hello.leavesC.chat.R;
import hello.leavesC.chat.cache.FriendCache;
import hello.leavesC.chat.view.base.BaseActivity;
import hello.leavesC.presenter.event.ModifyFriendProfileActionEvent;
import hello.leavesC.presenter.viewModel.ModifyFriendProfileViewModel;
import hello.leavesC.presenter.viewModel.base.BaseViewModel;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:12
 * 说明：修改好友备注
 */
public class AlterFriendRemarkActivity extends BaseActivity {

    private static final String IDENTIFIER = "identifier";

    private String identifier;

    private String original;

    private ModifyFriendProfileViewModel profileViewModel;

    public static void navigation(Context context, String identifier) {
        Intent intent = new Intent(context, AlterFriendRemarkActivity.class);
        intent.putExtra(IDENTIFIER, identifier);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_friend_remark);
        identifier = getIntent().getStringExtra(IDENTIFIER);
        original = FriendCache.getInstance().getProfile(identifier).getRemark();
        initView();
    }

    @Override
    protected BaseViewModel initViewModel() {
        profileViewModel = ViewModelProviders.of(this).get(ModifyFriendProfileViewModel.class);
        profileViewModel.getModifyLiveData().observe(this, this::handleModifyEvent);
        return profileViewModel;
    }

    private void handleModifyEvent(ModifyFriendProfileActionEvent modifyFriendProfileActionEvent) {
        switch (modifyFriendProfileActionEvent.getAction()) {
            case ModifyFriendProfileActionEvent.MODIFY_SUCCESS: {
                finish();
                break;
            }
        }
    }

    private void initView() {
        setToolbarTitle("设置备注");
        EditText et_alterFriendRemark = findViewById(R.id.et_alterFriendRemark);
        setToolbarBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remark = et_alterFriendRemark.getText().toString().trim();
                profileViewModel.modifyFriendRemark(identifier, remark);
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
                    getToolbarBtn().setEnabled(false);
                } else {
                    getToolbarBtn().setEnabled(true);
                }
            }
        });
    }

}