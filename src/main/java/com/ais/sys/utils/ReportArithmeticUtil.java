package com.ais.sys.utils;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

public final class ReportArithmeticUtil {
	
	private static final String ZERO_STRING = "0";
	
	private static final int defaultScale = 6;
	
	private static final int defaultRoundingMode = BigDecimal.ROUND_HALF_UP;
	
	private ReportArithmeticUtil() {}
	
	public static BigDecimal add(String a, String b, int scale, int roundingMode) {
		if(StringUtils.isEmpty(a)) {
			a = ZERO_STRING;
		}
		if(StringUtils.isEmpty(b)) {
			b = ZERO_STRING;
		}

		BigDecimal c = new BigDecimal(a);
		BigDecimal d = new BigDecimal(b);
		return c.add(d).setScale(scale, roundingMode);
	}
	
	public static BigDecimal add(String a, String b) {
		return add(a, b, defaultScale, defaultRoundingMode);
	}
	
	public static BigDecimal subtract(String a, String b, int scale, int roundingMode) {
		if(StringUtils.isEmpty(a)) {
			a = ZERO_STRING;
		}
		if(StringUtils.isEmpty(b)) {
			b = ZERO_STRING;
		}
		
		BigDecimal c = new BigDecimal(a);
		BigDecimal d = new BigDecimal(b);
		return c.subtract(d).setScale(scale, roundingMode);
	}
	
	public static BigDecimal subtract(String a, String b) {
		return subtract(a, b, defaultScale, defaultRoundingMode);
	}
	
	public static BigDecimal multiply(String a, String b, int scale, int roundingMode) {
		if(StringUtils.isEmpty(a)) {
			a = ZERO_STRING;
		}
		if(StringUtils.isEmpty(b)) {
			b = ZERO_STRING;
		}
		
		BigDecimal c = new BigDecimal(a);
		BigDecimal d = new BigDecimal(b);
		return c.multiply(d).setScale(scale, roundingMode);
	}
	
	public static BigDecimal multiply(String a, String b) {
		return multiply(a, b, defaultScale, defaultRoundingMode);
	}
	
	public static BigDecimal divide(String a, String b, int scale, int roundingMode) {
		if(StringUtils.isEmpty(a)) {
			a = ZERO_STRING;
		}
		if(StringUtils.isEmpty(b)) {
			b = ZERO_STRING;
		}
		
		BigDecimal c = new BigDecimal(a);
		BigDecimal d = new BigDecimal(b);
		if(BigDecimal.ZERO.compareTo(d) == 0) {
			d = null;// help gc
			d = BigDecimal.ONE;
		}
		return c.divide(d, scale, roundingMode);
	}
	
	public static BigDecimal divide(String a, String b) {
		return divide(a, b, defaultScale, defaultRoundingMode);
	}
	
	public static String toString(Integer a) {
		if(a == null) a = 0;
		return Integer.toString(a);
	}
	
	public static String toString(Double a) {
		if(a == null) a = 0d;
		return Double.toString(a);
	}
	
	public static String toString(Float a) {
		if(a == null) a = 0f;
		return Float.toString(a);
	}
	
	public static String toString(Long a) {
		if(a == null) a = 0l;
		return Long.toString(a);
	}
	
	public static String toString(Byte a) {
		if(a == null) a = 0;
		return Byte.toString(a);
	}
}
