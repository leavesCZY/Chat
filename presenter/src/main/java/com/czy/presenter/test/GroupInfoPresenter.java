package com.czy.presenter.test;

import com.czy.presenter.view.GroupInfoView;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;

import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:47
 * 说明：获取群组信息
 */
public class GroupInfoPresenter {

    private GroupInfoView groupInfoView;

    private List<String> groupIds;

    private boolean isInGroup;

    private TIMValueCallBack<List<TIMGroupDetailInfo>> valueCallBack = new TIMValueCallBack<List<TIMGroupDetailInfo>>() {
        @Override
        public void onError(int i, String s) {

        }

        @Override
        public void onSuccess(List<TIMGroupDetailInfo> timGroupDetailInfos) {
            groupInfoView.showGroupInfo(timGroupDetailInfos);
        }
    };

    public GroupInfoPresenter(GroupInfoView groupInfoView, List<String> groupIds, boolean isInGroup) {
        this.groupInfoView = groupInfoView;
        this.groupIds = groupIds;
        this.isInGroup = isInGroup;
    }

    public void getGroupDetailInfo() {
        if (isInGroup) {
            TIMGroupManagerExt.getInstance().getGroupDetailInfo(groupIds, valueCallBack);
        } else {
            TIMGroupManagerExt.getInstance().getGroupPublicInfo(groupIds, valueCallBack);
        }
    }

}
