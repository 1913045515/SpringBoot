package com.example.demo.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_SLASH_PATTERN = "yyyy/MM/dd HH:mm:ss";
    public static final String TIME_PATTERN = "HH:mm:ss";


    public static String format(long millis, String pattern) {
        return format(new Date(millis), pattern);
    }

    public static String format(Date date, String pattern) {
        DateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public static String formartTimestamp(Date date) {
        return format(date, "yyyyMMdd");
    }

    public static String formartDate(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String formartTime(Date date) {
        return format(date, TIME_PATTERN);
    }

    /**
     * 格式化时间"yyyy-MM-dd HH:mm:ss"
     *
     * @param date
     * @return
     */
    public static String formartDateTime(Date date) {
        return format(date, DATE_TIME_PATTERN);
    }

    public static String formatCurrent(String pattern) {
        return format(new Date(), pattern);
    }

    public static String formartCurrentDate() {
        return format(new Date(), DATE_PATTERN);
    }

    public static String formartCurrentTime() {
        return format(new Date(), TIME_PATTERN);
    }

    /**
     * 格式化时间("yyyy/MM/dd HH:mm:ss")
     *
     * @param date 时间
     * @return
     */
    public static String formartMongoDate(Date date) {
        return format(date, DATE_TIME_SLASH_PATTERN);
    }

    public static String formartCurrentDateTime() {
        return format(new Date(), DATE_TIME_PATTERN);
    }

    public static Date getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    public static Date getNow() {
        return new Date();
    }

    public static Date getTheDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    public static int compareDate(Date start, Date end) {
        if ((start == null) && (end == null)) {
            return 0;
        }
        if (end == null) {
            return 1;
        }
        if (start == null) {
            start = new Date();
        }
        start = getTheDate(start);
        end = getTheDate(end);
        return start.compareTo(end);
    }

    public static Date parse(String dateString, String pattern) {
        if (pattern.contains(":start")) {
            pattern = pattern.replace(":start", TIME_PATTERN);
            dateString = dateString + " 00:00:00";
        }
        if (pattern.contains(":end")) {
            pattern = pattern.replace(":end", TIME_PATTERN);
            dateString = dateString + " 23:59:59";
        }
        DateFormat formatter = new SimpleDateFormat(pattern);
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date addYears(Date date, int amount) {
        return add(date, 1, amount);
    }

    public static Date addMonths(Date date, int amount) {
        return add(date, 2, amount);
    }

    public static Date addWeeks(Date date, int amount) {
        return add(date, 3, amount);
    }

    public static Date addDays(Date date, int amount) {
        return add(date, 5, amount);
    }

    public static Date addHours(Date date, int amount) {
        return add(date, 11, amount);
    }

    public static Date addMinutes(Date date, int amount) {
        return add(date, 12, amount);
    }

    public static Date addSeconds(Date date, int amount) {
        return add(date, 13, amount);
    }

    public static Date addMilliseconds(Date date, int amount) {
        return add(date, 14, amount);
    }

    private static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("日期对象不允许为null!");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    @SuppressWarnings("deprecation")
    public static String getCostDate(long time) {
        Date now = new Date();
        Date pass = new Date(time);
        long temp = now.getTime() - time;

        if (now.getYear() == pass.getYear()) {
            // 同一年
            if (now.getMonth() == pass.getMonth()) {
                // 同一月

                if (now.getDate() == pass.getDate()) {
                    // 同一天

                    if (temp < 1000 * 60 * 60 * 6) {
                        // 6小时内
                        if (temp < 1000 * 60 * 60) {

                            if (temp < 1000 * 60) {
                                return "刚刚";
                            } else {
                                // 1小时内
                                return (temp / 1000 / 60) + "分钟前";
                            }

                        } else {
                            // 1小时到6小时间
                            return (temp / 1000 / 60 / 60) + "小时前";
                        }

                    } else {
                        return new SimpleDateFormat("HH:mm", Locale.CHINA).format(time);
                    }

                } else if (now.getDate() - 1 == pass.getDate()) {
                    // 前一天
                    return new SimpleDateFormat("昨天 HH:mm", Locale.CHINA).format(time);
                } else if (now.getDate() - 2 == pass.getDate()) {
                    return new SimpleDateFormat("前天 HH:mm", Locale.CHINA).format(time);
                } else {
                    return (pass.getMonth() + 1) + "月" + pass.getDate() + "日 "
                            + new SimpleDateFormat("HH:mm", Locale.CHINA).format(time);
                }
            } else {
                return (pass.getMonth() + 1) + "月" + pass.getDate() + "日 "
                        + new SimpleDateFormat("HH:mm", Locale.CHINA).format(time);
            }

        } else {
            return (pass.getYear() + 1900) + "年" + (pass.getMonth() + 1) + "月" + pass.getDate() + "日 "
                    + new SimpleDateFormat("HH:mm", Locale.CHINA).format(time);
        }

    }

    /**
     * Timestamp 转 Date
     *
     * @param timestamp
     * @return
     */
    public static Date getDateByTimeStamp(Timestamp timestamp) {
        return new Date(timestamp.getTime());
    }

    /**
     * Date 转 Timestamp
     *
     * @param date
     * @return
     */
    public static Timestamp getTimeStampByDate(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * Long 转 Date
     *
     * @param longTime
     * @return
     */
    public static Date getDateByLong(Long longTime) {
        return new Date(longTime);
    }

    /**
     * Date 转 Long
     *
     * @param date
     * @return
     */
    public static Long getLongByDate(Date date) {
        return date.getTime();
    }

    public static String formatDateByPattern(Date date,String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }


    public static String formatCron (Date date,String min) {
        String dateFormat="ss mm HH dd MM ? yyyy";
        String beginCron = formatDateByPattern(date,dateFormat);
        String beginTmp = beginCron.substring(0,5);
        String midTmp =  beginTmp+"/"+min;
        String lastTmp = beginCron.substring(5,beginCron.length());
        return midTmp +""+lastTmp;

    }

    public static Date getDateAfter(Date d,int day){
        Calendar now =Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE,now.get(Calendar.DATE)+day);
        return now.getTime();
        }

    /**
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/");
        String str = "2012/02/";
        Date date = df.parse(str);
        Map<String, Object> map = getFirstdayLastdayMonth(date);
        System.out.println("一个月前第一天：" + map.get("first"));
        System.out.println("一个月前最后一天：" + map.get("last"));
    }

    /**
     * 某一个月第一天和最后一天
     * @param date
     * @return
     */
    public static Map<String, Object> getFirstdayLastdayMonth(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date theDate = calendar.getTime();
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("first", gcLast.getTime());
        map.put("last", calendar.getTime());
        return map;
    }

    /**
     * 当月第一天
     * @return
     */
    public static String getFirstDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        return str.toString();

    }

    /**
     * 当月最后一天
     * @return
     */
    public static String getLastDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        String s = df.format(theDate);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
        String day_last = df.format(calendar.getTime());
        StringBuffer endStr = new StringBuffer().append(day_last);
        day_last = endStr.toString();
        StringBuffer str = new StringBuffer().append(day_last);
        return str.toString();
    }
}
