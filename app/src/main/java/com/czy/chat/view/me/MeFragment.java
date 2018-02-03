package com.czy.chat.view.me;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.czy.chat.ChatApplication;
import com.czy.chat.R;
import com.czy.chat.utils.TransformUtil;
import com.czy.chat.view.MainActivity;
import com.czy.chat.view.base.BaseFragment;
import com.czy.presenter.business.LoginBusiness;
import com.czy.presenter.log.Logger;
import com.czy.presenter.manager.SelfProfileManager;
import com.czy.presenter.presenter.ProfilePresenter;
import com.czy.presenter.view.ProfileView;
import com.czy.ui.common.OptionView;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMUserProfile;

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

    private Button btn_logout;

    private static final String TAG = "MeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_me, container, false);
            rl_avatar = (RelativeLayout) view.findViewById(R.id.rl_avatar);
            OptionView ov_identifier = (OptionView) view.findViewById(R.id.ov_identifier);
            ov_identifier.setContent(ChatApplication.identifier);
            ov_nickname = (OptionView) view.findViewById(R.id.ov_nickname);
            ov_gender = (OptionView) view.findViewById(R.id.ov_gender);
            ov_signature = (OptionView) view.findViewById(R.id.ov_signature);
            ov_allowType = (OptionView) view.findViewById(R.id.ov_allowType);
            btn_logout = (Button) view.findViewById(R.id.btn_logout);
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
        Logger.e(TAG, "all： " + userProfile.getAllowType());
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
    }

    @Override
    public void getProfileFail(int code, String desc) {
        showToast("获取个人资料失败，" + "code：" + code + " desc:" + desc);
    }

}