package com.czy.chat.view.group;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.czy.chat.R;
import com.czy.chat.adapter.GroupListAdapter;
import com.czy.chat.cache.GroupCache;
import com.czy.chat.model.GroupProfile;
import com.czy.chat.view.base.BaseActivity;
import com.czy.chat.view.chat.ChatActivity;
import com.czy.ui.recycler.common.CommonItemDecoration;
import com.czy.ui.recycler.common.CommonRecyclerViewHolder;
import com.tencent.imsdk.TIMConversationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 作者：叶应是叶
 * 时间：2018/1/7 20:18
 * 说明：群组列表界面
 */
public class GroupListActivity extends BaseActivity implements Observer {

    private List<GroupProfile> groupProfileList;

    private GroupListAdapter groupListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        initToolbar(R.id.toolbar_groupList, "群");
        RecyclerView rv_groupList = (RecyclerView) findViewById(R.id.rv_groupList);
        rv_groupList.setLayoutManager(new LinearLayoutManager(this));
        rv_groupList.addItemDecoration(new CommonItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.divider), LinearLayoutManager.VERTICAL));
        groupProfileList = new ArrayList<>();
        groupProfileList.addAll(GroupCache.getInstance().getAllGroup());
        groupListAdapter = new GroupListAdapter(this, groupProfileList);
        groupListAdapter.setClickListener(new CommonRecyclerViewHolder.OnClickListener() {
            @Override
            public void onClick(int position) {
                ChatActivity.navigation(GroupListActivity.this, groupProfileList.get(position).getIdentifier(), TIMConversationType.Group);
                finish();
            }
        });
        rv_groupList.setAdapter(groupListAdapter);
        GroupCache.getInstance().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        groupProfileList.clear();
        groupProfileList.addAll(GroupCache.getInstance().getAllGroup());
        groupListAdapter.setData(groupProfileList);
    }

}
