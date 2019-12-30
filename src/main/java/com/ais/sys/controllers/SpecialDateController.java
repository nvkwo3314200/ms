package com.ais.sys.controllers;

import com.ais.sys.models.SpecialDateVo;
import com.ais.sys.services.SpecialDateService;
import com.ais.sys.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "spe")
public class SpecialDateController {

    private static final Logger logger = LoggerFactory.getLogger(SpecialDateController.class);

    @Autowired
    SpecialDateService specialDateService;

    @GetMapping("get")
    public SpecialDateVo get(@RequestParam String datestr) {
        try {
            Date date = DateUtil.parseDateOnly(datestr);
            return specialDateService.get(date);
        }catch (Exception e) {
            logger.error("{}", e);
        }
        return null;
    }
}
