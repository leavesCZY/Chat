package com.czy.chat.view.conversation;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.czy.chat.R;
import com.czy.chat.adapter.ConversationAdapter;
import com.czy.chat.model.BaseConversation;
import com.czy.chat.model.BaseMessage;
import com.czy.chat.model.ChatConversation;
import com.czy.chat.model.SystemConversation;
import com.czy.chat.utils.ConversationComparator;
import com.czy.chat.view.MessageFactory;
import com.czy.chat.view.base.BaseFragment;
import com.czy.chat.view.chat.ChatActivity;
import com.czy.presenter.log.Logger;
import com.czy.presenter.presenter.ConversationPresenter;
import com.czy.presenter.view.ConversationView;
import com.czy.ui.dialog.ListPickerDialog;
import com.czy.ui.recycler.common.CommonItemDecoration;
import com.czy.ui.recycler.common.CommonRecyclerViewHolder;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:14
 * 说明：会话列表界面
 */
public class ConversationFragment extends BaseFragment implements ConversationView {

    private List<BaseConversation> conversationList;

    private ConversationAdapter conversationAdapter;

    private View view;

    private ConversationPresenter conversationPresenter;

    private LinearLayoutManager linearLayoutManager;

    private static final String TAG = "ConversationFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_conversation, container, false);
            conversationList = new ArrayList<>();
            conversationAdapter = new ConversationAdapter(getContext(), conversationList);
            conversationAdapter.setClickListener(new CommonRecyclerViewHolder.OnClickListener() {
                @Override
                public void onClick(int position) {
                    switch (conversationList.get(position).getConversationType()) {
                        case C2C:
                        case Group:
                            ChatActivity.navigation(getContext(), conversationList.get(position).getPeer(), conversationList.get(position).getConversationType());
                            break;
                        case System:
                            SystemMessageListActivity.navigation(getContext(), conversationList.get(position).getPeer());
                            break;
                    }
                }
            });
            conversationAdapter.setLongClickListener(new CommonRecyclerViewHolder.OnLongClickListener() {
                @Override
                public void onLongClick(int position) {
                    String[] options = {"删除会话"};
                    ListPickerDialog dialog = new ListPickerDialog();
                    final TIMConversationType conversationType = conversationList.get(position).getConversationType();
                    final String peer = conversationList.get(position).getPeer();
                    dialog.show(options, getFragmentManager(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            conversationPresenter.deleteConversation(peer, conversationType);
                        }
                    });
                }
            });
            RecyclerView rv_conversationList = (RecyclerView) view.findViewById(R.id.rv_conversationList);
            linearLayoutManager = new LinearLayoutManager(getContext());
            rv_conversationList.setLayoutManager(linearLayoutManager);
            rv_conversationList.setAdapter(conversationAdapter);
            rv_conversationList.addItemDecoration(new CommonItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.divider), LinearLayoutManager.VERTICAL));
            conversationPresenter = new ConversationPresenter(this);
            conversationPresenter.getConversation();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        for (BaseConversation conversation : conversationList) {
            conversation.freshenUnreadMessageNumber();
        }
        conversationAdapter.setData(conversationList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        conversationPresenter.clean();
        conversationPresenter = null;
    }

    @Override
    public synchronized void initConversation(List<TIMConversation> conversationList) {
        this.conversationList.clear();
        for (TIMConversation conversation : conversationList) {
            switch (conversation.getType()) {
                case C2C:
                case Group:
                    this.conversationList.add(new ChatConversation(conversation));
                    break;
                case System:
                    this.conversationList.add(new SystemConversation(conversation));
                    break;
            }
        }
       refresh();
    }

    @Override
    public synchronized void updateC2CMessage(TIMMessage message) {
        Logger.e(TAG, "updateC2CMessage");
        String peer = message.getConversation().getPeer();
        for (BaseConversation conversation : conversationList) {
            if (conversation.getPeer().equals(peer) && conversation.getConversationType() == TIMConversationType.C2C) {
                conversation.setLastMessage(MessageFactory.getMessage(message));
                refresh();
                return;
            }
        }
        ChatConversation chatConversation = new ChatConversation(message.getConversation());
        chatConversation.setLastMessage(MessageFactory.getMessage(message));
        conversationList.add(chatConversation);
        refresh();
    }

    @Override
    public synchronized void updateGroupMessage(TIMMessage message) {
        Logger.e(TAG, "updateGroupMessage");
        String peer = message.getConversation().getPeer();
        for (BaseConversation conversation : conversationList) {
            if (conversation.getPeer().equals(peer) && conversation.getConversationType() == TIMConversationType.Group) {
                conversation.setLastMessage(MessageFactory.getMessage(message));
                refresh();
                return;
            }
        }
        ChatConversation chatConversation = new ChatConversation(message.getConversation());
        chatConversation.setLastMessage(MessageFactory.getMessage(message));
        conversationList.add(chatConversation);
        refresh();
    }

    @Override
    public synchronized void updateSystemMessage(TIMMessage message) {
        Logger.e(TAG, "updateSystemMessage");
        String peer = message.getConversation().getPeer();
        for (BaseConversation conversation : conversationList) {
            if (conversation.getPeer().equals(peer) && conversation.getConversationType() == TIMConversationType.System) {
                conversation.setLastMessage(MessageFactory.getMessage(message));
                refresh();
                return;
            }
        }
        SystemConversation systemConversation = new SystemConversation(message.getConversation());
        systemConversation.setLastMessage(MessageFactory.getMessage(message));
        conversationList.add(systemConversation);
        refresh();
    }

    @Override
    public synchronized void updateFriendshipMessage() {
        Logger.e(TAG, "updateFriendshipMessage");
    }

    @Override
    public synchronized void updateGroupInfo(TIMGroupCacheInfo info) {
        Log.e(TAG, "updateGroupInfo");
        refresh();
    }

    @Override
    public synchronized void updateFriendProfile(List<String> identifierList) {
        Logger.e(TAG, "updateFriendProfile");
        for (String identifier : identifierList) {
            ChatConversation chatConversation = null;
            for (BaseConversation conversation : conversationList) {
                if (conversation.getPeer().equals(identifier)) {
                    chatConversation = new ChatConversation(TIMManager.getInstance().getConversation(TIMConversationType.C2C, identifier));
                    conversationList.remove(conversation);
                    break;
                }
            }
            if (chatConversation != null) {
                conversationList.add(chatConversation);
            }
        }
        refresh();
    }

    @Override
    public synchronized void removeConversation(TIMConversationType type, String identifier) {
        BaseConversation baseConversation = null;
        for (BaseConversation conversation : conversationList) {
            if (conversation.getPeer().equals(identifier) && conversation.getConversationType() == type) {
                baseConversation = conversation;
                break;
            }
        }
        if (baseConversation != null) {
            conversationList.remove(baseConversation);
            refresh();
        }
    }

    @Override
    public void refresh() {
        Collections.sort(conversationList, new ConversationComparator());
        conversationAdapter.setData(conversationList);
        linearLayoutManager.scrollToPosition(0);
    }

}

