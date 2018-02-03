package com.czy.chat.model;

import android.support.annotation.DrawableRes;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:52
 * 说明：资料-基类
 */
abstract class BaseProfile {

    abstract public String getIdentifier();

    abstract public String getName();

    @DrawableRes
    abstract public int getDefaultAvatarResource();

    abstract public String getAvatarUrl();

}
