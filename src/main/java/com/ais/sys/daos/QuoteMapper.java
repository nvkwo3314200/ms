package com.ais.sys.daos;

import com.ais.sys.models.Quote;

import java.util.List;

public interface QuoteMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Quote record);

    int insertSelective(Quote record);

    Quote selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Quote record);

    int updateByPrimaryKey(Quote record);

    List<Quote> selectByModel(Quote quote);

    Quote selectRandomOne();

}