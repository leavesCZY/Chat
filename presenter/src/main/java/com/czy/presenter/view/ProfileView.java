package com.czy.presenter.view;

import com.tencent.imsdk.TIMUserProfile;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:50
 * 说明：
 */
public interface ProfileView {

    void showProfile(TIMUserProfile userProfile);

    void getProfileFail(int code, String desc);

}
