package com.ais.sys.services;

import com.ais.sys.daos.CallDateMapper;
import com.ais.sys.exception.ServiceException;
import com.ais.sys.models.CallDate;
import com.ais.sys.models.Picture;
import com.ais.sys.models.UserInfoModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class CallDateService {
    @Autowired
    CallDateMapper callDateMapper;

    @Autowired
    SessionService sessionService;

    @Autowired
    UserManagerService userManagerService;

    public CallDate search(final CallDate callDate, String token) throws ServiceException {
        UserInfoModel backModel = getUserInfoModelByToken(token);
        callDate.setStatus(1);
        List<CallDate> callDateList = callDateMapper.selectByModel(callDate);
        CallDate callDateModel = null;
        if(callDateList.size() != 0) {
            callDateModel = callDateList.get(0);
        }
        callDateModel.setEmail(backModel.getEmail());
        return callDateModel;
    }

    public void insert(CallDate callDate) {
        callDateMapper.insertSelective(callDate);
    }

    public void update(CallDate callDate) {
        callDate.setCreateBy(null);
        callDate.setCreateDate(null);
        callDateMapper.updateByPrimaryKeySelective(callDate);
    }

    public CallDate addCallDate(String token) throws ServiceException {
        UserInfoModel backModel = getUserInfoModelByToken(token);
        CallDate callDate = new CallDate(null, new Date(), getWaitTime(), 1, backModel.getUserCode(), backModel.getUserCode(), new Date(), new Date(), null);
        insert(callDate);
        return callDateMapper.selectByPrimaryKey(callDate.getId());
    }

    private UserInfoModel getUserInfoModelByToken(String token) throws ServiceException {
        if(StringUtils.isBlank(token)) {
            throw new ServiceException("token is null");
        }
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setSsoLoginId(token);
        List<UserInfoModel> infoList = userManagerService.selectUserList(userInfoModel);
        if(infoList.size() == 0) throw new ServiceException("wrong token");
        UserInfoModel infoModel = infoList.get(0);
        return infoModel;
    }

    private int getWaitTime() {
        Random random = new Random();
        int waitSecond = random.nextInt(60 * 5);
        return waitSecond;
    }

    public void handleCallback(String token, Integer status, Integer id) throws ServiceException {
        UserInfoModel backModel = getUserInfoModelByToken(token);
        CallDate callDate = callDateMapper.selectByPrimaryKey(id);
        if(callDate == null) throw new ServiceException("wrong info");
        if(!callDate.getCreateBy().equals(backModel.getUserCode())) throw new ServiceException("wrong info");
        callDate.setStatus(status);
        callDate.setLastUpdateDate(new Date());
        update(callDate);
    }
}
