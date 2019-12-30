package com.ais.sys.daos;

import com.ais.sys.models.CallDate;

import java.util.List;

public interface CallDateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CallDate record);

    int insertSelective(CallDate record);

    CallDate selectByPrimaryKey(Integer id);

    List<CallDate> selectByModel(CallDate callDate);

    int updateByPrimaryKeySelective(CallDate record);

    int updateByPrimaryKey(CallDate record);
}