package com.hsuyeung.blog.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期相关工具类
 *
 * @author hsuyeung
 * @date 2022/02/24
 */
public final class DateUtil {
    /**
     * 获取指定日期当天的开始时间，比如：2018-01-01 10:00:00 -> 2018-01-01 00:00:00
     *
     * @param date 指定日期
     * @return 指定日期当天的开始时间，类型为 {@link Date}
     */
    public static Date getStartOfDayDate(Date date) {
        LocalDateTime time = fromDateToJava8LocalDate(date);
        LocalDateTime startOfDay = LocalDateTime.of(time.toLocalDate(), LocalTime.MIN);
        return fromJava8LocalDateToDate(startOfDay);
    }

    /**
     * 获取指定日期当前整点时间，比如：2018-01-01 10:20:00 -> 2018-01-01 10:00:00
     *
     * @param date 指定日期
     * @return 指定日期当前整点时间，类型为 {@link Date}
     */
    public static Date getStartOfDayHour(Date date) {
        LocalDateTime time = fromDateToJava8LocalDate(date);
        LocalDateTime todayStart = LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), time.getHour(), 0);
        return fromJava8LocalDateToDate(todayStart);
    }

    /**
     * 获取指定日期当天的开始时间，比如：2018-01-01 10:00:00 -> 2018-01-01 00:00:00
     *
     * @param date 指定日期
     * @return 指定日期当天的开始时间，类型为 {@link LocalDateTime}
     */
    public static LocalDateTime getStartOfDayJava8LocalDateTime(Date date) {
        LocalDateTime time = fromDateToJava8LocalDate(date);
        return LocalDateTime.of(time.toLocalDate(), LocalTime.MIN);
    }

    /**
     * 获取指定日期当年的开始时间，比如：2018-12-25 10:00:00 -> 2018-01-01 00:00:00
     *
     * @param date 指定日期
     * @return 指定日期当年的开始时间，类型为 {@link LocalDateTime}
     */
    public static LocalDateTime getYearStartOfDayJava8LocalDateTime(Date date) {
        LocalDateTime time = fromDateToJava8LocalDate(date);
        return LocalDateTime.of(time.getYear(), 1, 1, 0, 0, 0);
    }

    /**
     * 将指定日期字符串按照指定格式转换为 {@link LocalDateTime} 对象
     *
     * @param content 指定日期字符串
     * @param pattern 指定格式
     * @return 对应的 {@link LocalDateTime} 对象
     */
    public static LocalDateTime parseToGetLocalDateTime(String content, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(content, formatter);
    }

    /**
     * 获取指定日期向后偏移 offset 天后的日期
     *
     * @param date   指定日期
     * @param offset 往后的偏移量
     * @return 偏移后的日期，类型为 {@link Date}
     */
    public static Date getDayOffset(Date date, int offset) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, offset);
        return calendar.getTime();
    }

    /**
     * 获取指定日期当天的结束时间，比如：2018-01-01 10:00:00 -> 2018-01-01 23:59:59
     *
     * @param date 指定日期
     * @return 指定日期当天的结束时间，类型为 {@link Date}
     */
    public static Date getEndOfDayDate(Date date) {
        LocalDateTime time = fromDateToJava8LocalDate(date);
        LocalDateTime todayEnd = LocalDateTime.of(time.toLocalDate(), LocalTime.MAX);
        return fromJava8LocalDateToDate(todayEnd);
    }

    /**
     * 获取指定日期当天的结束时间，比如：2018-01-01 10:00:00 -> 2018-01-01 23:59:59
     *
     * @param date 指定日期
     * @return 指定日期当天的结束时间，类型为 {@link LocalDateTime}
     */
    public static LocalDateTime getEndOfDayDateJava8LocalDateTime(Date date) {
        LocalDateTime time = fromDateToJava8LocalDate(date);
        return LocalDateTime.of(time.toLocalDate(), LocalTime.MAX);
    }

    /**
     * 将指定 {@link LocalDateTime} 类型日期格式化为指定格式的字符串
     *
     * @param localDateTime 指定日期
     * @param pattern       格式
     * @return 格式化后的日期字符串
     */
    public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }

    /**
     * 将系统本地时区的指定日期转换为 UTC 时区日期
     *
     * @param systemDate 系统本地时区的指定日期
     * @return UTC 时区的日期，类型为 {@link Date}
     */
    public static Date systemDateToUtc(Date systemDate) {
        return fromJava8LocalDateToDate(systemLocalDateTimeToUtc(fromDateToJava8LocalDate(systemDate)));
    }

    /**
     * 将 UTC 时区时间转为系统本地时区时间
     *
     * @param utc UTC 时区时间
     * @return 系统本地时区时间，类型为 {@link Date}
     */
    public static Date utcToSystemDate(Date utc) {
        return fromJava8LocalDateToDate(utcToSystemLocalDateTime(fromDateToJava8LocalDate(utc)));
    }

    /**
     * 将系统本地时区的指定日期转换为 UTC 时区日期
     *
     * @param systemLocalDateTime 系统本地时区的指定日期
     * @return UTC 时区的日期，类型为 {@link LocalDateTime}
     */
    public static LocalDateTime systemLocalDateTimeToUtc(LocalDateTime systemLocalDateTime) {
        return systemLocalDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    /**
     * 将 UTC 时区时间转为系统本地时区时间
     *
     * @param utcLocalDateTime UTC 时区时间
     * @return 系统本地时区时间，类型为 {@link LocalDateTime}
     */
    public static LocalDateTime utcToSystemLocalDateTime(LocalDateTime utcLocalDateTime) {
        return utcLocalDateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 将 {@link Date} 类型日期按照指定格式格式化为日期字符串
     *
     * @param date   指定日期
     * @param format 格式化字符串
     * @return 格式化后的日期字符串
     */
    public static String getFormattedDate(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 将 {@link LocalDateTime} 类型转为 {@link Date} 类型对象
     *
     * @param localDateTime {@link LocalDateTime}
     * @return {@link Date}
     */
    public static Date fromJava8LocalDateToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 将毫秒级时间戳转为 {@link LocalDateTime} 对象
     *
     * @param time 毫秒级时间戳
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime fromLongToJava8LocalDate(long time) {
        return Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 将 {@link Date} 对象转为 {@link LocalDateTime} 对象
     *
     * @param date {@link Date}
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime fromDateToJava8LocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * 获取指定 {@link LocalDateTime} 时间的毫秒级时间戳
     *
     * @param localDateTime {@link LocalDateTime}
     * @return 毫秒级时间戳
     */
    public static long getTimeMills(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 计算两个 {@link Date} 相差的时间，，单位根据传入参数决定
     *
     * @param from 开始时间
     * @param to   结束时间
     * @return 相差单位数
     */
    public static long getDiff(Date from, Date to, ChronoUnit unit) {
        LocalDateTime fromTime = fromDateToJava8LocalDate(from);
        LocalDateTime toTime = fromDateToJava8LocalDate(to);
        return fromTime.until(toTime, unit);
    }

    /**
     * 计算两个 {@link LocalDateTime} 相差的时间，单位根据传入参数决定
     *
     * @param from 开始时间
     * @param to   结束时间
     * @return 相差单位数
     */
    public static long getDiff(LocalDateTime from, LocalDateTime to, ChronoUnit unit) {
        return from.until(to, unit);
    }

    /**
     * 美化时间
     *
     * @param time {@link LocalDateTime}
     * @return 返回给定的时间距离当前时间过去了 xx 年/月/周/天/小时/分钟/秒
     */
    public static String beautifyTime(LocalDateTime time) {
        LocalDateTime now = LocalDateTime.now();
        AssertUtil.isTrue(now.isAfter(time), "不能大于当前时间");

        long years = getDiff(time, now, ChronoUnit.YEARS);
        long months = getDiff(time, now, ChronoUnit.MONTHS);
        long weeks = getDiff(time, now, ChronoUnit.WEEKS);
        long days = getDiff(time, now, ChronoUnit.DAYS);
        long hours = getDiff(time, now, ChronoUnit.HOURS);
        long minutes = getDiff(time, now, ChronoUnit.MINUTES);
        long seconds = getDiff(time, now, ChronoUnit.SECONDS);

        if (years > 0) {
            return years == 1 ? "去年" : String.format("%d 年前", years);
        } else if (months > 0) {
            return months == 1 ? "上个月" : String.format("%d 个月前", months);
        } else if (weeks > 0) {
            return weeks == 1 ? "上周" : String.format("%d 周前", weeks);
        } else if (days > 0) {
            return days == 1 ? "昨天" : days == 2 ? "前天" : String.format("%d 天前", days);
        } else if (hours > 0) {
            return String.format("%d 小时前", hours);
        } else if (minutes > 0) {
            return String.format("%d 分钟前", minutes);
        } else if (seconds > 0) {
            return String.format("%d 秒前", seconds);
        } else {
            return "刚刚";
        }
    }

    private DateUtil() {
    }
}
