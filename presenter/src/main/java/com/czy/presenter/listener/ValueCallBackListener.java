package com.czy.presenter.listener;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:44
 * 说明：
 */
public interface ValueCallBackListener<T> {

    void onSuccess(T result);

    void onError(int code, String desc);

}
