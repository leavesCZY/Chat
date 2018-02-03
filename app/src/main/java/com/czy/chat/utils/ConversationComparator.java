package com.czy.chat.utils;

import com.czy.chat.model.BaseConversation;

import java.util.Comparator;

/**
 * 作者：叶应是叶
 * 时间：2017/12/10 13:20
 * 说明：会话列表按时间排序
 */
public class ConversationComparator implements Comparator<BaseConversation> {

    @Override
    public int compare(BaseConversation o1, BaseConversation o2) {
        long time1 = o1.getLastMessageTime();
        long time2 = o2.getLastMessageTime();
        if (time1 > time2) {
            return -1;
        }
        if (time1 < time2) {
            return 1;
        }
        return 0;
    }

}
