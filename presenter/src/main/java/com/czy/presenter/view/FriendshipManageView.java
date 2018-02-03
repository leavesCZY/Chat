package com.czy.presenter.view;

import com.tencent.imsdk.ext.sns.TIMFriendStatus;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:49
 * 说明：
 */
public interface FriendshipManageView {

    /**
     * 添加好友结果回调
     *
     * @param status 返回状态
     */
    void onAddFriend(TIMFriendStatus status);

    /**
     * 删除好友结果回调
     *
     * @param status 返回状态
     */
    void onDelFriend(TIMFriendStatus status);


    /**
     * 修改好友分组回调
     *
     * @param status 返回状态
     * @param groupName 分组名
     */
    void onChangeGroup(TIMFriendStatus status, String groupName);

}
