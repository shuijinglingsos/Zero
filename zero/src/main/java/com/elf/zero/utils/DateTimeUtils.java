package com.elf.zero.utils;

import java.text.DecimalFormat;

/**
 * 日期工具类
 * Created by Lidong on 2018/2/24.
 */
public class DateTimeUtils {

    public final static int MILLISECOND = 1;
    public final static int SECOND = 1000 * MILLISECOND;
    public final static int MINUTE = 60 * SECOND;
    public final static int HOUR = 60 * MINUTE;
    public final static int DAY = 24 * HOUR;



    public static String toMinute(long milliSecond) {
        if (milliSecond < 0) {
            return "00:00";
        }

        DecimalFormat df = new DecimalFormat("00");

        long minute = milliSecond / MINUTE;
        long second = milliSecond % MINUTE / SECOND;

        return df.format(minute) + ":" + df.format(second);
    }
}
