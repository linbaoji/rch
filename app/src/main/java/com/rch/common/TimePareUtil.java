package com.rch.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/20.
 */

public class TimePareUtil {

    /** 初始化Calendar */
    public static Calendar calendar() {
        Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        return cal;
    }
    // --------------------------------------时间的一些转换-----------------------------------------
    /**
     * 获取当前时间
     *
     * @param Format
     *            时间格式，如：yyyy-MM-dd HH:mm:ss yyyy年MM月dd日 HH:mm:ss yyyy年MM月dd日
     *            hh时mm分ss秒 yyyy-MM-dd EE HH-mm-ss MM-dd HH:mm:ss yyyy-MM-dd
     *            HH-mm-ss-SSS …… 当传入的格式不正确 会抛出非法参数异常
     * @return
     */
    public static String getCurrentTime(String Format) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(Format);
        String time = df.format(date);
        return time;
    }



    /**
     * 获取当前Date
     *
     * @param Format
     *            时间格式，如：yyyy-MM-dd HH:mm:ss yyyy年MM月dd日 HH:mm:ss yyyy年MM月dd日
     *            hh时mm分ss秒 yyyy-MM-dd EE HH-mm-ss MM-dd HH:mm:ss yyyy-MM-dd
     *            HH-mm-ss-SSS …… 当传入的格式不正确 会抛出非法参数异常
     * @return
     */
    public static Date getCurrentDate(String Format) {
        return getDateTimeForTime(Format, getCurrentTime(Format));
    }


    /**
     * 将时间转换成Date类型
     *
     * @param Format
     *            要转换时间的格式，如：yyyy-MM-dd HH:mm
     * @param time
     *            要转换的时间，格式必须是第一个参数，如："2016-5-10 21:08"
     * @return 转换后的Date类型的时间，如果返回null，则转换失败
     */
    public static Date getDateTimeForTime(String Format, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat(Format);
        // sdf.applyPattern(timeFormat);
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 将Date类型挫转换成String时间
     *
     * @param date
     *            需要转换的Date类型的时间
     * @param Format
     * @return
     */
    public static String getTimeForDate(String Format, Date date) {
        SimpleDateFormat df = new SimpleDateFormat(Format);
        String time = df.format(date);
        return time;
    }

    /**
     * 将时间挫转换成时间
     *
     * @param lose
     *            需要转换的long类型的时间挫
     * @param Format
     * @return
     */
    public static String getTimeForLose(String Format, long lose) {
        SimpleDateFormat df = new SimpleDateFormat(Format);
        String time = df.format(lose);
        return time;
    }


    /**
     * 将时间转换成时间挫
     *
     * @param Format
     *            要转换时间的格式，如：yyyy-MM-dd HH:mm
     * @param time
     *            要转换的时间，格式必须是第一个参数，如："2016-5-10 21:08"
     * @return 转换后的long类型的时间，如果返回0，则转换失败
     */
    public static long getLoseForTime(String Format, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat(Format);
        Date dt;
        try {
            dt = sdf.parse(time);
            return dt.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * Calendar转化为Date
     * @return
     */
    public static Date getDateForCalendar(){
        Calendar cal=Calendar.getInstance();
        Date date=cal.getTime();
        return date;
    }

    /**
     * Calendar转化为Date
     * @return
     */
    public static Calendar getCalendarForDate(Date date){
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }












    // --------------------------------------获取 日、星期、月、季度、年
    // 相关方法---------------------------------------------

    /** 获得当前日期，其实就是当前月中的第几天 */
    public static int getCurrentDay() {
        // return calendar().get(Calendar.DAY_OF_MONTH);一样
        return calendar().get(Calendar.DATE);
    }

    /** 获得当前是星期几 */
    public static int getCurrentWeek() {
        return calendar().get(Calendar.DAY_OF_WEEK) - 1;
    }

    /** 获得当前月份 */
    public static int getCurrentMonth() {
        return calendar().get(Calendar.MONTH) ;
    }

    /** 获得当前季度 */
    public static String getCurrentQur() {
        Date date = new Date();
        int x = date.getMonth() + 1;
        String strTime = getTimeForDate("yyyy", date);
        Date year = getDateTimeForTime("yyyy", strTime);
        String time;
        if (x < 4) {
            time = getTimeForDate("yyyy", date) + "-Q1";
            // time = getTimeForDate("yyyy", date) + "第一季度";
        } else if (x < 7) {
            time = getTimeForDate("yyyy", date) + "-Q2";
        } else if (x < 10) {
            time = getTimeForDate("yyyy", date) + "-Q3";
        } else {
            time = getTimeForDate("yyyy", date) + "-Q4";
        }
        return time;
    }

    /** 获得当前年份 */
    public static int getCurrentYear() {
        return calendar().get(Calendar.YEAR);
    }

    /** 获得传入时间的日期 */
    public static int getDay(Date date) {
        Calendar c = calendar();
        c.setTime(date);
        return c.get(Calendar.DATE);
    }

    /** 获得传入时间是星期几 */
    public static int getWeek(Date date) {
        Calendar c = calendar();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /** 获得传入时间的月份 */
    public static int getMonth(Date date) {
        Calendar c = calendar();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /** 获得传入时间的季度 */
    public static String getQur(Date date) {
        int x = date.getMonth() + 1;
        String strTime = getTimeForDate("yyyy", date);
        Date year = getDateTimeForTime("yyyy", strTime);
        String time;
        if (x < 4) {
            time = getTimeForDate("yyyy", date) + "-Q1";
            // time = getTimeForDate("yyyy", date) + "第一季度";
        } else if (x < 7) {
            time = getTimeForDate("yyyy", date) + "-Q2";
        } else if (x < 10) {
            time = getTimeForDate("yyyy", date) + "-Q3";
        } else {
            time = getTimeForDate("yyyy", date) + "-Q4";
        }
        return time;
    }

    /** 获得传入时间的年份 */
    public static int getYear(Date date) {
        Calendar c = calendar();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }


    /**
     * 返回前一天日期的long值
     *
     * @param longTime
     *            参照日期
     * @return 前一天日期的long值
     */
    public static long lastDay(long longTime) {
        Calendar cal = calendar();
        Date date = new Date(longTime);
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return cal.getTime().getTime();
    }


    /**
     * 返回前一月日期的long值
     *
     * @param longTime
     *            参照日期
     * @return 前一月日期的long值
     */
    public static long lastMonth(long longTime) {
        Calendar cal = calendar();
        Date date = new Date(longTime);
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime().getTime();
    }


    /**
     * 返回前一季度日期
     *
     * @param longTime
     *            参照日期
     * @return 前一季度日期的long值
     */
    public static long lastQur(long longTime) {
        Calendar cal = calendar();
        Date date = new Date(longTime);
        cal.setTime(date);
        cal.add(Calendar.MONTH, -3);
        return cal.getTime().getTime();
    }

    /**
     * 返回前一年日期的long值
     *
     * @param longTime
     *            参照日期
     * @return 前一年日期的long值
     */
    public static long lastYear(long longTime) {
        Calendar cal = calendar();
        Date date = new Date(longTime);
        cal.setTime(date);
        cal.add(Calendar.YEAR, -1);
        return cal.getTime().getTime();
    }

    /**
     * 返回下一天日期的long值
     *
     * @param longTime
     *            参照日期
     * @return 下一天日期的long值
     */
    public static long nextDay(long longTime) {
        Calendar cal = calendar();
        Date date = new Date(longTime);
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return cal.getTime().getTime();
    }

    /**
     * 返回下一月日期的long值
     *
     * @param longTime
     *            参照日期
     * @return 下一月日期的long值
     */
    public static long nextMonth(long longTime) {
        Calendar cal = calendar();
        Date date = new Date(longTime);
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime().getTime();
    }

    /**
     * 返回下一季度日期
     *
     * @param longTime
     *            参照日期
     * @return 下一季度日期的long值
     */
    public static long nextQur(long longTime) {
        Calendar cal = calendar();
        Date date = new Date(longTime);
        cal.setTime(date);
        cal.add(Calendar.MONTH, 3);
        return cal.getTime().getTime();
    }

    /**
     * 返回下一年日期的long值
     *
     * @param longTime
     *            参照日期
     * @return 下一年日期的long值
     */
    public static long nextYear(long longTime) {
        Calendar cal = calendar();
        Date date = new Date(longTime);
        cal.setTime(date);
        cal.add(Calendar.YEAR, 1);
        return cal.getTime().getTime();
    }

    /** 今天是年中的第几天 */
    public static int dayOfYear() {
        return calendar().get(Calendar.DAY_OF_YEAR);
    }



    // ---------------------------------------------日期之间的一些计算 和
    // 比较---------------------------------------

    /**
     * 判断某月有多少天
     *
     * @param
     * @return 天数
     */
    public static int getMonthHaveDay(long longTime) {
        Calendar cal = calendar();
        Date date = new Date(longTime);
        cal.setTime(date);
        int dateOfMonth = cal.getActualMaximum(Calendar.DATE);
        return dateOfMonth;
    }


    /** 判断原日期是否在目标日期之前 */
    public static boolean isBefore(Date src, Date dst) {
        return src.before(dst);
    }

    /** 判断原日期是否在目标日期之后 */
    public static boolean isAfter(Date src, Date dst) {
        return src.after(dst);
    }


    /** 判断两日期是否相同 */
    public static boolean isEqual(Date date1, Date date2) {
        return date1.compareTo(date2) == 0;
    }

    /**
     * 判断某个日期是否在某个日期范围
     *
     * @param beginDate
     *            日期范围开始
     * @param endDate
     *            日期范围结束
     * @param src
     *            需要判断的日期
     * @return
     */
    public static boolean between(Date beginDate, Date endDate, Date src) {
        return beginDate.before(src) && endDate.after(src);
    }

    /**
     * 判断某日是否为当月
     *
     * @param longTime
     *            某日的long值
     * @return true为是
     */
    public static boolean orCurrentMonth(long longTime) {
        // if (getTimeForLongTime("yyyy-MM",
        // longTime).equals(getTimeForLongTime("yyyy-MM",
        // System.currentTimeMillis()))) {
        // return true;
        // } else {
        // return false;
        // }
        return getTimeForLose("yyyy-MM", longTime)
                .equals(getTimeForLose("yyyy-MM", System.currentTimeMillis()));
    }


    /**
     * 输入一个已经过去的时间，计算出与当前时间的间隔
     *
     * @param outTime
     * @return
     */
    public static String timeGap(long outTime) {
        long currentTime = System.currentTimeMillis();
        long timeGap = currentTime - outTime; // 与现在时间相差秒数
        String timeStr = null;
        if (timeGap > 1000 * 60 * 60 * 24) { // 1天以上
            timeStr = timeGap / (1000 * 60 * 60 * 24) + "天前";
        } else if (timeGap > 1000 * 60 * 60) { // 1小时-24小时
            timeStr = timeGap / (1000 * 60 * 60) + "小时前";
        } else if (timeGap > 1000 * 60) { // 1分钟-59分钟
            timeStr = timeGap / (1000 * 60) + "分钟前";
        } else { // 1秒钟-59秒钟
            // timeStr = "刚刚";
            timeStr = timeGap / 1000 + "秒前";
        }
        return timeStr;
    }

    /**
     * 获得当前月的最后一天 HH:mm:ss为23:59:59，毫秒为999
     */
    public static Date getCurrentMonthOfLastDay() {
        Calendar cal = calendar();
        cal.set(Calendar.DAY_OF_MONTH, 1); // 转变为当前月的第一天，例如2016-08-01
        cal.add(Calendar.MONTH, 1); // 转变为下个月的第一天，例如2016-09-01
        cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
        cal.set(Calendar.MINUTE, 0);// m置零
        cal.set(Calendar.SECOND, 0);// s置零
        cal.set(Calendar.MILLISECOND, 0);// S置零
        cal.set(Calendar.MILLISECOND, -1);// 毫秒-1，下个月第一天，毫秒减一，即为当前月的最后一天的最后一毫秒。例如2016-08-31
        // 23:59:59 999
        return cal.getTime();
    }


    /**
     * 获得当前月的第一天 HH:mm:ss为00:00:00，毫秒为000
     */
    public static Date getCurrentMonthOfFirstDay() {
        Calendar cal = calendar();
        cal.set(Calendar.DAY_OF_MONTH, 1); // M月置1
        cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
        cal.set(Calendar.MINUTE, 0);// m置零
        cal.set(Calendar.SECOND, 0);// s置零
        cal.set(Calendar.MILLISECOND, 0);// S置零
        return cal.getTime();
    }

    /** 把一个以秒为单位的数值，格式化为分种小时天 */
    public static String parseSecond(int second) {
        if (second >= 60 * 60 * 24) {
            return second / (60 * 60 * 24) + "天";
        } else if (second >= 60 * 60) {
            return second / (60 * 60) + "时";
        } else if (second >= 60) {
            return second / 60 + "分";
        } else {
            return second + "秒";
        }
    }

    /**
     * 获得天数差
     *
     * @param begin
     * @param end
     * @return
     */
    public static long getDayDiff(Date begin, Date end) {
        long day = 1;
        if (end.getTime() < begin.getTime()) {
            day = -1;
        } else if (end.getTime() == begin.getTime()) {
            day = 1;
        } else {
            day += (end.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000);
        }
        // return day;
        return day - 1;
    }
    // -----------------------------------------------------------------------------
    private static Date weekDay(int week) {
        Calendar cal = calendar();
        cal.set(Calendar.DAY_OF_WEEK, week);
        return cal.getTime();
    }

    /**
     * 获得本周五的日期 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     */
    public static Date friday() {
        return weekDay(Calendar.FRIDAY);
    }

    /**
     * 获得本周六的日期 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     */
    public static Date saturday() {
        return weekDay(Calendar.SATURDAY);
    }

    /**
     * 获得本周日的日期 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     */
    public static Date sunday() {
        return weekDay(Calendar.SUNDAY);
    }

    /**
     * 获取某年某月的第一天
     */
    public static String getFisrtDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, date.getYear() + 1900);
        // 设置月份
        cal.set(Calendar.MONTH, date.getMonth());
        // 获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfMonth = sdf.format(cal.getTime());
        return firstDayOfMonth;
    }

    /**
     * 获取某年月的最后一天日期,对当前月做了相关处理
     */
    public static String getLastDayOfMonth(Date date) {
        boolean isCurrentMonth = orCurrentMonth(getLoseForTime("yyyy-MM-dd", getTimeForDate("yyyy-MM-dd", date)));
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, date.getYear() + 1900);
        // 设置月份
        cal.set(Calendar.MONTH, date.getMonth());
        // 获取某月最大天数
        int lastDay = isCurrentMonth ? getCurrentDay() : cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }

    /**
     * 获取某年月的最后一天日期,对当前月未做处理
     */
    public static String getLastDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, date.getYear() + 1900);
        // 设置月份
        cal.set(Calendar.MONTH, date.getMonth());
        // 获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }

    /**
     * 获取某年月的最后一天日期
     */
    public static String getFirstAndLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, date.getYear() + 1900);
        // 设置月份
        cal.set(Calendar.MONTH, date.getMonth());
        // 获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/");
        String strYearAndMonth = sdf.format(cal.getTime());
        String strDate = strYearAndMonth + "01-" + String.valueOf(lastDay);
        return strDate;
    }

    /**
     * 获取每月时间段：M + W
     *
     * @param date
     * @return
     */
    public static List<String> getTimePeriod(Date date) {
        List<String> timePeriodList = new ArrayList<String>();
        timePeriodList.add("M");
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, date.getYear() + 1900);
        // 设置月份
        cal.set(Calendar.MONTH, date.getMonth());

        cal.setFirstDayOfWeek(Calendar.MONDAY);

        int weekNum = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
        for (int i = 1; i <= weekNum; i++) {
            timePeriodList.add("W" + i);
        }
        return timePeriodList;
    }

    /**
     * 获取每月时间段长度：M + Number(W)
     *
     * @param date
     * @return
     */
    public static int getPeriodNum(Date date) {
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, date.getYear() + 1900);
        // 设置月份
        cal.set(Calendar.MONTH, date.getMonth());

        cal.setFirstDayOfWeek(Calendar.MONDAY);
        return cal.getActualMaximum(Calendar.WEEK_OF_MONTH) + 1;
    }

    /**
     * 获取某月每周开始结束日期（每周开始日期为星期一）
     *
     * @param date
     * @param weekMaps
     */
    public static void getWeekPeriod(Date date, Map<String, Map<String, String>> weekMaps) {
        if (null != weekMaps) {
            weekMaps.clear();
        }
        // 日期“年月”格式化
        date = getDateTimeForTime("yyyy-MM", getTimeForDate("yyyy-MM", date));
        String strDate = getTimeForDate("yyyy-MM", date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weekNum = 0;
        for (int i = 1; i <= dayOfMonth; i++) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date curDate = dateFormat.parse(strDate + "-" + i);
                cal.clear();
                cal.setTime(curDate);
                int dayOfWeek = new Integer(cal.get(Calendar.DAY_OF_WEEK));
                // 若当天是周日
                if (dayOfWeek == 1) {
                    Map<String, String> weekMap = new HashMap<String, String>();
                    weekNum++;
                    if (i - 6 <= 1) {
                        weekMap.put("start", strDate + "-0" + 1);
                    } else {
                        weekMap.put("start", strDate + ((i - 6 < 10) ? "-0" : "-") + (i - 6));
                    }
                    weekMap.put("end", strDate + ((i < 10) ? "-0" : "-") + i);
                    weekMaps.put(String.valueOf(weekNum), weekMap);
                }
                // 若是本月最后一天，且不是周日
                if (dayOfWeek != 1 && i == dayOfMonth) {
                    weekNum++;
                    Map<String, String> weekMap = new HashMap<String, String>();
                    weekMap.put("start", strDate + ((i - dayOfWeek + 2 < 10) ? "-0" : "-") + (i - dayOfWeek + 2));
                    weekMap.put("end", strDate + ((i < 10) ? "-0" : "-") + i);
                    weekMaps.put(String.valueOf(weekNum), weekMap);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 组拼星期周期
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getWeekPeriodString(String startTime, String endTime) {
        startTime = startTime.replace('-', '/');
        endTime = endTime.split("-")[2];
        return startTime + "-" + endTime;
    }






}
