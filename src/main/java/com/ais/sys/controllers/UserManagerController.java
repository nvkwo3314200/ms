package com.ais.sys.controllers;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ais.sys.exception.ServiceException;
import com.ais.sys.exception.SystemException;
import com.ais.sys.models.ResponseData;
import com.ais.sys.models.UserInfoModel;
import com.ais.sys.services.RoleManagerService;
import com.ais.sys.services.SessionService;
import com.ais.sys.services.UserManagerService;
import com.ais.sys.utils.ConstantUtil;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "/userManager")
public class UserManagerController extends BaseController{
	private static final Logger LOG = LoggerFactory.getLogger(UserManagerController.class);
    private UserInfoModel userInfoModel =new UserInfoModel();
	@Resource(name = "userManagerService")
	private UserManagerService userManagerService;
	@Resource(name = "roleManagerService")
	private RoleManagerService roleManagerService;
	@Autowired
	private SessionService sessionService;
	

	@RequestMapping(value = "/userList", 
					method = { RequestMethod.POST,RequestMethod.GET }, 
					produces = { "application/json" })
	@ResponseBody
	public List<UserInfoModel> userViewList(@RequestBody final UserInfoModel userVo) 
		throws SystemException {
		List<UserInfoModel> userVos = null;
		try {
			userInfoModel=userVo;
			userVos = userManagerService.selectUserList(userVo);
		} catch (Exception e) {
			LOG.error("productInfoVos error:" + e.getMessage(), e);
			throw new SystemException(e.getMessage(),e);
		}
		return userVos;
	}
	
	@RequestMapping(value = "/search", 
			method = { RequestMethod.POST,RequestMethod.GET }, 
			produces = { "application/json" })
	@ResponseBody
	public PageInfo<UserInfoModel> search(@RequestBody final UserInfoModel userVo) 
		throws SystemException {
		try {
			return userManagerService.search(userVo);
		} catch (Exception e) {
			LOG.error("productInfoVos error:" + e.getMessage(), e);
			throw new SystemException(e.getMessage(),e);
		}
	}
	
	@RequestMapping(value = "/saveUser", method = {RequestMethod.POST,
			RequestMethod.GET}, produces={"application/xml", "application/json"})
	@ResponseBody
	public ResponseData<UserInfoModel> saveUser(
			@RequestBody final UserInfoModel user) 
		throws SystemException, ServiceException {
		ResponseData<UserInfoModel> responseData = responseDataService.getResponseData();
		 	boolean falg=userManagerService.chack(user);
		 	if(falg==false){
			 	responseData = userManagerService.checkUserSave(user);
			 	//liyuan 20160929
			 	if("N".equals(user.getAccountLock())) {
			 		user.setLoginFailTimes(0);
			 	}else {
			 		user.setLoginFailTimes(5);
			 	}
			 	userManagerService.addUser(user, responseData);
			 	// by lh -10-12
//			 	sessionService.initCurrentUser();
			 	// by lh -8-19
//				sessionService.initCurrentUserPower();
			 	
			 	
		 	}
			return responseData;
	}
	
	@RequestMapping(value = "/saveCurrentUser", method = {RequestMethod.POST,
			RequestMethod.GET}, produces={"application/xml", "application/json"})
	@ResponseBody
	public ResponseData<UserInfoModel> saveCurrentUser(
			@RequestBody final UserInfoModel user) 
		throws SystemException, ServiceException {
		ResponseData<UserInfoModel> responseData = responseDataService.getResponseData();
		 	boolean falg=userManagerService.checkCurrent(user);
		 	if(falg==false){
		 		if(!StringUtils.isEmpty(user.getUserPwd())) {
		 			userManagerService.changePassword(user);
				}
			 	int i = userManagerService.updateUser(user);
			 	if(i == 1) {
			 		responseData.setErrorType("success");
			 	}		 	
		 	}
			return responseData;
	}
	
	
	@RequestMapping(value = "/userDetail", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/xml","application/json" })
	@ResponseBody
	public UserInfoModel userDetail(
			@RequestParam(value = "userId", required = false) final Integer userId) throws SystemException {
		UserInfoModel userVo = null;
		try{
			if(userId==null){
				userVo=new UserInfoModel();
				userVo.setUserActiveInd("Y");
				userVo.setAccountLock("N"); //liyuan 20160929
			}else{
				userVo = userManagerService.getUserViewVoById(userId);		
				userVo.setSubRoleModelList(roleManagerService.getRoleUserlist(userId));	
				if(userVo != null  && userVo.getLoginFailTimes() != null && userVo.getLoginFailTimes() >= 5) {
					userVo.setAccountLock("Y");
				}else {
					userVo.setAccountLock("N");
				}
		}
		
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			 throw new SystemException(e.getMessage(),e);
		}
		return userVo;
	}
	
	@RequestMapping(value = "/currentUser", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/xml","application/json" })
	@ResponseBody
	public UserInfoModel currentUser() throws SystemException {
		UserInfoModel userVo = null;
		Integer userId = sessionService.getCurrentUser().getId();
		try{
			userVo = userManagerService.getUserViewVoById(userId);		
//			userVo.setSubRoleModelList(roleManagerService.getRoleUserlist(userId));	
			if(userVo != null  && userVo.getLoginFailTimes() != null && userVo.getLoginFailTimes() >= 5) {
				userVo.setAccountLock("Y");
			}else {
				userVo.setAccountLock("N");
			}
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			 throw new SystemException(e.getMessage(),e);
		}
		return userVo;
	}
	
	

	

	
	@RequestMapping(value = "/deleteUser", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/xml",
			"application/json" })
	@ResponseBody
	public ResponseData deleteUser(
			@RequestParam(value = "userIds", required = false) final String userIds) throws SystemException {
		ResponseData responseData = responseDataService.getResponseData();
		try {
			userManagerService.deleteByPrimaryKey(userIds);
			responseData.setErrorType(ConstantUtil.ERROR_TYPE_SUCCESS);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		    throw new SystemException(e.getMessage(),e);
		}

		return responseData;

	}
	
	
	@RequestMapping(value = "/saveAddRole", method = {RequestMethod.POST,
			RequestMethod.GET}, produces={"application/xml", "application/json"})
	@ResponseBody
	public ResponseData<UserInfoModel> saveAddRole(
			@RequestBody final UserInfoModel userVo) 
		throws SystemException, ServiceException {
			ResponseData<UserInfoModel> responseData = userManagerService.checkUserRoleSave(userVo);
			if(responseData.getErrorType() == null || !responseData.getErrorType().equals(ConstantUtil.ERROR_TYPE_DANGER)) {
				responseData.setErrorType("success");
			}
			return responseData;
	}
	
	@RequestMapping(value = "/cancel", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/xml",
			"application/json" })
	@ResponseBody
	public UserInfoModel cancel(){
		return userInfoModel;
	}
	
	
	@RequestMapping(value = "/deleteRolePop", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/xml",
			"application/json" })
	@ResponseBody
	public ResponseData<UserInfoModel> deleteRolePop(
			@RequestBody final UserInfoModel userVo) throws SystemException {
		ResponseData<UserInfoModel> responseData = userManagerService.deletebufferRoles(userVo);
		responseData.setErrorType(ConstantUtil.ERROR_TYPE_SUCCESS);
		return responseData;

	}
	
}
