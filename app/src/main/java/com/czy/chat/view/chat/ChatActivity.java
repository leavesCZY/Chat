package com.czy.chat.view.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.czy.chat.ChatApplication;
import com.czy.chat.R;
import com.czy.chat.adapter.ChatAdapter;
import com.czy.chat.cache.FriendCache;
import com.czy.chat.cache.GroupCache;
import com.czy.chat.model.BaseMessage;
import com.czy.chat.model.FriendProfile;
import com.czy.chat.model.GroupProfile;
import com.czy.chat.model.TextMessage;
import com.czy.chat.view.MessageFactory;
import com.czy.chat.view.base.BaseActivity;
import com.czy.chat.view.contacts.FriendProfileActivity;
import com.czy.chat.view.group.GroupMemberProfileActivity;
import com.czy.chat.view.group.GroupProfileActivity;
import com.czy.presenter.log.Logger;
import com.czy.presenter.presenter.ChatPresenter;
import com.czy.presenter.view.ChatView;
import com.czy.ui.input.EmojiFragment;
import com.czy.ui.input.EmojiKeyboard;
import com.czy.ui.input.utils.EmojiUtils;
import com.czy.ui.input.utils.SpanStringUtils;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.ext.message.TIMMessageDraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static com.tencent.imsdk.TIMConversationType.C2C;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:08
 * 说明：聊天界面
 */
public class ChatActivity extends BaseActivity implements ChatView, EmojiFragment.OnEmoticonClickListener {

    //好友ID或群组ID
    private String peer;

    //会话类型
    private TIMConversationType conversationType;

    private static final String PEER = "peer";

    private static final String CONVERSATION_TYPE = "conversationType";

    private EditText et_input;

    private ChatPresenter chatPresenter;

    private List<BaseMessage> messageList;

    private ChatAdapter chatAdapter;

    private EmojiKeyboard emojiKeyboard;

    private LinearLayoutManager linearLayoutManager;

    private static final int CODE_GROUP_PROFILE = 30;

    private static final String TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        peer = getIntent().getStringExtra(PEER);
        conversationType = (TIMConversationType) getIntent().getSerializableExtra(CONVERSATION_TYPE);
        initView();
        chatPresenter = new ChatPresenter(this, peer, conversationType);
        chatPresenter.start();
    }

    private void initView() {
        if (conversationType == TIMConversationType.C2C) {
            FriendProfile friendProfile = FriendCache.getInstance().getProfile(peer);
            initToolbar(R.id.toolbar_chat, friendProfile == null ? peer : friendProfile.getName());
        } else if (conversationType == TIMConversationType.Group) {
            GroupProfile groupProfile = GroupCache.getInstance().getGroupProfile(peer);
            initToolbar(R.id.toolbar_chat, groupProfile == null ? peer : groupProfile.getName());
        }
        EmojiFragment emojiFragment = EmojiFragment.newInstance(EmojiUtils.EMOJI_TYPE_CLASSICS);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.ll_emoji, emojiFragment, "EmojiFragment");
        fragmentTransaction.commit();
        et_input = (EditText) findViewById(R.id.et_input);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, conversationType, messageList);
        chatAdapter.setChatClickListener(new ChatAdapter.OnChatClickListener() {
            @Override
            public void onMyAvatarClick() {
                if (conversationType == C2C) {

                } else if (conversationType == TIMConversationType.Group) {
                    GroupMemberProfileActivity.navigation(ChatActivity.this, peer, ChatApplication.identifier);
                }
            }

            @Override
            public void onFriendAvatarClick(int position) {
                if (conversationType == C2C) {
                    FriendProfileActivity.navigation(getContext(), peer);
                } else if (conversationType == TIMConversationType.Group) {
                    GroupMemberProfileActivity.navigation(ChatActivity.this, peer, messageList.get(position).getSender());
                }
            }

            @Override
            public void onMessageLongClick(BaseMessage message) {
                showToast("长按");
            }
        });
        RecyclerView rv_chat = (RecyclerView) findViewById(R.id.rv_chat);
        rv_chat.setLayoutManager(linearLayoutManager);
        rv_chat.setAdapter(chatAdapter);
        LinearLayout ll_emojiPanel = (LinearLayout) findViewById(R.id.ll_emoji);
        ImageView iv_showEmoji = (ImageView) findViewById(R.id.iv_showEmoji);
        emojiKeyboard = new EmojiKeyboard(this, et_input, ll_emojiPanel, iv_showEmoji, rv_chat);
        emojiKeyboard.setEmoticonPanelVisibilityChangeListener(new EmojiKeyboard.OnEmojiPanelVisibilityChangeListener() {
            @Override
            public void onShowEmojiPanel() {
                Logger.e(TAG, "onShowEmojiPanel");
            }

            @Override
            public void onHideEmojiPanel() {
                Logger.e(TAG, "onHideEmojiPanel");
            }
        });
        et_input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String content = et_input.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                        BaseMessage message = new TextMessage(content);
                        chatPresenter.sendMessage(message.getMessage());
                        et_input.setText("");
                        linearLayoutManager.scrollToPosition(messageList.size() - 1);
                        return true;
                    }
                }
                return false;
            }
        });
        rv_chat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {
                    if (messageList.size() > 0) {
                        chatPresenter.getMessage(messageList.get(0).getMessage());
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_chat:
                if (conversationType == C2C) {

                } else {
                    GroupProfileActivity.navigation(this, peer, CODE_GROUP_PROFILE);
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!emojiKeyboard.interceptBackPress()) {
            super.onBackPressed();
        }
    }

    @Override
    public void showMessage(TIMMessage message) {
        if (message == null) {
            Logger.e(TAG, "showMessage message == null");
            chatAdapter.setData(messageList);
        } else {
            Logger.e(TAG, "showMessage message != null");
            BaseMessage baseMessage = MessageFactory.getMessage(message);
            if (baseMessage != null) {
                messageList.add(baseMessage);
                chatAdapter.setData(messageList);
            }
        }
        linearLayoutManager.scrollToPosition(messageList.size() - 1);
    }

    @Override
    public void showMessage(List<TIMMessage> messageList) {
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
        chatAdapter.setData(this.messageList);
    }

    @Override
    public void showDraft(TIMMessageDraft messageDraft) {
        TextMessage textMessage = new TextMessage(messageDraft);
        et_input.setText(textMessage.getMessageSummary());
    }

    @Override
    public void clearAllMessage() {
        messageList.clear();
        chatAdapter.setData(messageList);
    }

    @Override
    public void onSendMessageFail(int code, String desc, TIMMessage message) {
        Logger.e(TAG, "onSendMessageFail: " + code + " " + desc);
        chatAdapter.setData(messageList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_GROUP_PROFILE && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void onEmoticonClick(int emoticonType, String emoticonName) {
        int curPosition = et_input.getSelectionStart();
        StringBuilder sb = new StringBuilder(et_input.getText().toString());
        sb.insert(curPosition, emoticonName);
        et_input.setText(SpanStringUtils.getEmojiContent(this, et_input, emoticonType, sb.toString()));
        et_input.setSelection(curPosition + emoticonName.length());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String draft = et_input.getText().toString().trim();
        if (draft.length() != 0) {
            TIMMessage timMessage = new TIMMessage();
            TIMTextElem textElem = new TIMTextElem();
            textElem.setText(draft);
            timMessage.addElement(textElem);
            chatPresenter.saveDraft(timMessage);
        } else {
            chatPresenter.saveDraft(null);
        }
        chatPresenter.stop();
        chatPresenter = null;
    }

    public static void navigation(Context context, String peer, TIMConversationType conversationType) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(PEER, peer);
        intent.putExtra(CONVERSATION_TYPE, conversationType);
        context.startActivity(intent);
    }

}
