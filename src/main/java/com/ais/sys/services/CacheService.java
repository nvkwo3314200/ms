package com.ais.sys.services;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ais.sys.utils.JsonUtils;
import com.ais.sys.utils.RedisUtil;

//@Service("cacheService")
// use RedisCacheAspect deal cache
@Deprecated
public class CacheService {
	private static final Logger _logger = LoggerFactory.getLogger(CacheService.class);
	
	private int expireSecond = 7*24*3600;
	
	private String saveType = "json";
	
	public void put(String sym, Object o, Object value) {
		_logger.debug("put {}, {}, {} ", sym, o, value);
		String key = null;
		if(saveType.equals("json")) {
			key = sym + '-' + JsonUtils.object2Json(o);
			RedisUtil.set(key, JsonUtils.object2Json(value), expireSecond);
		} else {
			key = sym + "-" + o.toString();
			RedisUtil.set(serizlize(key), serizlize((Serializable)value), expireSecond);
		}
		RedisUtil.sadd(sym, key, expireSecond);
	}
	
	
	
	public Object get(String sym, Object o, Class clazz) {
		String key = null; 
		_logger.debug("get from cache key : {}", key);
		if(saveType.equals("json")) {
			key = sym + '-' + JsonUtils.object2Json(o);
			String value = RedisUtil.get(key);
			return JsonUtils.json2Object(value, clazz);
		} else {
			key = sym + "-" + o.toString();
			return deserialize(RedisUtil.get(serizlize(key)));
		}
	}
	
	public void del(String sym) {
		Set<String> set = RedisUtil.smembers(sym);
		if(set != null) {
			set.forEach(item -> {
				_logger.debug("delete key {} ", item);
				if(saveType.equals("json")) {
					RedisUtil.remove(item);
				} else {
					RedisUtil.remove(serizlize(item));
				}
			});
		}
	}
	
	/*
     * 序列化
     * */
    private static byte[] serizlize(Serializable object){
    	if(object == null) return null;
        return SerializationUtils.serialize(object);
    }
    /*
     * 反序列化
     * */
    private static Object deserialize(byte[] bytes){
    	if(bytes == null) return null;
    	return SerializationUtils.deserialize(bytes);
    }

}
