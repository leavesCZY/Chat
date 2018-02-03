package com.czy.presenter.manager;

import com.czy.presenter.listener.CallBackListener;
import com.czy.presenter.listener.ValueCallBackListener;
import com.czy.presenter.model.GroupMemberInfo;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.tencent.imsdk.ext.group.TIMGroupMemberResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:45
 * 说明：
 */
public class GroupManager {

    /**
     * 创建群
     *
     * @param name     群名称
     * @param type     群类型
     * @param members  群成员
     * @param callBack 回调
     */
    public static void createGroup(String name, String type, List<String> members, final ValueCallBackListener<String> callBack) {
        List<TIMGroupMemberInfo> groupMemberInfoList = new ArrayList<>();
        for (String member : members) {
            TIMGroupMemberInfo newMember = new TIMGroupMemberInfo(member);
            groupMemberInfoList.add(newMember);
        }
        TIMGroupManager.CreateGroupParam groupGroupParam = new TIMGroupManager.CreateGroupParam(type, name);
        groupGroupParam.setMembers(groupMemberInfoList);
        TIMGroupManager.getInstance().createGroup(groupGroupParam, new TIMValueCallBack<String>() {
            @Override
            public void onError(int i, String s) {
                if (callBack != null) {
                    callBack.onError(i, s);
                }
            }

            @Override
            public void onSuccess(String s) {
                if (callBack != null) {
                    callBack.onSuccess(s);
                }
            }
        });
    }

    /**
     * 邀请入群
     *
     * @param groupId  群组ID
     * @param members  邀请的好友
     * @param callBack 回调
     */
    public static void inviteGroup(String groupId, List<String> members, final ValueCallBackListener<List<TIMGroupMemberResult>> callBack) {
        TIMGroupManagerExt.getInstance().inviteGroupMember(groupId, members, new TIMValueCallBack<List<TIMGroupMemberResult>>() {
            @Override
            public void onError(int i, String s) {
                if (callBack != null) {
                    callBack.onError(i, s);
                }
            }

            @Override
            public void onSuccess(List<TIMGroupMemberResult> timGroupMemberResults) {
                if (callBack != null) {
                    callBack.onSuccess(timGroupMemberResults);
                }
            }
        });
    }

    /**
     * 退出群
     *
     * @param groupId          群组ID
     * @param callBackListener 回调
     */
    public static void quitGroup(String groupId, final CallBackListener callBackListener) {
        TIMGroupManager.getInstance().quitGroup(groupId, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                if (callBackListener != null) {
                    callBackListener.onError(i, s);
                }
            }

            @Override
            public void onSuccess() {
                if (callBackListener != null) {
                    callBackListener.onSuccess();
                }
            }
        });
    }

    /**
     * 解散群
     *
     * @param groupId          群组ID
     * @param callBackListener 回调
     */
    public static void dismissGroup(String groupId, final CallBackListener callBackListener) {
        TIMGroupManager.getInstance().deleteGroup(groupId, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                if (callBackListener != null) {
                    callBackListener.onError(i, s);
                }
            }

            @Override
            public void onSuccess() {
                if (callBackListener != null) {
                    callBackListener.onSuccess();
                }
            }
        });
    }

    public static void getGroupMembers(String groupId, final ValueCallBackListener<List<GroupMemberInfo>> callBackListener) {
        TIMGroupManagerExt.getInstance().getGroupMembers(groupId, new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int i, String s) {
                if (callBackListener != null) {
                    callBackListener.onError(i, s);
                }
            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfoList) {
                if (callBackListener != null) {
                    List<GroupMemberInfo> groupMemberInfoList = new ArrayList<>();
                    for (TIMGroupMemberInfo info : timGroupMemberInfoList) {
                        groupMemberInfoList.add(new GroupMemberInfo(info));
                    }
                    callBackListener.onSuccess(groupMemberInfoList);
                }
            }
        });
    }

    /**
     * 申请加入群
     *
     * @param groupId  群组ID
     * @param reason   申请理由
     * @param callBack 回调
     */
    public static void applyJoinGroup(String groupId, String reason, final CallBackListener callBack) {
        TIMGroupManager.getInstance().applyJoinGroup(groupId, reason, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                if (callBack != null) {
                    callBack.onError(i, s);
                }
            }

            @Override
            public void onSuccess() {
                if (callBack != null) {
                    callBack.onSuccess();
                }
            }
        });
    }

    /**
     * 将群管理消息标记为已读
     *
     * @param timeStamp 最后一条消息的时间戳
     * @param callBack  回调
     */
    public static void readGroupManageMessage(long timeStamp, TIMCallBack callBack) {
        TIMGroupManagerExt.getInstance().reportGroupPendency(timeStamp, callBack);
    }

}
