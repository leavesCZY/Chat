package com.czy.presenter.presenter;

import com.czy.presenter.view.ProfileView;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:47
 * 说明：
 */
public class ProfilePresenter {

    private ProfileView profileView;

    public ProfilePresenter(ProfileView profileView) {
        this.profileView = profileView;
    }

    public void getSelfProfile() {
        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {
                if (profileView != null) {
                    profileView.getProfileFail(i, s);
                }
            }

            @Override
            public void onSuccess(TIMUserProfile profile) {
                if (profileView != null) {
                    profileView.showProfile(profile);
                }
            }
        });
    }

    public void searchUser(String identifier) {
        List<String> identifierList = new ArrayList<>();
        identifierList.add(identifier);
        TIMFriendshipManager.getInstance().getUsersProfile(identifierList, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {
                if (profileView != null) {
                    profileView.getProfileFail(i, s);
                }
            }

            @Override
            public void onSuccess(List<TIMUserProfile> profile) {
                if (profileView != null) {
                    profileView.showProfile(profile.get(0));
                }
            }
        });
    }

}
