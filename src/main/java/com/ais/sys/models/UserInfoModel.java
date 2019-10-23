package com.ais.sys.models;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ais.sys.exception.ServiceException;
import com.ais.sys.services.UserManagerService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class UserInfoModel extends BaseModel implements HttpSessionBindingListener{
	
	private static final long serialVersionUID = 6641264991752813549L;

	private static final Logger LOG = LoggerFactory.getLogger(UserInfoModel.class);
	
    private Integer id;
    private String userCode;
    private String userName;
    private String userPwd;
    private String userActiveInd;
    private String deleteInd;
    private String tel;
    private String email;
    private Date userActiveDate;
    private String accountLock; //liyuan 20160929

    //LY role model pop
    private List<RoleModel> subRoleModelList;
    private RoleModel selectedRoleModel;
    private RoleModel bufferRoleModel;
    private String bufferRoleCodes;

    
    //by lh
    private String roleCode;
    private String sessionLang;
    private Date lastLoginDate;
    private Integer loginFailTimes;
    private String currentMenu;
    
    
    //by lijun
    private List<RoleModel> roleList;
    private String plantCode;
    private String plantDesc;	
    private String prodLn;	
    private String prodName;

    
    private String loginUserInfoId;
    private String ip;
    private String operate;
    private Date operateDate;
    private String opDate;
    
    //create by cgp 2016.11.22
    private String ssoHost;
    private String ssoLoginId;
    private String ssoStaffId;
    
	public String getOpDate() {
		return opDate;
	}
	public void setOpDate(String opDate) {
		this.opDate = opDate;
	}
	public List<RoleModel> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<RoleModel> roleList) {
		this.roleList = roleList;
	}	

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getUserActiveInd() {
		return userActiveInd;
	}
	public void setUserActiveInd(String userActiveInd) {
		this.userActiveInd = userActiveInd;
	}
	public String getDeleteInd() {
		return deleteInd;
	}
	public void setDeleteInd(String deleteInd) {
		this.deleteInd = deleteInd;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getUserActiveDate() {
		return userActiveDate;
	}
	public void setUserActiveDate(Date userActiveDate) {
		this.userActiveDate = userActiveDate;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public String getSessionLang() {
		return sessionLang;
	}
	public void setSessionLang(String sessionLang) {
		this.sessionLang = sessionLang;
	}
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public Integer getLoginFailTimes() {
		return loginFailTimes;
	}
	public void setLoginFailTimes(Integer loginFailTimes) {
		this.loginFailTimes = loginFailTimes;
	}
	
	public String getCurrentMenu() {
		return currentMenu;
	}
	public void setCurrentMenu(String currentMenu) {
		this.currentMenu = currentMenu;
	}
	public List<RoleModel> getSubRoleModelList() {
		return subRoleModelList;
	}
	public void setSubRoleModelList(List<RoleModel> subRoleModelList) {
		this.subRoleModelList = subRoleModelList;
	}
	public RoleModel getSelectedRoleModel() {
		return selectedRoleModel;
	}
	public void setSelectedRoleModel(RoleModel selectedRoleModel) {
		this.selectedRoleModel = selectedRoleModel;
	}
	public RoleModel getBufferRoleModel() {
		return bufferRoleModel;
	}
	public void setBufferRoleModel(RoleModel bufferRoleModel) {
		this.bufferRoleModel = bufferRoleModel;
	}
	public String getBufferRoleCodes() {
		return bufferRoleCodes;
	}
	public void setBufferRoleCodes(String bufferRoleCodes) {
		this.bufferRoleCodes = bufferRoleCodes;
	}
	public String getPlantCode() {
		return plantCode;
	}
	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}
	public String getPlantDesc() {
		return plantDesc;
	}
	public void setPlantDesc(String plantDesc) {
		this.plantDesc = plantDesc;
	}
	public String getProdLn() {
		return prodLn;
	}
	public void setProdLn(String prodLn) {
		this.prodLn = prodLn;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public String getAccountLock() {
	
		return accountLock;
	}
	public void setAccountLock(String accountLock) {
		this.accountLock = accountLock;
	}
	public String getLoginUserInfoId() {
		return loginUserInfoId;
	}
	public void setLoginUserInfoId(String loginUserInfoId) {
		this.loginUserInfoId = loginUserInfoId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public Date getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}
	public String getSsoHost() {
		return ssoHost;
	}
	public void setSsoHost(String ssoHost) {
		this.ssoHost = ssoHost;
	}
	public String getSsoLoginId() {
		return ssoLoginId;
	}
	public void setSsoLoginId(String ssoLoginId) {
		this.ssoLoginId = ssoLoginId;
	}
	public String getSsoStaffId() {
		return ssoStaffId;
	}
	public void setSsoStaffId(String ssoStaffId) {
		this.ssoStaffId = ssoStaffId;
	}
	public void valueBound(HttpSessionBindingEvent event) {
		operate = "LOGIN";
		operateDate = new Date(System.currentTimeMillis());
		try {
			UserManagerService.saveUserLoginSuccess(this);
			System.out.println("INFO ["+(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(operateDate)+"] : "+userCode+" login success."); 
		} catch (ServiceException e) {
			LOG.error("login error : "+e.getMessage(),e);
		}
		
	}
	
	public void valueUnbound(HttpSessionBindingEvent event) {
		operate = "LOGOUT";
		operateDate = new Date(System.currentTimeMillis());
		try {
			UserManagerService.saveUserLoginSuccess(this);
			System.out.println("INFO ["+(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(operateDate)+"] : "+userCode+" logout success."); 
		} catch (ServiceException e) {
			LOG.error("logout error : "+e.getMessage(),e);
		}
	}
	
}
