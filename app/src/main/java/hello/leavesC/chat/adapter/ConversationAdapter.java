package hello.leavesC.chat.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import hello.leavesC.chat.R;
import hello.leavesC.chat.model.BaseConversation;
import hello.leavesC.chat.model.ChatConversation;
import hello.leavesC.chat.model.SystemConversation;
import hello.leavesC.chat.utils.TimeUtil;
import hello.leavesC.presenter.log.Logger;
import hello.leavesC.common.input.utils.SpanStringUtils;
import hello.leavesC.common.recycler.common.CommonRecyclerViewAdapter;
import hello.leavesC.common.recycler.common.CommonRecyclerViewHolder;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:14
 * 说明：会话列表Adapter
 */
public class ConversationAdapter extends CommonRecyclerViewAdapter<BaseConversation> {

    private static final String TAG = "ConversationAdapter";

    private Context context;

    public ConversationAdapter(Context context, List<BaseConversation> dataList) {
        super(context, dataList, R.layout.item_conversation);
        this.context = context;
    }

    @Override
    protected BaseConversation clone(BaseConversation data) {
        if (data instanceof ChatConversation) {
            return new ChatConversation(data.getTimConversation());
        }
        if (data instanceof SystemConversation) {
            return new SystemConversation(data.getTimConversation());
        }
        return null;
    }

    @Override
    protected boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return dataList.get(oldItemPosition).equals(newDataList.get(newItemPosition));
    }

    @Override
    protected boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        BaseConversation oldConversation = dataList.get(oldItemPosition);
        BaseConversation newConversation = newDataList.get(newItemPosition);
        return oldConversation.getName().equals(newConversation.getName())
                && oldConversation.getLastMessageSummary().equals(newConversation.getLastMessageSummary())
                && oldConversation.getLastMessageTime() == newConversation.getLastMessageTime()
                && oldConversation.getUnreadMessageNumber() == newConversation.getUnreadMessageNumber();
    }

    @NonNull
    @Override
    protected Bundle getChangePayload(int oldItemPosition, int newItemPosition) {
        Bundle bundle = new Bundle();
        BaseConversation oldConversation = dataList.get(oldItemPosition);
        BaseConversation newConversation = newDataList.get(newItemPosition);
        if (!oldConversation.getName().equals(newConversation.getName())) {
            bundle.putString("Name", newConversation.getName());
        }
        if (!oldConversation.getLastMessageSummary().equals(newConversation.getLastMessageSummary())) {
            bundle.putString("Summary", newConversation.getLastMessageSummary().toString());
        }
        if (oldConversation.getLastMessageTime() != newConversation.getLastMessageTime()) {
            bundle.putLong("Time", newConversation.getLastMessageTime());
        }
        if (oldConversation.getUnreadMessageNumber() != newConversation.getUnreadMessageNumber()) {
            bundle.putLong("Number", newConversation.getUnreadMessageNumber());
        }
        return bundle;
    }

    @Override
    protected void entirelyBindData(CommonRecyclerViewHolder holder, BaseConversation data, int position) {
        Logger.e(TAG, "entirelyBindData");
        if (data.getUnreadMessageNumber() > 0) {
            holder.setText(R.id.tv_conversation_unreadNumber, String.valueOf(data.getUnreadMessageNumber()))
                    .setViewVisibility(R.id.tv_conversation_unreadNumber, View.VISIBLE);
        } else {
            holder.setViewVisibility(R.id.tv_conversation_unreadNumber, View.GONE);
        }
        holder.setImageResource(R.id.iv_conversation_avatar, data.getDefaultAvatar())
                .setText(R.id.tv_conversation_name, data.getName())
                .setText(R.id.tv_conversation_lastMsgSendTime, TimeUtil.getConversationTimeString(data.getLastMessageTime()))
                .setText(R.id.tv_conversation_lastMsg, SpanStringUtils.getEmojiContent(context, (TextView) holder.getView(R.id.tv_conversation_lastMsg), data.getLastMessageSummary()));
    }

    @Override
    protected synchronized void partialBindData(CommonRecyclerViewHolder holder, @NonNull Bundle bundle) {
        Logger.e(TAG, "partialBindData");
        for (String key : bundle.keySet()) {
            Logger.e(TAG, "partialBindData: " + key);
            switch (key) {
                case "Name":
                    holder.setText(R.id.tv_conversation_name, bundle.getString(key));
                    break;
                case "Summary":
                    holder.setText(R.id.tv_conversation_lastMsg, SpanStringUtils.getEmojiContent(context, (TextView) holder.getView(R.id.tv_conversation_lastMsg), new SpannableString(bundle.getString(key))));
                    break;
                case "Time":
                    holder.setText(R.id.tv_conversation_lastMsgSendTime, TimeUtil.getConversationTimeString(bundle.getLong(key)));
                    break;
                case "Number":
                    long unreadMessageNumber = bundle.getLong(key);
                    if (unreadMessageNumber > 0) {
                        holder.setText(R.id.tv_conversation_unreadNumber, String.valueOf(unreadMessageNumber))
                                .setViewVisibility(R.id.tv_conversation_unreadNumber, View.VISIBLE);
                    } else {
                        holder.setViewVisibility(R.id.tv_conversation_unreadNumber, View.GONE);
                    }
                    break;
            }
        }
    }

}
