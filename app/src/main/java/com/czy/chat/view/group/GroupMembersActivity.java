package com.czy.chat.view.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.czy.chat.R;
import com.czy.chat.adapter.GroupMembersAdapter;
import com.czy.chat.view.base.BaseActivity;
import com.czy.presenter.listener.ValueCallBackListener;
import com.czy.presenter.manager.GroupManager;
import com.czy.presenter.model.GroupMemberInfo;
import com.czy.ui.recycler.common.CommonItemDecoration;
import com.czy.ui.recycler.common.CommonRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2018/1/22 21:59
 * 说明：群组成员列表界面
 */
public class GroupMembersActivity extends BaseActivity {

    private static final String GROUP_ID = "groupId";

    private GroupMembersAdapter groupMembersAdapter;

    private List<GroupMemberInfo> groupMemberInfoList;

    private ValueCallBackListener<List<GroupMemberInfo>> callBackListener = new ValueCallBackListener<List<GroupMemberInfo>>() {
        @Override
        public void onSuccess(List<GroupMemberInfo> result) {
            groupMemberInfoList.addAll(result);
            groupMembersAdapter.setData(groupMemberInfoList);
        }

        @Override
        public void onError(int code, String desc) {
            showToast("获取群成员失败,Code：" + code + " " + desc);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);
        final String groupId = getIntent().getStringExtra(GROUP_ID);
        initToolbar(R.id.toolbar_groupMembers, "群成员");
        GroupManager.getGroupMembers(groupId, callBackListener);
        groupMemberInfoList = new ArrayList<>();
        groupMembersAdapter = new GroupMembersAdapter(this, groupMemberInfoList);
        RecyclerView rv_groupMembers = (RecyclerView) findViewById(R.id.rv_groupMembers);
        rv_groupMembers.addItemDecoration(new CommonItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.divider), LinearLayoutManager.VERTICAL));
        rv_groupMembers.setLayoutManager(new LinearLayoutManager(this));
        rv_groupMembers.setAdapter(groupMembersAdapter);
        groupMembersAdapter.setClickListener(new CommonRecyclerViewHolder.OnClickListener() {
            @Override
            public void onClick(int position) {
                GroupMemberProfileActivity.navigation(GroupMembersActivity.this, groupId, groupMemberInfoList.get(position).getIdentifier());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        groupMemberInfoList.clear();
        callBackListener = null;
    }

    public static void navigation(Context context, String groupId) {
        Intent intent = new Intent(context, GroupMembersActivity.class);
        intent.putExtra(GROUP_ID, groupId);
        context.startActivity(intent);
    }

}
