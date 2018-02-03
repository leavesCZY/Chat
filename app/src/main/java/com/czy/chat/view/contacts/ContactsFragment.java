package com.czy.chat.view.contacts;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.czy.chat.R;
import com.czy.chat.adapter.FriendAdapter;
import com.czy.chat.cache.FriendCache;
import com.czy.chat.model.FriendProfile;
import com.czy.chat.view.base.BaseFragment;
import com.czy.chat.view.group.GroupListActivity;
import com.czy.ui.common.LetterIndexView;
import com.czy.ui.recycler.common.CommonItemDecoration;
import com.czy.ui.recycler.common.CommonRecyclerViewHolder;
import com.czy.ui.recycler.wrap.WrapRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:12
 * 说明：好友列表界面
 */
public class ContactsFragment extends BaseFragment implements Observer {

    private List<FriendProfile> friendProfileList;

    private FriendAdapter friendWrapAdapter;

    private View view;

    private static final String TAG = "ContactsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_contacts, container, false);
            friendProfileList = new ArrayList<>();
            friendProfileList.addAll(FriendCache.getInstance().getFriendProfileList());
            friendWrapAdapter = new FriendAdapter(getContext(), friendProfileList);
            friendWrapAdapter.setClickListener(new CommonRecyclerViewHolder.OnClickListener() {
                @Override
                public void onClick(int position) {
                    FriendProfileActivity.navigation(getContext(), friendProfileList.get(position - 1).getIdentifier());
                }
            });
            RecyclerView rv_friendList = (RecyclerView) view.findViewById(R.id.rv_friendList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            rv_friendList.setLayoutManager(linearLayoutManager);
            WrapRecyclerViewAdapter wrapRecyclerViewAdapter = new WrapRecyclerViewAdapter(friendWrapAdapter);
            View headerView = LayoutInflater.from(getContext()).inflate(R.layout.header_view_group, rv_friendList, false);
            headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(GroupListActivity.class);
                }
            });
            wrapRecyclerViewAdapter.addHeaderView(headerView);
            rv_friendList.setAdapter(wrapRecyclerViewAdapter);
            CommonItemDecoration itemDecoration = new CommonItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.divider), LinearLayoutManager.VERTICAL);
            itemDecoration.setLeftMargin(64);
            itemDecoration.setRightMargin(24);
            rv_friendList.addItemDecoration(itemDecoration);
            LetterIndexView liv_letters = (LetterIndexView) view.findViewById(R.id.liv_letters);
            TextView tv_hint = (TextView) view.findViewById(R.id.tv_hint);
            liv_letters.bindIndexView(tv_hint, linearLayoutManager, getLetterMap(friendProfileList));
            FriendCache.getInstance().addObserver(this);
        }
        return view;
    }

    private Map<String, Integer> getLetterMap(List<FriendProfile> friendProfileList) {
        Map<String, Integer> letterMap = new HashMap<>();
        if (friendProfileList == null || friendProfileList.size() == 0) {
            return letterMap;
        }
        String letter = friendProfileList.get(0).getNameHeaderLetter();
        letterMap.put(letter, 0);
        for (int i = 1; i < friendProfileList.size(); i++) {
            if (friendProfileList.get(i).getNameHeaderLetter().equals(letter)) {
                continue;
            }
            letter = friendProfileList.get(i).getNameHeaderLetter();
            letterMap.put(letter, i);
        }
        return letterMap;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FriendCache.getInstance().deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof FriendCache) {
            friendProfileList.clear();
            friendProfileList.addAll(FriendCache.getInstance().getFriendProfileList());
//            viewIndexControl.setLetterMap(getLetterMap(friendProfileList));
//            friendWrapAdapter.notifyDataSetChanged();
            friendWrapAdapter.setData(friendProfileList);
        }
    }

}