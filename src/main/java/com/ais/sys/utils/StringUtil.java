package com.ais.sys.utils;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringUtil {

	private static final Log LOG = LogFactory.getLog(StringUtil.class);
	private static SecureRandom random = new SecureRandom();

	private StringUtil(){
		
	}
		
	public static String repalceAllNewLineToBr(String str){
		if(StringUtils.isEmpty(str)){
			return str;
		}
		return str.replaceAll("\n", "<br/>");

		
	}

	public static String repalceAllBrToNewLine(String str){
		if(StringUtils.isEmpty(str)){
			return str;
		}
		return str.replaceAll("<br/>", "\n");

		
	}
	
	public static boolean checkItemNameEn(String itemNameEn){  	 
		
		String regExp ="^[A-Z0-9 `~!@#$%^&*()_+-={}\\:;'<>?./]+$";  
		  
		Pattern pattern = Pattern.compile(regExp);  
		   
	    return pattern.matcher(itemNameEn).matches();  
		
	 } 


	public static String[] getAllProductId(String pIds) {
		String[] ids = null;
		if(StringUtils.isNotEmpty(pIds)){
			if(pIds.indexOf(",") != -1){
				ids = pIds.split(",");
			}else{
				ids = new String[]{pIds};
			}
		}
		return ids;
	}
	
	 public static boolean checkEmail(String email){
	        boolean flag = false;
	        try{
	                String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	                Pattern regex = Pattern.compile(check);
	                Matcher matcher = regex.matcher(email);
	                flag = matcher.matches();
	            }catch(Exception e){
	                flag = false;
	                LOG.error(e.getMessage(), e);
	            }
	        return flag;
	    }
	 
	 public static boolean checkStringEquals(String str1,String str2){
		 
		 
		 if(StringUtils.isEmpty(str1) && StringUtils.isEmpty(str2))
		 {
			 return false;
		 }else if(StringUtils.isEmpty(str1) && StringUtils.isNotEmpty(str2)){
			 return true;
		 }else if(StringUtils.isNotEmpty(str1) && StringUtils.isEmpty(str2)){
			 return true;
		 }else if(StringUtils.isNotEmpty(str1)&&StringUtils.isNotEmpty(str2)&&!str1.equals(str2)){
			 return true;
		 }
		 return false;
	 }
	 
	public static boolean checkBigDecimalEquals(BigDecimal a, BigDecimal b) {

		if (a == null && b == null) {
			return false;
		} else if (a == null) {
			return true;
		} else if (b == null) {
			return true;
		} else if (a.compareTo(b) != 0) {
			return true;
		}
		
		return false;
	}
	//TJL
	public static boolean cheackNonNegativeNumber8_2(String number) {
		 String check = "^(\\d{1,6})?(\\.\\d{0,2})?$";
         Pattern regex = Pattern.compile(check);
         Matcher matcher = regex.matcher(number);
         return matcher.matches();
	}
	//TJL 2016/8/19
	public static boolean checkNonNegativeNumber8(String number) {
		String check = "^(\\d{1,8})$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(number);
        return matcher.matches();
	}
	
	public static int getRandomNumber(int n) {
		random.setSeed(new Date().getTime());
		return random.nextInt(n);
	}
	
	//TJL 2016/9/11 "  " -> true
	public static boolean isTrimEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	//TJL 2016/9/11 "  " -> false
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
}
