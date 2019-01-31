package com.ais.sys.controllers;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ais.sys.exception.SystemException;
import com.ais.sys.models.UserInfoModel;
import com.ais.sys.services.UserLoginInfoService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "/userLoginInfo")
public class UserLoginInfoController extends BaseController{
	private static final Logger LOG = LoggerFactory.getLogger(UserLoginInfoController.class);
	 
	@Resource(name = "userLoginInfoService")
	 private UserLoginInfoService userLoginInfoService;
	 
	 @RequestMapping(value = "/search", method = { RequestMethod.POST,
				RequestMethod.GET }, produces = { "application/xml",
				"application/json" })
	 @ResponseBody
	public PageInfo<UserInfoModel> search(@RequestBody final UserInfoModel userInfoModel) throws SystemException{
	 	try {
	 		if(userInfoModel.getRoleCode()!=null){
	 			userInfoModel.setRoleCode(userInfoModel.getRoleCode().toUpperCase());	 			
	 		}
	 		if(userInfoModel.getPlantCode()!=null){
	 			userInfoModel.setPlantCode(userInfoModel.getPlantCode().toUpperCase());
	 		}
	 		if(userInfoModel.getProdLn()!=null){
	 			userInfoModel.setProdLn(userInfoModel.getProdLn().toUpperCase());
	 		}
	 		return userLoginInfoService.search(userInfoModel);				
		} catch (Exception e) {
			LOG.error("productInfoVos error:" + e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
	 }
}
