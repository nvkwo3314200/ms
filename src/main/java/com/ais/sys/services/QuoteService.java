package com.ais.sys.services;

import com.ais.sys.daos.QuoteMapper;
import com.ais.sys.models.Quote;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class QuoteService {

    @Resource
    QuoteMapper quoteMapper;

    public void insert(Quote quote) {
        quoteMapper.insert(quote);
    }

    public Quote findOne(Quote quote) {
        List<Quote> quoteList = quoteMapper.selectByModel(quote);
        if(CollectionUtils.isNotEmpty(quoteList)) {
            return quoteList.get(0);
        }
        return null;
    }

    public Quote findRandomOne() {
        return quoteMapper.selectRandomOne();
    }


}
