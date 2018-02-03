package com.czy.chat.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.czy.chat.R;
import com.czy.chat.cache.FriendCache;
import com.czy.presenter.model.GroupMemberInfo;
import com.czy.ui.recycler.common.CommonRecyclerViewAdapter;
import com.czy.ui.recycler.common.CommonRecyclerViewHolder;

import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2018/1/22 22:05
 * 说明：群组成员Adapter
 */
public class GroupMembersAdapter extends CommonRecyclerViewAdapter<GroupMemberInfo> {

    public GroupMembersAdapter(Context context, List<GroupMemberInfo> dataList) {
        super(context, dataList, R.layout.item_group_member);
    }

    @Override
    protected GroupMemberInfo clone(GroupMemberInfo data) {
        return new GroupMemberInfo(data.getIdentifier(), data.getJoinTime(), data.getRoleType());
    }

    @Override
    protected boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

    @NonNull
    @Override
    protected Bundle getChangePayload(int oldItemPosition, int newItemPosition) {
        return new Bundle();
    }

    @Override
    protected void partialBindData(CommonRecyclerViewHolder holder, @NonNull Bundle bundle) {

    }

    @Override
    protected void entirelyBindData(CommonRecyclerViewHolder holder, GroupMemberInfo data, int position) {
        holder.setText(R.id.tv_groupMember_name, FriendCache.getInstance().getFriendName(data.getIdentifier()));
    }

}
