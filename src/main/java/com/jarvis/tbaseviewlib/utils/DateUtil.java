/**
 * 版权：Hillsun Cloud Commerce Technology Co. Ltd<p>
 * 作者: jarvisT<p>
 * 创建日期:2014-10-20
 */
package com.jarvis.tbaseviewlib.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * <p/>
 * 作者: jarvisT
 * <p/>
 * 创建日期：2014-10-20上午11:28:31
 * <p/>
 * 示例：
 */
@SuppressLint("SimpleDateFormat")
@SuppressWarnings("unused")
public class DateUtil {

    private final String ZERO = "0"; // 请求服务器连接异常
    private final String COMMA = ",";
    private final String COLON = ":";

    /**
     * 单例对象
     */
    private static DateUtil uniqueInstance = null;
    /**
     * 日期格式 yyyy-MM-dd HH:mm:ss
     */
    private final String format24FullWithHLineAndColon = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式 yyyy-MM-dd hh:mm:ss
     */
    private final String format12FullWithHLineAndColon = "yyyy-MM-dd hh:mm:ss";
    /**
     * 日期格式yyyy/MM/dd HH:mm:ss
     */
    private final String format24FullWithSLineAndColon = "yyyy/MM/dd HH:mm:ss";
    /**
     * 日期格式yyyy/MM/dd hh:mm:ss
     */
    private final String format12FullWithSLineAndColon = "yyyy/MM/dd hh:mm:ss";
    /**
     * 日期格式yyyy-MM-dd
     */
    private final String formatNoTimeWithHLine = "yyyy-MM-dd";

    /**
     * 日期格式yyyy/MM/dd
     */
    private final String formatNoTimeWithSLine = "yyyy/MM/dd";

    /**
     * 日期格式HH:mm:ss
     */
    private final String format24NoDateWithColon = "HH:mm:ss";

    /**
     * 日期格式HH:mm:ss
     */
    private final String format12NoDateWithColon = "hh:mm:ss";
    /**
     * 相对时间1分钟内显示字符串
     */
    private final String secondsStr = "1分钟前";
    /**
     * 相对时间小时内显示字符串
     */
    private final String minuteStr = "分钟前";
    /**
     * 相对时间天内显示字符串
     */
    private final String hourStr = "小时前";
    /**
     * 相对时月内显示字符串
     */
    private final String dayStr = "天前";
    /**
     * 相对时间年内显示字符串
     */
    private final String monthStr = "月前";
    /**
     * 相对时间年显示字符串
     */
    private final String yearStr = "年前";

    private final String errorMsg = "刚刚";

    public String getFormat24FullWithHLineAndColon() {
        return format24FullWithHLineAndColon;
    }

    private final long zero = 0L;
    private final long secondsConditions = 60000L;
    private final long minuteConditions = 3599000L;
    private final long hourConditions = 86399L * 1000L;
    private final long dayConditions = 86399L * 1000L * 30L;
    private final long monthConditions = 86399L * 1000L * 365L;

    private DateUtil() {
    }

    /**
     * 功能说明：获取DateUtil单例对象实例 作者: jarvisT 创建日期:2014-10-13 参数：
     *
     * @return 单例对象 示例：DateUtil.getInstance()
     */
    public static DateUtil getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new DateUtil();
        }
        return uniqueInstance;
    }

    /**
     * 功能说明：获取格式化日期字符串yyyy-MM-dd HH:mm:ss
     * <p/>
     * 作者: jarvisT
     * <p/>
     * 创建日期:2014-10-28
     * <p/>
     * 参数：
     *
     * @param date 日期对象
     * @return 日期字符串 示例：getFormat24FullWithHLineAndColon(date)
     */
    public String getFormat24FullWithHLineAndColon(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format24FullWithHLineAndColon);
        if (date != null) {
            return simpleDateFormat.format(date);
        }
        return null;
    }

    /**
     * 功能说明：获取格式化日期字符串yyyy-MM-dd hh:mm:ss
     * <p/>
     * 作者: jarvisT
     * <p/>
     * 创建日期:2014-10-28
     * <p/>
     * 参数：
     *
     * @param date 日期对象
     * @return 日期字符串 示例：getFormat12FullWithHLineAndColon(date)
     */
    public String getFormat12FullWithHLineAndColon(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format12FullWithHLineAndColon);
        if (date != null) {
            return simpleDateFormat.format(date);
        }
        return null;
    }

    /**
     * 功能说明：获取格式化日期字符串yyyy/MM/dd HH:mm:ss
     * <p/>
     * 作者: jarvisT
     * <p/>
     * 创建日期:2014-10-28
     * <p/>
     * 参数：
     *
     * @param date 日期对象
     * @return 日期字符串 示例：getFormat24FullWithSLineAndColon(date)
     */
    public String getFormat24FullWithSLineAndColon(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format24FullWithSLineAndColon);
        if (date != null) {
            return simpleDateFormat.format(date);
        }
        return null;
    }

    /**
     * 功能说明：获取格式化日期字符串yyyy/MM/dd hh:mm:ss
     * <p/>
     * 作者: jarvisT
     * <p/>
     * 创建日期:2014-10-28
     * <p/>
     * 参数：
     *
     * @param date 日期对象
     * @return 日期字符串 示例：getFormat12FullWithSLineAndColon(date)
     */
    public String getFormat12FullWithSLineAndColon(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format12FullWithSLineAndColon);
        if (date != null) {
            return simpleDateFormat.format(date);
        }
        return null;
    }

    /**
     * 功能说明：获取格式化日期字符串yyyy-MM-dd
     * <p/>
     * 作者: jarvisT
     * <p/>
     * 创建日期:2014-10-28
     * <p/>
     * 参数：
     *
     * @param date 日期对象
     * @return 日期字符串 示例：getFormatNoTimeWithHLine(date)
     */
    public String getFormatNoTimeWithHLine(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatNoTimeWithHLine);
        if (date != null) {
            return simpleDateFormat.format(date);
        }
        return null;
    }

    /**
     * 功能说明：获取格式化日期字符串yyyy/MM/dd
     * <p/>
     * 作者: jarvisT
     * <p/>
     * 创建日期:2014-10-28
     * <p/>
     * 参数：
     *
     * @param date 日期对象
     * @return 日期字符串 示例：getFormatNoTimeWithSLine(date)
     */
    public String getFormatNoTimeWithSLine(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatNoTimeWithSLine);
        if (date != null) {
            return simpleDateFormat.format(date);
        }
        return null;
    }

    /**
     * 功能说明：获取格式化日期字符串HH:mm:ss
     * <p/>
     * 作者: jarvisT
     * <p/>
     * 创建日期:2014-10-28
     * <p/>
     * 参数：
     *
     * @param date 日期对象
     * @return 日期字符串 示例：getFormat24NoDateWithColon(date)
     */
    public String getFormat24NoDateWithColon(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format24NoDateWithColon);
        if (date != null) {
            return simpleDateFormat.format(date);
        }
        return null;
    }

    /**
     * 功能说明：获取格式化日期字符串hh:mm:ss
     * <p/>
     * 作者: jarvisT
     * <p/>
     * 创建日期:2014-10-28
     * <p/>
     * 参数：
     *
     * @param date 日期对象
     * @return 日期字符串 示例：getFormat12NoDateWithColon(date)
     */
    public String getFormat12NoDateWithColon(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format12NoDateWithColon);
        if (date != null) {
            return simpleDateFormat.format(date);
        }
        return null;
    }

    /**
     * 功能说明：获取格式化日期字符串
     * <p/>
     * 作者: jarvisT
     * <p/>
     * 创建日期:2014-10-28
     * <p/>
     * 参数：
     *
     * @param date   日期
     * @param format 日期格式
     * @return 日期字符串 示例：getFormat(date,"yyyy-MM-dd")
     */
    public String getFormat(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        if (date != null) {
            if (format != null && format.length() > 0) {
                return simpleDateFormat.format(date);
            }
        }
        return null;
    }

    /**
     * 功能说明：将日期格式字符串转换为Date对象
     * <p/>
     * 作者: jarvisT
     * <p/>
     * 创建日期:2014-10-28
     * <p/>
     * 参数：
     *
     * @param str    日期字符串
     *               <p/>
     * @param format 日期格式的格式
     *               <p/>
     * @return 日期对象
     * @throws ParseException <p/>
     *                        示例：getDate(str)
     */
    public Date getDate(String str, String format) throws ParseException {
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        date = simpleDateFormat.parse(str);
        return date;
    }

    /**
     * 功能说明：获取相对于当前时间字符串
     * <p/>
     * 作者: jarvisT
     * <p/>
     * 创建日期:2014-11-20
     * <p/>
     * 参数：
     *
     * @param dateStr 日期字符串
     * @return 示例：
     */
    public String customFormat(String dateStr) {
        Date nowDate = new Date();
        long nowLong = nowDate.getTime();
        Date date = null;
        try {
            date = getDate(dateStr, format24FullWithHLineAndColon);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long dateLong = date.getTime();
        long resultLog = nowLong - dateLong;
        String tmp = "";
        if (resultLog <= 0L) {
            return errorMsg;
        } else if (zero < resultLog && resultLog <= secondsConditions) {
            return secondsStr;
        } else if (resultLog > secondsConditions && resultLog <= minuteConditions) {
            tmp = toMinutes(resultLog);
            return tmp;
        } else if (resultLog > minuteConditions && resultLog <= hourConditions) {
            tmp = toHour(resultLog);
            return tmp;
        } else if (resultLog > hourConditions && resultLog <= dayConditions) {
            tmp = toDay(resultLog);
            return tmp;
        } else if (resultLog > dayConditions && resultLog <= monthConditions) {
            tmp = toMouth(resultLog);
            return tmp;
        } else if (resultLog > monthConditions) {
            tmp = toYear(resultLog);
            return tmp;
        }
        return tmp;
    }

    public String toMinutes(long time) {
        long minutes = time / (1000L * 60);
        return minutes + minuteStr;
    }

    public String toHour(long time) {
        long hour = time / (1000L * 60 * 60);
        return hour + hourStr;
    }

    public String toDay(long time) {
        long day = time / (1000L * 60 * 60 * 24);
        return day + dayStr;
    }

    public String toMouth(long time) {
        long day = time / (1000L * 60 * 60 * 24 * 30);
        return day + monthStr;
    }

    public String toYear(long time) {
        long day = time / (1000L * 60 * 60 * 24 * 365);
        return day + yearStr;
    }

    public String getHMS(long time) {
        long surplusHour = 0;
        long surplusMinute = 0;
        long surplusSecond = 0;
        StringBuffer sb = new StringBuffer();
        // 剩余的总时间（单位秒）
        long surplusAllTime = time / 1000;
        if (surplusAllTime > 3600) {
            // 换算成剩余的小时数
            surplusHour = surplusAllTime / (60 * 60);
            if (surplusHour < 10) {
                sb.append(ZERO);
                sb.append(String.valueOf(surplusHour));
                sb.append(COLON);
            } else {
                sb.append(String.valueOf(surplusHour));
            }
            sb.append(COLON);

            long tmpTime = surplusAllTime % (60 * 60);
            if (tmpTime > 60) {
                // 换算成剩余的分钟数
                surplusMinute = tmpTime / 60;
                if (surplusMinute < 10) {
                    sb.append(ZERO);
                    sb.append(String.valueOf(surplusHour));
                } else {
                    sb.append(String.valueOf(surplusHour));
                }
                sb.append(COLON);
                // 秒
                tmpTime = tmpTime % 60;
                surplusSecond = tmpTime;
                if (surplusSecond < 10) {
                    sb.append(ZERO);
                    sb.append(String.valueOf(surplusHour));
                } else {
                    sb.append(String.valueOf(surplusHour));
                }

            } else {
                // 换算成剩余的分钟数
                surplusMinute = 0;
                sb.append(ZERO);
                sb.append(surplusMinute);
                sb.append(COLON);
                // 秒
                surplusSecond = tmpTime;
                if (surplusSecond < 10) {
                    sb.append(ZERO);
                    sb.append(String.valueOf(surplusHour));
                } else {
                    sb.append(String.valueOf(surplusHour));
                }

            }

        } else if (surplusAllTime > 60) {
            sb.append(ZERO);
            sb.append(ZERO);
            sb.append(COLON);
            // 换算成剩余的分钟数
            surplusMinute = surplusAllTime / 60;
            // 秒
            if (surplusMinute < 10) {
                sb.append(ZERO);
                sb.append(String.valueOf(surplusMinute));
            } else {
                sb.append(String.valueOf(surplusMinute));
            }
            // 换算成剩余的秒数
            surplusSecond = surplusAllTime % 60;
            if (surplusSecond < 10) {
                sb.append(ZERO);
                sb.append(String.valueOf(surplusSecond));
            } else {
                sb.append(String.valueOf(surplusSecond));
            }

        } else {
            sb.append(ZERO);
            sb.append(ZERO);
            sb.append(COLON);
            sb.append(ZERO);
            sb.append(ZERO);
            sb.append(COLON);
            if (surplusAllTime < 10) {
                sb.append(ZERO);
                sb.append(String.valueOf(surplusAllTime));
            } else {
                sb.append(String.valueOf(surplusAllTime));
            }

        }
        return sb.toString();
    }

    public String getDHMS(long time) {
        long surplusDay = 0;
        long surplusHour = 0;
        long surplusMinute = 0;
        long surplusSecond = 0;
        StringBuffer sb = new StringBuffer();
        // 剩余的总时间（单位秒）
        long surplusAllTime = time / 1000;

        if (surplusAllTime > 3600 * 24) {
            //换算成剩余的天数
            surplusDay = surplusAllTime / (60 * 60 * 24);
            if (surplusDay < 10) {
                sb.append(ZERO);
                sb.append(String.valueOf(surplusDay));
            } else {
                sb.append(String.valueOf(surplusDay));
            }
            sb.append("天");


            // 换算成剩余的小时数
            long temTime = surplusAllTime % (60 * 60 * 24);
            surplusHour = temTime / (60 * 60);
            if (surplusHour < 10) {
                sb.append(ZERO);
                sb.append(String.valueOf(surplusHour));
            } else {
                sb.append(String.valueOf(surplusHour));
            }
            sb.append(COLON);

            long tmpTime = surplusAllTime % (60 * 60);
            if (tmpTime > 60) {
                // 换算成剩余的分钟数
                surplusMinute = tmpTime / 60;
                if (surplusMinute < 10) {
                    sb.append(ZERO);
                    sb.append(String.valueOf(surplusMinute));
                } else {
                    sb.append(String.valueOf(surplusMinute));
                }
                sb.append(COLON);
                // 秒
                tmpTime = tmpTime % 60;
                surplusSecond = tmpTime;
                if (surplusSecond < 10) {
                    sb.append(ZERO);
                    sb.append(String.valueOf(surplusSecond));
                } else {
                    sb.append(String.valueOf(surplusSecond));
                }

            } else {
                // 换算成剩余的分钟数
                surplusMinute = 0;
                sb.append(ZERO);
                sb.append(surplusMinute);
                sb.append(COLON);
                // 秒
                surplusSecond = tmpTime;
                if (surplusSecond < 10) {
                    sb.append(ZERO);
                    sb.append(String.valueOf(surplusSecond));
                } else {
                    sb.append(String.valueOf(surplusSecond));
                }

            }


        } else if (surplusAllTime > 3600) {
            // 换算成剩余的小时数
            surplusHour = surplusAllTime / (60 * 60);
            if (surplusHour < 10) {
                sb.append(ZERO);
                sb.append(String.valueOf(surplusHour));
            } else {
                sb.append(String.valueOf(surplusHour));
            }
            sb.append(COLON);

            long tmpTime = surplusAllTime % (60 * 60);
            if (tmpTime > 60) {
                // 换算成剩余的分钟数
                surplusMinute = tmpTime / 60;
                if (surplusMinute < 10) {
                    sb.append(ZERO);
                    sb.append(String.valueOf(surplusMinute));
                } else {
                    sb.append(String.valueOf(surplusMinute));
                }
                sb.append(COLON);
                // 秒
                tmpTime = tmpTime % 60;
                surplusSecond = tmpTime;
                if (surplusSecond < 10) {
                    sb.append(ZERO);
                    sb.append(String.valueOf(surplusSecond));
                } else {
                    sb.append(String.valueOf(surplusSecond));
                }

            } else {
                // 换算成剩余的分钟数
                surplusMinute = 0;
                sb.append(ZERO);
                sb.append(surplusMinute);
                sb.append(COLON);
                // 秒
                surplusSecond = tmpTime;
                if (surplusSecond < 10) {
                    sb.append(ZERO);
                    sb.append(String.valueOf(surplusSecond));
                } else {
                    sb.append(String.valueOf(surplusSecond));
                }

            }

        } else if (surplusAllTime > 60) {
            sb.append(ZERO);
            sb.append(ZERO);
            sb.append(COLON);
            // 换算成剩余的分钟数
            surplusMinute = surplusAllTime / 60;
            // 秒
            if (surplusMinute < 10) {
                sb.append(ZERO);
                sb.append(String.valueOf(surplusMinute));
            } else {
                sb.append(String.valueOf(surplusMinute));
            }
            // 换算成剩余的秒数
            surplusSecond = surplusAllTime % 60;
            if (surplusSecond < 10) {
                sb.append(ZERO);
                sb.append(String.valueOf(surplusSecond));
            } else {
                sb.append(String.valueOf(surplusSecond));
            }

        } else {
            sb.append(ZERO);
            sb.append(ZERO);
            sb.append(COLON);
            sb.append(ZERO);
            sb.append(ZERO);
            sb.append(COLON);
            if (surplusAllTime < 10) {
                sb.append(ZERO);
                sb.append(String.valueOf(surplusAllTime));
            }else if(surplusAllTime <=0){
                return "0";
            }else {
                sb.append(String.valueOf(surplusAllTime));
            }
        }
        return sb.toString();
    }

    /**
     * 获得指定日期的前一天
     *
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public String getSpecifiedDayBefore(String specifiedDay, String fomat) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(fomat).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = new SimpleDateFormat(fomat).format(c.getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的后一天
     *
     * @param specifiedDay
     * @return
     */
    public String getSpecifiedDayAfter(String specifiedDay, String fomat) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(fomat).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat(fomat).format(c.getTime());
        return dayAfter;
    }

    /**
     * 功能说明：获得指定日期的后n天
     * <p/>
     * 作者: jarvisT
     * <p/>
     * 创建日期:2014-12-23
     * <p/>
     * 参数：
     *
     * @param specifiedDay 制定日期
     *                     <p/>
     * @param fomat        制定日期格式
     *                     <p/>
     * @param days         间隔的天数
     *                     <p/>
     * @return 示例：
     */
    public String getSpecifiedDayAfter(String specifiedDay, String fomat, int days) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(fomat).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + days);
        String dayAfter = new SimpleDateFormat(fomat).format(c.getTime());
        return dayAfter;
    }

    /**
     * 功能说明：获得当前日期
     * <p/>
     * 作者：jarvisT 创建日期:2014-12-23 参数：
     *
     * @param format 制定日期格式
     * @return 当前日期
     */
    public String getCurrentDate(String format) {
        String currentDate = "";
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(format);
        currentDate = sf.format(date);

        return currentDate;
    }

    /**
     * 功能说明:毫秒转成日期
     *
     * @param millsTime 毫秒
     * @param format    转换后的格式（默认："yyyy-MM-dd"）
     * @return
     * @author 作者：jarvisT
     * @date 创建日期：2015-2-23 上午2:04:01
     */
    public String mills2Date(long millsTime, String format) {
        DateFormat formatter;
        if (TextUtils.isEmpty(format)) {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            formatter = new SimpleDateFormat(format);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millsTime);

        return formatter.format(calendar.getTime());
    }

    /**
     * 功能说明：得到2个时间的时间差
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param day       间隔天数
     * @param format    日期格式(yyyy-MM-dd)
     * @return true 表示超过间隔时间，，false表示未超过间隔时间
     * @author 作者：jarvisT
     * @date 2015-3-3 下午4:05:11
     */
    public boolean twoDateDistance(String startDate, String endDate, int day, String format) {
        Date startDate1 = null;
        Date endDate1 = null;
        if (TextUtils.isEmpty(startDate)||TextUtils.isEmpty(endDate)){
            return false;
        }
        try {
            SimpleDateFormat f = new SimpleDateFormat(format);
            startDate1 = (Date) f.parseObject(startDate);
            endDate1 = (Date) f.parseObject(endDate);
        } catch (Exception e) {
        }

        if (startDate1 == null || endDate1 == null) {
            return false;
        }
        // 日期转毫秒
        long timeLong = endDate1.getTime() - startDate1.getTime();
        BigDecimal a = BigDecimal.valueOf(day).multiply(BigDecimal.valueOf(24)).multiply(BigDecimal.valueOf(3600000));// 计算间隔时间(单位毫秒)
        if (timeLong < 0 || BigDecimal.valueOf(timeLong).compareTo(a) == 1) {
            // 表示超过间隔时间
            return true;
        }
        return false;
    }

    /**
     * 日期转毫秒
     *
     * @param date   日期
     * @param format 日期格式
     * @return 返回日期的毫秒
     */
    public long dateToLong(String date, String format) {
        Date newDate = null;
        try {
            SimpleDateFormat f = new SimpleDateFormat(format);
            newDate = (Date) f.parseObject(date);
        } catch (Exception e) {
        }

        if (newDate == null) {
            return 0;
        }
        // 日期转毫秒
        long timeLong = newDate.getTime();
        return timeLong;
    }

}
