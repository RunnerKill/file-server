package cn.sowell.file.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {

    private String format = "yyyy-MM-dd HH:mm:ss";

    public DateFormatUtil() {
        super();
    }

    public DateFormatUtil(String format) {
        super();
        this.format = format;
    }

    // 字符串形式的时间转化为Long形。
    public Long toLong(String dateTime) {
        SimpleDateFormat sdf = null;
        sdf = new SimpleDateFormat(format);
        Date date = null;
        if(dateTime != null) {
            try {
                date = sdf.parse(dateTime);
            } catch(ParseException e) {
                return 0l;
            }
        }
        return new Long(date.getTime());
    }

    // 把Long形的时间转化为string形式的。
    public String toString(Long dateTime) {
        String str = "";
        Date date = new Date();
        if(dateTime != null) {
            date.setTime(dateTime.longValue());
        }
        SimpleDateFormat sdf = null;
        sdf = new SimpleDateFormat(format);
        str = sdf.format(date);
        return str;
    }

    // 把String 类型的时间转化为Date类型的时间。
    public Date toDate(String date) {
        Date d = null;
        if(date != null && !date.equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                d = sdf.parse(date);
            } catch(ParseException e) {
                return null;
            }
        }
        return d;
    }

    // 把date类型的时间转化为String类型。
    public String toString(Date date) {
        String d = null;
        if(date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            d = sdf.format(date);
        }
        return d;
    }

    // 获取时间的格式信息
    public String getDateFormatMsg(String date) {
        if(date != null && !"".equals(date)) {
            date = date.trim();
            String reg1 = "[0-9]{4}[-]{1}[0-9]{1,2}[-]{1}[0-9]{1,2}";
            if(date.matches(reg1)) {
                return "yyyy-MM-dd";
            }
            String reg2 = "[0-9]{4}[-]{1}[0-9]{1,2}[-]{1}[0-9]{1,2}[\\s]+[0-9]{1,2}[:]{1}[0-9]{1,2}";
            if(date.matches(reg2)) {
                return "yyyy-MM-dd HH:mm";
            }
            String reg3 = "[0-9]{4}[-]{1}[0-9]{1,2}[-]{1}[0-9]{1,2}[\\s]+[0-9]{1,2}[:]{1}[0-9]{1,2}[:]{1}[0-9]{1,2}";
            if(date.matches(reg3)) {
                return "yyyy-MM-dd HH:mm:ss";
            }
        }
        return null;
    }

    public Long addDay(Long date, Integer addCounts) {
        if(date != null && format != null) {
            date += addCounts * 24 * 60 * 60 * 1000;
        }
        return date;
    }

    public String addDay(String date, Integer addCounts) {
        return toString(addDay(toLong(date), addCounts));
    }

    public Date addDay(Date date, Integer addCounts) {
        return new Date(addDay(date.getTime(), addCounts));
    }

    public Long getMillisAtDay(String time) {
        String[] times = time.split(":");
        int hour = Integer.parseInt(times[0]);
        int minute = Integer.parseInt(times[1]);
        int second = Integer.parseInt(times[2]);
        Long mills = (long)(hour * 60 * 60 + minute * 60 + second) * 1000;
        return mills;
    }

    public String getTimeAtDay(Long startTime) {
        int totalSecond = (int)(startTime / 1000);
        int hour = totalSecond / 3600;
        int minute = (totalSecond - (hour * 3600)) / 60;
        int second = totalSecond - (hour * 3600) - (minute * 60);
        String hourStr = hour < 10 ? "0" + hour : hour + "";
        String minuteStr = minute < 10 ? "0" + minute : minute + "";
        String secondStr = second < 10 ? "0" + second : second + "";
        return hourStr + ":" + minuteStr + ":" + secondStr;
    }

}
