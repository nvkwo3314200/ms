package com.ais.sys.utils;

public class PropertiesUtils {
	public static String getValue(String key, String... args) {
		return PropertiesFileUtil.getInstance("config/system").get(key);
	}
}
