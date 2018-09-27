package hello.leavesC.chat.view.group;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hello.leavesC.chat.R;
import hello.leavesC.chat.adapter.SelectFriendAdapter;
import hello.leavesC.chat.cache.FriendCache;
import hello.leavesC.chat.cache.GroupCache;
import hello.leavesC.chat.model.FriendProfile;
import hello.leavesC.chat.view.base.BaseActivity;
import hello.leavesC.common.common.LetterIndexView;
import hello.leavesC.common.recycler.common.CommonItemDecoration;
import hello.leavesC.common.recycler.common.CommonRecyclerViewHolder;
import hello.leavesC.presenter.listener.ValueCallBackListener;
import hello.leavesC.presenter.manager.GroupManager;

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
        setToolbarTitle("至少选择两位好友");
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
                getToolbarBtn().setEnabled(peerList.size() > 1);
            }
        });
        RecyclerView rv_selectFriend = findViewById(R.id.rv_selectFriend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_selectFriend.setLayoutManager(linearLayoutManager);
        rv_selectFriend.setAdapter(selectFriendAdapter);
        rv_selectFriend.addItemDecoration(new CommonItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.divider), LinearLayoutManager.VERTICAL));
        LetterIndexView liv_letters = findViewById(R.id.liv_selectFriend_letters);
        TextView tv_selectFriend_hint = findViewById(R.id.tv_selectFriend_hint);
        liv_letters.bindIndexView(tv_selectFriend_hint, linearLayoutManager, new HashMap<String, Integer>());
        setToolbarBtnClickListener(new View.OnClickListener() {
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