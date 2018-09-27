package hello.leavesC.chat.view.contacts;

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
import hello.leavesC.presenter.listener.CallBackListener;
import hello.leavesC.presenter.manager.FriendManager;

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
        setToolbarTitle("设置备注");
        final EditText et_alterFriendRemark = findViewById(R.id.et_alterFriendRemark);
        setBtnToolbarSureClickListener(new View.OnClickListener() {
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
                    getBtnToolbarSure().setEnabled(false);
                } else {
                    getBtnToolbarSure().setEnabled(true);
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
