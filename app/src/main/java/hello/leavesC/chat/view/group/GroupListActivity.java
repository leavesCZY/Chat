package hello.leavesC.chat.view.group;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tencent.imsdk.TIMConversationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hello.leavesC.chat.R;
import hello.leavesC.chat.adapter.GroupListAdapter;
import hello.leavesC.chat.cache.GroupCache;
import hello.leavesC.chat.model.GroupProfile;
import hello.leavesC.chat.view.base.BaseActivity;
import hello.leavesC.chat.view.chat.ChatActivity;
import hello.leavesC.common.recycler.common.CommonItemDecoration;
import hello.leavesC.common.recycler.common.CommonRecyclerViewHolder;
import hello.leavesC.presenter.viewModel.base.BaseViewModel;

/**
 * 作者：叶应是叶
 * 时间：2018/1/7 20:18
 * 说明：群组列表界面
 */
public class GroupListActivity extends BaseActivity {

    private List<GroupProfile> groupProfileList;

    private GroupListAdapter groupListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        setToolbarTitle("群");
        RecyclerView rv_groupList = findViewById(R.id.rv_groupList);
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
        GroupCache.getInstance().observe(this, this::handle);
    }

    private void handle(Map<String, List<GroupProfile>> stringListMap) {
        groupProfileList.clear();
        groupProfileList.addAll(GroupCache.getInstance().getAllGroup());
        groupListAdapter.setData(groupProfileList);
    }

    @Override
    protected BaseViewModel initViewModel() {
        return null;
    }

}