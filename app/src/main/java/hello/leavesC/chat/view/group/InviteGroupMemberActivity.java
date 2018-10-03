package hello.leavesC.chat.view.group;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hello.leavesC.chat.R;
import hello.leavesC.chat.adapter.SelectFriendAdapter;
import hello.leavesC.chat.cache.FriendCache;
import hello.leavesC.chat.model.FriendProfile;
import hello.leavesC.chat.view.base.BaseActivity;
import hello.leavesC.common.common.LetterIndexView;
import hello.leavesC.common.recycler.common.CommonItemDecoration;
import hello.leavesC.common.recycler.common.CommonRecyclerViewHolder;
import hello.leavesC.presenter.event.GroupProfileActionEvent;
import hello.leavesC.presenter.viewModel.GroupProfileViewModel;
import hello.leavesC.presenter.viewModel.base.BaseViewModel;

/**
 * 作者：叶应是叶
 * 时间：2018/2/3 11:14
 * 说明：
 */
public class InviteGroupMemberActivity extends BaseActivity {

    private List<FriendProfile> friendProfileList;

    private SelectFriendAdapter selectFriendAdapter;

    private List<String> peerList;

    private static final String GROUP_ID = "groupId";

    private GroupProfileViewModel groupProfileViewModel;

    public static void navigation(Context context, String groupId) {
        Intent intent = new Intent(context, InviteGroupMemberActivity.class);
        intent.putExtra(GROUP_ID, groupId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_group_member);
        setToolbarTitle("邀请好友入群");
        final String groupId = getIntent().getStringExtra(GROUP_ID);
        peerList = new ArrayList<>();
        friendProfileList = new ArrayList<>();
        friendProfileList.addAll(FriendCache.getInstance().getFriendProfileList());
        selectFriendAdapter = new SelectFriendAdapter(getContext(), friendProfileList);
        selectFriendAdapter.setClickListener(new CommonRecyclerViewHolder.OnClickListener() {
            @Override
            public void onClick(int position) {
                FriendProfile friendProfile = friendProfileList.get(position);
                friendProfile.setSelected(!friendProfile.isSelected());
                if (friendProfile.isSelected()) {
                    peerList.add(friendProfile.getIdentifier());
                } else {
                    peerList.remove(friendProfile.getIdentifier());
                }
                selectFriendAdapter.setData(friendProfileList);
                getToolbarBtn().setEnabled(peerList.size() > 0);
            }
        });
        RecyclerView rv_inviteGroupMember = findViewById(R.id.rv_inviteGroupMember);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_inviteGroupMember.setLayoutManager(linearLayoutManager);
        rv_inviteGroupMember.setAdapter(selectFriendAdapter);
        rv_inviteGroupMember.addItemDecoration(new CommonItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.divider), LinearLayoutManager.VERTICAL));
        LetterIndexView liv_inviteGroupMember_letters = findViewById(R.id.liv_inviteGroupMember_letters);
        TextView tv_inviteGroupMember_hint = findViewById(R.id.tv_inviteGroupMember_hint);
        liv_inviteGroupMember_letters.bindIndexView(tv_inviteGroupMember_hint, linearLayoutManager, new HashMap<String, Integer>());
        setToolbarBtnClickListener(v -> groupProfileViewModel.inviteGroup(groupId, peerList));
    }

    @Override
    protected BaseViewModel initViewModel() {
        groupProfileViewModel = ViewModelProviders.of(this).get(GroupProfileViewModel.class);
        groupProfileViewModel.getActionEventLiveData().observe(this, this::handleAction);
        return groupProfileViewModel;
    }

    private void handleAction(GroupProfileActionEvent groupProfileActionEvent) {
        switch (groupProfileActionEvent.getAction()) {
            case GroupProfileActionEvent.INVITE_GROUP_SUCCESS: {
                finish();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (FriendProfile friendProfile : friendProfileList) {
            friendProfile.setSelected(false);
        }
    }

}