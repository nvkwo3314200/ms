package com.ais.sys.daos;

import com.ais.sys.models.OneNoteTask;

import java.util.List;

public interface OneNoteTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OneNoteTask record);

    int insertSelective(OneNoteTask record);

    OneNoteTask selectByPrimaryKey(Integer id);

    List<OneNoteTask> selectByModel(OneNoteTask record);

    int updateByPrimaryKeySelective(OneNoteTask record);

    int updateByPrimaryKey(OneNoteTask record);
}