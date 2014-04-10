package com.wcs.tih.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>Project: tih</p>
 * <p>Description: 日期处理工具类</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
public final class DateUtil {
    private DateUtil() {
    }
	
	/**
	 * <p>Description: 相隔日期相差的天数</p>
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public static int daysOfTwo(Date fDate, Date oDate) {
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(fDate);
		int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
		aCalendar.setTime(oDate);
		int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
		return (day2 - day1);
	}
	
	/**
	 * <p>Description: 某个日期是否在一个时间范围内</p>
	 * @param startDatetime
	 * @param endDatetime
	 * @param testDatetime
	 * @return
	 */
	public static boolean dateIsExistDateArea(Date startDatetime, Date endDatetime,Date testDatetime) {
		boolean b=false;
		//在指定时间范围内，不包括边界时间
		if(testDatetime.after(startDatetime) && testDatetime.before(endDatetime)){
			b=true;
		}
		//和开始时间相等
		if(daysOfTwo(startDatetime,testDatetime)==0){
			b=true;
		}
		//和结束时间相等
		if(daysOfTwo(endDatetime,testDatetime)==0){
			b=true;
		}
		return b;
	}
	
    /**
     * <p>Description: 取得某个日期后一个日期</p>
     * @param date 
     * @return
     */
    public static Date getNextDate(Date date) {
        Date nextDate = getNextDate(date, 1);
        return nextDate;
    }
    
    /**
     * <p>Description: 取得某个日期后一个日期</p>
     * @param date 
     * @param amount 
     * @return
     */
    public static Date getNextDate(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, amount);
        Date nextDate = cal.getTime();
        return nextDate;
    }
    /**
     * <p>Description: 取当前日期的形式为：yyyy-MM-dd 00:00:00</p>
     * @param date
     * @return
     */
    public static Date getCurrentDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return df.parse(df.format(date));
        } catch (ParseException e) {
            return date;
        }
    }
}
