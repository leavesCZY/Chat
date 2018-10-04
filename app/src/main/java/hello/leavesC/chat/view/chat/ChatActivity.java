package hello.leavesC.chat.view.chat;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.ext.message.TIMMessageDraft;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hello.leavesC.chat.ChatApplication;
import hello.leavesC.chat.R;
import hello.leavesC.chat.adapter.ChatAdapter;
import hello.leavesC.chat.cache.FriendCache;
import hello.leavesC.chat.cache.GroupCache;
import hello.leavesC.chat.model.BaseMessage;
import hello.leavesC.chat.model.FriendProfile;
import hello.leavesC.chat.model.GroupProfile;
import hello.leavesC.chat.model.TextMessage;
import hello.leavesC.chat.utils.MessageFactory;
import hello.leavesC.chat.view.base.BaseActivity;
import hello.leavesC.chat.view.contacts.FriendProfileActivity;
import hello.leavesC.chat.view.group.GroupMemberProfileActivity;
import hello.leavesC.chat.view.group.GroupProfileActivity;
import hello.leavesC.common.input.EmojiFragment;
import hello.leavesC.common.input.EmojiKeyboard;
import hello.leavesC.common.input.utils.EmojiUtils;
import hello.leavesC.common.input.utils.SpanStringUtils;
import hello.leavesC.presenter.event.ChatActionEvent;
import hello.leavesC.presenter.log.Logger;
import hello.leavesC.presenter.viewModel.ChatViewModel;
import hello.leavesC.presenter.viewModel.base.BaseViewModel;

import static com.tencent.imsdk.TIMConversationType.C2C;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:08
 * 说明：聊天界面
 */
public class ChatActivity extends BaseActivity implements EmojiFragment.OnEmoticonClickListener {

    //好友ID或群组ID
    private String peer;

    //会话类型
    private TIMConversationType conversationType;

    private static final String PEER = "peer";

    private static final String CONVERSATION_TYPE = "conversationType";

    @BindView(R.id.et_input)
    EditText et_input;

    private ChatViewModel chatViewModel;

    private List<BaseMessage> messageList;

    private ChatAdapter chatAdapter;

    private EmojiKeyboard emojiKeyboard;

    private LinearLayoutManager linearLayoutManager;

    private static final int CODE_GROUP_PROFILE = 30;

    private static final String TAG = "ChatActivity";

    public static void navigation(Context context, String peer, TIMConversationType conversationType) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(PEER, peer);
        intent.putExtra(CONVERSATION_TYPE, conversationType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        peer = getIntent().getStringExtra(PEER);
        conversationType = (TIMConversationType) getIntent().getSerializableExtra(CONVERSATION_TYPE);
        initView();
        chatViewModel.start(peer, conversationType, this);
    }

    @Override
    protected BaseViewModel initViewModel() {
        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        chatViewModel.getChatActionLiveData().observe(this, this::chatActionEvent);
        chatViewModel.getMessageDraftLiveData().observe(this, this::showDraft);
        chatViewModel.getShowMessageLiveData().observe(this, this::showMessage);
        chatViewModel.getShowListMessageLiveData().observe(this, this::showMessage);
        return chatViewModel;
    }

    private void chatActionEvent(ChatActionEvent chatActionEvent) {
        switch (chatActionEvent.getAction()) {
            case ChatActionEvent.CLEAN_MESSAGE: {
                messageList.clear();
                chatAdapter.setData(messageList);
                break;
            }
            case ChatActionEvent.SEND_MESSAGE_FAIL: {
                chatAdapter.setData(messageList);
                break;
            }
        }
    }

    public void showDraft(TIMMessageDraft messageDraft) {
        TextMessage textMessage = new TextMessage(messageDraft);
        et_input.setText(textMessage.getMessageSummary());
    }

    public void showMessage(TIMMessage message) {
        if (message == null) {
            chatAdapter.setData(messageList);
        } else {
            BaseMessage baseMessage = MessageFactory.getMessage(message);
            if (baseMessage != null) {
                messageList.add(baseMessage);
                chatAdapter.setData(messageList);
            }
        }
        linearLayoutManager.scrollToPosition(messageList.size() - 1);
    }

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

    private void initView() {
        ButterKnife.bind(this);
        if (conversationType == TIMConversationType.C2C) {
            FriendProfile friendProfile = FriendCache.getInstance().getProfile(peer);
            setToolbarTitle(friendProfile == null ? peer : friendProfile.getName());
        } else if (conversationType == TIMConversationType.Group) {
            GroupProfile groupProfile = GroupCache.getInstance().getGroupProfile(peer);
            setToolbarTitle(groupProfile == null ? peer : groupProfile.getName() + "（" + groupProfile.getMemberNumber() + "）");
        }
        EmojiFragment emojiFragment = EmojiFragment.newInstance(EmojiUtils.EMOJI_TYPE_CLASSICS);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.ll_emoji, emojiFragment, "EmojiFragment");
        fragmentTransaction.commit();
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, conversationType, messageList);
        chatAdapter.setChatClickListener(new ChatAdapter.OnChatClickListener() {
            @Override
            public void onMyAvatarClick() {
                if (conversationType == TIMConversationType.C2C) {

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
        RecyclerView rv_chat = findViewById(R.id.rv_chat);
        rv_chat.setLayoutManager(linearLayoutManager);
        rv_chat.setAdapter(chatAdapter);
        LinearLayout ll_emojiPanel = findViewById(R.id.ll_emoji);
        ImageView iv_showEmoji = findViewById(R.id.iv_showEmoji);
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
        et_input.setOnKeyListener((v, keyCode, event) -> {
            String content = et_input.getText().toString().trim();
            if (!TextUtils.isEmpty(content)) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    BaseMessage message = new TextMessage(content);
                    chatViewModel.sendMessage(message.getMessage());
                    et_input.setText("");
                    linearLayoutManager.scrollToPosition(messageList.size() - 1);
                    return true;
                }
            }
            return false;
        });
        rv_chat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {
                    if (messageList.size() > 0) {
                        chatViewModel.getMessage(messageList.get(0).getMessage());
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
                switch (conversationType) {
                    case C2C: {
                        break;
                    }
                    case Group: {
                        GroupProfileActivity.navigation(this, peer, CODE_GROUP_PROFILE);
                        break;
                    }
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
        chatViewModel.saveDraft(et_input.getText().toString().trim());
    }

}