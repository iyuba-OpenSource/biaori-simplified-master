package com.iyuba.biaori.simplified.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {


    public static Long getLongTime(String dateStr) {

        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date == null ? 0 : date.getTime();
    }


    /**
     * 获取日期，如：2022-06-27
     *
     * @param dateStr
     * @return
     */
    public static String getDateYMD(String dateStr) {

        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        String dateYMD = null;
        try {
            Date date = simpleDateFormat.parse(dateStr);
            simpleDateFormat.applyPattern("yyyy-MM-dd");
            dateYMD = simpleDateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateYMD;
    }


    public static String getAudioAllTime(int seconds) {
        StringBuffer timeBuffer = new StringBuffer("");
        int musicTime = seconds;
        String minute = "00";// 分
        String second = "00";// 秒
        if ((musicTime / 60) < 10) {
            minute = "0" + String.valueOf(musicTime / 60);

        } else {
            minute = String.valueOf(musicTime / 60);
        }
        if ((musicTime % 60) < 10) {
            second = "0" + String.valueOf(musicTime % 60);
        } else {
            second = String.valueOf(musicTime % 60);
        }
        timeBuffer.append(minute).append(":").append(second);

        return timeBuffer.toString();
    }

    public static String getCurDate() {

        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern("yyyy-MM-dd");
        return simpleDateFormat.format(new Date());
    }


    /**
     * 获取当前的时间   2022-08-26 13:32：56
     *
     * @return
     */
    public static String getCurTime() {

        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }


    /**
     * 从1970年1月1日开始到现在是多少天
     *
     * @return 天数
     */
    public static long getDays() {
        //东八区;
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.set(1970, 0, 1, 0, 0, 0);
        Calendar now = Calendar.getInstance(Locale.CHINA);
        long intervalMilli = now.getTimeInMillis() - cal.getTimeInMillis();
        long xcts = intervalMilli / (24 * 60 * 60 * 1000);
        return xcts;
    }
}
