package com.ais.sys.daos;

import com.ais.sys.models.SpecialDate;

public interface SpecialDateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SpecialDate record);

    int insertSelective(SpecialDate record);

    SpecialDate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SpecialDate record);

    int updateByPrimaryKey(SpecialDate record);
}