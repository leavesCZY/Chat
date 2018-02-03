package com.czy.presenter.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.czy.presenter.listener.CallBackListener;
import com.czy.presenter.listener.ValueCallBackListener;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMAddFriendRequest;
import com.tencent.imsdk.ext.sns.TIMDelFriendType;
import com.tencent.imsdk.ext.sns.TIMFriendAddResponse;
import com.tencent.imsdk.ext.sns.TIMFriendResponseType;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:45
 * 说明：
 */
public class FriendManager {

    /**
     * 同意加好友请求
     */
    public static void acceptFriendRequest(@NonNull String identifier, @Nullable final ValueCallBackListener<TIMFriendResult> valueCallBackListener) {
        TIMFriendAddResponse response = new TIMFriendAddResponse(identifier);
        response.setType(TIMFriendResponseType.AgreeAndAdd);
        TIMFriendshipManagerExt.getInstance().addFriendResponse(response, new TIMValueCallBack<TIMFriendResult>() {
            @Override
            public void onSuccess(TIMFriendResult timFriendResult) {
                if (valueCallBackListener != null) {
                    valueCallBackListener.onSuccess(timFriendResult);
                }
            }

            @Override
            public void onError(int i, String s) {
                if (valueCallBackListener != null) {
                    valueCallBackListener.onError(i, s);
                }
            }
        });
    }

    /**
     * 拒绝好友请求
     */
    public static void refuseFriendRequest(@NonNull String identifier, @Nullable final ValueCallBackListener<TIMFriendResult> valueCallBackListener) {
        TIMFriendAddResponse response = new TIMFriendAddResponse(identifier);
        response.setType(TIMFriendResponseType.Reject);
        TIMFriendshipManagerExt.getInstance().addFriendResponse(response, new TIMValueCallBack<TIMFriendResult>() {
            @Override
            public void onSuccess(TIMFriendResult timFriendResult) {
                if (valueCallBackListener != null) {
                    valueCallBackListener.onSuccess(timFriendResult);
                }
            }

            @Override
            public void onError(int i, String s) {
                if (valueCallBackListener != null) {
                    valueCallBackListener.onError(i, s);
                }
            }
        });
    }

    /**
     * 添加好友
     */
    public static void addFriend(@NonNull String identifier, @Nullable final ValueCallBackListener<TIMFriendResult> valueCallBackListener) {
        addFriend(identifier, "", "", valueCallBackListener);
    }

    /**
     * 添加好友
     */
    public static void addFriend(@NonNull String identifier, String remark, String message, @Nullable final ValueCallBackListener<TIMFriendResult> valueCallBackListener) {
        List<TIMAddFriendRequest> friendRequestList = new ArrayList<>();
        TIMAddFriendRequest friendRequest = new TIMAddFriendRequest(identifier);
        if (!TextUtils.isEmpty(remark)) {
            friendRequest.setRemark(remark);
        }
        if (!TextUtils.isEmpty(message)) {
            friendRequest.setAddWording(message);
        }
        friendRequestList.add(friendRequest);
        TIMFriendshipManagerExt.getInstance().addFriend(friendRequestList, new TIMValueCallBack<List<TIMFriendResult>>() {
            @Override
            public void onError(int i, String s) {
                if (valueCallBackListener != null) {
                    valueCallBackListener.onError(i, s);
                }
            }

            @Override
            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                if (valueCallBackListener != null) {
                    valueCallBackListener.onSuccess(timFriendResults.get(0));
                }
            }
        });
    }

    /**
     * 删除好友
     */
    public static void deleteFriend(@NonNull String identifier, @Nullable final ValueCallBackListener<TIMFriendResult> valueCallBackListener) {
        TIMFriendshipManagerExt.DeleteFriendParam param = new TIMFriendshipManagerExt.DeleteFriendParam();
        //双向删除好友
        param.setType(TIMDelFriendType.TIM_FRIEND_DEL_BOTH);
        param.setUsers(Collections.singletonList(identifier));
        TIMFriendshipManagerExt.getInstance().delFriend(param, new TIMValueCallBack<List<TIMFriendResult>>() {
            @Override
            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                if (valueCallBackListener != null) {
                    valueCallBackListener.onSuccess(timFriendResults.get(0));
                }
            }

            @Override
            public void onError(int i, String s) {
                if (valueCallBackListener != null) {
                    valueCallBackListener.onError(i, s);
                }
            }
        });
    }

    /**
     * 加入黑名单
     */
    public static void addBlack(@NonNull String identifier, @Nullable final ValueCallBackListener<TIMFriendResult> valueCallBackListener) {
        TIMFriendshipManagerExt.getInstance().addBlackList(Collections.singletonList(identifier), new TIMValueCallBack<List<TIMFriendResult>>() {
            @Override
            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                if (valueCallBackListener != null) {
                    valueCallBackListener.onSuccess(timFriendResults.get(0));
                }
            }

            @Override
            public void onError(int i, String s) {
                if (valueCallBackListener != null) {
                    valueCallBackListener.onError(i, s);
                }
            }
        });
    }

    /**
     * 移除黑名单
     */
    public static void removeBlack(@NonNull String identifier, @Nullable final ValueCallBackListener<TIMFriendResult> valueCallBackListener) {
        TIMFriendshipManagerExt.getInstance().delBlackList(Collections.singletonList(identifier), new TIMValueCallBack<List<TIMFriendResult>>() {
            @Override
            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                if (valueCallBackListener != null) {
                    valueCallBackListener.onSuccess(timFriendResults.get(0));
                }
            }

            @Override
            public void onError(int i, String s) {
                if (valueCallBackListener != null) {
                    valueCallBackListener.onError(i, s);
                }
            }
        });
    }

    /**
     * 设置好友备注
     */
    public static void setFriendRemark(@NonNull String identifier, @NonNull String remark, @Nullable final CallBackListener callBackListener) {
        TIMFriendshipManagerExt.ModifySnsProfileParam param = new TIMFriendshipManagerExt.ModifySnsProfileParam(identifier);
        param.setRemark(remark);
        TIMFriendshipManagerExt.getInstance().modifySnsProfile(param, new TIMCallBack() {
            @Override
            public void onSuccess() {
                if (callBackListener != null) {
                    callBackListener.onSuccess();
                }
            }

            @Override
            public void onError(int i, String s) {
                if (callBackListener != null) {
                    callBackListener.onError(i, s);
                }
            }
        });
    }

}
