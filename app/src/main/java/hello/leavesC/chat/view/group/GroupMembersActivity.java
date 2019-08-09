package hello.leavesC.chat.view.group;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hello.leavesC.chat.R;
import hello.leavesC.chat.adapter.GroupMembersAdapter;
import hello.leavesC.chat.view.base.BaseActivity;
import hello.leavesC.common.recycler.common.CommonItemDecoration;
import hello.leavesC.presenter.viewModel.GroupProfileViewModel;
import hello.leavesC.presenter.model.GroupMemberInfo;

/**
 * 作者：叶应是叶
 * 时间：2018/1/22 21:59
 * 说明：群组成员列表界面
 */
public class GroupMembersActivity extends BaseActivity {

    private static final String GROUP_ID = "groupId";

    private GroupMembersAdapter groupMembersAdapter;

    private List<GroupMemberInfo> groupMemberInfoList;

    private GroupProfileViewModel memberProfileViewModel;

    public static void navigation(Context context, String groupId) {
        Intent intent = new Intent(context, GroupMembersActivity.class);
        intent.putExtra(GROUP_ID, groupId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);
        String groupId = getIntent().getStringExtra(GROUP_ID);
        setToolbarTitle("群成员");
        groupMemberInfoList = new ArrayList<>();
        groupMembersAdapter = new GroupMembersAdapter(this, groupMemberInfoList);
        RecyclerView rv_groupMembers = findViewById(R.id.rv_groupMembers);
        rv_groupMembers.addItemDecoration(new CommonItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.divider), LinearLayoutManager.VERTICAL));
        rv_groupMembers.setLayoutManager(new LinearLayoutManager(this));
        rv_groupMembers.setAdapter(groupMembersAdapter);
        groupMembersAdapter.setClickListener(position -> GroupMemberProfileActivity.navigation(GroupMembersActivity.this, groupId, groupMemberInfoList.get(position).getIdentifier()));
        memberProfileViewModel.getGroupMembers(groupId);
    }

    private void handleAction(List<GroupMemberInfo> result) {
        groupMemberInfoList.clear();
        groupMemberInfoList.addAll(result);
        groupMembersAdapter.setData(groupMemberInfoList);
    }

    @Override
    protected ViewModel initViewModel() {
        memberProfileViewModel = ViewModelProviders.of(this).get(GroupProfileViewModel.class);
        memberProfileViewModel.getGroupMemberInfoListLiveData().observe(this, this::handleAction);
        return memberProfileViewModel;
    }

}