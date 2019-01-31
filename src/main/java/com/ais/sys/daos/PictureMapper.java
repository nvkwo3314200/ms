package com.ais.sys.daos;

import java.util.List;

import com.ais.sys.models.Picture;

public interface PictureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Picture record);

    int insertSelective(Picture record);

    Picture selectByPrimaryKey(Integer id);
    
    List<Picture> selectByModel(Picture record);

    int updateByPrimaryKeySelective(Picture record);

    int updateByPrimaryKey(Picture record);
}