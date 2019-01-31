package com.ais.sys.utils;

import java.text.DecimalFormat;

public final class NumberUtil {

	private static final String defaultPattern = "#.#";
	
	private NumberUtil() {}
	
	public static String format(Number number) {
		return format(number, defaultPattern);
	}
	
	public static String format(Number number, String pattern) {
		DecimalFormat df = new DecimalFormat();
		df.applyPattern(pattern == null ? defaultPattern : pattern);
		return df.format(number);
	}
}
