package com.czy.chat.view.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.czy.chat.R;
import com.czy.chat.adapter.SelectFriendAdapter;
import com.czy.chat.cache.FriendCache;
import com.czy.chat.model.FriendProfile;
import com.czy.chat.view.base.BaseActivity;
import com.czy.presenter.listener.ValueCallBackListener;
import com.czy.presenter.manager.GroupManager;
import com.czy.ui.common.LetterIndexView;
import com.czy.ui.recycler.common.CommonItemDecoration;
import com.czy.ui.recycler.common.CommonRecyclerViewHolder;
import com.tencent.imsdk.ext.group.TIMGroupMemberResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_group_member);
        initToolbar(R.id.toolbar_inviteGroupMember, "邀请好友入群");
        final String groupId = getIntent().getStringExtra(GROUP_ID);
        final Button btn_inviteGroupMember_invite = (Button) findViewById(R.id.btn_inviteGroupMember_invite);
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
                btn_inviteGroupMember_invite.setEnabled(peerList.size() > 0);
            }
        });
        RecyclerView rv_inviteGroupMember = (RecyclerView) findViewById(R.id.rv_inviteGroupMember);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_inviteGroupMember.setLayoutManager(linearLayoutManager);
        rv_inviteGroupMember.setAdapter(selectFriendAdapter);
        rv_inviteGroupMember.addItemDecoration(new CommonItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.divider), LinearLayoutManager.VERTICAL));
        LetterIndexView liv_inviteGroupMember_letters = (LetterIndexView) findViewById(R.id.liv_inviteGroupMember_letters);
        TextView tv_inviteGroupMember_hint = (TextView) findViewById(R.id.tv_inviteGroupMember_hint);
        liv_inviteGroupMember_letters.bindIndexView(tv_inviteGroupMember_hint, linearLayoutManager, new HashMap<String, Integer>());
        btn_inviteGroupMember_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog("正在邀请好友入群");
                GroupManager.inviteGroup(groupId, peerList, new ValueCallBackListener<List<TIMGroupMemberResult>>() {
                    @Override
                    public void onSuccess(List<TIMGroupMemberResult> result) {
                        if (!isFinishingOrDestroyed()) {
                            dismissLoadingDialog();
                            showToast("好友已入群");
                            finish();
                        }
                    }

                    @Override
                    public void onError(int code, String desc) {
                        if (!isFinishingOrDestroyed()) {
                            dismissLoadingDialog();
                            showToast("Error ：" + code + " " + desc);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (FriendProfile friendProfile : friendProfileList) {
            friendProfile.setSelected(false);
        }
    }

    public static void navigation(Context context, String groupId) {
        Intent intent = new Intent(context, InviteGroupMemberActivity.class);
        intent.putExtra(GROUP_ID, groupId);
        context.startActivity(intent);
    }

}
