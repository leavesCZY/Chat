package com.czy.chat.utils;

import com.czy.chat.model.FriendProfile;

import java.util.Comparator;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:55
 * 说明：好友列表按称呼排序
 */
public class FriendProfileComparator implements Comparator<FriendProfile> {

    @Override
    public int compare(FriendProfile o1, FriendProfile o2) {
        String name_1 = o1.getName();
        String name_2 = o2.getName();
        if (name_1.equals(name_2)) {
            return 0;
        }
        int minLength = Math.min(name_1.length(), name_2.length());
        char headerLetter_1;
        char headerLetter_2;
        for (int i = 0; i < minLength; i++) {
            headerLetter_1 = name_1.charAt(i);
            headerLetter_2 = name_2.charAt(i);
            if (headerLetter_1 == headerLetter_2) {
                continue;
            }
            return LetterUtil.getHeaderLetter(headerLetter_1) > LetterUtil.getHeaderLetter(headerLetter_2) ? 1 : -1;
        }
        return name_1.length() > name_2.length() ? 1 : -1;
    }

}
