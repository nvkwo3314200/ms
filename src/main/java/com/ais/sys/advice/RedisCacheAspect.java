package com.ais.sys.advice;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.ais.sys.cache.RedisCache;
import com.ais.sys.cache.RedisEvict;
import com.ais.sys.utils.JsonUtils;

@Aspect
@Component
@SuppressWarnings(value = {"rawtypes", "unchecked"})
public class RedisCacheAspect {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheAspect.class);
    /**
     * 分隔符 生成key 格式为 类全类名|方法名|参数所属类全类名
     **/
    private static final String DELIMITER = "|";
    
    private Integer expireSecond = 10 * 60;
    
    /**
     * spring-redis.xml配置连接池、连接工厂、Redis模板
     **/
    @Autowired
    @Qualifier("redisTemplateForString")
    StringRedisTemplate srt;

    /**
     * Service层切点 使用到了我们定义的 RedisCache 作为切点表达式。
     * 而且我们可以看出此表达式基于 annotation。
     * 并且用于内建属性为查询的方法之上
     */
    @Pointcut("@annotation(com.ais.sys.cache.RedisCache)")
    public void redisCacheAspect() {
    }

    /**
     * Service层切点 使用到了我们定义的 RedisEvict 作为切点表达式。
     * 而且我们可以看出此表达式是基于 annotation 的。
     * 并且用于内建属性为非查询的方法之上，用于更新表
     */
    @Pointcut("@annotation(com.ais.sys.cache.RedisEvict)")
    public void redisCacheEvict() {
    }

    @Around("redisCacheAspect()")
    public Object cache(ProceedingJoinPoint joinPoint) {
        // 得到类名、方法名和参数
        String clazzName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // 根据类名、方法名和参数生成Key
        logger.debug("key参数: {}.{}",clazzName, methodName);
        //System.out.println("key参数: " + clazzName + "." + methodName);
        String key = getKey(clazzName, methodName, args);
        
        logger.debug("生成key: {} " , key);

        // 得到被代理的方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        // 得到被代理的方法上的注解
        Class modelType = method.getAnnotation(RedisCache.class).type();

        // 检查Redis中是否有缓存
        String value = (String) srt.opsForHash().get(modelType.getName(), key);

        // 得到被代理方法的返回值类型
        Class returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType();

        // result是方法的最终返回结果
        Object result = null;
        try {
            if (null == value) {
            	
            	if (logger.isInfoEnabled()) {
                    logger.info("缓存未命中");
                }

                // 调用数据库查询方法
                result = joinPoint.proceed(args);

                // 序列化查询结果
                String json = JsonUtils.object2Json(result);
                //String json = GsonUtil.toJson(result);
                //System.out.println("打印："+json);

                // 序列化结果放入缓存
                srt.opsForHash().put(modelType.getName(), key, json);
                srt.expire(modelType.getName(), expireSecond, TimeUnit.SECONDS);
                logger.debug("设置缓存时间 {} 秒", expireSecond);
            } else {

                // 缓存命中
                if (logger.isInfoEnabled()) {
                    logger.info("缓存命中, value = {} " + value);
                }

                result = value;
                // 反序列化 从缓存中拿到的json字符串
                result = JsonUtils.json2Object(value, returnType);
                //result = GsonUtil.fromJson(value,returnType);
                System.out.println(result.toString());

                if (logger.isInfoEnabled()) {
                    logger.debug("gson反序列化结果 = {} " , result);
                }
            }
        } catch (Throwable e) {
            logger.error("解析异常",e);
        }
        return result;
    }

    /**
     *      * 在方法调用前清除缓存，然后调用业务方法
     *      * @param joinPoint
     *      * @return
     *      * @throws Throwable
     *      
     */
    @Around("redisCacheEvict()")
    public Object evictCache(ProceedingJoinPoint joinPoint) throws Throwable {
        // 得到被代理的方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 得到被代理的方法上的注解
        Class modelType = method.getAnnotation(RedisEvict.class).type();
        if (logger.isInfoEnabled()) {
            logger.info("清空缓存 = " + modelType.getName());
        }
        // 清除对应缓存
        srt.delete(modelType.getName());
        return joinPoint.proceed(joinPoint.getArgs());
    }

    /**
     * @param json
     * @param clazz
     * @param modelType
     * @return 反序列化json字符串
     * Question 遇到问题，如何将复杂json字符串解析为复杂java object
     */
    private Object deserialize(String json, Class clazz, Class modelType) {
        // 序列化结果是List对象
        if (clazz.isAssignableFrom(List.class)) {
            return JsonUtils.json2Object(json, modelType);
        }
        // 序列化结果是普通对象
        return JsonUtils.json2Object(json, clazz);
    }

    private String serialize(Object result, Class clazz) {
        return JsonUtils.object2Json(result);
    }

    /**
     *      * 根据类名、方法名和参数生成Key
     *      * @param clazzName
     *      * @param methodName
     *      * @param args
     *      * @return key格式：全类名|方法名｜参数类型
     *      
     */
    private String getKey(String clazzName, String methodName, Object[] args) {
        StringBuilder key = new StringBuilder(clazzName);
        key.append(DELIMITER);
        key.append(methodName);
        key.append(DELIMITER);

        for (Object obj : args) {
        	String json = JsonUtils.object2Json(obj);
            key.append(json);
            key.append(DELIMITER);
        }

        return key.toString();
    }

	public Integer getExpireSecond() {
		return expireSecond;
	}

	public void setExpireSecond(Integer expireSecond) {
		this.expireSecond = expireSecond;
	}
    
    
}