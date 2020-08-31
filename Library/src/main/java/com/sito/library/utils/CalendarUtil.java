package com.sito.library.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by zhangan on 2017-07-03.
 */

public class CalendarUtil {

    // 获取当前时间所在周的开始日期
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    // 获取当前时间所在周的结束日期
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }

    public static List<Date> getDaysOfWeek(Date today) {
        List<Date> list = new ArrayList<>();
        Calendar c = Calendar.getInstance(Locale.CHINA);
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        c.setTime(today);
        for (int i = 0; i < 7; i++) {
            c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + i);
            list.add(c.getTime());
        }
        return list;
    }

    public static String getWeek(Calendar calendar) {
        String str;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                str = "星期天";
                break;
            case 2:
                str = "星期一";
                break;
            case 3:
                str = "星期二";
                break;
            case 4:
                str = "星期三";
                break;
            case 5:
                str = "星期四";
                break;
            case 6:
                str = "星期五";
                break;
            default:
                str = "星期六";
                break;
        }
        return str;
    }

    public static int getWeekInt(Calendar calendar) {
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int str;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                str = 7;
                break;
            case 2:
                str = 1;
                break;
            case 3:
                str = 2;
                break;
            case 4:
                str = 3;
                break;
            case 5:
                str = 4;
                break;
            case 6:
                str = 5;
                break;
            default:
                str = 6;
                break;
        }
        return str;
    }

}
