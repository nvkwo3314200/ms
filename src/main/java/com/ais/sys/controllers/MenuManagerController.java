package com.ais.sys.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ais.sys.exception.ServiceException;
import com.ais.sys.exception.SystemException;
import com.ais.sys.models.MenuModel;
import com.ais.sys.models.ResponseData;
import com.ais.sys.models.UserInfoModel;
import com.ais.sys.services.MenuService;
import com.ais.sys.services.RoleMenuService;
import com.ais.sys.utils.ConstantUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

@Controller("MenuManagerController")
@RequestMapping(value = "/menuManager")
public class MenuManagerController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(MenuManagerController.class);

	@Resource(name = "menuService")
	private MenuService menuService;

	@Resource(name = "roleMenuService")
	private RoleMenuService roleMenuService;
	
	List<MenuModel> list=new ArrayList<>();
	
	
	@RequestMapping(value = "/search", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/xml",
			"application/json" })
	@ResponseBody
	public ResponseData<MenuModel> search(@RequestBody final MenuModel menuModel)
			throws SystemException {
		ResponseData<MenuModel> responseData = responseDataService.getResponseData();
		UserInfoModel user = sessionService.getCurrentUser();
		MenuModel model = new MenuModel(user.getUserCode());
		try {		
			if(menuModel.getLev1()!=null){
				model.setLev1(menuModel.getLev1());
			}
			if(menuModel.getLev2()!=null){
				model.setLev2(menuModel.getLev1());
			
			}
			PageInfo<MenuModel> page = menuService.search(model);
			responseData.setPage(page);
			responseData.setErrorType("success");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return responseData;
	}

	@RequestMapping(value = "/searchMenu", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/xml",
			"application/json" })
	@ResponseBody
	public ResponseData<MenuModel> searchMenu(@RequestBody final MenuModel menuModel)
			throws SystemException {
		ResponseData<MenuModel> responseData = responseDataService.getResponseData();
		UserInfoModel user = sessionService.getCurrentUser();
		if(user!=null)
		menuModel.setOther(user.getUserCode());
		try {		
			PageInfo<MenuModel> page = menuService.searchMenu(menuModel);
			responseData.setPage(page);
			responseData.setErrorType("success");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return responseData;
	}
	
	
	@RequestMapping(value = "/getMenuName", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/xml",
			"application/json" })
	@ResponseBody
	public ResponseData<MenuModel> getMenuName()
			throws SystemException {
		ResponseData<MenuModel> responseData = responseDataService.getResponseData();
		UserInfoModel user = sessionService.getCurrentUser();
		if (user == null) {
			responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
			responseData.add("change_password_invalid_login");
			return responseData;
		}
			
		try {			
			MenuModel menuModel=new MenuModel();
			menuModel.setOther(user.getUserCode());
			menuModel.setType("menu");
			List<MenuModel> list = menuService.getMenuName(menuModel);
			if (list != null && list.size() > 0) {
				responseData.setReturnDataList(list);
				responseData.setErrorType("success");
			} else {
				responseData.setErrorType("danger");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return responseData;
	}
	
	
	@RequestMapping(value = "/getfuncName", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/xml",
			"application/json" })
	@ResponseBody
	public ResponseData<MenuModel> getfuncName(@RequestBody final MenuModel model )
			throws SystemException {
		ResponseData<MenuModel> responseData = responseDataService.getResponseData();
		UserInfoModel user = sessionService.getCurrentUser();
		if (user == null) {
			responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
			responseData.add("change_password_invalid_login");
			return responseData;
		}
		try {			
			MenuModel menuModel=new MenuModel();
			menuModel.setOther(user.getUserCode());
			menuModel.setType("func");
			menuModel.setLev1(model.getLev1());
			List<MenuModel> list = menuService.getMenuName(menuModel);
			if (list != null && list.size() > 0) {
				responseData.setReturnDataList(list);
				responseData.setErrorType("success");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return responseData;
			
	}
	
	
	@RequestMapping(value = "/delete", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/json" })
	@ResponseBody
	public ResponseData<MenuModel> delete(@RequestBody final MenuModel[] models)
			throws SystemException {

		ResponseData<MenuModel> responseData = responseDataService.getResponseData();
		UserInfoModel user = sessionService.getCurrentUser();

		if (user == null) {
			responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
			responseData.add("change_password_invalid_login");
			return responseData;
		}

		try {
			int result = 0;
			for (MenuModel model : models) {
				int id = model.getId().intValue();
				result = roleMenuService.deleteRole(id);
				result = menuService.delete(model);

				if (result != 0) {
//					List<MenuModel> list = menuService.search(null);
//					responseData.setReturnDataList(list);
					responseData.setErrorType(ConstantUtil.ERROR_TYPE_SUCCESS);
				} else {
					responseData.add("email_update_failed");
					responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
				}
			}

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return responseData;

	}

	
	@RequestMapping(value = "/save", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/json" })
	@ResponseBody
	public ResponseData<MenuModel> save(@RequestBody final MenuModel model)
			throws SystemException, ServiceException {

		ResponseData<MenuModel> responseData = responseDataService.getResponseData();
		UserInfoModel user = sessionService.getCurrentUser();

		if (user == null) {
			responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
			responseData.add("change_password_invalid_login");
			return responseData;
		}
		boolean falg=menuService.chack(model);
		if(falg==false){
		model.setLastUpdatedBy(user.getUserCode());
		model.setFuncCode(model.getFuncCode().toUpperCase());
		try {
			int result = 0;
			
			if (model.getId() == null) {
				responseData = menuService.checkMenuSave(model);
				if (responseData.getErrorType() == null) {
					result = menuService.insert(model);
					
				}

			} else {
				responseData = menuService.checkMenuUpdateSave(model);
				if (responseData.getErrorType() == null) {
					if(model.getLev2()==0){
						for(MenuModel menuModel:list){
							menuModel.setLev1(model.getLev1());
							result = menuService.update(menuModel);												
						}
					}
					result = menuService.update(model);
					
				}

			}
			if (result != 0) {
//				List<MenuModel> list = menuService.search(model);
//				list.get(0).setFalg(true);
//				responseData.setReturnData(list.get(0));
				responseData
						.setErrorType(ConstantUtil.ERROR_TYPE_SUCCESS);
			} 
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		}
		return responseData;

	}

	
	@RequestMapping(value = "/selectMenu", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/xml",
			"application/json" })
	@ResponseBody
	public MenuModel selectMenu(@RequestBody final MenuModel model)
			throws SystemException {
		MenuModel menuModel = null;
		try {
			if (model.getId() != null) {
				menuModel = menuService.searchResult(model).get(0);
				menuModel.setFalg(true);
				if(menuModel.getLev2()==0){
					MenuModel model2=new MenuModel();
					model2.setLev1(menuModel.getLev1());
					list=menuService.searchResult(model2);
				}
			} else {
				menuModel = new MenuModel();
				menuModel.setFalg(false);
				menuModel.setType("menu");
				menuModel.setLev2(0);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return menuModel;
	}
	
	
	@RequestMapping(value = "/selectMenulist", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/xml",
			"application/json" })
	@ResponseBody
	public List<MenuModel> selectMenulist(@RequestBody final MenuModel menuModel)
			throws SystemException {
		List<MenuModel> menuModels = new ArrayList<>();
		UserInfoModel user = sessionService.getCurrentUser();
		menuModel.setOther(user.getUserCode());
		try {
			menuModels = menuService.selectmenu(menuModel);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return menuModels;
	}
}
