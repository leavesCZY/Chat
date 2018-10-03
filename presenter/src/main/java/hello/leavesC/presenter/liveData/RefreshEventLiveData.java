package hello.leavesC.presenter.liveData;

import android.arch.lifecycle.LiveData;

import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMRefreshListener;
import com.tencent.imsdk.TIMUserConfig;

import java.util.List;

import hello.leavesC.presenter.event.RefreshActionEvent;
import hello.leavesC.presenter.log.Logger;

/**
 * 作者：叶应是叶
 * 时间：2018/10/1 16:58
 * 描述：刷新与被动更新的通知
 */
public class RefreshEventLiveData extends LiveData<RefreshActionEvent> {

    private static final String TAG = "RefreshEventLiveData";

    private TIMRefreshListener refreshListener = new TIMRefreshListener() {
        //数据刷新通知，如未读计数、会话列表等
        @Override
        public void onRefresh() {
            Logger.e(TAG, "onRefresh");
            setValue(new RefreshActionEvent(RefreshActionEvent.REFRESH));
        }

        //部分会话刷新，多终端数据同步
        @Override
        public void onRefreshConversation(List<TIMConversation> list) {
            for (TIMConversation conversation : list) {
                Logger.e(TAG, "onRefreshConversation: " + conversation.getPeer());
            }
            setValue(new RefreshActionEvent(RefreshActionEvent.REFRESH));
        }
    };

    private RefreshEventLiveData() {

    }

    private static class SingletonHolder {
        private final static RefreshEventLiveData instance = new RefreshEventLiveData();
    }

    public static RefreshEventLiveData getInstance() {
        return RefreshEventLiveData.SingletonHolder.instance;
    }

    public TIMUserConfig init(TIMUserConfig userConfig) {
        userConfig.setRefreshListener(refreshListener);
        return userConfig;
    }

    @Override
    protected void onActive() {
        super.onActive();
        TIMManager.getInstance().getUserConfig().setRefreshListener(refreshListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        TIMManager.getInstance().getUserConfig().setRefreshListener(null);
    }

}