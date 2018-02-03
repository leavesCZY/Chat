package com.czy.chat.view.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.czy.chat.R;
import com.czy.chat.cache.FriendCache;
import com.czy.chat.model.FriendProfile;
import com.czy.chat.view.base.BaseActivity;
import com.czy.chat.view.chat.ChatActivity;
import com.czy.presenter.listener.ValueCallBackListener;
import com.czy.presenter.manager.FriendManager;
import com.czy.ui.common.OptionView;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendGenderType;
import com.tencent.imsdk.ext.sns.TIMFriendResult;

import java.util.Observable;
import java.util.Observer;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:14
 * 说明：好友资料详情页
 */
public class FriendProfileActivity extends BaseActivity implements Observer {

    private static final String IDENTIFIER = "identifier";

    private String identifier;

    private TextView tv_friendProfile_name;

    private ImageView iv_friendProfile_gender;

    private OptionView ov_friendProfile_identifier;

    private OptionView ov_friendProfile_nickname;

    private OptionView ov_friendProfile_signature;

    private Button btn_friendProfile_startChat;

    private Button btn_friendProfile_deleteFriend;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_friendProfile_startChat: {
                    ChatActivity.navigation(FriendProfileActivity.this, identifier, TIMConversationType.C2C);
                    setResult(RESULT_OK);
                    finish();
                    break;
                }
                case R.id.btn_friendProfile_deleteFriend: {
                    showMessageDialog(null, "确认删除好友？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showLoadingDialog("正在删除", false, false);
                            FriendManager.deleteFriend(identifier, new ValueCallBackListener<TIMFriendResult>() {
                                @Override
                                public void onSuccess(TIMFriendResult result) {
                                    dismissLoadingDialog();
                                    showToast("已删除好友");
                                    finish();
                                }

                                @Override
                                public void onError(int code, String desc) {
                                    dismissLoadingDialog();
                                    showMessageDialog(null, "code" + code + "desc:" + desc, null);
                                }
                            });
                        }
                    });
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        identifier = getIntent().getStringExtra(IDENTIFIER);
        if (TextUtils.isEmpty(identifier) || !FriendCache.getInstance().isFriend(identifier)) {
            finish();
        }
        initView();
        getProfile();
        FriendCache.getInstance().addObserver(this);
    }

    private void initView() {
        initToolbar(R.id.toolbar_friendProfile, "个人名片", true);
        tv_friendProfile_name = (TextView) findViewById(R.id.tv_friendProfile_name);
        iv_friendProfile_gender = (ImageView) findViewById(R.id.iv_friendProfile_gender);
        ov_friendProfile_identifier = (OptionView) findViewById(R.id.ov_friendProfile_identifier);
        ov_friendProfile_nickname = (OptionView) findViewById(R.id.ov_friendProfile_nickname);
        ov_friendProfile_signature = (OptionView) findViewById(R.id.ov_friendProfile_signature);
        btn_friendProfile_startChat = (Button) findViewById(R.id.btn_friendProfile_startChat);
        btn_friendProfile_deleteFriend = (Button) findViewById(R.id.btn_friendProfile_deleteFriend);
    }

    private void getProfile() {
        FriendProfile friendProfile = FriendCache.getInstance().getProfile(identifier);
        if (friendProfile != null) {
            tv_friendProfile_name.setText(friendProfile.getName());
            if (friendProfile.getGender() == TIMFriendGenderType.Male) {
                iv_friendProfile_gender.setImageResource(R.drawable.gender_male);
            } else if (friendProfile.getGender() == TIMFriendGenderType.Female) {
                iv_friendProfile_gender.setImageResource(R.drawable.gender_female);
            }
            ov_friendProfile_identifier.setContent(identifier);
            ov_friendProfile_nickname.setContent(friendProfile.getNickname());
            ov_friendProfile_signature.setContent(friendProfile.getSelfSignature());
            btn_friendProfile_startChat.setOnClickListener(clickListener);
            btn_friendProfile_deleteFriend.setOnClickListener(clickListener);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof FriendCache) {
            getProfile();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.set_remark, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_setRemark) {
            AlterFriendRemarkActivity.navigation(this, identifier);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FriendCache.getInstance().deleteObserver(this);
    }

    public static void navigation(Context context, String identifier) {
        Intent intent = new Intent(context, FriendProfileActivity.class);
        intent.putExtra(IDENTIFIER, identifier);
        context.startActivity(intent);
    }

    public static void navigation(Activity activity, String identifier, int requestCode) {
        Intent intent = new Intent(activity, FriendProfileActivity.class);
        intent.putExtra(IDENTIFIER, identifier);
        activity.startActivityForResult(intent, requestCode);
    }

}