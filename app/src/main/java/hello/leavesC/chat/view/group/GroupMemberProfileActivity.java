package hello.leavesC.chat.view.group;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.imsdk.TIMGroupMemberInfo;

import hello.leavesC.chat.R;
import hello.leavesC.chat.cache.FriendCache;
import hello.leavesC.chat.model.FriendProfile;
import hello.leavesC.chat.utils.TimeUtil;
import hello.leavesC.chat.view.base.BaseActivity;
import hello.leavesC.common.common.OptionView;
import hello.leavesC.presenter.viewModel.GroupProfileViewModel;
import hello.leavesC.presenter.TransformUtil;
import hello.leavesC.presenter.viewModel.base.BaseViewModel;

/**
 * 作者：叶应是叶
 * 时间：2018/1/21 10:33
 * 说明：群组成员资料界面
 */
public class GroupMemberProfileActivity extends BaseActivity {

    private static final String GROUP_ID = "groupId";

    private static final String IDENTIFIER = "identifier";

    private GroupProfileViewModel profileViewModel;

    private static final String TAG = "GroupMemberProfileActivity";

    public static void navigation(Context context, String groupId, String identifier) {
        Intent intent = new Intent(context, GroupMemberProfileActivity.class);
        intent.putExtra(GROUP_ID, groupId);
        intent.putExtra(IDENTIFIER, identifier);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_profile);
        setToolbarTitle("群成员信息");
        String groupId = getIntent().getStringExtra(GROUP_ID);
        String identifier = getIntent().getStringExtra(IDENTIFIER);
        profileViewModel.getGroupMemberInfo(groupId, identifier);
    }

    @Override
    protected BaseViewModel initViewModel() {
        profileViewModel = ViewModelProviders.of(this).get(GroupProfileViewModel.class);
        profileViewModel.getGroupMemberInfoLiveData().observe(this, this::initView);
        return profileViewModel;
    }

    private void initView(TIMGroupMemberInfo memberInfo) {
        OptionView ov_groupMemberProfile_identifier = findViewById(R.id.ov_groupMemberProfile_identifier);
        OptionView ov_groupMemberProfile_role = findViewById(R.id.ov_groupMemberProfile_role);
        OptionView ov_groupMemberProfile_joinTime = findViewById(R.id.ov_groupMemberProfile_joinTime);
        OptionView ov_groupMemberProfile_remark = findViewById(R.id.ov_groupMemberProfile_remark);
        ov_groupMemberProfile_identifier.setContent(memberInfo.getUser());
        ov_groupMemberProfile_role.setContent(TransformUtil.parseGroupMemberRoleType(memberInfo.getRole()));
        ov_groupMemberProfile_joinTime.setContent(TimeUtil.formatTime(memberInfo.getJoinTime() * 1000, TimeUtil.FORMAT_YYYY_MM_DD_HH_MM));
        FriendProfile friendProfile = FriendCache.getInstance().getProfile(memberInfo.getUser());
        if (friendProfile != null) {
            ov_groupMemberProfile_remark.setContent(friendProfile.getName());
        }
    }

}