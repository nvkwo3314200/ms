package com.ais.sys.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ais.sys.daos.IpTableMapper;
import com.ais.sys.models.IpTable;
import com.ais.sys.utils.HttpUtils;

@Service
public class IpTableService {
	@Autowired
	IpTableMapper ipTableMapper;
	
	public IpTable getIpTableByIp(String ip) {
		return ipTableMapper.selectByPrimaryIp(ip);
	}
	
	public void saveIpTable(IpTable ipTable) {
		ipTableMapper.insertSelective(ipTable);
	}
	
	public void saveIpTabelByIp(String ip) {
		IpTable ipTable = HttpUtils.getIpTableByIp(ip);
		if(ipTable != null) {
			saveIpTable(ipTable);
		}
	}
}
