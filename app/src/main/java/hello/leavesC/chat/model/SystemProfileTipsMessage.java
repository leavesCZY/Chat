package hello.leavesC.chat.model;

import android.text.SpannableString;

import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMProfileSystemElem;

import hello.leavesC.chat.ChatApplication;
import hello.leavesC.chat.cache.FriendCache;
import hello.leavesC.chat.utils.SpannableStringUtil;

/**
 * 作者：叶应是叶
 * 时间：2018/2/1 20:01
 * 说明：
 */
public class SystemProfileTipsMessage extends BaseMessage {

    private static final String TAG = "SystemProfileTipsMessage";

    public SystemProfileTipsMessage(TIMMessage message) {
        super(message);
    }

    @Override
    public SpannableString getMessageSummary() {
        TIMProfileSystemElem profileSystemElem = (TIMProfileSystemElem) message.getElement(0);
        String user = profileSystemElem.getFromUser();
        if (user.equals(ChatApplication.identifier)) {
            user = "你";
        } else {
            user = FriendCache.getInstance().getFriendName(user);
        }
        String hint = user + " 修改了个人资料";
        return SpannableStringUtil.parseForegroundColorSpan(hint, 0, user.length());
    }

}
