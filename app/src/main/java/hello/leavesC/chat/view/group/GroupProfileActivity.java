package hello.leavesC.chat.view.group;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;

import hello.leavesC.chat.ChatApplication;
import hello.leavesC.chat.R;
import hello.leavesC.chat.cache.FriendCache;
import hello.leavesC.chat.cache.GroupCache;
import hello.leavesC.chat.model.GroupProfile;
import hello.leavesC.chat.presenter.GroupProfilePresenter;
import hello.leavesC.chat.utils.TimeUtil;
import hello.leavesC.chat.utils.TransformUtil;
import hello.leavesC.chat.view.base.BaseActivity;
import hello.leavesC.presenter.listener.CallBackListener;
import hello.leavesC.presenter.manager.GroupManager;
import hello.leavesC.presenter.manager.GroupProfileManager;
import hello.leavesC.common.common.CircleImageView;
import hello.leavesC.common.common.OptionView;

/**
 * 作者：叶应是叶
 * 时间：2018/1/13 22:37
 * 说明：群组资料界面
 */
public class GroupProfileActivity extends BaseActivity implements Observer {

    private static final String TAG = "GroupProfileActivity";

    private static final String GROUP_ID = "groupId";

    private String groupId;

    private GroupProfilePresenter groupProfilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);
        groupId = getIntent().getStringExtra(GROUP_ID);
        GroupProfile groupProfile = GroupCache.getInstance().getGroupProfile(groupId);
        if (groupProfile != null) {
            initView(groupProfile);
            groupProfilePresenter = new GroupProfilePresenter(groupId);
            groupProfilePresenter.addObserver(this);
        }
    }

    private void initView(final GroupProfile groupProfile) {
        initToolbar(R.id.toolbar_groupProfile, groupProfile.getName());
        CircleImageView iv_groupProfile_avatar = (CircleImageView) findViewById(R.id.iv_groupProfile_avatar);
        OptionView ov_groupProfile_groupName = (OptionView) findViewById(R.id.ov_groupProfile_groupName);
        OptionView ov_groupProfile_groupOwner = (OptionView) findViewById(R.id.ov_groupProfile_groupOwner);
        OptionView ov_groupProfile_members = (OptionView) findViewById(R.id.ov_groupProfile_members);
        OptionView ov_groupProfile_introduction = (OptionView) findViewById(R.id.ov_groupProfile_introduction);
        OptionView ov_groupProfile_notification = (OptionView) findViewById(R.id.ov_groupProfile_notification);
        OptionView ov_groupProfile_createTime = (OptionView) findViewById(R.id.ov_groupProfile_createTime);
        final OptionView ov_groupProfile_receiveMessageOpt = (OptionView) findViewById(R.id.ov_groupProfile_receiveMessageOpt);
        Button btn_groupProfile_inviteGroupMember = (Button) findViewById(R.id.btn_groupProfile_inviteGroupMember);
        Button btn_groupProfile_quitGroup = (Button) findViewById(R.id.btn_groupProfile_quitGroup);
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
                        showMessageDialog(null, "确认退出群组吗？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    GroupManager.quitGroup(groupId, new CallBackListener() {
                                        @Override
                                        public void onSuccess() {
                                            if (!isFinishing() && !isDestroyed()) {
                                                showToast("已退出群组");
                                                setResult(RESULT_OK);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onError(int code, String desc) {
                                            if (!isFinishing() && !isDestroyed()) {
                                                showToast("群出群组出错，" + code + " " + desc);
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
                GroupProfileManager.modifyGroupReceiveMessageOpt(groupId, ChatApplication.identifier,
                        TransformUtil.parseGroupReceiveMessageOpt(content), null);
            }
        });
    }

    public static void navigation(Activity activity, String identifier, int requestCode) {
        Intent intent = new Intent(activity, GroupProfileActivity.class);
        intent.putExtra(GROUP_ID, identifier);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (groupProfilePresenter != null) {
            groupProfilePresenter.deleteObserver(this);
            groupProfilePresenter.clean();
            groupProfilePresenter = null;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof GroupProfilePresenter) {
            if (arg != null && arg instanceof GroupProfile) {
                initView((GroupProfile) arg);
            }
        }
    }

}
