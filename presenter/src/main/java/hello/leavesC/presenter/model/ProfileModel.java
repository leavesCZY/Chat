package hello.leavesC.presenter.model;

import com.tencent.imsdk.TIMFriendAllowType;
import com.tencent.imsdk.TIMFriendGenderType;

/**
 * 作者：叶应是叶
 * 时间：2018/10/1 9:47
 * 描述：
 */
public class ProfileModel {

    private String identifier;

    private String nickName;

    private String selfSignature;

    private String faceUrl;

    private String gender;

    private TIMFriendGenderType genderType;

    private String allow;

    private TIMFriendAllowType allowType;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSelfSignature() {
        return selfSignature;
    }

    public void setSelfSignature(String selfSignature) {
        this.selfSignature = selfSignature;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public TIMFriendGenderType getGenderType() {
        return genderType;
    }

    public void setGenderType(TIMFriendGenderType genderType) {
        this.genderType = genderType;
    }

    public String getAllow() {
        return allow;
    }

    public void setAllow(String allow) {
        this.allow = allow;
    }

    public TIMFriendAllowType getAllowType() {
        return allowType;
    }

    public void setAllowType(TIMFriendAllowType allowType) {
        this.allowType = allowType;
    }

}