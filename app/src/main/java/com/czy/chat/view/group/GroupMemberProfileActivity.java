package com.czy.chat.view.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.czy.chat.ChatApplication;
import com.czy.chat.R;
import com.czy.chat.cache.FriendCache;
import com.czy.chat.model.FriendProfile;
import com.czy.chat.utils.TimeUtil;
import com.czy.chat.utils.TransformUtil;
import com.czy.chat.view.base.BaseActivity;
import com.czy.presenter.listener.ValueCallBackListener;
import com.czy.presenter.log.Logger;
import com.czy.presenter.manager.GroupProfileManager;
import com.czy.ui.common.OptionView;
import com.tencent.imsdk.TIMGroupMemberInfo;

/**
 * 作者：叶应是叶
 * 时间：2018/1/21 10:33
 * 说明：群组成员资料界面
 */
public class GroupMemberProfileActivity extends BaseActivity {

    private static final String GROUP_ID = "groupId";

    private static final String IDENTIFIER = "identifier";

    private static final String TAG = "GroupMemberProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_profile);
        initToolbar(R.id.toolbar_groupMemberProfile, "群成员信息");
        String groupId = getIntent().getStringExtra(GROUP_ID);
        String identifier = getIntent().getStringExtra(IDENTIFIER);
        GroupProfileManager.getGroupMemberInfo(groupId, identifier, new ValueCallBackListener<TIMGroupMemberInfo>() {
            @Override
            public void onSuccess(TIMGroupMemberInfo result) {
                initView(result);
                Logger.e(TAG, "result: " + result);
            }

            @Override
            public void onError(int code, String desc) {
                showToast("获取群成员资料失败，" + desc);
            }
        });
    }

    private void initView(TIMGroupMemberInfo memberInfo) {
        OptionView ov_groupMemberProfile_identifier = (OptionView) findViewById(R.id.ov_groupMemberProfile_identifier);
        OptionView ov_groupMemberProfile_role = (OptionView) findViewById(R.id.ov_groupMemberProfile_role);
        OptionView ov_groupMemberProfile_joinTime = (OptionView) findViewById(R.id.ov_groupMemberProfile_joinTime);
        OptionView ov_groupMemberProfile_remark = (OptionView) findViewById(R.id.ov_groupMemberProfile_remark);
        ov_groupMemberProfile_identifier.setContent(memberInfo.getUser());
        ov_groupMemberProfile_role.setContent(TransformUtil.parseGroupMemberRoleType(memberInfo.getRole()));
        ov_groupMemberProfile_joinTime.setContent(TimeUtil.formatTime(memberInfo.getJoinTime() * 1000, TimeUtil.FORMAT_YYYY_MM_DD_HH_MM));
        if (ChatApplication.identifier.equals(memberInfo.getUser())) {
            return;
        }
        FriendProfile friendProfile = FriendCache.getInstance().getProfile(memberInfo.getUser());
        if (friendProfile == null) {
            Button btn_groupProfile_addFriend = (Button) findViewById(R.id.btn_groupProfile_addFriend);
            btn_groupProfile_addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showToast("添加为好友");
                }
            });
        } else {
            ov_groupMemberProfile_remark.setContent(friendProfile.getName());
        }
    }

    public static void navigation(Context context, String groupId, String identifier) {
        Intent intent = new Intent(context, GroupMemberProfileActivity.class);
        intent.putExtra(GROUP_ID, groupId);
        intent.putExtra(IDENTIFIER, identifier);
        context.startActivity(intent);
    }

}
