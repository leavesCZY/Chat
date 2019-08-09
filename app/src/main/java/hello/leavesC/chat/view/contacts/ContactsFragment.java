package hello.leavesC.chat.view.contacts;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hello.leavesC.chat.R;
import hello.leavesC.chat.adapter.FriendAdapter;
import hello.leavesC.chat.cache.FriendCache;
import hello.leavesC.chat.model.FriendProfile;
import hello.leavesC.chat.view.base.BaseFragment;
import hello.leavesC.chat.view.group.GroupListActivity;
import hello.leavesC.common.common.LetterIndexView;
import hello.leavesC.common.recycler.common.CommonItemDecoration;
import hello.leavesC.common.recycler.wrap.WrapRecyclerViewAdapter;
import hello.leavesC.presenter.viewModel.base.BaseViewModel;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:12
 * 说明：好友列表界面
 */
public class ContactsFragment extends BaseFragment {

    private List<FriendProfile> friendProfileList;

    private FriendAdapter friendWrapAdapter;

    private View view;

    private static final String TAG = "ContactsFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_contacts, container, false);
            friendProfileList = new ArrayList<>(FriendCache.getInstance().getFriendProfileList());
            friendWrapAdapter = new FriendAdapter(getContext(), friendProfileList);
            friendWrapAdapter.setClickListener(position -> FriendProfileActivity.navigation(getContext(), friendProfileList.get(position - 1).getIdentifier()));
            RecyclerView rv_friendList = view.findViewById(R.id.rv_friendList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            rv_friendList.setLayoutManager(linearLayoutManager);
            WrapRecyclerViewAdapter wrapRecyclerViewAdapter = new WrapRecyclerViewAdapter(friendWrapAdapter);
            View headerView = LayoutInflater.from(getContext()).inflate(R.layout.header_view_group, rv_friendList, false);
            headerView.setOnClickListener(v -> startActivity(GroupListActivity.class));
            wrapRecyclerViewAdapter.addHeaderView(headerView);
            rv_friendList.setAdapter(wrapRecyclerViewAdapter);
            CommonItemDecoration itemDecoration = new CommonItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.divider), LinearLayoutManager.VERTICAL);
            itemDecoration.setLeftMargin(64);
            itemDecoration.setRightMargin(24);
            rv_friendList.addItemDecoration(itemDecoration);
            LetterIndexView liv_letters = view.findViewById(R.id.liv_letters);
            TextView tv_hint = view.findViewById(R.id.tv_hint);
            liv_letters.bindIndexView(tv_hint, linearLayoutManager, getLetterMap(friendProfileList));
            FriendCache.getInstance().observe(this, this::handle);
        }
        return view;
    }

    private void handle(Map<String, FriendProfile> stringFriendProfileMap) {
        friendProfileList.clear();
        friendProfileList.addAll(FriendCache.getInstance().getFriendProfileList());
        friendWrapAdapter.setData(friendProfileList);
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
    protected BaseViewModel initViewModel() {
        return null;
    }

}