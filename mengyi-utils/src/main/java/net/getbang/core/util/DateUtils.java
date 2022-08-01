package net.getbang.core.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * 时间工具类
 * @author yuhaiguang
 */
public class DateUtils extends cn.hutool.core.date.DateUtil {

    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";
    /**
     * 字符串时间
     */
    public static final String YMDHMSA = "yyyy-MM-dd-HH-mm-ss-AAAAAAAAAA";
    /**
     * 缺省的日期显示格式： yyyy-MM-dd
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 用做单号
     */
    public static final String DEFAULT_DATE_FORMAT_ORDER = "yyyyMMdd";

    /**
     * 缺省的日期显示格式： yyyy-MM
     */
    public static final String DEFAULT_YYMM_FORMAT = "yyyy-MM";
    /**
     * 缺省的日期时间显示格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 字符串时间
     */
    public static final String YMDHMS = "yyyyMMddHHmmss";

    public static String getShortDayTime() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(YMDHMS);
        return date.format(fmt);
    }
    /**
     * @Title getLongDayTime
     * @Description 年月日时分秒毫秒 （当天） 此方法支持毫秒
     * @Param []
     * @Return java.lang.String
     * @Date 2019/6/4 15:28
     * @Author QiMing
     */
    public static String getLongDayTime() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(YMDHMSA);
        return date.format(fmt);
    }

}

