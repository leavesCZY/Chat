package com.czy.chat.utils;

import com.tencent.imsdk.TIMFriendAllowType;
import com.tencent.imsdk.TIMFriendGenderType;
import com.tencent.imsdk.TIMGroupMemberRoleType;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;

import java.util.HashMap;
import java.util.Map;

import static com.tencent.imsdk.TIMFriendAllowType.TIM_FRIEND_INVALID;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:57
 * 说明：枚举转义
 */
public class TransformUtil {

    private static final Map<TIMFriendGenderType, String> FRIEND_GENDER_TYPE_MAP = new HashMap<>();

    private static final Map<TIMFriendAllowType, String> FRIEND_ALLOW_TYPE_STRING_MAP = new HashMap<>();

    private static final Map<TIMGroupReceiveMessageOpt, String> GROUP_RECEIVE_MESSAGE_OPT_MAP = new HashMap<>();

    private static final Map<TIMGroupMemberRoleType, String> GROUP_MEMBER_ROLE_TYPE_STRING_MAP = new HashMap<>();

    static {
        FRIEND_GENDER_TYPE_MAP.put(TIMFriendGenderType.Male, "男");
        FRIEND_GENDER_TYPE_MAP.put(TIMFriendGenderType.Female, "女");
        FRIEND_GENDER_TYPE_MAP.put(TIMFriendGenderType.Unknow, "");
        FRIEND_ALLOW_TYPE_STRING_MAP.put(TIMFriendAllowType.TIM_FRIEND_NEED_CONFIRM, "需验证");
        FRIEND_ALLOW_TYPE_STRING_MAP.put(TIMFriendAllowType.TIM_FRIEND_ALLOW_ANY, "允许所有人添加");
        FRIEND_ALLOW_TYPE_STRING_MAP.put(TIMFriendAllowType.TIM_FRIEND_DENY_ANY, "拒绝任何人添加");
        FRIEND_ALLOW_TYPE_STRING_MAP.put(TIMFriendAllowType.TIM_FRIEND_INVALID, "");
        GROUP_RECEIVE_MESSAGE_OPT_MAP.put(TIMGroupReceiveMessageOpt.ReceiveAndNotify, "接收并提醒");
        GROUP_RECEIVE_MESSAGE_OPT_MAP.put(TIMGroupReceiveMessageOpt.ReceiveNotNotify, "接收但不提醒");
        GROUP_RECEIVE_MESSAGE_OPT_MAP.put(TIMGroupReceiveMessageOpt.NotReceive, "不接收");
        GROUP_MEMBER_ROLE_TYPE_STRING_MAP.put(TIMGroupMemberRoleType.Owner, "群主");
        GROUP_MEMBER_ROLE_TYPE_STRING_MAP.put(TIMGroupMemberRoleType.Admin, "管理员");
        GROUP_MEMBER_ROLE_TYPE_STRING_MAP.put(TIMGroupMemberRoleType.Normal, "群成员");
        GROUP_MEMBER_ROLE_TYPE_STRING_MAP.put(TIMGroupMemberRoleType.NotMember, "非群成员");
    }

    public static String parseGender(TIMFriendGenderType genderType) {
        if (genderType == null) {
            return FRIEND_GENDER_TYPE_MAP.get(TIMFriendGenderType.Unknow);
        }
        return FRIEND_GENDER_TYPE_MAP.get(genderType);
    }

    public static TIMFriendGenderType parseGender(String gender) {
        for (TIMFriendGenderType genderType : FRIEND_GENDER_TYPE_MAP.keySet()) {
            if (FRIEND_GENDER_TYPE_MAP.get(genderType).equals(gender)) {
                return genderType;
            }
        }
        return TIMFriendGenderType.Unknow;
    }

    public static String parseAllowType(TIMFriendAllowType friendAllowType) {
        if (friendAllowType == null) {
            return "";
        }
        return FRIEND_ALLOW_TYPE_STRING_MAP.get(friendAllowType);
    }

    public static TIMFriendAllowType parseAllowType(String allowType) {
        for (TIMFriendAllowType friendAllowType : FRIEND_ALLOW_TYPE_STRING_MAP.keySet()) {
            if (FRIEND_ALLOW_TYPE_STRING_MAP.get(friendAllowType).equals(allowType)) {
                return friendAllowType;
            }
        }
        return TIM_FRIEND_INVALID;
    }

    public static String[] getGenderOption() {
        return new String[]{FRIEND_GENDER_TYPE_MAP.get(TIMFriendGenderType.Male),
                FRIEND_GENDER_TYPE_MAP.get(TIMFriendGenderType.Female)};
    }

    public static String[] getAllowTypeOption() {
        return new String[]{FRIEND_ALLOW_TYPE_STRING_MAP.get(TIMFriendAllowType.TIM_FRIEND_NEED_CONFIRM),
                FRIEND_ALLOW_TYPE_STRING_MAP.get(TIMFriendAllowType.TIM_FRIEND_ALLOW_ANY),
                FRIEND_ALLOW_TYPE_STRING_MAP.get(TIMFriendAllowType.TIM_FRIEND_DENY_ANY)};
    }

    public static TIMGroupReceiveMessageOpt parseGroupReceiveMessageOpt(String opt) {
        for (TIMGroupReceiveMessageOpt messageOpt : GROUP_RECEIVE_MESSAGE_OPT_MAP.keySet()) {
            if (GROUP_RECEIVE_MESSAGE_OPT_MAP.get(messageOpt).equals(opt)) {
                return messageOpt;
            }
        }
        return null;
    }

    public static String parseGroupReceiveMessageOpt(TIMGroupReceiveMessageOpt messageOpt) {
        return GROUP_RECEIVE_MESSAGE_OPT_MAP.get(messageOpt);
    }

    public static String[] getGroupReceiveMessageOpt() {
        return new String[]{GROUP_RECEIVE_MESSAGE_OPT_MAP.get(TIMGroupReceiveMessageOpt.ReceiveAndNotify),
                GROUP_RECEIVE_MESSAGE_OPT_MAP.get(TIMGroupReceiveMessageOpt.ReceiveNotNotify),
                GROUP_RECEIVE_MESSAGE_OPT_MAP.get(TIMGroupReceiveMessageOpt.NotReceive)};
    }

    public static String parseGroupMemberRoleType(TIMGroupMemberRoleType memberRoleType) {
        return GROUP_MEMBER_ROLE_TYPE_STRING_MAP.get(memberRoleType);
    }

}