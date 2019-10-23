package com.ais.sys.security;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.ais.sys.utils.AESUtil;
 
/**
 * 继承PropertyPlaceholderConfigurer定义支持密文版属性的属性配置器
 * 
 * 
 */
public class EncryptPropertyPlaceholderConfigurer extends
        PropertyPlaceholderConfigurer {
    private String[] encryptPropNames = { "master.redis.password", "master.redis.port" };
 
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (isEncryptProp(propertyName)) {
            String decryptValue = AESUtil.aesDecode(propertyValue);
            System.out.println(propertyName + "解密内容:" + decryptValue);
            return decryptValue;
        } else {
            return propertyValue;
        }
    }
 
    /**
     * 判断是否是加密的属性
     * 
     * @param propertyName
     * @return
     */
    private boolean isEncryptProp(String propertyName) {
        for (String encryptpropertyName : encryptPropNames) {
            if (encryptpropertyName.equals(propertyName))
                return true;
        }
        return false;
    }
}