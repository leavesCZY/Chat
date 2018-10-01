package hello.leavesC.presenter.model;

import com.tencent.imsdk.TIMConversationType;

/**
 * 作者：叶应是叶
 * 时间：2018/10/1 22:32
 * 描述：
 */
public class ConversationModel {

    private String peer;

    private TIMConversationType conversationType;

    public ConversationModel(String peer, TIMConversationType conversationType) {
        this.peer = peer;
        this.conversationType = conversationType;
    }

    public String getPeer() {
        return peer;
    }

    public void setPeer(String peer) {
        this.peer = peer;
    }

    public TIMConversationType getConversationType() {
        return conversationType;
    }

    public void setConversationType(TIMConversationType conversationType) {
        this.conversationType = conversationType;
    }

}
