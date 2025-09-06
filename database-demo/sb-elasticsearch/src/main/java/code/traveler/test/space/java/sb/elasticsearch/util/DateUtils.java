package code.traveler.test.space.java.sb.elasticsearch.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DateUtils {
    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    public class DatePattern {
        public static final String NORMAL_DATE_PATTERN = "yyyy-MM-dd";
        public static final String NORMAL_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
        public static final String NORMAL_DATE_TIME_PATTERN_STANDARD = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        public static final String NON_SPLIT_DATE_PATTERN = "yyyyMMdd";

        public static final String DATE_LOCAL_ZH = "zh";
        public static final String DATE_TIMEZONE_BEIJING = "GMT+8";
    }



    public DateUtils() {
    }


    public static long mapNormal2Timestamp(Object normal) {
        long timestamp = 0L;
        if (!Objects.isNull(normal)) {
            if (Date.class.equals(normal.getClass())) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime((Date) normal);
                LocalDateTime localDT = LocalDateTime.of(calendar.get(1), calendar.get(2), calendar.get(5), calendar.get(10), calendar.get(12), calendar.get(13));
                timestamp = Timestamp.valueOf(localDT).getTime();
            } else if (LocalDateTime.class.equals(normal.getClass())) {
                timestamp = Timestamp.valueOf((LocalDateTime) normal).getTime();
            } else if (!LocalDate.class.equals(normal.getClass()) && String.class.equals(normal.getClass())) {
                timestamp = Timestamp.valueOf(String.valueOf(normal)).getTime();
            }
        } else {
            timestamp = Timestamp.valueOf(LocalDateTime.now()).getTime();
        }

        return timestamp;
    }

    public static LocalDateTime mapString2LocalDateTime(String stringDate) {
        return LocalDateTime.parse(stringDate, DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE));
    }

    public static String mapLocalDateTime2String(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE));
    }

    public static long mapNow2Timestamp() {
        return Timestamp.valueOf(LocalDateTime.now()).getTime();
    }

    public static String mapDate2Normal(Date now) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(now);
    }

    public static Date mapString2Date(String stringDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;

        try {
            date = sdf.parse(stringDate);
        } catch (ParseException var4) {
            logger.error(var4.getMessage());
        }

        return date;
    }

    public static boolean between(Date target, Date start, Date end) {
        return target.after(start) && target.before(end);
    }

    public static int getDurationOfDays(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isBefore(endTime)) {
            long startTimestamp = mapNormal2Timestamp(startTime);
            long endTimestamp = mapNormal2Timestamp(endTime);
            int durationOfDays = (int) ((endTimestamp - startTimestamp) / 1000L / 3600L / 24L);
            return durationOfDays == 0 ? 1 : durationOfDays + 1;
        } else {
            return -1;
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

    /**
     * @deprecated
     */
    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(calendarField, amount);
            return c.getTime();
        }
    }

    public static String getYesterdayTime() {
        Calendar ca = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDate = new Date();
        ca.setTime(nowDate);
        ca.add(5, -1);
        Date yesterdayDate = ca.getTime();
        String yesterdayTime = sdf.format(yesterdayDate);
        return yesterdayTime;
    }


    /**
     * @description 将yyyy-MM-dd HH:mm:ss格式字符串转化为yyyy-MM-dd格式
     * @author tiger
     * @param dateTimeStr
     * @return java.lang.String
     * @time 2021/7/13 14:04
     */
    public static String mapDateTimeStr2DateStr(String dateTimeStr) {
            SimpleDateFormat sdf = new SimpleDateFormat(DatePattern.NORMAL_DATE_PATTERN);
            String result = "";

            try {
                result = sdf.format(sdf.parse(dateTimeStr));
            } catch (ParseException e) {
                throw new IllegalArgumentException("dateTimeStr must be a valid format parameter, and provided is" + dateTimeStr);
            }
            return result;
    }
}
