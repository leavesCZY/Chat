package hello.leavesC.presenter.viewModel;

import android.arch.lifecycle.MediatorLiveData;
import android.text.TextUtils;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.tencent.imsdk.ext.group.TIMGroupMemberResult;

import java.util.ArrayList;
import java.util.List;

import hello.leavesC.presenter.event.GroupProfileActionEvent;
import hello.leavesC.presenter.model.GroupMemberInfo;
import hello.leavesC.presenter.viewModel.base.BaseViewModel;

/**
 * 作者：叶应是叶
 * 时间：2018/10/3 12:27
 * 描述：
 */
public class GroupProfileViewModel extends BaseViewModel {

    private MediatorLiveData<TIMGroupMemberInfo> groupMemberInfoLiveData = new MediatorLiveData<>();

    private MediatorLiveData<List<GroupMemberInfo>> groupMemberInfoListLiveData = new MediatorLiveData<>();

    private MediatorLiveData<GroupProfileActionEvent> actionEventLiveData = new MediatorLiveData<>();

    public void getGroupMemberInfo(String groupId, String identifier) {
        showLoadingDialog("正在获取群成员资料");
        List<String> identifierList = new ArrayList<>();
        identifierList.add(identifier);
        TIMGroupManagerExt.getInstance().getGroupMembersInfo(groupId, identifierList, new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int i, String s) {
                dismissLoadingDialog();
                showToast(s);
            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> groupMemberInfoList) {
                dismissLoadingDialog();
                if (groupMemberInfoList != null && groupMemberInfoList.size() > 0) {
                    groupMemberInfoLiveData.setValue(groupMemberInfoList.get(0));
                }
            }
        });
    }

    public void getGroupMembers(String groupId) {
        showLoadingDialog("正在获取群成员列表");
        TIMGroupManagerExt.getInstance().getGroupMembers(groupId, new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int i, String s) {
                dismissLoadingDialog();
                showToast(s);
            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfoList) {
                dismissLoadingDialog();
                List<GroupMemberInfo> groupMemberInfoList = new ArrayList<>();
                for (TIMGroupMemberInfo info : timGroupMemberInfoList) {
                    groupMemberInfoList.add(new GroupMemberInfo(info));
                }
                groupMemberInfoListLiveData.setValue(groupMemberInfoList);
            }
        });
    }

    public void quitGroup(String groupId) {
        showLoadingDialog("正在退出群组");
        TIMGroupManager.getInstance().quitGroup(groupId, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                dismissLoadingDialog();
                showToast(s);
            }

            @Override
            public void onSuccess() {
                dismissLoadingDialog();
                showToast("已退出群组");
                actionEventLiveData.setValue(new GroupProfileActionEvent(GroupProfileActionEvent.QUIT_GROUP_SUCCESS));
            }
        });
    }

    public void modifyGroupReceiveMessageOpt(String groupId, String identifier, TIMGroupReceiveMessageOpt opt) {
        TIMGroupManagerExt.ModifyMemberInfoParam param = new TIMGroupManagerExt.ModifyMemberInfoParam(groupId, identifier);
        param.setReceiveMessageOpt(opt);
        TIMGroupManagerExt.getInstance().modifyMemberInfo(param, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                showToast(s);
            }

            @Override
            public void onSuccess() {

            }
        });
    }

    private void modifyProfile(String groupId, String groupName, String introduction, String notification) {
        showLoadingDialog("正在修改");
        TIMGroupManagerExt.ModifyGroupInfoParam param = new TIMGroupManagerExt.ModifyGroupInfoParam(groupId);
        if (!TextUtils.isEmpty(groupName)) {
            param.setGroupName(groupName);
        }
        if (!TextUtils.isEmpty(introduction)) {
            param.setIntroduction(introduction);
        }
        if (!TextUtils.isEmpty(notification)) {
            param.setNotification(notification);
        }
        TIMGroupManagerExt.getInstance().modifyGroupInfo(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                showToast(desc);
                dismissLoadingDialog();
            }

            @Override
            public void onSuccess() {
                showToast("修改成功");
                dismissLoadingDialog();
                actionEventLiveData.setValue(new GroupProfileActionEvent(GroupProfileActionEvent.MODIFY_PROFILE_SUCCESS));
            }
        });
    }

    public void modifyGroupName(String groupId, String groupName) {
        modifyProfile(groupId, groupName, null, null);
    }

    public void modifyGroupIntroduction(String groupId, String introduction) {
        modifyProfile(groupId, null, introduction, null);
    }

    public void modifyGroupNotification(String groupId, String notification) {
        modifyProfile(groupId, null, null, notification);
    }

    public void inviteGroup(String groupId, List<String> members) {
        showLoadingDialog("正在邀请好友入群");
        TIMGroupManagerExt.getInstance().inviteGroupMember(groupId, members, new TIMValueCallBack<List<TIMGroupMemberResult>>() {
            @Override
            public void onError(int i, String s) {
                dismissLoadingDialog();
                showToast(s);
            }

            @Override
            public void onSuccess(List<TIMGroupMemberResult> timGroupMemberResults) {
                dismissLoadingDialog();
                showToast("好友已入群");
                actionEventLiveData.setValue(new GroupProfileActionEvent(GroupProfileActionEvent.INVITE_GROUP_SUCCESS));
            }
        });
    }

    public void createGroup(String name, String type, List<String> members) {
        showLoadingDialog("正在创建群组");
        List<TIMGroupMemberInfo> groupMemberInfoList = new ArrayList<>();
        for (String member : members) {
            TIMGroupMemberInfo newMember = new TIMGroupMemberInfo(member);
            groupMemberInfoList.add(newMember);
        }
        TIMGroupManager.CreateGroupParam groupGroupParam = new TIMGroupManager.CreateGroupParam(type, name);
        groupGroupParam.setMembers(groupMemberInfoList);
        TIMGroupManager.getInstance().createGroup(groupGroupParam, new TIMValueCallBack<String>() {
            @Override
            public void onError(int i, String s) {
                dismissLoadingDialog();
                showToast(s);
            }

            @Override
            public void onSuccess(String s) {
                dismissLoadingDialog();
                showToast("创建成功");
                actionEventLiveData.setValue(new GroupProfileActionEvent(GroupProfileActionEvent.CREATE_GROUP_SUCCESS));
            }
        });
    }

    public MediatorLiveData<TIMGroupMemberInfo> getGroupMemberInfoLiveData() {
        return groupMemberInfoLiveData;
    }

    public MediatorLiveData<List<GroupMemberInfo>> getGroupMemberInfoListLiveData() {
        return groupMemberInfoListLiveData;
    }

    public MediatorLiveData<GroupProfileActionEvent> getActionEventLiveData() {
        return actionEventLiveData;
    }

}