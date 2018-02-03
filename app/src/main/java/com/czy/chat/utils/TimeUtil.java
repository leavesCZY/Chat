package com.czy.chat.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:56
 * 说明：时间格式转换
 */
public class TimeUtil {

    public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String formatTime(long time, String format) {
        return new SimpleDateFormat(format, Locale.CHINA).format(new Date(time));
    }

    /**
     * 将long类型的时间值转化为指定格式的字符串类型，用于会话列表
     *
     * @param timeStamp 时间值，单位为秒
     */
    public static String getConversationTimeString(long timeStamp) {
        if (timeStamp < 1) {
            return "";
        }
        Calendar inputCalendar = Calendar.getInstance();
        inputCalendar.setTimeInMillis(timeStamp * 1000);
        Date date = inputCalendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH" + ":mm", Locale.CHINA);
        // 今天凌晨
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);
        todayCalendar.set(Calendar.MILLISECOND, 0);
        if (inputCalendar.after(todayCalendar)) {
            return simpleDateFormat.format(date);
        }
        // 昨天凌晨
        Calendar yesterdayCalendar = Calendar.getInstance();
        yesterdayCalendar.add(Calendar.DATE, -1);
        yesterdayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        yesterdayCalendar.set(Calendar.MINUTE, 0);
        yesterdayCalendar.set(Calendar.MILLISECOND, 0);
        // 前天凌晨
        Calendar beforeYesCalendar = Calendar.getInstance();
        beforeYesCalendar.add(Calendar.DATE, -2);
        beforeYesCalendar.set(Calendar.HOUR_OF_DAY, 0);
        beforeYesCalendar.set(Calendar.MINUTE, 0);
        beforeYesCalendar.set(Calendar.MILLISECOND, 0);
        if (inputCalendar.before(beforeYesCalendar)) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM" + "-" + "dd", Locale.CHINA);
            return sdf.format(date);
        } else if (inputCalendar.before(yesterdayCalendar)) {
            return "前天";
        }
        return "昨天";
    }

    /**
     * 将long类型的时间值转化为指定格式的字符串类型，用于聊天界面
     *
     * @param timeStamp 时间值，单位为秒
     */
    public static String getChatTimeString(long timeStamp) {
        Calendar inputCalendar = Calendar.getInstance();
        inputCalendar.setTimeInMillis(timeStamp * 1000);
        Date date = inputCalendar.getTime();
        SimpleDateFormat sd = new SimpleDateFormat("HH" + ":mm", Locale.CHINA);
        // 今天凌晨
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);
        todayCalendar.set(Calendar.MILLISECOND, 0);
        if (inputCalendar.after(todayCalendar)) {
            return sd.format(date);
        }
        // 昨天凌晨
        Calendar yesterdayCalendar = Calendar.getInstance();
        yesterdayCalendar.add(Calendar.DATE, -1);
        yesterdayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        yesterdayCalendar.set(Calendar.MINUTE, 0);
        yesterdayCalendar.set(Calendar.MILLISECOND, 0);
        // 前天凌晨
        Calendar beforeYesCalendar = Calendar.getInstance();
        beforeYesCalendar.add(Calendar.DATE, -2);
        beforeYesCalendar.set(Calendar.HOUR_OF_DAY, 0);
        beforeYesCalendar.set(Calendar.MINUTE, 0);
        beforeYesCalendar.set(Calendar.MILLISECOND, 0);
        if (inputCalendar.before(beforeYesCalendar)) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM" + "-" + "dd" + " " + "HH" + ":mm", Locale.CHINA);
            return sdf.format(date);
        } else if (inputCalendar.before(yesterdayCalendar)) {
            return "前天 " + sd.format(date);
        }
        return "昨天 " + sd.format(date);
    }

}