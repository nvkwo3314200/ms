package com.ais.sys.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ais.sys.models.IpTable;

import net.sf.json.JSONObject;

public class HttpUtils {
	private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	private static final String M_CODE = "code";
	private static final String M_DATA = "data";
	private static final String M_COUNTRY = "country";
	private static final String M_AREA = "area";
	private static final String M_REGION = "region";
	private static final String M_CITY = "city";
	private static final String M_COUNTY = "county";
	private static final String M_ISP = "isp";
	private static final String M_ISP_ID = "isp_id";
	
	public static String doGet(String path, String method){
		StringBuffer strBuf = new StringBuffer();
		try {
            URL url = new URL(path);
            InputStream in =url.openStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader bufr = new BufferedReader(isr);
            String str;
            while ((str = bufr.readLine()) != null) {
            	strBuf.append(str);
            }
            bufr.close();
            isr.close();
            in.close();
        } catch (Exception e) {
        	logger.error(e.getMessage());
        }
		return strBuf.toString();
	}
	
	public static IpTable getIpTableByIp(String ip) {
		IpTable ipTable = null;
		String url = PropertiesUtils.getValue(ConstantUtil.QUERY_IP_URL, ip);
		String result = doGet(url, "GET");
		Map<String, Object> map = (Map<String, Object>) JsonUtils.JsonToMap(result);
		if(map != null && (Integer)map.get(M_CODE) == 0) {
			Map<String, String> dataMap = (Map<String, String>)map.get(M_DATA);
			ipTable = (IpTable) JSONObject.toBean((JSONObject)dataMap, IpTable.class);
		}
		return ipTable;
	}
	
	public static void main(String[] args) {
		
	}
}
