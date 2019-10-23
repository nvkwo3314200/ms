package com.ais.sys.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtils {
	private static final Logger _logger = LoggerFactory.getLogger(JsonUtils.class);
		
	public static ObjectMapper mapper = new ObjectMapper();

	static {
	    // 转换为格式化的json
	    mapper.enable(SerializationFeature.INDENT_OUTPUT);

	    // 如果json中有新增的字段并且是实体类类中不存在的，不报错
	    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    // null 不参加序列化
	    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}
	
	public static String object2Json(Object o) {
		if(o == null) return null;
		try {
			return mapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			_logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public static Object json2Object(String o, Class c) {
		if(o == null) return null;
		try {
			return mapper.readValue(o, c);
		} catch (IOException e) {
			_logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static void main(String[] args) {
		String o = "{" +
				"    \"total\": 5," +
				"    \"result\": [" +
				"        {" +
				"            \"famous_name\": \"朱自清\"," +
				"            \"famous_saying\": \"学习文学而懒于记诵是不成的，特别是诗。一个高中文科的学生，与其囫囵吞枣或走马观花地读十部诗集，不如仔仔细细地背诵三百首诗。\"" +
				"        }," +
				"        {" +
				"            \"famous_name\": \"朱自清\"," +
				"            \"famous_saying\": \"洗手的时候，日子从水盆里过去；吃饭的时候，日子从饭碗里过去；默默时，便从凝然的双眼过去。我觉察他去得匆匆了，伸出手遮挽时，他又从遮挽着的手边过去。天黑时，我躺在床上，他便伶伶俐俐地从我身边跨过，从我脚边飞去了。等我睁开眼和太阳再见，这算又溜走了一日，我掩着面叹息。但是新来的日子的影儿又开始在叹息里闪过了。\"" +
				"        }," +
				"        {" +
				"            \"famous_name\": \"朱自清\"," +
				"            \"famous_saying\": \"洗手的时候，日子从水盒进而过去；吃饭的时候，日子从饭碗里过去。我觉察他去的匆匆了，伸出手遮挽时，他又从遮挽着的手边过去；天黑时，我躺在床上，他便伶伶俐俐地从我身上跨过，从我的脚边飞去了。等我睁开眼和太阳再见，这算又溜走了一日。我掩面叹息。但是新来的日子的影子又开始在叹息里闪过了。\"" +
				"        }," +
				"        {" +
				"            \"famous_name\": \"朱自清\"," +
				"            \"famous_saying\": \"从此我不再仰脸看青天，不再低头看白水，只谨慎着我双双的脚步，我要一不一不踏在泥土上，打上深深的脚印！\"" +
				"        }," +
				"        {" +
				"            \"famous_name\": \"朱自清\"," +
				"            \"famous_saying\": \"从此我不再仰脸看青天，不再低头看白水，只谨慎着我双双的脚步，我要一步一步踏在泥土上，打上深深的脚印！\"" +
				"        }" +
				"    ]," +
				"    \"error_code\": 0," +
				"    \"reason\": \"Succes\"" +
				"}";
		Map<String, Object> map = (Map<String, Object>) json2Object(o, Map.class);
		System.out.println(map);
		System.out.println(map.get("total"));
		System.out.println(map.get("error_code"));
		List<Map<String, String>> resultList = (List<Map<String, String>>) map.get("result");
		resultList.forEach(item -> {
			System.out.println(item.get("famous_name") + " ==== " + item.get("famous_saying"));
		});

	}
}
