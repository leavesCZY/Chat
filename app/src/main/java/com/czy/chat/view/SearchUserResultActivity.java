package com.czy.chat.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.czy.chat.R;
import com.czy.chat.view.base.BaseActivity;
import com.czy.chat.view.contacts.FriendProfileActivity;
import com.czy.presenter.listener.ValueCallBackListener;
import com.czy.presenter.manager.FriendManager;
import com.czy.ui.common.OptionView;
import com.tencent.imsdk.TIMFriendGenderType;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.ext.sns.TIMFriendResult;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:05
 * 说明：搜索用户结果
 */
public class SearchUserResultActivity extends BaseActivity {

    private static final String IDENTIFIER = "identifier";

    private static final String GENDER = "gender";

    private static final String NICKNAME = "nickname";

    private static final String SIGNATURE = "signature";

    private String identifier;

    private ValueCallBackListener<TIMFriendResult> callBackListener = new ValueCallBackListener<TIMFriendResult>() {
        @Override
        public void onSuccess(TIMFriendResult result) {
            String message;
            switch (result.getStatus()) {
                case TIM_FRIEND_STATUS_SUCC:
                    //添加好友成功后，等待一段时间以让好友数据缓存刷新
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismissLoadingDialog();
                            showToast("添加好友成功");
                            FriendProfileActivity.navigation(SearchUserResultActivity.this, identifier);
                            setResult(RESULT_OK);
                            finish();
                        }
                    }, 1500);
                    return;
                case TIM_ADD_FRIEND_STATUS_PENDING:
                    message = "等待对方通过添加请求";
                    break;
                case TIM_ADD_FRIEND_STATUS_ALREADY_FRIEND:
                    message = "对方已经是好友";
                    break;
                case TIM_ADD_FRIEND_STATUS_FRIEND_SIDE_FORBID_ADD:
                    message = "对方禁止添加好友";
                    break;
                case TIM_ADD_FRIEND_STATUS_IN_OTHER_SIDE_BLACK_LIST:
                    message = "已被对方列入黑名单中";
                    break;
                case TIM_ADD_FRIEND_STATUS_IN_SELF_BLACK_LIST:
                    message = "对方在黑名单中";
                    break;
                case TIM_ADD_FRIEND_STATUS_SELF_FRIEND_FULL:
                    message = "好友数量已满";
                    break;
                case TIM_FRIEND_STATUS_UNKNOWN:
                default:
                    message = "未知错误";
                    break;
            }
            dismissLoadingDialog();
            showConfirmMessageDialog("添加好友失败", message, null);
        }

        @Override
        public void onError(int code, String desc) {
            dismissLoadingDialog();
            showConfirmMessageDialog("添加好友失败", "code：" + code + " desc：" + desc, null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user_result);
        identifier = getIntent().getStringExtra(IDENTIFIER);
        if (TextUtils.isEmpty(identifier)) {
            finish();
            return;
        }
        TIMFriendGenderType genderType = (TIMFriendGenderType) getIntent().getSerializableExtra(GENDER);
        String nickname = getIntent().getStringExtra(NICKNAME);
        String signature = getIntent().getStringExtra(SIGNATURE);
        initView(genderType, nickname, signature);
    }

    private void initView(TIMFriendGenderType genderType, String nickname, String signature) {
        initToolbar(R.id.toolbar_searchUserResult, "个人名片", true);
        TextView tv_searchUserResult_identifier = (TextView) findViewById(R.id.tv_searchUserResult_identifier);
        tv_searchUserResult_identifier.setText(identifier);
        if (genderType != null && genderType != TIMFriendGenderType.Unknow) {
            ImageView iv_searchUserResult_gender = (ImageView) findViewById(R.id.iv_searchUserResult_gender);
            iv_searchUserResult_gender.setImageResource(genderType == TIMFriendGenderType.Male ? R.drawable.gender_male : R.drawable.gender_female);
        }
        if (!TextUtils.isEmpty(nickname)) {
            OptionView ov_searchUserResult_nickname = (OptionView) findViewById(R.id.ov_searchUserResult_nickname);
            ov_searchUserResult_nickname.setContent(nickname);
        }
        if (!TextUtils.isEmpty(signature)) {
            OptionView ov_searchUserResult_signature = (OptionView) findViewById(R.id.ov_searchUserResult_signature);
            ov_searchUserResult_signature.setContent(signature);
        }
        Button btn_searchUserResult_addFriend = (Button) findViewById(R.id.btn_searchUserResult_addFriend);
        btn_searchUserResult_addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog("正在添加好友", false, false);
                FriendManager.addFriend(identifier, callBackListener);
            }
        });
    }

    public static void navigation(Activity activity, int requestCode, TIMUserProfile profile) {
        Intent intent = new Intent(activity, SearchUserResultActivity.class);
        intent.putExtra(IDENTIFIER, profile.getIdentifier());
        intent.putExtra(GENDER, profile.getGender());
        intent.putExtra(NICKNAME, profile.getNickName());
        intent.putExtra(SIGNATURE, profile.getSelfSignature());
        activity.startActivityForResult(intent, requestCode);
    }
}
