package com.czy.chat.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.czy.chat.R;
import com.czy.chat.model.GroupProfile;
import com.czy.ui.recycler.common.CommonRecyclerViewAdapter;
import com.czy.ui.recycler.common.CommonRecyclerViewHolder;

import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2018/1/7 20:19
 * 说明：群组列表Adapter
 */
public class GroupListAdapter extends CommonRecyclerViewAdapter<GroupProfile> {

    public GroupListAdapter(Context context, List<GroupProfile> dataList) {
        super(context, dataList, R.layout.item_group);
    }

    @Override
    protected GroupProfile clone(GroupProfile data) {
        return new GroupProfile(data.getGroupDetailInfo(), data.getGroupBasicSelfInfo());
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
    protected void entirelyBindData(CommonRecyclerViewHolder holder, GroupProfile data, int position) {
        holder.setText(R.id.tv_groupList_name, data.getName());
    }

}
