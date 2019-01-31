package com.ais.sys.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.ais.sys.models.MenuModel;
import com.ais.sys.models.PowerModel;
import com.ais.sys.models.UserInfoModel;

@Service("sessionService")
public class SessionService {

	@Autowired  
	private HttpSession session;
	
	@Resource(name ="userManagerService")
	private UserManagerService userManagerService;
	
	@Resource(name ="menuService")
	private MenuService menuService;

	// login initCurrentUser
	public void initCurrentUser(UserInfoModel userInfo) {
		session.setAttribute("userInfo", userInfo);
	}
	
	// update user initCurrentUser
	public void initCurrentUser() {
		session.setAttribute("userInfo", getCurrentUserInfo());
	}
	
	// get userInfo in session
	public UserInfoModel getCurrentUser() {
		return (UserInfoModel)session.getAttribute("userInfo");
	}
	
	//by lh(login) -8-12
	public UserInfoModel getCurrentUserInfo() {
		//get user code
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	
    	if(authentication!=null &&  authentication.getPrincipal() instanceof User){
    		final User user = (User)authentication.getPrincipal();
    		//get user info
    		UserInfoModel userInfo = userManagerService.getUserInfo(user.getUsername());
    		userInfo.setCurrentMenu(getCurrentMenu(userInfo));
    		return userInfo;
    	}
    	return null;
	}

	public String getCurrentMenu(UserInfoModel user) {
		String currentMenu = null;
//		<1>
//    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    	if(authentication!=null &&  authentication.getPrincipal() instanceof User){
//    		final User user = (User)authentication.getPrincipal();
    		//get menu
//    		List<MenuModel> MenuList = userManagerService.getCurrentMenu(user.getUsername());
		//<2>
    	if(user.getUserCode() != null){
    		//get menu data
    		List<MenuModel> MenuList = userManagerService.getCurrentMenu(user.getUserCode());
    		if(MenuList!=null&&MenuList.size()>0){
    			//create Menu
    			currentMenu = "";
	    		for(int i=0; i<MenuList.size(); i++){
	    			MenuModel muen = MenuList.get(i);
    				currentMenu = currentMenu + "<li class=\"treeview\"><a href=\"javascript:void(0)\"><i class=\"fa fa-link\"></i><span>"+muen.getFuncName(user.getSessionLang())+"</span><i class=\"fa fa-angle-left pull-right\"></i></a>";
	    			if(i+1<MenuList.size()){
	    				currentMenu = currentMenu + "<ul class=\"treeview-menu\">";
	    				for(int j=i+1; j<MenuList.size(); i++,j++){
		    				MenuModel func = MenuList.get(j);
		    				if("menu".equals(func.getType())){
		    					break;
		    				}else{
		    					currentMenu = currentMenu + "<li class=\"active\" ><a ui-sref=\""+func.getUiSref()+"\" href=\"#/main/"+func.getFuncUrl()+"\">"+func.getFuncName(user.getSessionLang())+"</a></li>";
		    				}
		        		}
    					currentMenu = currentMenu + "</ul>";
	    			}
		    		currentMenu = currentMenu + "</li>";
	    		}
    		}
    	}
    	return currentMenu;
	}
	
	// getCurrentUserPower : url
	public PowerModel getCurrentUserPower(String url) {
		Map<String, PowerModel> powerMap = (Map<String, PowerModel>) session.getAttribute("userAllFuncPower");
		if(powerMap!=null&&powerMap.size()>0){
			return (PowerModel)powerMap.get(url);
		}
		return null;
	}

	// initCurrentUserPower
	public void initCurrentUserPower() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication!=null &&  authentication.getPrincipal() instanceof User){
			final User user = (User)authentication.getPrincipal();
			initCurrentUserPower(user.getUsername());
		}
	}
	
	// initCurrentUserPower : parameter
	public void initCurrentUserPower(String userCode) {
		session.setAttribute("userAllFuncPower", getCurrentUserAllFuncPower(userCode));
	}
	
	//by lh -9-26
	public Map<String, PowerModel> getCurrentUserAllFuncPower(String userCode) {
		Map<String, PowerModel> powerMap = new HashMap<String, PowerModel>();
    	if(StringUtils.isNotEmpty(userCode)){
    		//get user all func curd
    		List<PowerModel> powerList = userManagerService.getUserAllFuncPower(userCode);
    		if(powerList!=null&&powerList.size()>0){
    			for(PowerModel powerModel : powerList){
    				powerMap.put(powerModel.getUrl(), powerModel);
    			}
    		}
    	}
    	return powerMap;
	}
	
	public void setAttribute(String key, Object value) {
		session.setAttribute(key, value);
	}
	
	public Object getAttribute(String key) {
		return (Object)session.getAttribute(key);
	}
	
	public void removeAttribute(String key) {
		session.removeAttribute(key);
	}
}
