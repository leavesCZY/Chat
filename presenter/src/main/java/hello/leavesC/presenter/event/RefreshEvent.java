package hello.leavesC.presenter.event;

import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMRefreshListener;
import com.tencent.imsdk.TIMUserConfig;

import java.util.List;
import java.util.Observable;

import hello.leavesC.presenter.log.Logger;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:44
 * 说明：刷新与被动更新的通知
 */
public class RefreshEvent extends Observable {

    private static final String TAG = "RefreshEvent";

    private static RefreshEvent sInstance;

    private RefreshEvent() {

    }

    public static RefreshEvent getInstance() {
        if (sInstance == null) {
            synchronized (RefreshEvent.class) {
                if (sInstance == null) {
                    sInstance = new RefreshEvent();
                }
            }
        }
        return sInstance;
    }

    public TIMUserConfig init(TIMUserConfig userConfig) {
        userConfig.setRefreshListener(new TIMRefreshListener() {
            //数据刷新通知，如未读计数、会话列表等
            @Override
            public void onRefresh() {
                Logger.e(TAG, "onRefresh");
                setChanged();
                notifyObservers();
            }

            //部分会话刷新，多终端数据同步
            @Override
            public void onRefreshConversation(List<TIMConversation> list) {
                for (TIMConversation conversation : list) {
                    Logger.e(TAG, "onRefreshConversation: " + conversation.getPeer());
                }
                setChanged();
                notifyObservers();
            }
        });
        return userConfig;
    }

    public void clean() {
        sInstance = null;
    }

}
