package com.czy.chat.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.czy.chat.R;
import com.czy.chat.cache.FriendCache;
import com.czy.chat.view.base.BaseActivity;
import com.czy.chat.view.contacts.FriendProfileActivity;
import com.czy.presenter.presenter.ProfilePresenter;
import com.czy.presenter.view.ProfileView;
import com.tencent.imsdk.TIMUserProfile;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:03
 * 说明：搜索用户界面
 */
public class SearchUserActivity extends BaseActivity {

    private SearchView sv_searchUser;

    private RelativeLayout rl_searchUserHint;

    private TextView tv_searchUser_identifier;

    private TextView tv_searchUser_noResult;

    private ProfilePresenter profilePresenter;

    private final int REQUEST_CODE_SEARCH_RESULT = 1;

    private final int REQUEST_CODE_SHOW_PROFILE = 2;

    private static final String TAG = "SearchUserActivity";

    private ProfileView profileView = new ProfileView() {
        @Override
        public void showProfile(TIMUserProfile userProfile) {
            dismissLoadingDialog();
            rl_searchUserHint.setVisibility(View.GONE);
            tv_searchUser_noResult.setVisibility(View.GONE);
            SearchUserResultActivity.navigation(SearchUserActivity.this, REQUEST_CODE_SEARCH_RESULT, userProfile);
        }

        @Override
        public void getProfileFail(int code, String desc) {
            dismissLoadingDialog();
            rl_searchUserHint.setVisibility(View.GONE);
            tv_searchUser_noResult.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        initView();
        profilePresenter = new ProfilePresenter(profileView);
    }

    private void initView() {
        sv_searchUser = (SearchView) findViewById(R.id.sv_searchUser);
        rl_searchUserHint = (RelativeLayout) findViewById(R.id.rl_searchUserHint);
        tv_searchUser_identifier = (TextView) findViewById(R.id.tv_searchUser_identifier);
        tv_searchUser_noResult = (TextView) findViewById(R.id.tv_searchUser_noResult);
        sv_searchUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchUser(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    rl_searchUserHint.setVisibility(View.GONE);
                } else {
                    rl_searchUserHint.setVisibility(View.VISIBLE);
                    tv_searchUser_identifier.setText(newText);
                }
                tv_searchUser_noResult.setVisibility(View.GONE);
                return false;
            }
        });
        rl_searchUserHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUser(sv_searchUser.getQuery().toString());
            }
        });
    }

    private void searchUser(String identifier) {
        rl_searchUserHint.setVisibility(View.GONE);
        tv_searchUser_noResult.setVisibility(View.GONE);
        sv_searchUser.clearFocus();
        if (FriendCache.getInstance().isFriend(identifier)) {
            FriendProfileActivity.navigation(this, identifier, REQUEST_CODE_SHOW_PROFILE);
        } else {
            showLoadingDialog("正在搜索", false, false);
            profilePresenter.searchUser(identifier);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SEARCH_RESULT:
                    finish();
                    break;
                case REQUEST_CODE_SHOW_PROFILE:
                    finish();
                    break;
            }
        }
    }

}
