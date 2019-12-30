package com.ais.sys.controllers;

import com.ais.sys.exception.ServiceException;
import com.ais.sys.models.CallDate;
import com.ais.sys.services.CallDateService;
import com.ais.sys.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(value = "/callDate")
public class CallDateController {
    private static final Logger logger = LoggerFactory.getLogger(CallDateController.class);

    @Autowired
    CallDateService callDateService;

    @GetMapping("add")
    public Object addCallDate(@RequestParam String token) throws ServiceException {
        logger.info("[/callDate/add]: {}", token);
        return callDateService.addCallDate(token);
    }

    @GetMapping("query")
    public CallDate queryCallDate(@RequestParam String datestr, @RequestParam String token) {
        try {
            logger.info("[/callDate/query]: {}, {}", datestr, token);
            Date date = DateUtil.parseDateOnly(datestr);
            if (date == null) return null;
            CallDate callDate = new CallDate();
            callDate.setExecDate(date);
            CallDate callDate1 = callDateService.search(callDate, token);
            return callDate1;
        } catch (Exception e) {
            logger.error("{}", e);
            return null;
        }
    }

    @GetMapping("callBack")
    public String callBack(@RequestParam String token, @RequestParam Integer status, @RequestParam Integer id) throws ServiceException {
        logger.info("[/callDate/query]: {}, {}, {}", token, status, id);
        try {
            callDateService.handleCallback(token, status, id);
            return "OK";
        } catch (Exception e) {
            logger.error("{}", e);
            return "FAIL";
        }
    }
}
