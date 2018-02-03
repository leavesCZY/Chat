package com.czy.chat.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import com.czy.chat.R;
import com.czy.chat.model.FriendProfile;
import com.czy.ui.recycler.common.CommonRecyclerViewAdapter;
import com.czy.ui.recycler.common.CommonRecyclerViewHolder;

import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:12
 * 说明：好友列表Adapter
 */
public class FriendAdapter extends CommonRecyclerViewAdapter<FriendProfile> {

    public FriendAdapter(Context context, List<FriendProfile> dataList) {
        super(context, dataList, R.layout.item_friend);
    }

    FriendAdapter(Context context, List<FriendProfile> dataList, @LayoutRes int layoutId) {
        super(context, dataList, layoutId);
    }

    @Override
    protected FriendProfile clone(FriendProfile data) {
        FriendProfile friendProfile = new FriendProfile(data.getUserProfile());
        friendProfile.setSelected(data.isSelected());
        return friendProfile;
    }

    @Override
    protected boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return dataList.get(oldItemPosition).getIdentifier().equals(newDataList.get(newItemPosition).getIdentifier());
    }

    @Override
    protected boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        FriendProfile oldFriendProfile = dataList.get(oldItemPosition);
        FriendProfile newFriendProfile = newDataList.get(newItemPosition);
        return oldFriendProfile.getAvatarUrl().equals(newFriendProfile.getAvatarUrl())
                && oldFriendProfile.getName().equals(newFriendProfile.getName());
    }

    @NonNull
    @Override
    protected Bundle getChangePayload(int oldItemPosition, int newItemPosition) {
        FriendProfile oldFriendProfile = dataList.get(oldItemPosition);
        FriendProfile newFriendProfile = newDataList.get(newItemPosition);
        Bundle bundle = new Bundle();
        if (!oldFriendProfile.getAvatarUrl().equals(newFriendProfile.getAvatarUrl())) {
            bundle.putString("AvatarUrl", newFriendProfile.getAvatarUrl());
        }
        if (!oldFriendProfile.getName().equals(newFriendProfile.getName())) {
            bundle.putString("Name", newFriendProfile.getName());
        }
        return bundle;
    }

    @Override
    protected void partialBindData(CommonRecyclerViewHolder holder, @NonNull Bundle bundle) {
        for (String key : bundle.keySet()) {
            switch (key) {
                case "AvatarUrl":
                    break;
                case "Name":
                    holder.setText(R.id.tv_friendName, bundle.getString(key));
                    break;
            }
        }
    }

    @Override
    protected void entirelyBindData(CommonRecyclerViewHolder holder, FriendProfile data, int position) {
        holder.setText(R.id.tv_friendName, data.getName());
    }

}