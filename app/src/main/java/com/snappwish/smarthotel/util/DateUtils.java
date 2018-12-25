package com.snappwish.smarthotel.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jinjin on 16/6/28.
 */
public class DateUtils {
    public static long getLongTime(char dateType) {
        Calendar c = Calendar.getInstance(); // 当时的日期和时间
        int hour; // 需要更改的小时
        int day; // 需要更改的天数
        switch (dateType) {
            case '0': // 1小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 1;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case '1': // 2小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 2;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case '2': // 3小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 3;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case '3': // 6小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 6;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case '4': // 12小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 12;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case '5': // 一天前
                day = c.get(Calendar.DAY_OF_MONTH) - 1;
                c.set(Calendar.DAY_OF_MONTH, day);
                // System.out.println(df.format(c.getTime()));
                break;
            case '6': // 一星期前
                day = c.get(Calendar.DAY_OF_MONTH) - 7;
                c.set(Calendar.DAY_OF_MONTH, day);
                // System.out.println(df.format(c.getTime()));
                break;
            case '7': // 一个月前
                day = c.get(Calendar.DAY_OF_MONTH) - 30;
                c.set(Calendar.DAY_OF_MONTH, day);
                // System.out.println(df.format(c.getTime()));
                break;
        }
        return c.getTimeInMillis();
    }

    /**
     * 获取阶段日期
     *
     * @param dateType
     * @author Yangtse
     */
    //使用方法 char datetype = '7';
    public static StringBuilder getPeriodDate(char dateType) {
        Calendar c = Calendar.getInstance(); // 当时的日期和时间
        int hour; // 需要更改的小时
        int day; // 需要更改的天数
        switch (dateType) {
            case '0': // 1小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 1;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case '1': // 2小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 2;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case '2': // 3小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 3;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case '3': // 6小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 6;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case '4': // 12小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 12;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case '5': // 一天前
                day = c.get(Calendar.DAY_OF_MONTH) - 1;
                c.set(Calendar.DAY_OF_MONTH, day);
                // System.out.println(df.format(c.getTime()));
                break;
            case '6': // 一星期前
                day = c.get(Calendar.DAY_OF_MONTH) - 7;
                c.set(Calendar.DAY_OF_MONTH, day);
                // System.out.println(df.format(c.getTime()));
                break;
            case '7': // 一个月前
                day = c.get(Calendar.DAY_OF_MONTH) - 30;
                c.set(Calendar.DAY_OF_MONTH, day);
                // System.out.println(df.format(c.getTime()));
                break;
        }
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        StringBuilder strForwardDate = new StringBuilder().append(mYear).append(
                (mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append(
                (mDay < 10) ? "0" + mDay : mDay);
        System.out.println("strDate------->" + strForwardDate + "-" + c.getTimeInMillis());
        return strForwardDate;
        //return c.getTimeInMillis();
    }

    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获取指定月的第一天
     *
     * @param position 0为当月,正数为+,负数为减
     */
    public static String monthFirst(int position) {
        //获取当前日期
        Calendar calendar = Calendar.getInstance();
        //设置月
        calendar.add(Calendar.MONTH, position);
        //设置日
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        //        LogUtil.d("DateUtils", format.format(calendar.getTime()));
        return format.format(calendar.getTime());
    }

    /**
     * 获取指定月的最后一天
     *
     * @param position 0为当月,正数为+,负数为减
     */
    public static String monthLast(int position) {
        Calendar calendar = Calendar.getInstance();
        //设置月
        calendar.add(Calendar.MONTH, position);
        //设置日
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        //        LogUtil.d("DateUtils", format.format(calendar.getTime()));
        return format.format(calendar.getTime());
    }

    /**
     * 获取指定时间与当前时间相差的月份
     *
     * @param date
     * @return
     */
    public static int monthGap(String date) {
        int nowMonth = nowMonth();
        int nowYear = nowYear();
        int dateMonth = getDateMonth(date);
        int dateYear = getDateYear(date);
        if (dateYear == nowYear) {
            // 在同一年份
            return dateMonth - nowMonth;
        } else if (dateYear > nowYear) {
            // 指定年份比当前年份大
            return (dateYear - nowYear - 1) * 12 + (12 - nowMonth) + dateMonth;
        } else {
            // 指定年份比当前年份小
            return (nowYear - dateYear - 1) * 12 + (12 - dateMonth) + nowMonth;
        }
    }

//    public static SpannableString nowPeriod(Context context, int period) {
//        String tempDate = context.getString(R.string.mifi_flow_pay_date, DateUtils.nowTime(), DateUtils.monthLast(period - 1));
//        SpannableString spDate = new SpannableString(tempDate);
//        spDate.setSpan(new ForegroundColorSpan(Color.parseColor("#5aafee")), "购买后有效期 : ".length(), tempDate.length(), Spanned.SPAN_COMPOSING);
//        return spDate;
//    }
//
//    public static SpannableString nextPeriod(Context context, int period) {
//        String tempDate = context.getString(R.string.mifi_flow_pay_date, DateUtils.monthFirst(1), DateUtils.monthLast(period));
//        SpannableString spDate = new SpannableString(tempDate);
//        spDate.setSpan(new ForegroundColorSpan(Color.parseColor("#5aafee")), "购买后有效期 : ".length(), tempDate.length(), Spanned.SPAN_COMPOSING);
//        return spDate;
//    }

    // 获取当前时间,自定义format格式 yyyy-MM-dd
    public static String nowTime() {
        Calendar calendar = Calendar.getInstance();
        //        LogUtil.d("DateUtils", format.format(calendar.getTime()));
        return format.format(calendar.getTime());
    }

    public static int nowMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int nowYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getDateMonth(String date) {
        long l = Long.parseLong(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(l));
        // 这里要注意，月份是从0开始。
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getDateYear(String date) {
        long l = Long.parseLong(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(l));
        // 获取年
        return calendar.get(Calendar.YEAR);
    }

    // 获取当前时间,自定义format格式 yyyy-MM-dd HH:mm:ss
    public static String nowTimeDetail() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        //        LogUtil.d("DateUtils", format.format(calendar.getTime()));
        return format.format(calendar.getTime());
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(date);
        return str;
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToStr2(long date) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        String str = format.format(date * 1000);
        return str;
    }

    /**
     * eg:2016-7-12 转化为 2016年7月12日
     *
     * @param date
     * @return
     */
    public static String DateToStr3(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            return format1.format(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 日期转换成字符串 yyyy-MM-dd
     *
     * @param date
     * @return str
     */
    public static String DateToStr4(long date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(date);
        return str;
    }

    /**
     * 日期转换成字符串 yyyy年MM月dd日
     *
     * @return str
     */
    public static String nowTimeDetail2() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 E",Locale.CHINA);
        String str = format.format(System.currentTimeMillis());
        return str;
    }

    /**
     * 日期转换成字符串 HH:mm
     *
     * @return str
     */
    public static String nowTimeDetail3() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String str = format.format(System.currentTimeMillis());
        return str;
    }

    //    /**
    //     * 字符串转换成日期
    //     *
    //     * @param str
    //     * @return date
    //     */
    //    public static long StrToDate(String str) {
    //        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    //        java.util.Date date = null;
    //        try {
    //            date = format.parse(str);
    //        } catch (ParseException e) {
    //            e.printStackTrace();
    //        }
    //        return date.getTime();
    //    }
    private final static String HOUR_FORMAT = "%02d:%02d:%02d";

    public static int getCurrentDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static String durationToStr(int length) {
        int hour = length / 3600;
        int minute = length / 60 % 60;
        int second = length % 60;
        if (hour < 0) {
            return "";
        }
        return String.format(Locale.US, HOUR_FORMAT, hour, minute, second);
    }

    public static String dirationToStrCN(int length) {
        String temp = durationToStr(length);
        String[] split = temp.split(":");
        String hour = split[0].equals("00") ? "" : Integer.parseInt(split[0]) + "小时";
        String minute = split[1].equals("00") ? "" : Integer.parseInt(split[1]) + "分钟";
        String second = split[2].equals("00") ? "" : Integer.parseInt(split[2]) + "秒";
        return hour + minute + second;
    }


    /**
     * 判断2个日期大小
     *
     * @return 0: 一致, 1: 第一个时间大, 2: 第二个时间大
     */
    public static int compareWithDate(String firstTime, String secondTime) {
        String[] firstTimeArray = firstTime.split("-");
        String[] secondTimeArray = secondTime.split("-");
        //比较 年份
        if (Integer.parseInt(firstTimeArray[0]) > Integer.parseInt(secondTimeArray[0])) {
            return 1;
        } else if (Integer.parseInt(firstTimeArray[0]) < Integer.parseInt(secondTimeArray[0])) {
            return 2;
        } else {
            //年份相同,比较月份
            if (Integer.parseInt(firstTimeArray[1]) > Integer.parseInt(secondTimeArray[1])) {
                return 1;
            } else if (Integer.parseInt(firstTimeArray[0]) < Integer.parseInt(secondTimeArray[0])) {
                return 2;
            } else {
                //年份,月份相同,比较日子
                if (Integer.parseInt(firstTimeArray[2]) > Integer.parseInt(secondTimeArray[2])) {
                    return 1;
                } else if (Integer.parseInt(firstTimeArray[2]) < Integer.parseInt(secondTimeArray[2])) {
                    return 2;
                } else {
                    return 0;
                }

            }
        }
    }
}
