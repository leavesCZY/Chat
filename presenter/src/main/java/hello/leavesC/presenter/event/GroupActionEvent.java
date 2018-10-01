package hello.leavesC.presenter.event;

import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;

import java.util.List;

import hello.leavesC.presenter.event.base.BaseEvent;

/**
 * 作者：叶应是叶
 * 时间：2018/10/1 16:37
 * 描述：
 */
public class GroupActionEvent extends BaseEvent {

    //添加群
    public static final int ADD = 10;

    //删除群
    public static final int DELETE = 20;

    //群信息更新
    public static final int GROUP_PROFILE_UPDATE = 30;

    //有新成员加入群
    public static final int JOIN = 40;

    //有群成员退出
    public static final int QUIT = 50;

    //群成员资料刷新
    public static final int MEMBER_PROFILE_UPDATE = 60;

    private TIMGroupCacheInfo groupCacheInfo;

    private String groupId;

    private List<TIMGroupMemberInfo> memberInfoList;

    private List<String> identifierList;

    public GroupActionEvent(int action) {
        super(action);
    }

    public TIMGroupCacheInfo getGroupCacheInfo() {
        return groupCacheInfo;
    }

    public void setGroupCacheInfo(TIMGroupCacheInfo groupCacheInfo) {
        this.groupCacheInfo = groupCacheInfo;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<TIMGroupMemberInfo> getMemberInfoList() {
        return memberInfoList;
    }

    public void setMemberInfoList(List<TIMGroupMemberInfo> memberInfoList) {
        this.memberInfoList = memberInfoList;
    }

    public List<String> getIdentifierList() {
        return identifierList;
    }

    public void setIdentifierList(List<String> identifierList) {
        this.identifierList = identifierList;
    }
}
