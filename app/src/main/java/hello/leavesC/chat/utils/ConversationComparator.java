package hello.leavesC.chat.utils;

import java.util.Comparator;

import hello.leavesC.chat.model.BaseConversation;

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
        return Long.compare(time2, time1);
    }

}
