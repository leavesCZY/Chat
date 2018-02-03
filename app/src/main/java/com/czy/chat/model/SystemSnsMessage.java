package com.czy.chat.model;

import android.text.SpannableString;

import com.czy.chat.cache.FriendCache;
import com.czy.chat.utils.SpannableStringUtil;
import com.czy.presenter.log.Logger;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMSNSChangeInfo;
import com.tencent.imsdk.TIMSNSSystemElem;

import java.util.List;

/**
 * 作者：叶应是叶
 * 时间：2018/1/28 13:15
 * 说明：
 */
public class SystemSnsMessage extends BaseMessage {

    private static final String TAG = "SnsSystemMessage";

    public SystemSnsMessage(TIMMessage message) {
        super(message);
    }

    @Override
    public SpannableString getMessageSummary() {
        TIMSNSSystemElem timsnsSystemElem = (TIMSNSSystemElem) message.getElement(0);
        //关系链变更消息详细信息列表
        List<TIMSNSChangeInfo> changeInfoList = timsnsSystemElem.getChangeInfoList();
        TIMSNSChangeInfo changeInfo = changeInfoList.get(0);
        String name = FriendCache.getInstance().getFriendName(changeInfo.getIdentifier());
        Logger.e(TAG, "" + timsnsSystemElem.getSubType());

        switch (timsnsSystemElem.getSubType()) {
            //添加好友系统通知，当两个用户成为好友时，两个用户均可收到添加好友系统消息
            case TIM_SNS_SYSTEM_ADD_FRIEND: {
                String hint = "已添加 " + name + " 为好友";
                return SpannableStringUtil.parseForegroundColorSpan(hint, 4, hint.length() - 4);
            }
            //删除好友系统通知，当两个用户解除好友关系时，会收到删除好友系统消息
            case TIM_SNS_SYSTEM_DEL_FRIEND: {
                String hint = "已删除好友 " + name;
                return SpannableStringUtil.parseForegroundColorSpan(hint, 6, hint.length());
            }
            //好友申请系统通知，当申请好友时对方需要验证，自己和对方会收到好友申请系统通知
            case TIM_SNS_SYSTEM_ADD_FRIEND_REQ: {
                String wording = changeInfo.getWording();
                String source = changeInfo.getSource();
                String hint = "申请添加 " + name + " 为好友" + "\n理由：" + wording + "来源：" + source;
                return SpannableStringUtil.parseForegroundColorSpan(hint, 5, 5 + name.length());
            }
            //当申请对方为好友，申请审核通过后，自己会收到删除未决请求消息，表示之前的申请已经通过
            case TIM_SNS_SYSTEM_DEL_FRIEND_REQ: {
                String hint = "删除未决请求 " + name;
                return SpannableStringUtil.parseForegroundColorSpan(hint, 7, hint.length());
            }
            case TIM_SNS_SYSTEM_SNS_PROFILE_CHANGE: {
                String hint = "好友 " + name + " 修改了个人资料";
                return SpannableStringUtil.parseForegroundColorSpan(hint, 3, hint.length() - 8);
            }
        }
        return new SpannableString("未知消息");
    }

}
