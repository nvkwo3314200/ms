  package com.ais.sys.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ais.sys.cache.RedisCache;
import com.ais.sys.cache.RedisEvict;
import com.ais.sys.daos.UserManagerMapper;
import com.ais.sys.daos.UserRoleManagerMapper;
import com.ais.sys.exception.ServiceException;
import com.ais.sys.exception.SystemException;
import com.ais.sys.models.MenuModel;
import com.ais.sys.models.PowerModel;
import com.ais.sys.models.ResponseData;
import com.ais.sys.models.RoleModel;
import com.ais.sys.models.UserInfoModel;
import com.ais.sys.utils.ConstantUtil;
import com.ais.sys.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("userManagerService")
public class UserManagerService extends BaseService{
	private static final Logger LOG = LoggerFactory.getLogger(UserManagerService.class);
	
	// the static update by lh -16-10-12
	private static UserManagerMapper userManagerMapper;
	
	private UserRoleManagerMapper userRoleManagerMapper;

	@Resource(name = "roleManagerService")
	private RoleManagerService roleManagerService;
	
	
	public UserManagerMapper getUserManagerMapper() {
		return userManagerMapper;
	}
	
	@Autowired
	public void UserManagerMapper(UserManagerMapper userManagerMapper) {
		this.userManagerMapper = userManagerMapper;
	}
	
	public UserRoleManagerMapper getUserRoleManagerMapper() {
		return userRoleManagerMapper;
	}
	
	@Autowired
	public void setUserRoleManagerMapper(UserRoleManagerMapper userRoleManagerMapper) {
		this.userRoleManagerMapper = userRoleManagerMapper;
	}
	
	//@RedisCache(type=UserInfoModel.class)
	public List<UserInfoModel> selectUserList(UserInfoModel searchUserVo) throws ServiceException {
		try {
			List<UserInfoModel> list = userManagerMapper.selectUserList(searchUserVo);
			if(list != null && list.size() > 0) {
				return list;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage(), e);
		}
		return new ArrayList<UserInfoModel>();
	}
	
	@RedisCache(type=UserInfoModel.class)
	public PageInfo<UserInfoModel> search(UserInfoModel searchUserVo) throws ServiceException {
		try {
			return PageHelper.startPage(searchUserVo.getPage(), searchUserVo.getSize()).doSelectPageInfo(() -> userManagerMapper.selectUserList(searchUserVo));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	
	public boolean chack(UserInfoModel userInfoModel){
		boolean falg=false;
		if(userInfoModel.getUserCode()==null){
			falg=true;
		}else if (userInfoModel.getId()==null) {
			if(userInfoModel.getUserPwd()==null){				
				falg=true;
			}
		}
				
		return falg;
	}
	
	public boolean checkCurrent(UserInfoModel userInfoModel) {
		boolean flag = false;
		if(userInfoModel.getUserCode() == null) {
			flag = true;
		}
		if(userInfoModel.getUserName() == null) {
			flag = true;
		}
		if(userInfoModel.getUserPwd() == null) {
			flag = true;
		}
		return flag;
	}
	
	public ResponseData<UserInfoModel> checkUserSave(UserInfoModel userInfoVo) throws SystemException {
		ResponseData<UserInfoModel> responseData = responseDataService.getResponseData();
		if (userInfoVo != null) {
//			if (StringUtils.isEmpty(userInfoVo.getUserCode())) {
//				responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
//				responseData.add("user_add_user_code");
//			}else {
//				userInfoVo.setUserCode(userInfoVo.getUserCode().toUpperCase());
				Integer existNum = null;
				if(userInfoVo.getId() == null) {
					existNum = userManagerMapper.createExsitUser(userInfoVo);
				}
				if(existNum != null && existNum !=0) {
					responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
					responseData.add("user_code_unique");
				}
			}
//			if (StringUtils.isEmpty(userInfoVo.getUserCode())) {
//				responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
//				responseData.add("user_add_user_name");
//			}
//			if (StringUtils.isEmpty(userInfoVo.getUserPwd())) {
//				responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
//				responseData.add("user_add_user_password");
//			}
//		}
		return responseData;
	}

	@RedisEvict(type=UserInfoModel.class)
	public void addUser(UserInfoModel userInfoVo,ResponseData<UserInfoModel> responseData)
			throws ServiceException {
		try {
			if (responseData.getErrorType() == null || !responseData.getErrorType().equals(ConstantUtil.ERROR_TYPE_DANGER)) {
				if(!StringUtils.isEmpty(userInfoVo.getUserPwd())) {
					changePassword(userInfoVo);
				}
				
				if (userInfoVo.getId() == null) {
					int id = insertUser(userInfoVo);
					userInfoVo.setId(id);
				} else {
					updateUser(userInfoVo);
				}
				userRoleListSaveAll(userInfoVo);
				responseData.setErrorType("success");
			}
			responseData.setReturnData(userInfoVo);
		} catch (Exception e) {
			LOG.error("roleViewVo not save:" + e.getMessage(), e);
			throw new ServiceException(e.getMessage(), e);
		}
	}
	

	@RedisEvict(type=UserInfoModel.class)
	public int insertUser(UserInfoModel userVo) {
		userVo.setCreatedBy(sessionService.getCurrentUser().getUserCode());
		userVo.setCreatedDate(new Date());
		userVo.setLastUpdatedBy(sessionService.getCurrentUser().getUserCode());
		userVo.setLastUpdatedDate(new Date());
		userManagerMapper.insert(userVo);
		int id =userVo.getId();
		return id;
	}
	
	@RedisEvict(type=UserInfoModel.class)
	public int updateUser(UserInfoModel userVo) {
		int inserSupper = 0;
		userVo.setLastUpdatedBy(sessionService.getCurrentUser().getUserCode());
		userVo.setLastUpdatedDate(new Date());
		inserSupper = userManagerMapper.update(userVo);
		return inserSupper;
	}
	
	public UserInfoModel getUserViewVoById(Integer userId) {
		UserInfoModel userInfoVo = new UserInfoModel();
		if(userId != null)  {
			userInfoVo.setId(userId);
			List<UserInfoModel> roleList = userManagerMapper.selectUserList(userInfoVo);
			if(roleList != null && roleList.size() > 0) {
				userInfoVo = roleList.get(0);
			}
		}
		return userInfoVo;
	}
	
	@RedisEvict(type=UserInfoModel.class)
	public int deleteByPrimaryKey(String ids) {
		if (StringUtils.isNotEmpty(ids)) {
			String[] pStrings = StringUtil.getAllProductId(ids);
			if (pStrings != null && pStrings.length > 0) {
				for (String strId : pStrings) {
					if (StringUtils.isNotEmpty(strId)) {
						userManagerMapper.deleteByPrimaryKey(Integer.valueOf(strId));
						userManagerMapper.deleteUser(Integer.valueOf(strId));
					}
				}
			}
		}
		return 0;
	}
	
	//login	by lh-6-29
	public UserInfoModel getUserInfo(String userCode) {
		return userManagerMapper.selectUser(userCode);
	}

	//login	by lh-7-1
	public List<MenuModel> getCurrentMenu(String userCode) {
		return userManagerMapper.selectMenu(userCode);
	}
	
	//login	by lh-9-8
	public List<PowerModel> getUserAllFuncPower(String userCode) {
		return userManagerMapper.getUserAllFuncPower(userCode);
	}
	
	//login	by lh-6-30 PS:Update Login successful information
	public int updateUserLoginSuccess(UserInfoModel record) throws ServiceException {
		try {
			return userManagerMapper.updateUserLoginSuccess(record);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	//login	by lh-10-12 PS:Save Login successful data(static)
	public static int saveUserLoginSuccess(UserInfoModel record) throws ServiceException {
		try {
			return userManagerMapper.saveUserLoginSuccess(record);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	//login	by lh-6-30 PS:Update Login fail information
	public int updateUserLoginFail(UserInfoModel record) throws ServiceException {
		try {
			return userManagerMapper.updateUserLoginFail(record);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	public void changePassword (UserInfoModel userInfoVo) {
		Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
		String pwd = md5PasswordEncoder.encodePassword(userInfoVo.getUserPwd(),userInfoVo.getUserCode());
		userInfoVo.setUserPwd(pwd);
	}
	

	public ResponseData<UserInfoModel> checkUserRoleSave(UserInfoModel userInfoVo) throws SystemException {
		ResponseData<UserInfoModel> responseData = responseDataService.getResponseData();
		RoleModel selectedRoleModel = userInfoVo.getSelectedRoleModel();
		RoleModel bufferRoleModel = userInfoVo.getBufferRoleModel();
		List<RoleModel> bufferRoleList = userInfoVo.getSubRoleModelList();
		
		if (selectedRoleModel != null) {
			if (StringUtils.isEmpty(selectedRoleModel.getRoleCode())) {
				responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
				responseData.add("user_role_add_role_code");
			}else {
				if(bufferRoleList != null && !bufferRoleList.isEmpty()) {
					if(bufferRoleModel != null && !StringUtils.isEmpty(bufferRoleModel.getRoleCode())) {
						int index = 0;
						boolean flag = false;
						for (RoleModel roleModel : bufferRoleList) {
							if(bufferRoleModel.getRoleCode().equals(roleModel.getRoleCode())) {
								index = bufferRoleList.indexOf(roleModel);
								break;
							}
						}
						if(selectedRoleModel.getRoleCode().equals(bufferRoleModel.getRoleCode())) {
							flag = true;
						}else {
							flag = true;
							for (RoleModel roleModel : bufferRoleList) {
								if(selectedRoleModel.getRoleCode().equals(roleModel.getRoleCode())) {
									responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
									responseData.add("user_role_add_role_code_unique");
									flag = false;
									break;
								}
							}
						}
						if(flag) {
							selectedRoleModel.setLastUpdatedBy(sessionService.getCurrentUser().getUserCode());
							selectedRoleModel.setLastUpdatedDate(new Date());
							bufferRoleList.set(index, selectedRoleModel);
						}
					}else {
						boolean flag = false;
						for (RoleModel roleModel : bufferRoleList) {
							if(selectedRoleModel.getRoleCode().equals(roleModel.getRoleCode())) {
								responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
								responseData.add("user_role_add_role_code_unique");
								flag = true;
								break;
							}
						}
						if(!flag) {
							selectedRoleModel.setCreatedBy(sessionService.getCurrentUser().getUserCode());
							selectedRoleModel.setCreatedDate(new Date());
							selectedRoleModel.setLastUpdatedBy(sessionService.getCurrentUser().getUserCode());
							selectedRoleModel.setLastUpdatedDate(new Date());
							bufferRoleList.add(selectedRoleModel);
						}
					}
				}else {
					selectedRoleModel.setCreatedBy(sessionService.getCurrentUser().getUserCode());
					selectedRoleModel.setCreatedDate(new Date());
					selectedRoleModel.setLastUpdatedBy(sessionService.getCurrentUser().getUserCode());
					selectedRoleModel.setLastUpdatedDate(new Date());
					bufferRoleList = new ArrayList<RoleModel>();
					bufferRoleList.add(selectedRoleModel);
				}
			}
		}
		userInfoVo.setSubRoleModelList(bufferRoleList);
		responseData.setReturnData(userInfoVo);
		return responseData;
	}

	
	public void userRoleListSaveAll(UserInfoModel userInfoVo) {
		int userId = userInfoVo.getId();
		List<RoleModel> bufferRoleList = userInfoVo.getSubRoleModelList();
		List<RoleModel> roleListDB = roleManagerService.getRoleUserlist(userId);
		if(bufferRoleList != null && !bufferRoleList.isEmpty()) {
			if(roleListDB != null && !roleListDB.isEmpty()) {
				for (RoleModel roleModelDB : roleListDB) {
					userRoleManagerMapper.delete(roleModelDB);
				}
				for (RoleModel roleModel : bufferRoleList) {
					roleModel.setUserId(userId);
					userRoleManagerMapper.insert(roleModel);
				}
			}else {
				for (RoleModel roleModel : bufferRoleList) {
					roleModel.setUserId(userId);
					userRoleManagerMapper.insert(roleModel);
				}
			}
		}else {
			if(roleListDB != null && !roleListDB.isEmpty()) {
				for (RoleModel roleModelDB : roleListDB) {
					userRoleManagerMapper.delete(roleModelDB);
				}
			}
		}
	}
	
	public ResponseData<UserInfoModel> deletebufferRoles(UserInfoModel userInfoVo) throws SystemException {
		ResponseData<UserInfoModel> responseData = responseDataService.getResponseData();
		String RoleCodes = userInfoVo.getBufferRoleCodes();
		List<RoleModel> roleList = userInfoVo.getSubRoleModelList();
		if (StringUtils.isNotEmpty(RoleCodes)) {
			String[] pStrings = StringUtil.getAllProductId(RoleCodes);
			if (pStrings != null && pStrings.length > 0) {
				for (String strCode : pStrings) {
					if (StringUtils.isNotEmpty(strCode)) {
						if(roleList != null && !roleList.isEmpty()) {
							int index  = 0;
							boolean flag = false;
							for (RoleModel roleModel : roleList) {
								if(roleModel.getRoleCode().equals(strCode)) {
									flag = true;
									index = roleList.indexOf(roleModel);
									break;
								}
							}
							if(flag) {
								roleList.remove(index);
							}
						}
					}
				}
			}
		}
		userInfoVo.setSubRoleModelList(roleList);
		responseData.setReturnData(userInfoVo);
		return responseData;
	}
	
	//update the user selected language by Keven in July 20, 2016
	public int updateUserLang(UserInfoModel model) throws ServiceException {
		try{
			return userManagerMapper.updateUserLang(model);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
}
