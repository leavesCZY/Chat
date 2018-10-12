package hello.leavesC.chat.view.group;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.List;
import java.util.Map;

import hello.leavesC.chat.ChatApplication;
import hello.leavesC.chat.R;
import hello.leavesC.chat.cache.FriendCache;
import hello.leavesC.chat.cache.GroupCache;
import hello.leavesC.chat.model.GroupProfile;
import hello.leavesC.chat.utils.TimeUtil;
import hello.leavesC.chat.view.base.BaseActivity;
import hello.leavesC.common.common.CircleImageView;
import hello.leavesC.common.common.OptionView;
import hello.leavesC.presenter.utils.TransformUtil;
import hello.leavesC.presenter.viewModel.GroupProfileViewModel;
import hello.leavesC.presenter.viewModel.base.BaseViewModel;

/**
 * 作者：叶应是叶
 * 时间：2018/1/13 22:37
 * 说明：群组资料界面
 */
public class GroupProfileActivity extends BaseActivity {

    private static final String TAG = "GroupProfileActivity";

    private static final String GROUP_ID = "groupId";

    private String groupId;

    private GroupProfileViewModel groupProfileViewModel;

    public static void navigation(Activity activity, String identifier, int requestCode) {
        Intent intent = new Intent(activity, GroupProfileActivity.class);
        intent.putExtra(GROUP_ID, identifier);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);
        groupId = getIntent().getStringExtra(GROUP_ID);
        GroupProfile groupProfile = GroupCache.getInstance().getGroupProfile(groupId);
        initView(groupProfile);
        GroupCache.getInstance().observe(this, this::handle);
    }

    private void handle(Map<String, List<GroupProfile>> stringListMap) {
        GroupProfile groupProfile = GroupCache.getInstance().getGroupProfile(groupId);
        initView(groupProfile);
    }

    @Override
    protected BaseViewModel initViewModel() {
        groupProfileViewModel = ViewModelProviders.of(this).get(GroupProfileViewModel.class);
        return groupProfileViewModel;
    }

    private void initView(final GroupProfile groupProfile) {
        setToolbarTitle(groupProfile.getName());
        CircleImageView iv_groupProfile_avatar = findViewById(R.id.iv_groupProfile_avatar);
        OptionView ov_groupProfile_groupName = findViewById(R.id.ov_groupProfile_groupName);
        OptionView ov_groupProfile_groupOwner = findViewById(R.id.ov_groupProfile_groupOwner);
        OptionView ov_groupProfile_members = findViewById(R.id.ov_groupProfile_members);
        OptionView ov_groupProfile_introduction = findViewById(R.id.ov_groupProfile_introduction);
        OptionView ov_groupProfile_notification = findViewById(R.id.ov_groupProfile_notification);
        OptionView ov_groupProfile_createTime = findViewById(R.id.ov_groupProfile_createTime);
        final OptionView ov_groupProfile_receiveMessageOpt = findViewById(R.id.ov_groupProfile_receiveMessageOpt);
        Button btn_groupProfile_inviteGroupMember = findViewById(R.id.btn_groupProfile_inviteGroupMember);
        Button btn_groupProfile_quitGroup = findViewById(R.id.btn_groupProfile_quitGroup);
        ov_groupProfile_groupName.setContent(groupProfile.getName());
        ov_groupProfile_groupOwner.setContent(FriendCache.getInstance().getFriendName(groupProfile.getOwner()));
        ov_groupProfile_members.setContent(String.valueOf(groupProfile.getMemberNumber()));
        ov_groupProfile_introduction.setContent(groupProfile.getIntroduction());
        ov_groupProfile_notification.setContent(groupProfile.getNotification());
        ov_groupProfile_createTime.setContent(TimeUtil.formatTime(groupProfile.getCreateTime() * 1000, TimeUtil.FORMAT_YYYY_MM_DD_HH_MM));
        ov_groupProfile_receiveMessageOpt.setContent(TransformUtil.parseGroupReceiveMessageOpt(groupProfile.getGroupBasicSelfInfo().getRecvMsgOption()));
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ov_groupProfile_groupName: {
                        GroupProfileModifyActivity.navigation(GroupProfileActivity.this, groupProfile.getIdentifier(), GroupProfileModifyActivity.ALTER_GROUP_NAME);
                        break;
                    }
                    case R.id.ov_groupProfile_members: {
                        GroupMembersActivity.navigation(GroupProfileActivity.this, groupId);
                        break;
                    }
                    case R.id.ov_groupProfile_introduction: {
                        GroupProfileModifyActivity.navigation(GroupProfileActivity.this, groupProfile.getIdentifier(), GroupProfileModifyActivity.ALTER_GROUP_INTRODUCTION);
                        break;
                    }
                    case R.id.ov_groupProfile_notification: {
                        GroupProfileModifyActivity.navigation(GroupProfileActivity.this, groupProfile.getIdentifier(), GroupProfileModifyActivity.ALTER_GROUP_NOTIFICATION);
                        break;
                    }
                    case R.id.btn_groupProfile_inviteGroupMember: {
                        InviteGroupMemberActivity.navigation(GroupProfileActivity.this, groupId);
                        break;
                    }
                    case R.id.btn_groupProfile_quitGroup: {
                        new QMUIDialog.MessageDialogBuilder(GroupProfileActivity.this)
                                .setTitle(null)
                                .setMessage("确认退出群组吗？")
                                .addAction("取消", (dialog, index) -> dialog.dismiss())
                                .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                        groupProfileViewModel.quitGroup(groupId);
                                    }
                                })
                                .create().show();
                        break;
                    }
                }
            }
        };
        ov_groupProfile_groupName.setOnClickListener(clickListener);
        ov_groupProfile_members.setOnClickListener(clickListener);
        ov_groupProfile_introduction.setOnClickListener(clickListener);
        ov_groupProfile_notification.setOnClickListener(clickListener);
        btn_groupProfile_inviteGroupMember.setOnClickListener(clickListener);
        btn_groupProfile_quitGroup.setOnClickListener(clickListener);
        ov_groupProfile_receiveMessageOpt.setOnClickShowPickerDialog("消息接收", TransformUtil.getGroupReceiveMessageOpt(), getSupportFragmentManager(), new OptionView.onClickOptionListener() {
            @Override
            public void onClick(int id, int which, String content) {
                ov_groupProfile_receiveMessageOpt.setContent(content);
                groupProfileViewModel.modifyGroupReceiveMessageOpt(groupId, ChatApplication.identifier,
                        TransformUtil.parseGroupReceiveMessageOpt(content));
            }
        });
    }

}