package com.gjj.shop.util;

import com.gjj.applibrary.util.Util;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/10/22.
 */
public class DateUtil {
    public static String getTimeStr(long timeMs) {
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTimeInMillis(timeMs);
        StringBuilder stringBuilder = Util.getThreadSafeStringBuilder();
        stringBuilder.append(dateCalendar.get(Calendar.YEAR)).append("-").append(dateCalendar.get(Calendar.MONTH) + 1).append("-").append(dateCalendar.get(Calendar.DAY_OF_MONTH));
        return stringBuilder.toString();
    }
}
