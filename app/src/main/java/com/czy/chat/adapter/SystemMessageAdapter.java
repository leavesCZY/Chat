package com.czy.chat.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.czy.chat.R;
import com.czy.chat.model.BaseMessage;
import com.czy.chat.model.SystemGroupMessage;
import com.czy.chat.model.SystemProfileTipsMessage;
import com.czy.chat.model.SystemSnsMessage;
import com.czy.chat.utils.TimeUtil;
import com.czy.ui.recycler.common.CommonRecyclerViewAdapter;
import com.czy.ui.recycler.common.CommonRecyclerViewHolder;

import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2018/1/27 21:12
 * 说明：系统消息Adapter
 */
public class SystemMessageAdapter extends CommonRecyclerViewAdapter<BaseMessage> {

    public SystemMessageAdapter(Context context, List<BaseMessage> dataList) {
        super(context, dataList, R.layout.item_message_system_overall);
    }

    @Override
    protected BaseMessage clone(BaseMessage data) {
        if (data instanceof SystemGroupMessage) {
            return new SystemGroupMessage(data.getMessage());
        }
        if (data instanceof SystemSnsMessage) {
            return new SystemSnsMessage(data.getMessage());
        }
        if (data instanceof SystemProfileTipsMessage) {
            return new SystemProfileTipsMessage(data.getMessage());
        }
        return null;
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
    protected void entirelyBindData(CommonRecyclerViewHolder holder, BaseMessage data, int position) {
        holder.setText(R.id.tv_systemMessage_lastMsgTime, TimeUtil.formatTime(data.getMessageTime() * 1000, TimeUtil.FORMAT_YYYY_MM_DD_HH_MM_SS))
                .setText(R.id.tv_systemMessage_msgContent, data.getMessageSummary());
    }

}
