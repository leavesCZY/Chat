package com.czy.chat.model;

import android.text.SpannableString;
import android.text.TextUtils;

import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupTipsElem;
import com.tencent.imsdk.TIMGroupTipsElemGroupInfo;
import com.tencent.imsdk.TIMMessage;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 作者：叶应是叶
 * 时间：2018/1/14 20:12
 * 说明：会话内的群组系统消息
 */
public class GroupTipsMessage extends BaseMessage {

    public GroupTipsMessage(TIMMessage message) {
        super(message);
        setSystemMessage(true);
    }

    @Override
    public SpannableString getMessageSummary() {
        TIMGroupTipsElem groupTipsElem = (TIMGroupTipsElem) message.getElement(0);
        Iterator<Map.Entry<String, TIMGroupMemberInfo>> iterator = groupTipsElem.getChangedGroupMemberInfo().entrySet().iterator();
        switch (groupTipsElem.getTipsType()) {
            case Join: {
                StringBuilder stringBuilder = new StringBuilder();
                while (iterator.hasNext()) {
                    Map.Entry<String, TIMGroupMemberInfo> item = iterator.next();
                    stringBuilder.append(TextUtils.isEmpty(item.getValue().getNameCard()) ? item.getValue().getUser() : item.getValue().getNameCard());
                    stringBuilder.append(" ");
                }
                return new SpannableString(stringBuilder.append(" 加入了群组"));
            }
            case Quit:
                return new SpannableString(groupTipsElem.getOpUser() + " 退出群组");
            case Kick:
                return new SpannableString(groupTipsElem.getOpUser() + " 被踢出群组");
            case SetAdmin:
                return new SpannableString(groupTipsElem.getOpUser() + " 成为管理员");
            case CancelAdmin:
                return new SpannableString(groupTipsElem.getOpUser() + " 被取消管理员");
            case ModifyGroupInfo: {
                StringBuilder stringBuilder = new StringBuilder();
                List<TIMGroupTipsElemGroupInfo> groupTipsElemGroupInfoList = groupTipsElem.getGroupInfoList();
                for (TIMGroupTipsElemGroupInfo groupInfo : groupTipsElemGroupInfoList) {
                    switch (groupInfo.getType()) {
                        case ModifyName:
                            stringBuilder.append(groupTipsElem.getOpUser());
                            stringBuilder.append(" 将群名称修改为 ");
                            stringBuilder.append(groupInfo.getContent());
                            break;
                        case ModifyIntroduction:
                            stringBuilder.append(groupTipsElem.getOpUser());
                            stringBuilder.append(" 将群简介修改为 ");
                            stringBuilder.append(groupInfo.getContent());
                            break;
                        case ModifyNotification:
                            stringBuilder.append(groupTipsElem.getOpUser());
                            stringBuilder.append(" 将群公告修改为 ");
                            stringBuilder.append(groupInfo.getContent());
                            break;
                        case ModifyFaceUrl:
                            stringBuilder.append(groupTipsElem.getOpUser());
                            stringBuilder.append(" 将群头像修改为 ");
                            stringBuilder.append(groupInfo.getContent());
                            break;
                        case ModifyOwner:
                            stringBuilder.append(groupTipsElem.getOpUser());
                            stringBuilder.append(" 群主变更 ");
                            stringBuilder.append(groupInfo.getContent());
                            break;
                        case Invalid:
                        default:
                            stringBuilder.append(groupTipsElem.getOpUser());
                            stringBuilder.append(" 无效变更 ");
                            stringBuilder.append(groupInfo.getContent());
                            break;
                    }
                }
                return new SpannableString(stringBuilder);
            }
            case ModifyMemberInfo: {
                StringBuilder stringBuilder = new StringBuilder();
                while (iterator.hasNext()) {
                    Map.Entry<String, TIMGroupMemberInfo> item = iterator.next();
                    stringBuilder.append(TextUtils.isEmpty(item.getValue().getNameCard()) ? item.getValue().getUser() : item.getValue().getNameCard());
                }
                return new SpannableString(stringBuilder.append(" 群资料变更"));
            }
        }
        return null;
    }

}
