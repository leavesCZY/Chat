package com.czy.presenter.view;

import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;

import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:50
 * 说明：
 */
public interface GroupInfoView {

    /**
     * 显示群资料
     *
     * @param groupInfos 群资料信息列表
     */
    void showGroupInfo(List<TIMGroupDetailInfo> groupInfos);
}
