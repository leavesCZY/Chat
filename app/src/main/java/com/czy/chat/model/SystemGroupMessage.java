package com.czy.chat.model;

import android.text.SpannableString;

import com.czy.chat.cache.FriendCache;
import com.czy.chat.cache.GroupCache;
import com.czy.chat.utils.SpannableStringUtil;
import com.tencent.imsdk.TIMGroupSystemElem;
import com.tencent.imsdk.TIMMessage;

/**
 * 作者：叶应是叶
 * 时间：2018/1/27 20:08
 * 说明：全局的群组系统消息
 */
public class SystemGroupMessage extends BaseMessage {

    public SystemGroupMessage(TIMMessage message) {
        super(message);
    }

    @Override
    public SpannableString getMessageSummary() {
        TIMGroupSystemElem groupSystemElem = (TIMGroupSystemElem) message.getElement(0);
        String groupName = GroupCache.getInstance().getGroupName(groupSystemElem.getGroupId());
        String opUserName = FriendCache.getInstance().getFriendName(groupSystemElem.getOpUser());
        switch (groupSystemElem.getSubtype()) {
            //申请加群请求（只有管理员会收到）
            case TIM_GROUP_SYSTEM_ADD_GROUP_REQUEST_TYPE: {
                String hint = opUserName + " 申请加入群组 " + groupName;
                return SpannableStringUtil.parseForegroundColorSpan(hint, 0, opUserName.length());
            }
            //申请加群被同意（只有申请人能够收到）
            case TIM_GROUP_SYSTEM_ADD_GROUP_ACCEPT_TYPE: {
                String hint = "已加入群组 " + groupName;
                return SpannableStringUtil.parseForegroundColorSpan(hint, 6, hint.length());
            }
            //申请加群被拒绝（只有申请人能够收到）
            case TIM_GROUP_SYSTEM_ADD_GROUP_REFUSE_TYPE: {
                return new SpannableString(opUserName + " 拒绝你加入群组 " + groupName + " 的申请");
            }
            //邀请入群请求（被邀请者接收）
            case TIM_GROUP_SYSTEM_INVITE_TO_GROUP_REQUEST_TYPE: {
                String hint = opUserName + " 邀请你加入群组 " + groupName;
                return SpannableStringUtil.parseForegroundColorSpan(hint, 0, opUserName.length());
            }
            //邀请加群被同意(只有发出邀请者会接收到)
            case TIM_GROUP_SYSTEM_INVITATION_ACCEPTED_TYPE: {
                return new SpannableString(opUserName + " 同意了你邀请好友加入群组 " + groupName);
            }//邀请加群被拒绝(只有发出邀请者会接收到)
            case TIM_GROUP_SYSTEM_INVITATION_REFUSED_TYPE: {
                return new SpannableString(opUserName + " 拒绝了你邀请好友加入群组 " + groupName);
            }
            //被管理员踢出群（只有被踢的人能够收到）
            case TIM_GROUP_SYSTEM_KICK_OFF_FROM_GROUP_TYPE: {
                String hint = opUserName + " 将你移出群组 " + groupName;
                return SpannableStringUtil.parseForegroundColorSpan(hint, 0, opUserName.length());
            }
            //群被解散（全员能够收到）
            case TIM_GROUP_SYSTEM_DELETE_GROUP_TYPE: {
                String hint = "群组 " + groupName + " 被解散";
                return SpannableStringUtil.parseForegroundColorSpan(hint, 3, 3 + groupName.length());
            }
            //创建群消息（初始成员能够收到）
            case TIM_GROUP_SYSTEM_CREATE_GROUP_TYPE: {
                String hint = "已创建群组 " + groupName;
                return SpannableStringUtil.parseForegroundColorSpan(hint, 6, hint.length());
            }
            //邀请入群消息（被邀请者能够收到，此时用户已经加入到群组）
            case TIM_GROUP_SYSTEM_INVITED_TO_GROUP_TYPE: {
                return new SpannableString(opUserName + " 邀请你加入了群组 " + groupName);
            }
            //主动退群（主动退群者能够收到）
            case TIM_GROUP_SYSTEM_QUIT_GROUP_TYPE: {
                String hint = "已退出群组 " + groupName;
                return SpannableStringUtil.parseForegroundColorSpan(hint, 6, hint.length());
            }
            //设置管理员(被设置者接收)
            case TIM_GROUP_SYSTEM_GRANT_ADMIN_TYPE: {
                return new SpannableString("已成为群组 " + groupName + " 管理员");
            }
            //取消管理员(被取消者接收)
            case TIM_GROUP_SYSTEM_CANCEL_ADMIN_TYPE: {
                return new SpannableString("被取消群组 " + groupName + " 管理员身份");
            }
            //群已被回收(全员接收)
            case TIM_GROUP_SYSTEM_REVOKE_GROUP_TYPE: {
                return new SpannableString("群组 " + groupName + " 已被回收");
            }
        }
        return new SpannableString("未知消息");
    }

}
