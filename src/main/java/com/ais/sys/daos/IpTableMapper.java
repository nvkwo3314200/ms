package com.ais.sys.daos;

import com.ais.sys.models.IpTable;

public interface IpTableMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IpTable record);

    int insertSelective(IpTable record);

    IpTable selectByPrimaryKey(Integer id);
    
    IpTable selectByPrimaryIp(String ip);

    int updateByPrimaryKeySelective(IpTable record);

    int updateByPrimaryKey(IpTable record);
}