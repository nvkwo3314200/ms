package com.ais.sys.services;

import com.ais.sys.daos.SpecialDateMapper;
import com.ais.sys.models.SpecialDate;
import com.ais.sys.models.SpecialDateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SpecialDateService {

    @Autowired
    SpecialDateMapper specialDateMapper;

    public SpecialDateVo get(Date date) {
        if(date == null) return null;
        SpecialDate specialDate = new SpecialDate();
        specialDate.setSpecialDate(date);
        SpecialDate speDate = specialDateMapper.select(specialDate);
        if(speDate == null) return null;
        SpecialDateVo dateVo = new SpecialDateVo();
        dateVo.setCode(speDate.getType());
        dateVo.setName(speDate.getName());
        return dateVo;
    }
}
