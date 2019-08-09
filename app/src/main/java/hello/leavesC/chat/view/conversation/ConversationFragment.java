package hello.leavesC.chat.view.conversation;

import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hello.leavesC.chat.R;
import hello.leavesC.chat.adapter.ConversationAdapter;
import hello.leavesC.chat.model.BaseConversation;
import hello.leavesC.chat.model.ChatConversation;
import hello.leavesC.chat.model.SystemConversation;
import hello.leavesC.chat.utils.ConversationComparator;
import hello.leavesC.chat.utils.MessageFactory;
import hello.leavesC.chat.view.base.BaseFragment;
import hello.leavesC.chat.view.chat.ChatActivity;
import hello.leavesC.common.dialog.ListPickerDialog;
import hello.leavesC.common.recycler.common.CommonItemDecoration;
import hello.leavesC.presenter.event.ConversationActionEvent;
import hello.leavesC.presenter.model.ConversationModel;
import hello.leavesC.presenter.viewModel.ConversationViewModel;
import hello.leavesC.presenter.viewModel.base.BaseViewModel;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:14
 * 说明：会话列表界面
 */
public class ConversationFragment extends BaseFragment {

    private List<BaseConversation> conversationList;

    private ConversationAdapter conversationAdapter;

    private View view;

    private ConversationViewModel conversationViewModel;

    private LinearLayoutManager linearLayoutManager;

    private static final String TAG = "ConversationFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_conversation, container, false);
            conversationList = new ArrayList<>();
            conversationAdapter = new ConversationAdapter(getContext(), conversationList);
            conversationAdapter.setClickListener(position -> {
                switch (conversationList.get(position).getConversationType()) {
                    case C2C:
                    case Group:
                        ChatActivity.navigation(getContext(), conversationList.get(position).getPeer(), conversationList.get(position).getConversationType());
                        break;
                    case System:
                        SystemMessageListActivity.navigation(getContext(), conversationList.get(position).getPeer());
                        break;
                }
            });
            conversationAdapter.setLongClickListener(position -> {
                String[] options = {"删除会话"};
                ListPickerDialog dialog = new ListPickerDialog();
                final TIMConversationType conversationType = conversationList.get(position).getConversationType();
                final String peer = conversationList.get(position).getPeer();
                dialog.show(options, getFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        conversationViewModel.deleteConversation(peer, conversationType);
                    }
                });
            });
            RecyclerView rv_conversationList = view.findViewById(R.id.rv_conversationList);
            linearLayoutManager = new LinearLayoutManager(getContext());
            rv_conversationList.setLayoutManager(linearLayoutManager);
            rv_conversationList.setAdapter(conversationAdapter);
            rv_conversationList.addItemDecoration(new CommonItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.divider), LinearLayoutManager.VERTICAL));
            conversationViewModel.start(this);
            conversationViewModel.getConversation();
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
    protected BaseViewModel initViewModel() {
        conversationViewModel = ViewModelProviders.of(this).get(ConversationViewModel.class);
        conversationViewModel.getUpdateC2CMessageLiveData().observe(this, this::updateC2CMessage);
        conversationViewModel.getUpdateGroupMessageLiveData().observe(this, this::updateGroupMessage);
        conversationViewModel.getUpdateSystemMessageLiveData().observe(this, this::updateSystemMessage);
        conversationViewModel.getUpdateGroupInfoLiveData().observe(this, this::updateGroupInfo);
        conversationViewModel.getRemoveConversationLiveData().observe(this, this::removeConversation);
        conversationViewModel.getUpdateFriendProfileLiveData().observe(this, this::updateFriendProfile);
        conversationViewModel.getActionEventLiveData().observe(this, this::handleActionEvent);
        conversationViewModel.getInitConversationLiveData().observe(this, this::initConversation);
        return conversationViewModel;
    }

    private void handleActionEvent(ConversationActionEvent conversationActionEvent) {
        switch (conversationActionEvent.getAction()) {
            case ConversationActionEvent.UPDATE_FRIENDSHIP_MESSAGE: {
                updateFriendshipMessage();
                break;
            }
        }
    }

    public void initConversation(List<TIMConversation> conversationList) {
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

    public void updateC2CMessage(TIMMessage message) {
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

    public void updateGroupMessage(TIMMessage message) {
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

    public void updateSystemMessage(TIMMessage message) {
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


    public void updateFriendshipMessage() {

    }

    public void updateGroupInfo(TIMGroupCacheInfo info) {
        refresh();
    }

    public void updateFriendProfile(List<String> identifierList) {
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

    public void removeConversation(ConversationModel conversationModel) {
        BaseConversation baseConversation = null;
        for (BaseConversation conversation : conversationList) {
            if (conversation.getPeer().equals(conversationModel.getPeer())
                    && conversation.getConversationType() == conversationModel.getConversationType()) {
                baseConversation = conversation;
                break;
            }
        }
        if (baseConversation != null) {
            conversationList.remove(baseConversation);
            refresh();
        }
    }

    public void refresh() {
        Collections.sort(conversationList, new ConversationComparator());
        conversationAdapter.setData(conversationList);
        linearLayoutManager.scrollToPosition(0);
    }

}