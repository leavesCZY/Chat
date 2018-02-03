package com.czy.presenter.test;

import android.support.annotation.Nullable;
import android.util.Log;

import com.czy.presenter.event.FriendEvent;
import com.czy.presenter.view.FriendInfoView;
import com.czy.presenter.view.FriendshipManageView;
import com.czy.presenter.view.FriendshipMessageView;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMAddFriendRequest;
import com.tencent.imsdk.ext.sns.TIMDelFriendType;
import com.tencent.imsdk.ext.sns.TIMFriendFutureMeta;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendStatus;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;
import com.tencent.imsdk.ext.sns.TIMGetFriendFutureListSucc;
import com.tencent.imsdk.ext.sns.TIMPageDirectionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:47
 * 说明：好友关系链管理
 */
public class FriendshipManagerPresenter {

    private static final String TAG = "FriendManagerPresenter";

    private FriendshipMessageView friendshipMessageView;

    private FriendshipManageView friendshipManageView;

    private FriendInfoView friendInfoView;

    private static final int PAGE_SIZE = 20;

    private long pendSeq, decideSeq, recommendSeq;

    public FriendshipManagerPresenter(FriendshipMessageView friendshipMessageView) {
        this(friendshipMessageView, null, null);
    }

    public FriendshipManagerPresenter(FriendshipManageView friendshipManageView) {
        this(null, friendshipManageView, null);
    }

    public FriendshipManagerPresenter(FriendInfoView friendInfoView) {
        this(null, null, friendInfoView);
    }

    public FriendshipManagerPresenter(FriendshipMessageView friendshipMessageView, FriendshipManageView friendshipManageView, FriendInfoView friendInfoView) {
        this.friendshipMessageView = friendshipMessageView;
        this.friendshipManageView = friendshipManageView;
        this.friendInfoView = friendInfoView;
    }

    /**
     * 获取好友关系链最后一条消息和未读消息数
     * 包括：好友已决系统消息，好友未决系统消息，推荐好友消息
     */
    public void getFriendshipLastMessage() {
        if (friendshipMessageView == null) {
            return;
        }
        TIMFriendFutureMeta friendFutureMeta = new TIMFriendFutureMeta();
        //获取数量
        friendFutureMeta.setReqNum(1);
        //设置翻页类型为向下翻页
        friendFutureMeta.setDirectionType(TIMPageDirectionType.TIM_PAGE_DIRECTION_DOWN_TYPE);
        long flag = 0;
        flag |= TIMFriendshipManager.TIM_PROFILE_FLAG_NICK;
        flag |= TIMFriendshipManager.TIM_PROFILE_FLAG_REMARK;
        flag |= TIMFriendshipManager.TIM_PROFILE_FLAG_ALLOW_TYPE;
        //已决好友、收到的未决请求、发出去的未决请求、推荐好友
        long futureFlags = TIMFriendshipManagerExt.TIM_FUTURE_FRIEND_DECIDE_TYPE
                | TIMFriendshipManagerExt.TIM_FUTURE_FRIEND_PENDENCY_IN_TYPE
                | TIMFriendshipManagerExt.TIM_FUTURE_FRIEND_PENDENCY_OUT_TYPE
                | TIMFriendshipManagerExt.TIM_FUTURE_FRIEND_RECOMMEND_TYPE;
        //几个参数的含义：
        //1.获取的资料标识
        //2.指定要获取的类型
        //3.自定义字段，可填null
        //4.推荐好友元信息
        TIMFriendshipManagerExt.getInstance().getFutureFriends(flag, futureFlags, null, friendFutureMeta, new TIMValueCallBack<TIMGetFriendFutureListSucc>() {
            @Override
            public void onError(int arg0, String arg1) {
                Log.e(TAG, "onError code" + arg0 + " msg " + arg1);
            }

            @Override
            public void onSuccess(TIMGetFriendFutureListSucc getFriendFutureListSucc) {
                long unread = getFriendFutureListSucc.getMeta().getPendencyUnReadCnt() + //未决未读数量
                        getFriendFutureListSucc.getMeta().getDecideUnReadCnt() +         //已决未读数量
                        getFriendFutureListSucc.getMeta().getRecommendUnReadCnt();       //推荐未读数量
                if (getFriendFutureListSucc.getItems().size() > 0) {
                    friendshipMessageView.onGetFriendshipLastMessage(getFriendFutureListSucc.getItems().get(0), unread);
                }
            }
        });
    }

    /**
     * 获取好友关系链消息
     * 包括：好友已决系统消息，好友未决系统消息，推荐好友消息
     */
    public void getFriendshipMessage() {
        if (friendshipMessageView == null) {
            return;
        }
        TIMFriendFutureMeta meta = new TIMFriendFutureMeta();
        meta.setReqNum(PAGE_SIZE);          //获取数量
        meta.setPendencySeq(pendSeq);       //未决序列号
        meta.setDecideSeq(decideSeq);       //已决序列号
        meta.setRecommendSeq(recommendSeq); //推荐序列号
        meta.setDirectionType(TIMPageDirectionType.TIM_PAGE_DIRECTION_DOWN_TYPE); //设置翻页类型为向下翻页
        long flag = 0;
        flag |= TIMFriendshipManager.TIM_PROFILE_FLAG_NICK;
        flag |= TIMFriendshipManager.TIM_PROFILE_FLAG_REMARK;
        flag |= TIMFriendshipManager.TIM_PROFILE_FLAG_ALLOW_TYPE;
        long futureFlags = TIMFriendshipManagerExt.TIM_FUTURE_FRIEND_DECIDE_TYPE
                | TIMFriendshipManagerExt.TIM_FUTURE_FRIEND_PENDENCY_IN_TYPE
                | TIMFriendshipManagerExt.TIM_FUTURE_FRIEND_PENDENCY_OUT_TYPE
                | TIMFriendshipManagerExt.TIM_FUTURE_FRIEND_RECOMMEND_TYPE;
        TIMFriendshipManagerExt.getInstance().getFutureFriends(flag, futureFlags, null, meta, new TIMValueCallBack<TIMGetFriendFutureListSucc>() {
            @Override
            public void onError(int arg0, String arg1) {
                Log.e(TAG, "onError code" + arg0 + " msg " + arg1);
            }

            @Override
            public void onSuccess(TIMGetFriendFutureListSucc arg0) {
                pendSeq = arg0.getMeta().getPendencySeq();
                decideSeq = arg0.getMeta().getDecideSeq();
                recommendSeq = arg0.getMeta().getRecommendSeq();
                friendshipMessageView.onGetFriendshipMessage(arg0.getItems());
            }
        });
    }

    /**
     * 获取自己的资料
     */
    public void getMyProfile() {
        if (friendInfoView == null) {
            return;
        }
        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(TIMUserProfile profile) {
                friendInfoView.showUserInfo(Collections.singletonList(profile));
            }
        });
    }

    /**
     * 按照ID搜索好友
     *
     * @param identify id
     */
    public void searchFriendById(String identify) {
        if (friendInfoView == null) {
            return;
        }
        TIMFriendshipManager.getInstance().getUsersProfile(Collections.singletonList(identify), new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMUserProfile> profile) {
                friendInfoView.showUserInfo(profile);
            }
        });
    }

    /**
     * 添加好友
     *
     * @param id      添加对象Identify
     * @param remark  备注名
     * @param group   分组
     * @param message 附加消息
     */
    public void addFriend(final String id, String remark, String group, String message) {
        if (friendshipManageView == null) {
            return;
        }
        List<TIMAddFriendRequest> reqList = new ArrayList<>();
        TIMAddFriendRequest req = new TIMAddFriendRequest(id);
        //设置添加好友的添加来源
        req.setAddrSource("缘");
        //设置添加好友的申请理由
        req.setAddWording(message);
        //设置要添加好友的备注
        req.setRemark(remark);
        //设置好友分组名称
        req.setFriendGroup(group);
        reqList.add(req);
        TIMFriendshipManagerExt.getInstance().addFriend(reqList, new TIMValueCallBack<List<TIMFriendResult>>() {
            @Override
            public void onError(int arg0, String arg1) {
                Log.e(TAG, "onError code" + arg0 + " msg " + arg1);
                friendshipManageView.onAddFriend(TIMFriendStatus.TIM_FRIEND_STATUS_UNKNOWN);
            }

            @Override
            public void onSuccess(List<TIMFriendResult> arg0) {
                for (TIMFriendResult item : arg0) {
                    if (item.getIdentifer().equals(id)) {
                        friendshipManageView.onAddFriend(item.getStatus());
                    }
                }

            }
        });
    }

    /**
     * 删除好友
     *
     * @param id 删除对象Identify
     */
    public void delFriend(final String id) {
        if (friendshipManageView == null) {
            return;
        }
        TIMFriendshipManagerExt.DeleteFriendParam param = new TIMFriendshipManagerExt.DeleteFriendParam();
        //双向删除好友
        param.setType(TIMDelFriendType.TIM_FRIEND_DEL_BOTH);
        //设置要删除的好友ID列表
        param.setUsers(Collections.singletonList(id));
        TIMFriendshipManagerExt.getInstance().delFriend(param, new TIMValueCallBack<List<TIMFriendResult>>() {
            @Override
            public void onError(int i, String s) {
                friendshipManageView.onAddFriend(TIMFriendStatus.TIM_FRIEND_STATUS_UNKNOWN);
            }

            @Override
            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                for (TIMFriendResult item : timFriendResults) {
                    if (item.getIdentifer().equals(id)) {
                        friendshipManageView.onDelFriend(item.getStatus());
                    }
                }
            }
        });
    }


    /**
     * 好友关系链消息已读上报
     * 同时把已决未决消息和好友推荐消息已读
     *
     * @param timestamp 时间戳
     */
    public void readFriendshipMessage(long timestamp) {
        TIMFriendshipManagerExt.getInstance().pendencyReport(timestamp, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
                FriendEvent.getInstance().OnFriendshipMessageRead();
            }
        });
        TIMFriendshipManagerExt.getInstance().recommendReport(timestamp, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
                FriendEvent.getInstance().OnFriendshipMessageRead();
            }
        });
    }

    /**
     * 切换好友分组
     *
     * @param identify 好友identify
     * @param src      源分组，为空表示默认分组
     * @param dest     目的分组，为空表示默认分组
     */
    public void changeFriendGroup(final String identify, final @Nullable String src, final @Nullable String dest) {
        if (friendshipManageView == null || src != null && dest != null && src.equals(dest)) return;
        if (src != null) {
            TIMFriendshipManagerExt.getInstance().delFriendsFromFriendGroup(src, Collections.singletonList(identify), new TIMValueCallBack<List<TIMFriendResult>>() {
                @Override
                public void onError(int i, String s) {
                    Log.e(TAG, "changeFriendGroup.del src,code" + i + " msg " + s);
                    friendshipManageView.onChangeGroup(TIMFriendStatus.TIM_FRIEND_STATUS_UNKNOWN, src);
                }

                @Override
                public void onSuccess(List<TIMFriendResult> timFriendResults) {
                    if (dest != null) {
                        TIMFriendshipManagerExt.getInstance().addFriendsToFriendGroup(dest, Collections.singletonList(identify), new TIMValueCallBack<List<TIMFriendResult>>() {
                            @Override
                            public void onError(int i, String s) {
                                Log.e(TAG, "changeFriendGroup.add dest,code" + i + " msg " + s);
                                friendshipManageView.onChangeGroup(TIMFriendStatus.TIM_FRIEND_STATUS_UNKNOWN, null);
                            }

                            @Override
                            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                                friendshipManageView.onChangeGroup(timFriendResults.get(0).getStatus(), dest);
                            }
                        });
                    } else {
                        friendshipManageView.onChangeGroup(TIMFriendStatus.TIM_FRIEND_STATUS_SUCC, dest);
                    }
                }
            });
        } else {
            if (dest == null) return;
            TIMFriendshipManagerExt.getInstance().addFriendsToFriendGroup(dest, Collections.singletonList(identify), new TIMValueCallBack<List<TIMFriendResult>>() {
                @Override
                public void onError(int i, String s) {
                    Log.e(TAG, "changeFriendGroup.add dest,code" + i + " msg " + s);
                    friendshipManageView.onChangeGroup(TIMFriendStatus.TIM_FRIEND_STATUS_UNKNOWN, null);
                }

                @Override
                public void onSuccess(List<TIMFriendResult> timFriendResults) {
                    friendshipManageView.onChangeGroup(timFriendResults.get(0).getStatus(), dest);
                }
            });
        }
    }

}
