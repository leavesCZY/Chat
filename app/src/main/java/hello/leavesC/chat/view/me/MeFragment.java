package hello.leavesC.chat.view.me;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMUserProfile;

import hello.leavesC.chat.ChatApplication;
import hello.leavesC.chat.R;
import hello.leavesC.chat.utils.TransformUtil;
import hello.leavesC.chat.view.MainActivity;
import hello.leavesC.chat.view.base.BaseFragment;
import hello.leavesC.common.common.OptionView;
import hello.leavesC.presenter.business.LoginBusiness;
import hello.leavesC.presenter.listener.CallBackListener;
import hello.leavesC.presenter.manager.GroupManager;
import hello.leavesC.presenter.manager.SelfProfileManager;
import hello.leavesC.presenter.presenter.ProfilePresenter;
import hello.leavesC.presenter.view.ProfileView;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:15
 * 说明：个人信息界面
 */
public class MeFragment extends BaseFragment implements ProfileView {

    private View view;

    private RelativeLayout rl_avatar;

    private OptionView ov_nickname;

    private OptionView ov_gender;

    private OptionView ov_signature;

    private OptionView ov_allowType;

    private OptionView ov_reward;

    private OptionView ov_joinGroup;

    private Button btn_logout;

    private static final String TAG = "MeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_me, container, false);
            rl_avatar = view.findViewById(R.id.rl_avatar);
            OptionView ov_identifier = view.findViewById(R.id.ov_identifier);
            ov_identifier.setContent(ChatApplication.identifier);
            ov_nickname = view.findViewById(R.id.ov_nickname);
            ov_gender = view.findViewById(R.id.ov_gender);
            ov_signature = view.findViewById(R.id.ov_signature);
            ov_allowType = view.findViewById(R.id.ov_allowType);
            btn_logout = view.findViewById(R.id.btn_logout);
            ov_reward = view.findViewById(R.id.ov_reward);
            ov_joinGroup = view.findViewById(R.id.ov_joinGroup);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new ProfilePresenter(this).getSelfProfile();
    }

    @Override
    public void showProfile(TIMUserProfile userProfile) {
        ov_nickname.setContent(userProfile.getNickName());
        ov_gender.setContent(TransformUtil.parseGender(userProfile.getGender()));
        ov_signature.setContent(userProfile.getSelfSignature());
        ov_allowType.setContent(TransformUtil.parseAllowType(userProfile.getAllowType()));
        initListener();
    }

    private void initListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_avatar:
                        startActivity(AboutActivity.class);
                        break;
                    case R.id.ov_nickname:
                        ModifyInfoActivity.navigation(getContext(), ModifyInfoActivity.REQUEST_ALTER_NICKNAME, ov_nickname.getContent());
                        break;
                    case R.id.ov_signature:
                        ModifyInfoActivity.navigation(getContext(), ModifyInfoActivity.REQUEST_ALTER_SIGNATURE, ov_signature.getContent());
                        break;
                    case R.id.btn_logout:
                        showMessageDialog(null, "确认退出吗？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    LoginBusiness.logoutImServer(new TIMCallBack() {
                                        @Override
                                        public void onError(int i, String s) {
                                            showToast("注销失败");
                                        }

                                        @Override
                                        public void onSuccess() {
                                            if (getActivity() != null && getActivity() instanceof MainActivity) {
                                                MainActivity activity = (MainActivity) getActivity();
                                                activity.logout();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        break;
                    case R.id.ov_reward: {
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
                        break;
                    }
                    case R.id.ov_joinGroup: {
                        showMessageDialog(null, "是否加入开发者聊天群？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    showLoadingDialog("正在申请加入开发者聊天群");
                                    GroupManager.applyJoinGroup("@TGS#2VYICXBF3", "兴趣所致", new CallBackListener() {
                                        @Override
                                        public void onSuccess() {
                                            dismissLoadingDialog();
                                            showToast("已加入开发者聊天群");
                                        }

                                        @Override
                                        public void onError(int code, String desc) {
                                            dismissLoadingDialog();
                                            showToast("Error: " + code + " " + desc);
                                        }
                                    });
                                }
                            }
                        });
                        break;
                    }
                }
            }
        };
        OptionView.onClickOptionListener clickOptionListener = new OptionView.onClickOptionListener() {
            @Override
            public void onClick(int id, int which, String option) {
                switch (id) {
                    case R.id.ov_gender:
                        ov_gender.setContent(option);
                        SelfProfileManager.setGender(TransformUtil.parseGender(option), null);
                        break;
                    case R.id.ov_allowType:
                        ov_allowType.setContent(option);
                        SelfProfileManager.setAllowType(TransformUtil.parseAllowType(option), null);
                        break;
                }
            }
        };
        rl_avatar.setOnClickListener(clickListener);
        ov_nickname.setOnClickListener(clickListener);
        ov_signature.setOnClickListener(clickListener);
        btn_logout.setOnClickListener(clickListener);
        ov_gender.setOnClickShowPickerDialog("性别", TransformUtil.getGenderOption(), getFragmentManager(), clickOptionListener);
        ov_allowType.setOnClickShowPickerDialog("加好友选项", TransformUtil.getAllowTypeOption(), getFragmentManager(), clickOptionListener);
        ov_reward.setOnClickListener(clickListener);
        ov_joinGroup.setOnClickListener(clickListener);
    }

    @Override
    public void getProfileFail(int code, String desc) {
        showToast("获取个人资料失败，" + "code：" + code + " desc:" + desc);
    }

}