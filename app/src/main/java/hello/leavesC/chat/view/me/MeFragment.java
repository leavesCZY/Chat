package hello.leavesC.chat.view.me;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hello.leavesC.chat.R;
import hello.leavesC.chat.view.MainActivity;
import hello.leavesC.chat.view.base.BaseFragment;
import hello.leavesC.common.common.OptionView;
import hello.leavesC.presenter.event.SelfProfileActionEvent;
import hello.leavesC.presenter.model.ProfileModel;
import hello.leavesC.presenter.utils.TransformUtil;
import hello.leavesC.presenter.viewModel.ModifySelfProfileViewModel;
import hello.leavesC.presenter.viewModel.SelfProfileViewModel;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:15
 * 说明：个人信息界面
 */
public class MeFragment extends BaseFragment {

    private View view;

    @BindView(R.id.ov_identifier)
    OptionView ov_identifier;

    @BindView(R.id.ov_nickname)
    OptionView ov_nickname;

    @BindView(R.id.ov_gender)
    OptionView ov_gender;

    @BindView(R.id.ov_signature)
    OptionView ov_signature;

    @BindView(R.id.ov_allowType)
    OptionView ov_allowType;

    private SelfProfileViewModel selfProfileViewModel;

    private ModifySelfProfileViewModel modifySelfProfileViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_me, container, false);
            ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        selfProfileViewModel.getSelfProfile();
    }

    @Override
    protected ViewModel initViewModel() {
        return null;
    }

    @Override
    protected List<ViewModel> initViewModelList() {
        selfProfileViewModel = ViewModelProviders.of(this).get(SelfProfileViewModel.class);
        selfProfileViewModel.getProfileModelLiveData().observe(this, this::handleSelfProfile);
        selfProfileViewModel.getEventLiveData().observe(this, this::handleEvent);
        modifySelfProfileViewModel = ViewModelProviders.of(this).get(ModifySelfProfileViewModel.class);
        List<ViewModel> viewModelList = new ArrayList<>();
        viewModelList.add(selfProfileViewModel);
        viewModelList.add(modifySelfProfileViewModel);
        return viewModelList;
    }

    private void handleEvent(SelfProfileActionEvent selfProfileActionEvent) {
        switch (selfProfileActionEvent.getAction()) {
            case SelfProfileActionEvent.LOGOUT_SUCCESS: {
                if (getActivity() != null && getActivity() instanceof MainActivity) {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.logout();
                }
                break;
            }
        }
    }

    private void handleSelfProfile(ProfileModel profileModel) {
        ov_identifier.setContent(profileModel.getIdentifier());
        ov_nickname.setContent(profileModel.getNickName());
        ov_gender.setContent(profileModel.getGender());
        ov_signature.setContent(profileModel.getSelfSignature());
        ov_allowType.setContent(profileModel.getAllow());
    }

    @OnClick({R.id.ov_about, R.id.ov_nickname, R.id.ov_signature, R.id.ov_joinQQGroup})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.ov_about: {
                startActivity(AboutActivity.class);
                break;
            }
            case R.id.ov_nickname: {
                ModifyInfoActivity.navigation(getContext(), ModifyInfoActivity.REQUEST_ALTER_NICKNAME, ov_nickname.getContent());
                break;
            }
            case R.id.ov_signature: {
                ModifyInfoActivity.navigation(getContext(), ModifyInfoActivity.REQUEST_ALTER_SIGNATURE, ov_signature.getContent());
                break;
            }
            case R.id.ov_joinQQGroup: {
                joinQQGroup();
                break;
            }
        }
    }

    public void joinQQGroup() {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + "XLJLuVHI20biz0Dck3GrC5PMJRdJ-SxX"));
        //此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
        } catch (Exception e) {
            showToast(e.getMessage());
        }
    }

    @OnClick(R.id.btn_logout)
    void logout() {
        new QMUIDialog.CheckBoxMessageDialogBuilder(getActivity())
                .setTitle("确定注销登录？")
                .setMessage("删除账号信息")
                .setChecked(true)
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction("退出", (dialog, index) -> {
                    dialog.dismiss();
                    selfProfileViewModel.logout();
                })
                .create().show();
    }

    @OnClick(R.id.ov_reward)
    void reward() {
        ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            ClipData clip = ClipData.newPlainText("Hello", "#吱口令#长按复制此条消息，打开支付宝给我转账cN9ccz98uq");
            clipboardManager.setPrimaryClip(clip);
            try {
                startActivity(getActivity().getPackageManager().getLaunchIntentForPackage("com.eg.android.AlipayGphone"));
            } catch (Exception e) {
                showToast("启动支付宝失败 " + e.getMessage());
            }
        }
    }

    @OnClick(R.id.ov_joinGroup)
    void joinGroup() {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle("Hello")
                .setMessage("是否加入开发者交流群？")
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction("确定", (dialog, index) -> {
                    dialog.dismiss();
                    selfProfileViewModel.applyJoinGroup("@TGS#2VYICXBF3", "兴趣所致");
                })
                .create().show();
    }

    @OnClick(R.id.ov_gender)
    void gender() {
        final String[] items = TransformUtil.getGenderOption();
        new QMUIDialog.CheckableDialogBuilder(getActivity())
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ov_gender.setContent(items[which]);
                        modifySelfProfileViewModel.setGender(TransformUtil.parseGender(items[which]));
                        dialog.dismiss();
                    }
                })
                .setTitle("性别")
                .create().show();
    }

    @OnClick(R.id.ov_allowType)
    void allowType() {
        final String[] items = TransformUtil.getAllowTypeOption();
        new QMUIDialog.CheckableDialogBuilder(getActivity())
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ov_allowType.setContent(items[which]);
                        modifySelfProfileViewModel.setAllowType(TransformUtil.parseAllowType(items[which]));
                        dialog.dismiss();
                    }
                })
                .setTitle("加好友选项")
                .create().show();
    }

}