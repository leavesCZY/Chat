package hello.leavesC.chat.view.contacts;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendGenderType;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import hello.leavesC.chat.R;
import hello.leavesC.chat.cache.FriendCache;
import hello.leavesC.chat.model.FriendProfile;
import hello.leavesC.chat.view.base.BaseActivity;
import hello.leavesC.chat.view.chat.ChatActivity;
import hello.leavesC.common.common.OptionView;
import hello.leavesC.presenter.viewModel.ModifyFriendProfileViewModel;
import hello.leavesC.presenter.viewModel.base.BaseViewModel;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:14
 * 说明：好友资料详情页
 */
public class FriendProfileActivity extends BaseActivity {

    private static final String IDENTIFIER = "identifier";

    private String identifier;

    @BindView(R.id.tv_friendProfile_name)
    TextView tv_friendProfile_name;

    @BindView(R.id.iv_friendProfile_gender)
    ImageView iv_friendProfile_gender;

    @BindView(R.id.ov_friendProfile_identifier)
    OptionView ov_friendProfile_identifier;

    @BindView(R.id.ov_friendProfile_nickname)
    OptionView ov_friendProfile_nickname;

    @BindView(R.id.ov_friendProfile_signature)
    OptionView ov_friendProfile_signature;

    @BindView(R.id.btn_friendProfile_startChat)
    Button btn_friendProfile_startChat;

    @BindView(R.id.btn_friendProfile_deleteFriend)
    Button btn_friendProfile_deleteFriend;

    private ModifyFriendProfileViewModel profileViewModel;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_friendProfile_startChat: {
                    ChatActivity.navigation(FriendProfileActivity.this, identifier, TIMConversationType.C2C);
                    setResult(RESULT_OK);
                    finish();
                    break;
                }
                case R.id.btn_friendProfile_deleteFriend: {
                    new QMUIDialog.MessageDialogBuilder(FriendProfileActivity.this)
                            .setTitle(null)
                            .setMessage("确认删除好友？")
                            .addAction("取消", (dialog, index) -> dialog.dismiss())
                            .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    profileViewModel.deleteFriend(identifier);
                                    dialog.dismiss();
                                }
                            })
                            .create().show();
                    break;
                }
            }
        }
    };

    public static void navigation(Activity activity, String identifier, int requestCode) {
        Intent intent = new Intent(activity, FriendProfileActivity.class);
        intent.putExtra(IDENTIFIER, identifier);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void navigation(Context context, String identifier) {
        Intent intent = new Intent(context, FriendProfileActivity.class);
        intent.putExtra(IDENTIFIER, identifier);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        identifier = getIntent().getStringExtra(IDENTIFIER);
        if (TextUtils.isEmpty(identifier) || !FriendCache.getInstance().isFriend(identifier)) {
            finish();
        }
        initView();
        getProfile();
        FriendCache.getInstance().observe(this, this::handle);
    }

    private void handle(Map<String, FriendProfile> stringFriendProfileMap) {
        getProfile();
    }

    @Override
    protected BaseViewModel initViewModel() {
        profileViewModel = ViewModelProviders.of(this).get(ModifyFriendProfileViewModel.class);
        return profileViewModel;
    }

    private void initView() {
        ButterKnife.bind(this);
        setToolbarTitle("个人名片");
    }

    private void getProfile() {
        FriendProfile friendProfile = FriendCache.getInstance().getProfile(identifier);
        if (friendProfile != null) {
            tv_friendProfile_name.setText(friendProfile.getName());
            if (friendProfile.getGender() == TIMFriendGenderType.Male) {
                iv_friendProfile_gender.setImageResource(R.drawable.gender_male);
            } else if (friendProfile.getGender() == TIMFriendGenderType.Female) {
                iv_friendProfile_gender.setImageResource(R.drawable.gender_female);
            }
            ov_friendProfile_identifier.setContent(identifier);
            ov_friendProfile_nickname.setContent(friendProfile.getNickname());
            ov_friendProfile_signature.setContent(friendProfile.getSelfSignature());
            btn_friendProfile_startChat.setOnClickListener(clickListener);
            btn_friendProfile_deleteFriend.setOnClickListener(clickListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.set_remark, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_setRemark) {
            AlterFriendRemarkActivity.navigation(this, identifier);
        }
        return super.onOptionsItemSelected(item);
    }

}