package com.czy.ui.input.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：叶应是叶
 * 时间：2017/12/10 18:56
 * 说明：字符串与表情转换工具类
 */
public class SpanStringUtils {

    public static SpannableString getEmojiContent(Context context, TextView textView, int emoticonType, String source) {
        SpannableString spannableString = new SpannableString(source);
        Resources res = context.getResources();
        String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);
        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            // 利用表情名字获取到对应的图片
            Integer imageId = EmojiUtils.getEmojiId(emoticonType, key);
            // 压缩表情图片
            int size = (int) textView.getTextSize() * 13 / 10;
            Bitmap bitmap = BitmapFactory.decodeResource(res, imageId);
            Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
            ImageSpan span = new ImageSpan(context, scaleBitmap);
            spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    public static SpannableString getEmojiContent(Context context, TextView textView, SpannableString source) {
        SpannableString spannableString = new SpannableString(source);
        Resources res = context.getResources();
        String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);
        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            for (int emoticonType : EmojiUtils.emojiTypeList) {
                // 利用表情名字获取到对应的图片
                Integer imageId = EmojiUtils.getEmojiId(emoticonType, key);
                if (imageId == -1) {
                    continue;
                }
                // 压缩表情图片
                int size = (int) textView.getTextSize() * 13 / 10;
                Bitmap bitmap = BitmapFactory.decodeResource(res, imageId);
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                ImageSpan span = new ImageSpan(context, scaleBitmap);
                spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            }
        }
        return spannableString;
    }

}
