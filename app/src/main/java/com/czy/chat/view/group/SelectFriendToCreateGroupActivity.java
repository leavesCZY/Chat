package com.czy.chat.view.group;

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
import com.czy.chat.cache.GroupCache;
import com.czy.chat.model.FriendProfile;
import com.czy.chat.view.base.BaseActivity;
import com.czy.presenter.listener.ValueCallBackListener;
import com.czy.presenter.log.Logger;
import com.czy.presenter.manager.GroupManager;
import com.czy.ui.common.LetterIndexView;
import com.czy.ui.recycler.common.CommonItemDecoration;
import com.czy.ui.recycler.common.CommonRecyclerViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2018/1/7 20:44
 * 说明：
 */
public class SelectFriendToCreateGroupActivity extends BaseActivity {

    private List<FriendProfile> friendProfileList;

    private SelectFriendAdapter selectFriendAdapter;

    private List<String> peerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friend);
        initToolbar(R.id.toolbar_selectFriend, "至少选择两位好友");
        final Button btn_selectFriend_createGroup = (Button) findViewById(R.id.btn_selectFriend_createGroup);
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
                btn_selectFriend_createGroup.setEnabled(peerList.size() > 1);
            }
        });
        RecyclerView rv_selectFriend = (RecyclerView) findViewById(R.id.rv_selectFriend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_selectFriend.setLayoutManager(linearLayoutManager);
        rv_selectFriend.setAdapter(selectFriendAdapter);
        rv_selectFriend.addItemDecoration(new CommonItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.divider), LinearLayoutManager.VERTICAL));
        LetterIndexView liv_letters = (LetterIndexView) findViewById(R.id.liv_selectFriend_letters);
        TextView tv_selectFriend_hint = (TextView) findViewById(R.id.tv_selectFriend_hint);
        liv_letters.bindIndexView(tv_selectFriend_hint, linearLayoutManager, new HashMap<String, Integer>());
        btn_selectFriend_createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog("正在创建聊天群");
                GroupManager.createGroup("群聊", GroupCache.PRIVATE_GROUP, peerList, new ValueCallBackListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (!isFinishingOrDestroyed()) {
                            dismissLoadingDialog();
                            showToast("创建成功");
                            finish();
                        }
                    }

                    @Override
                    public void onError(int code, String desc) {
                        if (!isFinishingOrDestroyed()) {
                            dismissLoadingDialog();
                            showToast("创建聊天群失败：" + code + " " + desc);
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

}
