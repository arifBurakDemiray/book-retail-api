package com.bookretail.util;

import org.thymeleaf.util.DateUtils;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    /**
     * Compares that date1 is included in range date2 and date3
     *
     * @param date1 is the subject
     * @param date2 start range
     * @param date3 end range
     * @return true if date1 is included
     */
    public static boolean compareHourAndMinuteIncluded(Date date1, Date date2, Date date3) {
        if (DateUtils.hour(date1) >= DateUtils.hour(date2) ||
                DateUtils.minute(date1) >= DateUtils.minute(date2)) {
            return DateUtils.hour(date1) <= DateUtils.hour(date3) ||
                    DateUtils.minute(date1) <= DateUtils.minute(date3);
        }
        return false;
    }

    /**
     * Compares date1 and day that date1's weekday is same as day
     *
     * @param date subject 1
     * @param day  day of week
     * @return true if day names are same
     */
    public static boolean isSameWeekDay(Date date, DayOfWeek day) {
        return day.name().equals(DateUtils.dayOfWeekName(date, Locale.US).toUpperCase(Locale.US));
    }

    /**
     * Compares date1 and date2 that they are same day
     *
     * @param date1 subject 1
     * @param date2 subject 2
     * @return true if they are same days
     */
    public static boolean isSameDay(Date date1, Date date2) {
        return DateUtils.day(date1).equals(DateUtils.day(date2)) &&
                DateUtils.month(date1).equals(DateUtils.month(date2)) &&
                DateUtils.year(date1).equals(DateUtils.year(date2));
    }

    /**
     * Look given date1 is after then date2 by hour and minute
     *
     * @param date1 first
     * @param date2 second
     * @return boolean result, also checks equality
     */
    public static boolean after(Date date1, Date date2) {
        var hour1 = DateUtils.hour(date1);
        var hour2 = DateUtils.hour(date2);

        if (hour2 > hour1) {
            hour1 += 24;
        }

        return hour1 > hour2 || (hour1.equals(hour2) &&
                DateUtils.minute(date1) >= DateUtils.minute(date2));
    }

    /**
     * Look given date1 is before then date2 by hour and minute
     *
     * @param date1 first
     * @param date2 second
     * @return boolean result, also checks equality
     */
    public static boolean before(Date date1, Date date2) {

        var hour1 = DateUtils.hour(date1);
        var hour2 = DateUtils.hour(date2);

        if (hour1 > hour2) {
            hour2 += 24;
        }

        return hour1 < hour2 || (hour1.equals(hour2) &&
                DateUtils.minute(date1) <= DateUtils.minute(date2));
    }

    public static class HourMinute {

        public long hour;
        public long minute;
        public long day = 0;

        protected HourMinute(long hour, long minute) {
            this.hour = hour;
            this.minute = minute;
        }

        public static HourMinute of(long hour, long minute) {
            return new HourMinute(hour, minute);
        }

        public HourMinute withDay(long day) {
            this.day = day;
            return this;
        }

        @Override
        public String toString() {
            var result = hour + ".";
            if (minute < 10) {
                result += "0";
            }
            return result + minute;

        }
    }

    public static HourMinute subtract(Date date1, Date date2) {

        var diff = date1.getTime() - date2.getTime();
        var dayDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        diff -= dayDiff * 1000 * 86400;
        var hourDiff = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
        diff -= hourDiff * 1000 * 3600;
        var minDiff = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);

        if (hourDiff < 0) {
            hourDiff += 24;
        }
        if (minDiff < 0) {
            minDiff += 60;
        }


        return HourMinute.of(hourDiff, minDiff).withDay(dayDiff);
    }
}
