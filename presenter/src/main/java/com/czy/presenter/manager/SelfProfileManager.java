package com.czy.presenter.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.czy.presenter.listener.CallBackListener;
import com.czy.presenter.log.Logger;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendAllowType;
import com.tencent.imsdk.TIMFriendGenderType;
import com.tencent.imsdk.TIMFriendshipManager;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:46
 * 说明：
 */
public class SelfProfileManager {

    private static final String TAG = "SelfProfileManager";

    /**
     * 设置昵称
     */
    public static void setNickname(@NonNull String nickname, @Nullable final CallBackListener callBackListener) {
        modifyProfile(nickname, null, null, null, callBackListener);
    }

    /**
     * 设置性别
     */
    public static void setGender(@NonNull TIMFriendGenderType genderType, @Nullable final CallBackListener callBackListener) {
        modifyProfile(null, genderType, null, null, callBackListener);
    }

    /**
     * 设置个性签名
     */
    public static void setSignature(@NonNull String signature, @Nullable final CallBackListener callBackListener) {
        modifyProfile(null, null, signature, null, callBackListener);
    }

    /**
     * 设置好友验证类型
     */
    public static void setAllowType(@NonNull TIMFriendAllowType allowType, @Nullable final CallBackListener callBackListener) {
        modifyProfile(null, null, null, allowType, callBackListener);
    }

    private static void modifyProfile(String nickname, TIMFriendGenderType genderType, String signature, TIMFriendAllowType allowType, @Nullable final CallBackListener callBackListener) {
        TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
        if (nickname != null) {
            param.setNickname(nickname);
        }
        if (genderType != null) {
            param.setGender(genderType);
        }
        if (signature != null) {
            param.setSelfSignature(signature);
        }
        if (allowType != null) {
            Logger.e(TAG, "ALL: " + allowType);
            param.setAllowType(allowType);
        }
        TIMFriendshipManager.getInstance().modifyProfile(param, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Logger.e(TAG, "Error: " + i + " " + s);
                if (callBackListener != null) {
                    callBackListener.onError(i, s);
                }
            }

            @Override
            public void onSuccess() {
                Logger.e(TAG,"onSuccess");
                if (callBackListener != null) {
                    callBackListener.onSuccess();
                }
            }
        });
    }

}
