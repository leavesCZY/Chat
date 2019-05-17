package hello.leavesC.chat.model;

import android.text.SpannableString;

import com.tencent.imsdk.TIMMessage;

/**
 * 作者：leavesC
 * 时间：2019/5/17 22:18
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class DefaultMessage extends BaseMessage {

    public DefaultMessage(TIMMessage message) {
        super(message);
    }

    @Override
    public SpannableString getMessageSummary() {
        return new SpannableString("[未知消息]");
    }

}