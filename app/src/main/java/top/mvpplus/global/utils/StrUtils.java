package top.mvpplus.global.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zzh on 2018/7/13.
 */

public class StrUtils {
    /**
     * 时间戳转换日期格式
     */
    public static String timeToDate(long timeMs, String format) {
        SimpleDateFormat f = new SimpleDateFormat(format, Locale.CHINA);
        return f.format(new Date(timeMs));
    }

}
