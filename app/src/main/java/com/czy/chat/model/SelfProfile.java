package com.czy.chat.model;

import com.tencent.imsdk.TIMFriendAllowType;
import com.tencent.imsdk.TIMFriendGenderType;

/**
 * 作者：叶应是叶
 * 时间：2018/2/1 20:05
 * 说明：
 */
public class SelfProfile {

    private String identifier;

    private String nickname;

    private TIMFriendGenderType gender;

    private String selfSignature;

    private TIMFriendAllowType friendAllowType;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public TIMFriendGenderType getGender() {
        return gender;
    }

    public void setGender(TIMFriendGenderType gender) {
        this.gender = gender;
    }

    public String getSelfSignature() {
        return selfSignature;
    }

    public void setSelfSignature(String selfSignature) {
        this.selfSignature = selfSignature;
    }

    public TIMFriendAllowType getFriendAllowType() {
        return friendAllowType;
    }

    public void setFriendAllowType(TIMFriendAllowType friendAllowType) {
        this.friendAllowType = friendAllowType;
    }

}
