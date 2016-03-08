package com.gezbox.library.utils.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenzhaohua on 16/1/19.
 */
public class TimeUtils {

    /**
     * UTC time to format
     *
     * @param utcTime
     * @param format
     * @return
     */
    public static String formatTo(String utcTime, String format) {
        if (utcTime == null) {
            return "";
        }

        try {
            Date date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")).parse(utcTime.replaceAll("Z$", "+0000"));
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return utcTime;
    }


    /**
     * 将utc时间转换为当前时区的毫秒值
     *
     * @param utcTime
     * @return
     */
    public static long convertToMills(String utcTime) {
        if (TextUtils.isEmpty(utcTime)) {
            return 0;
        }

        try {
            Date date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")).parse(utcTime.replaceAll("Z$", "+0000"));
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }


    /**
     * 将Date转为服务端格式的utc时间
     *
     * @param date
     * @return
     */
    public static String convertToUtcTime(Date date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


}
