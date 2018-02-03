package com.czy.chat.view.conversation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.czy.chat.R;
import com.czy.chat.adapter.SystemMessageAdapter;
import com.czy.chat.model.BaseMessage;
import com.czy.chat.utils.SystemMessageComparator;
import com.czy.chat.view.MessageFactory;
import com.czy.chat.view.base.BaseActivity;
import com.czy.presenter.log.Logger;
import com.czy.presenter.presenter.ChatPresenter;
import com.czy.presenter.view.ChatView;
import com.czy.ui.recycler.common.CommonItemDecoration;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.ext.message.TIMMessageDraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2018/1/27 21:06
 * 说明：全局系统消息界面
 */
public class SystemMessageListActivity extends BaseActivity implements ChatView {

    private SystemMessageAdapter systemMessageAdapter;

    private List<BaseMessage> messageList;

    private static final String PEER = "peer";

    private ChatPresenter chatPresenter;

    private LinearLayoutManager linearLayoutManager;

    private static final String TAG = "SystemMessageListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message_list);
        initToolbar(R.id.toolbar_systemMessageList, "系统消息");
        RecyclerView rv_systemMessageList = (RecyclerView) findViewById(R.id.rv_systemMessageList);
        linearLayoutManager = new LinearLayoutManager(this);
        rv_systemMessageList.setLayoutManager(linearLayoutManager);
        rv_systemMessageList.addItemDecoration(new CommonItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.divider), LinearLayoutManager.VERTICAL));
        messageList = new ArrayList<>();
        systemMessageAdapter = new SystemMessageAdapter(this, messageList);
        rv_systemMessageList.setAdapter(systemMessageAdapter);
        chatPresenter = new ChatPresenter(this, getIntent().getStringExtra(PEER), TIMConversationType.System);
        chatPresenter.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatPresenter.stop();
    }

    public static void navigation(Context context, String peer) {
        Intent intent = new Intent(context, SystemMessageListActivity.class);
        intent.putExtra(PEER, peer);
        context.startActivity(intent);
    }

    @Override
    public void showMessage(TIMMessage message) {
        if (message == null) {
            Logger.e(TAG, "showMessage message == null");
        } else {
            Logger.e(TAG, "showMessage message != null");
            BaseMessage baseMessage = MessageFactory.getMessage(message);
            if (baseMessage != null) {
                messageList.add(baseMessage);
            }
        }
        Collections.sort(messageList, new SystemMessageComparator());
        systemMessageAdapter.setData(messageList);
        linearLayoutManager.scrollToPosition(0);
    }

    @Override
    public void showMessage(List<TIMMessage> messageList) {
        Logger.e(TAG, "showMessage messageList size: " + messageList.size());
        if (messageList.size() == 0) {
            return;
        }
        List<BaseMessage> messages = new ArrayList<>();
        for (int i = 0; i < messageList.size(); i++) {
            BaseMessage baseMessage = MessageFactory.getMessage(messageList.get(i));
            if (baseMessage != null) {
                messages.add(baseMessage);
            }
        }
        this.messageList.addAll(0, messages);
        Collections.sort(this.messageList, new SystemMessageComparator());
        systemMessageAdapter.setData(this.messageList);
    }

    @Override
    public void showDraft(TIMMessageDraft messageDraft) {

    }

    @Override
    public void clearAllMessage() {
        messageList.clear();
        systemMessageAdapter.setData(messageList);
    }

    @Override
    public void onSendMessageFail(int code, String desc, TIMMessage message) {

    }

}
