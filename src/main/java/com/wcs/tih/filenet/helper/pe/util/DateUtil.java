package com.wcs.tih.filenet.helper.pe.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    public static Date parseDate(String date) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            format.setTimeZone(TimeZone.getDefault());
            return format.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("Parse String to Date failed.", e);
        }
    }

    public static Date parseUTCDate(String date) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            return format.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("Parse String to Date failed.", e);
        }
    }

    public static String parseDateToUTCString(Date date) {
        DateFormat format = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        return format.format(getUTCTime(date));
    }

    public static String parseDateToLocalString(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy'��'MM'��'dd'��'HH'ʱ'mm'��'ss'��'");
        return format.format(getLocalTime(date));
    }

    private static Date getUTCTime(Date date) {
        Calendar cal = Calendar.getInstance();
        int offset = cal.get(15);
        return new Date(date.getTime() - offset);
    }

    private static Date getLocalTime(Date date) {
        Calendar cal = Calendar.getInstance();
        int offset = cal.get(15);
        return new Date(date.getTime() + offset);
    }
}