package com.czy.chat.utils;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 * 作者：叶应是叶
 * 时间：2018/1/28 13:49
 * 说明：
 */
public class SpannableStringUtil {

    public static SpannableString parseForegroundColorSpan(String content, int startIndex, int endIndex) {
        SpannableString spannableString = new SpannableString(content);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#30cfff"));
        spannableString.setSpan(foregroundColorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableString;
    }

}
