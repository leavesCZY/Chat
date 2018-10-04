package hello.leavesC.chat.view.friendship;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.imsdk.TIMFriendGenderType;
import com.tencent.imsdk.TIMUserProfile;

import hello.leavesC.chat.R;
import hello.leavesC.chat.view.base.BaseActivity;
import hello.leavesC.chat.view.contacts.FriendProfileActivity;
import hello.leavesC.common.common.OptionView;
import hello.leavesC.presenter.event.SearchUserActionEvent;
import hello.leavesC.presenter.viewModel.SearchUserViewModel;
import hello.leavesC.presenter.viewModel.base.BaseViewModel;

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

    private SearchUserViewModel searchUserViewModel;

    public static void navigation(Activity activity, int requestCode, TIMUserProfile profile) {
        Intent intent = new Intent(activity, SearchUserResultActivity.class);
        intent.putExtra(IDENTIFIER, profile.getIdentifier());
        intent.putExtra(GENDER, profile.getGender());
        intent.putExtra(NICKNAME, profile.getNickName());
        intent.putExtra(SIGNATURE, profile.getSelfSignature());
        activity.startActivityForResult(intent, requestCode);
    }

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

    @Override
    protected BaseViewModel initViewModel() {
        searchUserViewModel = ViewModelProviders.of(this).get(SearchUserViewModel.class);
        searchUserViewModel.getSearchUserLiveData().observe(this, this::handleAction);
        return searchUserViewModel;
    }

    private void handleAction(SearchUserActionEvent searchUserActionEvent) {
        switch (searchUserActionEvent.getAction()) {
            case SearchUserActionEvent.ADD_FRIEND_SUCCESS: {
                FriendProfileActivity.navigation(SearchUserResultActivity.this, identifier);
                setResult(RESULT_OK);
                finish();
                break;
            }
        }
    }

    private void initView(TIMFriendGenderType genderType, String nickname, String signature) {
        initToolbar("个人名片", true);
        TextView tv_searchUserResult_identifier = findViewById(R.id.tv_searchUserResult_identifier);
        tv_searchUserResult_identifier.setText(identifier);
        if (genderType != null && genderType != TIMFriendGenderType.Unknow) {
            ImageView iv_searchUserResult_gender = findViewById(R.id.iv_searchUserResult_gender);
            iv_searchUserResult_gender.setImageResource(genderType == TIMFriendGenderType.Male ? R.drawable.gender_male : R.drawable.gender_female);
        }
        if (!TextUtils.isEmpty(nickname)) {
            OptionView ov_searchUserResult_nickname = findViewById(R.id.ov_searchUserResult_nickname);
            ov_searchUserResult_nickname.setContent(nickname);
        }
        if (!TextUtils.isEmpty(signature)) {
            OptionView ov_searchUserResult_signature = findViewById(R.id.ov_searchUserResult_signature);
            ov_searchUserResult_signature.setContent(signature);
        }
        Button btn_searchUserResult_addFriend = findViewById(R.id.btn_searchUserResult_addFriend);
        btn_searchUserResult_addFriend.setOnClickListener(v -> searchUserViewModel.addFriend(identifier));
    }

}