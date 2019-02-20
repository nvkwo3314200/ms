package com.ais.sys.services;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ais.sys.component.ThreadPoolComponent;
import com.ais.sys.models.IpTable;

@Service
public class RequestService {
	private static final Logger logger = LoggerFactory.getLogger(RequestService.class);
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	IpTableService ipTableService;
	
	@Autowired
	ThreadPoolComponent threadPoolComponent;
	
	public String getRealRemoteIp() {
		String ip = null;
		String forwardIps = (String)request.getHeader("x-forwarded-for");
		if(StringUtils.isNotBlank(forwardIps)) {
			ip = forwardIps.split(",")[0];
		}
		if(StringUtils.isBlank(ip)) {
			ip = (String)request.getHeader("x-real-ip");
		}
		if(StringUtils.isBlank(ip)) {
			ip = request.getRemoteAddr();
		}
		if(ip != null) {
			IpTable ipTable = ipTableService.getIpTableByIp(ip);
			if(ipTable == null) {
				final String threadIp = ip;
				threadPoolComponent.exec(()-> ipTableService.saveIpTabelByIp(threadIp));
			}
		}
		logger.info(ip);
		return ip;
	}
}
