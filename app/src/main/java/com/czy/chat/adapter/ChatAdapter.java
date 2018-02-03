package com.czy.chat.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.czy.chat.R;
import com.czy.chat.model.BaseMessage;
import com.czy.chat.model.GroupTipsMessage;
import com.czy.chat.model.TextMessage;
import com.czy.chat.utils.TimeUtil;
import com.czy.ui.input.utils.SpanStringUtils;
import com.czy.ui.recycler.common.CommonRecyclerViewAdapter;
import com.czy.ui.recycler.common.CommonRecyclerViewHolder;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessageStatus;

import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2017/12/10 16:40
 * 说明：聊天消息Adapter
 */
public class ChatAdapter extends CommonRecyclerViewAdapter<BaseMessage> {

    private static final String TAG = "ChatAdapter";

    //每条消息之间的间隔时间如果超出这个限度（秒），则显示消息时间
    private static final int TIME = 120;

    public interface OnChatClickListener {

        void onMyAvatarClick();

        void onFriendAvatarClick(int position);

        void onMessageLongClick(BaseMessage message);

    }

    private OnChatClickListener chatClickListener;

    private Context context;

    private TIMConversationType conversationType;

    public ChatAdapter(Context context, TIMConversationType conversationType, List<BaseMessage> dataList) {
        super(context, dataList, new MultiTypeSupport<BaseMessage>() {
            @Override
            public int getLayoutId(BaseMessage baseMessage, int position) {
                if (baseMessage.isSelf()) {
                    return R.layout.item_message_me;
                }
                return baseMessage.isSystemMessage() ? R.layout.item_message_system_hint : R.layout.item_message_friend;
            }
        });
        this.context = context;
        this.conversationType = conversationType;
    }

    @Override
    protected BaseMessage clone(BaseMessage data) {
        if (data instanceof TextMessage) {
            return new TextMessage(data.getMessage());
        }
        if (data instanceof GroupTipsMessage) {
            return new GroupTipsMessage(data.getMessage());
        }
        return null;
    }

    @Override
    protected boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        BaseMessage message = dataList.get(oldItemPosition);
        BaseMessage newMessage = newDataList.get(newItemPosition);
        return message.getMsgId().equals(newMessage.getMsgId());
    }

    @Override
    protected boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        BaseMessage oldMessage = dataList.get(oldItemPosition);
        BaseMessage newMessage = newDataList.get(newItemPosition);
        boolean oldNeedShowTime = false;
        if (oldItemPosition == 0) {
            oldNeedShowTime = true;
        } else if (oldMessage.getMessageTime() - dataList.get(oldItemPosition - 1).getMessageTime() > TIME) {
            oldNeedShowTime = true;
        }
        boolean newNeedShowTime = false;
        if (newItemPosition == 0) {
            newNeedShowTime = true;
        } else if (newMessage.getMessageTime() - newDataList.get(newItemPosition - 1).getMessageTime() > TIME) {
            newNeedShowTime = true;
        }
        return oldMessage.getSenderName().equals(newMessage.getSenderName()) && (oldMessage.getMessageStatus() == newMessage.getMessageStatus()) && (oldNeedShowTime == newNeedShowTime);
    }

    @NonNull
    @Override
    protected Bundle getChangePayload(int oldItemPosition, int newItemPosition) {
        Bundle bundle = new Bundle();
        BaseMessage oldMessage = dataList.get(oldItemPosition);
        BaseMessage newMessage = newDataList.get(newItemPosition);
        boolean oldNeedShowTime = false;
        if (oldItemPosition == 0) {
            oldNeedShowTime = true;
        } else if (oldMessage.getMessageTime() - dataList.get(oldItemPosition - 1).getMessageTime() > TIME) {
            oldNeedShowTime = true;
        }
        boolean newNeedShowTime = false;
        if (newItemPosition == 0) {
            newNeedShowTime = true;
        } else if (newMessage.getMessageTime() - newDataList.get(newItemPosition - 1).getMessageTime() > TIME) {
            newNeedShowTime = true;
        }
        if (oldNeedShowTime ^ newNeedShowTime) {
            bundle.putBoolean("NeedShowTime", newNeedShowTime);
            bundle.putLong("ShowTime", newMessage.getMessageTime());
        }
        if (oldMessage.isSystemMessage()) {
            bundle.putString("Peer", "System");
        } else {
            bundle.putString("Peer", oldMessage.isSelf() ? "Self" : "Friend");
            if (!oldMessage.getSenderName().equals(newMessage.getSenderName())) {
                bundle.putString("SenderName", newMessage.getSenderName());
            }
            if (oldMessage.getMessageStatus() != newMessage.getMessageStatus()) {
                bundle.putSerializable("MessageStatus", newMessage.getMessageStatus());
            }
        }
        return bundle;
    }

    @Override
    protected void partialBindData(CommonRecyclerViewHolder holder, @NonNull Bundle bundle) {
        for (String key : bundle.keySet()) {
            switch (key) {
                case "NeedShowTime": {
                    int tv_sendTimeId;
                    if ("Self".equals(bundle.getString("Peer"))) {
                        tv_sendTimeId = R.id.tv_myMsgSendTime;
                    } else if ("Friend".equals(bundle.getString("Peer"))) {
                        tv_sendTimeId = R.id.tv_friendMsgSendTime;
                    } else {
                        tv_sendTimeId = R.id.tv_systemHintTime;
                    }
                    if (bundle.getBoolean("NeedShowTime")) {
                        holder.setViewVisibility(tv_sendTimeId, View.VISIBLE)
                                .setText(tv_sendTimeId, TimeUtil.getChatTimeString(bundle.getLong("ShowTime")));
                    } else {
                        holder.setViewVisibility(tv_sendTimeId, View.GONE);
                    }
                    break;
                }
                case "SenderName": {
                    if (conversationType == TIMConversationType.Group) {
                        holder.setViewVisibility(R.id.tv_friendName, View.VISIBLE)
                                .setText(R.id.tv_friendName, bundle.getString("SenderName"));
                    }
                    break;
                }
                case "MessageStatus": {
                    TIMMessageStatus messageStatus = (TIMMessageStatus) bundle.getSerializable("MessageStatus");
                    if (messageStatus != null) {
                        switch (messageStatus) {
                            case SendSucc: {
                                holder.setViewVisibility(R.id.pb_myMessageSending, View.GONE)
                                        .setViewVisibility(R.id.iv_myMessageSendError, View.GONE);
                                break;
                            }
                            case Sending: {
                                holder.setViewVisibility(R.id.pb_myMessageSending, View.VISIBLE)
                                        .setViewVisibility(R.id.iv_myMessageSendError, View.GONE);
                                break;
                            }
                            case SendFail:
                            case Invalid: {
                                holder.setViewVisibility(R.id.pb_myMessageSending, View.GONE)
                                        .setViewVisibility(R.id.iv_myMessageSendError, View.VISIBLE);
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    protected void entirelyBindData(CommonRecyclerViewHolder holder, final BaseMessage data, final int position) {
        if (data.isSelf()) {
            holder.setText(R.id.tv_my_message, SpanStringUtils.getEmojiContent(context, (TextView) holder.getView(R.id.tv_my_message), data.getMessageSummary()));
            if (position == 0) {
                holder.setViewVisibility(R.id.tv_myMsgSendTime, View.VISIBLE)
                        .setText(R.id.tv_myMsgSendTime, TimeUtil.getChatTimeString(data.getMessageTime()));
            } else {
                if (data.getMessageTime() - dataList.get(position - 1).getMessageTime() > TIME) {
                    holder.setViewVisibility(R.id.tv_myMsgSendTime, View.VISIBLE)
                            .setText(R.id.tv_myMsgSendTime, TimeUtil.getChatTimeString(data.getMessageTime()));
                } else {
                    holder.setViewVisibility(R.id.tv_myMsgSendTime, View.GONE);
                }
            }
            switch (data.getMessageStatus()) {
                case SendSucc:
                    holder.setViewVisibility(R.id.pb_myMessageSending, View.GONE)
                            .setViewVisibility(R.id.iv_myMessageSendError, View.GONE);
                    break;
                case Sending:
                    holder.setViewVisibility(R.id.pb_myMessageSending, View.VISIBLE)
                            .setViewVisibility(R.id.iv_myMessageSendError, View.GONE);
                    break;
                case SendFail:
                case Invalid:
                    holder.setViewVisibility(R.id.pb_myMessageSending, View.GONE)
                            .setViewVisibility(R.id.iv_myMessageSendError, View.VISIBLE);
                    break;
            }
            holder.setOnClickListener(R.id.iv_myAvatar, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chatClickListener != null) {
                        chatClickListener.onMyAvatarClick();
                    }
                }
            });
            holder.setOnLongClickListener(R.id.tv_my_message, new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (chatClickListener != null) {
                        chatClickListener.onMessageLongClick(data);
                    }
                    return false;
                }
            });
        } else if (data.isSystemMessage()) {
            if (position == 0) {
                holder.setViewVisibility(R.id.tv_systemHintTime, View.VISIBLE)
                        .setText(R.id.tv_systemHintTime, TimeUtil.getChatTimeString(data.getMessageTime()));
            } else {
                if (data.getMessageTime() - dataList.get(position - 1).getMessageTime() > TIME) {
                    holder.setViewVisibility(R.id.tv_systemHintTime, View.VISIBLE)
                            .setText(R.id.tv_systemHintTime, TimeUtil.getChatTimeString(data.getMessageTime()));
                } else {
                    holder.setViewVisibility(R.id.tv_systemHintTime, View.GONE);
                }
            }
            holder.setText(R.id.tv_systemHint, SpanStringUtils.getEmojiContent(context, (TextView) holder.getView(R.id.tv_systemHint), data.getMessageSummary()));
        } else {
            if (position == 0) {
                holder.setViewVisibility(R.id.tv_friendMsgSendTime, View.VISIBLE)
                        .setText(R.id.tv_friendMsgSendTime, TimeUtil.getChatTimeString(data.getMessageTime()));
            } else {
                if (data.getMessageTime() - dataList.get(position - 1).getMessageTime() > TIME) {
                    holder.setViewVisibility(R.id.tv_friendMsgSendTime, View.VISIBLE)
                            .setText(R.id.tv_friendMsgSendTime, TimeUtil.getChatTimeString(data.getMessageTime()));
                } else {
                    holder.setViewVisibility(R.id.tv_friendMsgSendTime, View.GONE);
                }
            }
            holder.setText(R.id.tv_friend_message, SpanStringUtils.getEmojiContent(context, (TextView) holder.getView(R.id.tv_friend_message), data.getMessageSummary()));
            if (conversationType == TIMConversationType.Group) {
                holder.setViewVisibility(R.id.tv_friendName, View.VISIBLE)
                        .setText(R.id.tv_friendName, data.getSenderName());
            }
            holder.setOnClickListener(R.id.iv_friendAvatar, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chatClickListener != null) {
                        chatClickListener.onFriendAvatarClick(position);
                    }
                }
            });
            holder.setOnLongClickListener(R.id.tv_friend_message, new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (chatClickListener != null) {
                        chatClickListener.onMessageLongClick(data);
                    }
                    return false;
                }
            });
        }
    }

    public void setChatClickListener(OnChatClickListener chatClickListener) {
        this.chatClickListener = chatClickListener;
    }

}
