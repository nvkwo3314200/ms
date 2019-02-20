package com.ais.sys.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ais.sys.exception.ServiceException;
import com.ais.sys.exception.SystemException;
import com.ais.sys.models.PowerModel;
import com.ais.sys.models.ResponseData;
import com.ais.sys.models.SystemParametersModel;
import com.ais.sys.models.UserInfoModel;
import com.ais.sys.services.SystemParametersService;
import com.ais.sys.services.UserManagerService;
import com.ais.sys.utils.ConstantUtil;

@Controller
@RequestMapping(value = "/login")
public class LoginController extends BaseController {
	
	@Resource(name = "userManagerService")
	private UserManagerService userManagerService;
	
	@Resource(name = "systemParametersService")
	private SystemParametersService systemParametersService;

	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);
	
	private final static int LOGIN_FAIL_TIMES= 5;

	@RequestMapping(value = "/success", method = { RequestMethod.POST, RequestMethod.GET }, produces = { "application/json" })
	@ResponseBody
	public ResponseData<Map<String, Object>> success(HttpServletRequest request, HttpServletResponse response) {
		ResponseData<Map<String, Object>> responseData = responseDataService.getResponseData();
		responseData.setErrorType(ConstantUtil.ERROR_TYPE_SUCCESS);
		UserInfoModel user = sessionService.getCurrentUserInfo();
		Map<String, Object> map = new HashMap<String, Object>();
		if(user !=null){
			//init login ip lh-10-13
			user.setIp(requestService.getRealRemoteIp());
			sessionService.initCurrentUser(user);
			sessionService.initCurrentUserPower(user.getUserCode());
			
			map.put("user", user);
			map.put("powers", sessionService.getCurrentUserAllFuncPower(user.getUserCode()));
			//clear loging fail times..(6-30)
			UserInfoModel record = new UserInfoModel();
			record.setUserCode(user.getUserCode());
			record.setLastLoginDate(new Date());
			record.setLastUpdatedBy(user.getUserCode());
			record.setLastUpdatedDate(new Date());
			try {
				userManagerService.updateUserLoginSuccess(record);
			} catch (ServiceException e) {
				LOG.error("updateUserLoginSuccess error:"+e.getMessage(),e);
			}
		}
		responseData.setReturnData(map);
		return responseData;
	}

	@RequestMapping(value = "/failure", method = { RequestMethod.POST, RequestMethod.GET }, produces = { "application/json" })
	@ResponseBody
	public ResponseData<UserInfoModel> failure(final HttpSession session) throws SystemException {
		String type = (String) session.getAttribute(ConstantUtil.LOGIN_FAILURE_TYPE);
		String userCode = (String) session.getAttribute(ConstantUtil.LOGIN_FAILURE_NAME);
		ResponseData<UserInfoModel> responseData = responseDataService.getResponseData();
		if (ConstantUtil.LOGIN_FAILURE_TYPE_DISABLED.equals(type)) {
			responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
			responseData.setErrorMessage("User account is disabled.");
		} else if (ConstantUtil.LOGIN_FAILURE_TYPE_BADCREDENTIALS.equals(type)) {
			responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
			responseData.setErrorMessage("Username or password invalid.");
		} else {
			responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
			responseData.setErrorMessage(type);
		}
		if(StringUtils.isNotEmpty(userCode)){
			//Determine whether the user has been locked
			UserInfoModel user =userManagerService.getUserInfo(userCode);
			if(user != null){
				UserInfoModel record = new UserInfoModel();
				record.setUserCode(userCode);
				record.setLastUpdatedBy(userCode);
				record.setLastUpdatedDate(new Date());
				try {
					userManagerService.updateUserLoginFail(record);
				} catch (ServiceException e) {
					LOG.error("updateUserForLoginFailTime error:"+e.getMessage(),e);
				}
				if(user.getLoginFailTimes().intValue()+1>=LOGIN_FAIL_TIMES){
					responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
					responseData.add("user_login_fail_times");
				}
				LOG.info(" Login fail times :"+ (user.getLoginFailTimes().intValue()+1));
			}
			
			//	old
//			if(userVo!=null && userVo.getLoginFailTimes()!=null && userVo.getLoginFailTimes().intValue()>=LOGIN_FAIL_TIMES){
//				LOG.info(" Login fail times >=5....");
//				responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
//				responseData.setErrorMessage("Login times greater than 5 times, the account has been locked.");
//			}		
		}		
		return responseData;
	}

	@RequestMapping(value = "/currentUser", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/json" })
	@ResponseBody
	public UserInfoModel currentUser() {
		return sessionService.getCurrentUser();
	}
	
	@RequestMapping(value = "/updateLoginMsg", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/json" })
	@ResponseBody
	public ResponseData<UserInfoModel> updateLoginMsg(@RequestParam(value = "lastLanguage", required = true) final String lastLanguage) throws SystemException {
		ResponseData<UserInfoModel> responseData = responseDataService.getResponseData();
		UserInfoModel user = sessionService.getCurrentUser();
    	if(user==null){
    		responseData.add("user_login_expire");
    		responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
    		return responseData;
    	}
//    	UserModel userModel=new UserModel();
//    	userModel.setId(user.getId());
//    	userModel.setSessionLang(lastLanguage);
//    	userModel.setLastLoginDate(new Date());
//    	userService.updateByPrimaryKeySelective(userModel);
		user.setSessionLang(lastLanguage);
		user.setLastLoginDate(new Date());
		try {
			userManagerService.updateUserLang(user);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseData;
	}
	
	@RequestMapping(value="/getCurrentPowers", method = {RequestMethod.POST,RequestMethod.GET}, produces={"application/xml", "application/json"})
	@ResponseBody
	public ResponseData<PowerModel> getCurrentPowers(@RequestBody final String url) throws ServiceException {
		
		ResponseData<PowerModel> responseData = responseDataService.getResponseData();
		
		UserInfoModel user = sessionService.getCurrentUser();
		
        if (user == null) {
        	responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
    		responseData.add("change_password_invalid_login");
    		return responseData;
        }
        
//        model.setUserId(user.getId());
        
		PowerModel power = sessionService.getCurrentUserPower(url);
		responseData.setReturnData(power);
        
		return responseData;
	}
	
	@RequestMapping(value="/initSysDateFormat", method = {RequestMethod.POST,RequestMethod.GET}, produces={"application/xml", "application/json"})
	@ResponseBody
	public ResponseData<String> initSysDateFormat() throws ServiceException {
		
		ResponseData<String> responseData = responseDataService.getResponseData();

		SystemParametersModel model = new SystemParametersModel();
		model.setCode("SYS_DATE_FORMAT");
		
		String sysDateFormat = "yyyy-MM-dd";
		
		try {
			
			List<SystemParametersModel> modelList = systemParametersService.searchList(model);
			
			if(modelList != null){
				sysDateFormat = modelList.get(0).getAttrib01();
			}
			
			responseData.setReturnData(sysDateFormat);
			
		} catch (ServiceException e) {
			
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
			
		}
		
		return responseData;
	}
	
}
