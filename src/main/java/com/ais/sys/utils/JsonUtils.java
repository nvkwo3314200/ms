package com.ais.sys.utils;

import java.util.Map;

import net.sf.json.JSONObject;

public class JsonUtils {
	public static Map<String, Object> JsonToMap(String source) {
		JSONObject jsonObject = JSONObject.fromObject(source);
		return (Map)jsonObject;
	}
}
