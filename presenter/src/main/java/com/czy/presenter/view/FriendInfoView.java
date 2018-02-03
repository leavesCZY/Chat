package com.czy.presenter.view;

import com.tencent.imsdk.TIMUserProfile;

import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:49
 * 说明：
 */
public interface FriendInfoView {

    /**
     * 显示用户信息
     *
     * @param users 资料列表
     */
    void showUserInfo(List<TIMUserProfile> users);

}
