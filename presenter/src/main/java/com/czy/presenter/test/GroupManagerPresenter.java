package com.czy.presenter.test;

import android.util.Log;

import com.czy.presenter.view.GroupInfoView;
import com.czy.presenter.view.GroupManageMessageView;
import com.czy.presenter.view.GroupManageView;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.tencent.imsdk.ext.group.TIMGroupPendencyGetParam;
import com.tencent.imsdk.ext.group.TIMGroupPendencyListGetSucc;
import com.tencent.imsdk.ext.group.TIMGroupSearchSucc;

import java.util.Collections;
import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:47
 * 说明：
 */
public class GroupManagerPresenter {

    private static final String TAG = "GroupManagerPresenter";

    private GroupManageMessageView groupManageMessageView;

    private GroupInfoView groupInfoView;

    private GroupManageView groupManageView;

    private long timeStamp = 0;

    public GroupManagerPresenter(GroupManageMessageView groupManageMessageView) {
        this(groupManageMessageView, null, null);
    }

    public GroupManagerPresenter(GroupInfoView groupInfoView) {
        this(null, groupInfoView, null);
    }

    public GroupManagerPresenter(GroupManageView groupManageView) {
        this(null, null, groupManageView);
    }

    public GroupManagerPresenter(GroupManageMessageView groupManageMessageView, GroupInfoView groupInfoView, GroupManageView groupManageView) {
        this.groupManageMessageView = groupManageMessageView;
        this.groupInfoView = groupInfoView;
        this.groupManageView = groupManageView;
    }

    /**
     * 获取群管理最后一条消息和未读消息数
     * 包括：加群等已决和未决的消息
     */
    public void getGroupManageLastMessage() {
        TIMGroupPendencyGetParam param = new TIMGroupPendencyGetParam();
        param.setNumPerPage(1);
        param.setTimestamp(0);
        TIMGroupManagerExt.getInstance().getGroupPendencyList(param, new TIMValueCallBack<TIMGroupPendencyListGetSucc>() {
            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "onError code" + i + " msg " + s);
            }

            @Override
            public void onSuccess(TIMGroupPendencyListGetSucc timGroupPendencyListGetSucc) {
                if (groupManageMessageView != null && timGroupPendencyListGetSucc.getPendencies().size() > 0) {
                    groupManageMessageView.onGetGroupManageLastMessage(timGroupPendencyListGetSucc.getPendencies().get(0),
                            timGroupPendencyListGetSucc.getPendencyMeta().getUnReadCount());
                }
            }
        });
    }

    /**
     * 获取群管理消息
     *
     * @param pageSize 每次拉取数量
     */
    public void getGroupManageMessage(int pageSize) {
        TIMGroupPendencyGetParam param = new TIMGroupPendencyGetParam();
        param.setNumPerPage(pageSize);
        param.setTimestamp(timeStamp);
        TIMGroupManagerExt.getInstance().getGroupPendencyList(param, new TIMValueCallBack<TIMGroupPendencyListGetSucc>() {
            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "onError code" + i + " msg " + s);
            }

            @Override
            public void onSuccess(TIMGroupPendencyListGetSucc timGroupPendencyListGetSucc) {
                if (groupManageMessageView != null) {
                    groupManageMessageView.onGetGroupManageMessage(timGroupPendencyListGetSucc.getPendencies());
                }
            }
        });
    }

    /**
     * 按照群名称搜索群
     *
     * @param key 关键字
     */
    public void searchGroupByName(String key) {
        long flag = 0;
        flag |= TIMGroupManager.TIM_GET_GROUP_BASE_INFO_FLAG_NAME;
        flag |= TIMGroupManager.TIM_GET_GROUP_BASE_INFO_FLAG_OWNER_UIN;
        TIMGroupManagerExt.getInstance().searchGroup(key, flag, null, 0, 30, new TIMValueCallBack<TIMGroupSearchSucc>() {
            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "onError code" + i + " msg " + s);
            }

            @Override
            public void onSuccess(TIMGroupSearchSucc timGroupSearchSucc) {
                if (groupInfoView == null) {
                    return;
                }
                groupInfoView.showGroupInfo(timGroupSearchSucc.getInfoList());
            }
        });
    }

    /**
     * 按照群ID搜索群
     *
     * @param groupId 群组ID
     */
    public void searchGroupByID(String groupId) {
        TIMGroupManagerExt.getInstance().getGroupPublicInfo(Collections.singletonList(groupId), new TIMValueCallBack<List<TIMGroupDetailInfo>>() {
            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "onError code" + i + " msg " + s);
            }

            @Override
            public void onSuccess(List<TIMGroupDetailInfo> timGroupDetailInfos) {
                if (groupInfoView == null) {
                    return;
                }
                groupInfoView.showGroupInfo(timGroupDetailInfos);
            }
        });
    }

}
