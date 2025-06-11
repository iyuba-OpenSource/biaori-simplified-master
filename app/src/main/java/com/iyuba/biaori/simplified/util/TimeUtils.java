package com.iyuba.biaori.simplified.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    /**
     * 毫秒转化为MM:ss的形式
     *
     * @param millisecond 传递的毫秒数
     * @return 返回转换后的分分:秒秒格式字符串
     */
    public static String milliSecToMinute(int millisecond) {
        String minuteStr = "00";
        String secStr;
        int second = millisecond / 1000;
        if (second > 60) {
            int minute = second / 60;
            int second2 = second % 60;  // 去除分钟后剩余的秒数
            if (minute < 10)
                minuteStr = "0" + minute;
            else
                minuteStr = String.valueOf(minute);
            if (second2 < 10)
                secStr = "0" + second2;
            else
                secStr = String.valueOf(second2);
        } else {
            if (second < 10)
                secStr = "0" + second;
            else
                secStr = String.valueOf(second);
        }
        return minuteStr + ":" + secStr;
    }

    // 毫秒转时间格式
    public static String mSecToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        int millisecond = 0;
        if (time <= 0)
            return "00:00:00.000";
        else {
            second = time / 1000;
            minute = second / 60;
            millisecond = time % 1000;
            if (second < 60) {
                timeStr = "00:00:" + unitFormat(second) + "." + unitFormat2(millisecond);
            } else if (minute < 60) {
                second = second % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second) + "." + unitFormat2(millisecond);
            } else {// 数字>=3600 000的时候
                hour = minute / 60;
                minute = minute % 60;
                second = second - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second) + "."
                        + unitFormat2(millisecond);
            }
        }
        return timeStr.substring(3, 8);
    }

    public static String unitFormat(int i) {// 时分秒的格式转换
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static String unitFormat2(int i) {// 毫秒的格式转换
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "00" + Integer.toString(i);
        else if (i >= 10 && i < 100) {
            retStr = "0" + Integer.toString(i);
        } else
            retStr = "" + i;
        return retStr;
    }

    public static String timeStamp2Date(String timeStamp) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(Long.parseLong(timeStamp)));
    }


    /**
     * 从2020-05-15 18:55:00形式的日期获取时间戳
     *
     * @param dateStr
     * @return
     */
    public static long getLongForDate(String dateStr) {

        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) {

            return -1;
        } else {

            return date.getTime();
        }
    }

    /**
     * 距离东八区1970-1-1 0时0分的天数
     *
     * @return
     */
    public static long getDays() {

        long days = new Date().getTime() / (24 * 60 * 60 * 1000);

        return days;
    }
}
