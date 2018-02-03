package com.czy.presenter.manager;

import com.czy.presenter.listener.CallBackListener;
import com.czy.presenter.listener.ValueCallBackListener;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMGroupAddOpt;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2018/1/14 13:37
 * 说明：
 */
public class GroupProfileManager {

    private static final String TAG = "GroupProfileManager";

    public static void modifyGroupName(String groupId, String groupName, final CallBackListener callBackListener) {
        TIMGroupManagerExt.ModifyGroupInfoParam param = new TIMGroupManagerExt.ModifyGroupInfoParam(groupId);
        param.setGroupName(groupName);
        TIMGroupManagerExt.getInstance().modifyGroupInfo(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                if (callBackListener != null) {
                    callBackListener.onError(code, desc);
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

    public static void modifyGroupIntroduction(String groupId, String introduction, final CallBackListener callBackListener) {
        TIMGroupManagerExt.ModifyGroupInfoParam param = new TIMGroupManagerExt.ModifyGroupInfoParam(groupId);
        param.setIntroduction(introduction);
        TIMGroupManagerExt.getInstance().modifyGroupInfo(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                if (callBackListener != null) {
                    callBackListener.onError(code, desc);
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

    public static void modifyGroupNotification(String groupId, String notification, final CallBackListener callBackListener) {
        TIMGroupManagerExt.ModifyGroupInfoParam param = new TIMGroupManagerExt.ModifyGroupInfoParam(groupId);
        param.setNotification(notification);
        TIMGroupManagerExt.getInstance().modifyGroupInfo(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                if (callBackListener != null) {
                    callBackListener.onError(code, desc);
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

    public static void modifyGroupReceiveMessageOpt(String groupId, String identifier, TIMGroupReceiveMessageOpt opt, final CallBackListener callBackListener) {
        TIMGroupManagerExt.ModifyMemberInfoParam param = new TIMGroupManagerExt.ModifyMemberInfoParam(groupId, identifier);
        param.setReceiveMessageOpt(opt);
        TIMGroupManagerExt.getInstance().modifyMemberInfo(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                if (callBackListener != null) {
                    callBackListener.onError(code, desc);
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

    public static void modifyGroupNameCard(String groupId, String identifier, String nameCard, final CallBackListener callBackListener) {
        TIMGroupManagerExt.ModifyMemberInfoParam param = new TIMGroupManagerExt.ModifyMemberInfoParam(groupId, identifier);
        param.setNameCard(nameCard);
        TIMGroupManagerExt.getInstance().modifyMemberInfo(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                if (callBackListener != null) {
                    callBackListener.onError(code, desc);
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

    public static void getGroupMemberInfo(String groupId, String identifier, final ValueCallBackListener<TIMGroupMemberInfo> callBackListener) {
        List<String> identifierList = new ArrayList<>();
        identifierList.add(identifier);
        TIMGroupManagerExt.getInstance().getGroupMembersInfo(groupId, identifierList, new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int i, String s) {
                if (callBackListener != null) {
                    callBackListener.onError(i, s);
                }
            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
                if (callBackListener != null) {
                    if (timGroupMemberInfos.size() > 0) {
                        callBackListener.onSuccess(timGroupMemberInfos.get(0));
                    } else {
                        callBackListener.onError(-100, "非群成员");
                    }
                }
            }
        });
    }

    public static void modifyGroupAddOption(String groupId, TIMGroupAddOpt addOpt, final CallBackListener callBackListener) {
        TIMGroupManagerExt.ModifyGroupInfoParam param = new TIMGroupManagerExt.ModifyGroupInfoParam(groupId);
        param.setAddOption(addOpt);
        TIMGroupManagerExt.getInstance().modifyGroupInfo(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                if (callBackListener != null) {
                    callBackListener.onError(code, desc);
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

}
