package com.ais.sys.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public final class DateUtil {

	private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private static final String DEFAULT_BETWEEN_DATE_PATTERN = "yyyy-MM-dd";
	private static final String DEFAULT_BETWEEN_DATE_PATTERN2 = "yyyyMMdd";
	
	private DateUtil() {}
	
	public static Date parse(String source) throws ParseException {
		return parse(source, DEFAULT_DATE_PATTERN);
	}

	public static Date parseDateOnly(String source){
		try {
			return parse(source, DEFAULT_BETWEEN_DATE_PATTERN);
		} catch (ParseException e) {
			try {
				return parse(source, DEFAULT_BETWEEN_DATE_PATTERN2);
			} catch (ParseException ex) {
				return null;
			}
		}
	}
	
	public static Date parse(String source, String pattern) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(pattern == null ? DEFAULT_DATE_PATTERN : pattern);
		return sdf.parse(source);
	}
	
	public static String format(Date date) {
		return format(date, DEFAULT_DATE_PATTERN);
	}
	
	public static String format(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(pattern == null ? DEFAULT_DATE_PATTERN : pattern);
		return sdf.format(date);
	}
	
	public static int daysBetween(Date from, Date to) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parse(format(from, DEFAULT_BETWEEN_DATE_PATTERN), DEFAULT_BETWEEN_DATE_PATTERN));
		long fromDateMillis = cal.getTimeInMillis();
		cal.setTime(parse(format(to, DEFAULT_BETWEEN_DATE_PATTERN), DEFAULT_BETWEEN_DATE_PATTERN));
		long toDateMillis = cal.getTimeInMillis();
		return Integer.parseInt(String.valueOf((toDateMillis - fromDateMillis) / (1000 * 3600 * 24)));
	}
	
	public static int workDaysBetween(Date from, Date to) throws ParseException {
		int returnNum = 0;
		Calendar cal = Calendar.getInstance();
		while(to.after(from)){
			if(cal.get(Calendar.DAY_OF_WEEK)!=1 && cal.get(Calendar.DAY_OF_WEEK)!=7){
				returnNum++;
			}
			long dateL1 =  from.getTime();
			dateL1 =  dateL1 + 24 * 60 * 60 * 1000l;
			from.setTime(dateL1);
	        cal.setTime(from); 
		}
		return returnNum;
	}
	
	public static int compareToIgnoreTime(Date oneDate, Date anotherDate) {
		try {
			return parse(format(oneDate, DEFAULT_BETWEEN_DATE_PATTERN), DEFAULT_BETWEEN_DATE_PATTERN)
			.compareTo(parse(format(anotherDate, DEFAULT_BETWEEN_DATE_PATTERN), DEFAULT_BETWEEN_DATE_PATTERN));
		} catch (ParseException e) { // Won't happen
			throw new RuntimeException(e.getMessage());
		}
	}
}
