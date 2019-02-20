package com.ais.sys.utils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtils {
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);
	private static Properties prop;
	
	static {
		prop = new Properties();
		try {
			prop.load(PropertiesUtils.class.getClassLoader().getResourceAsStream("config/system.properties"));
			logger.info("properties load ok");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	public static String getValue(String key) {
		return prop.getProperty(key);
	}
	
	public static String getValue(String key, String... args) {
		String result = prop.getProperty(key);
		return MessageFormat.format(result, args);
	}
	
	public static void main(String[] args) {
		System.out.println(getValue("imageHost"));
	}
}
