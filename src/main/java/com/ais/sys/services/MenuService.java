package com.ais.sys.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ais.sys.daos.MenuModelMapper;
import com.ais.sys.exception.ServiceException;
import com.ais.sys.exception.SystemException;
import com.ais.sys.models.MenuModel;
import com.ais.sys.models.ResponseData;
import com.ais.sys.utils.ConstantUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("menuService")
public class MenuService extends BaseService{
	private static final Logger LOG = LoggerFactory
			.getLogger(MenuService.class);
	private MenuModelMapper menuModelMapper;

	public MenuModelMapper getMenuModelMapper() {
		return menuModelMapper;
	}

	@Autowired
	public void setMenuModelMapper(MenuModelMapper menuModelMapper) {
		this.menuModelMapper = menuModelMapper;
	}

	public PageInfo<MenuModel> search(MenuModel model) throws ServiceException {
		try {
			return PageHelper.startPage(model.getPage(), model.getSize()).doSelectPageInfo(()-> menuModelMapper.search(model));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	public List<MenuModel> searchResult(MenuModel model) throws ServiceException {
		try {
			return menuModelMapper.search(model);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	public PageInfo<MenuModel> searchMenu(MenuModel model) throws ServiceException {
		try {
			return PageHelper.startPage(model.getPage(), model.getSize()).doSelectPageInfo(()-> menuModelMapper.searchMenu(model));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	public ResponseData<MenuModel> checkMenuSave(MenuModel menuModel)
			throws SystemException {
		ResponseData<MenuModel> responseData = responseDataService.getResponseData();
		if (menuModel != null) {
			MenuModel model2 = new MenuModel();
			model2.setType(menuModel.getType());
			List<MenuModel> menuModels = menuModelMapper.selectmenu(model2);
			for (MenuModel menu : menuModels) {
				if (menuModel.getFuncCode().equals(menu.getFuncCode())) {
					responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
					responseData.add("menu_add_Function_Code");
					break;
				}
				if (menuModel.getType().equals("menu")) {
					if (menuModel.getLev1().equals(menu.getLev1())) {
						responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
						responseData.add("menu_add_Level_1_Serial_Number");
						break;
					}
				} else if (menuModel.getType().equals("func")) {
					if (menuModel.getLev1().equals(menu.getLev1())&&menuModel.getLev2().equals(menu.getLev2())) {
						responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
						responseData.add("menu_add_Level_2_Serial_Number");
						break;
					}
				}

			}
		}

		return responseData;
	}
	
	public List<MenuModel> getMenuName(MenuModel model) throws SystemException, ServiceException{
		try {
			List<MenuModel> list = menuModelMapper.getMenuname(model);
			if (list != null && list.size() > 0) {
				return list;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return null;
		
	}
	
	public ResponseData<MenuModel> checkMenuUpdateSave(MenuModel menuModel)
			throws SystemException {
		ResponseData<MenuModel> responseData = responseDataService.getResponseData();
		
		if (menuModel != null) {
			MenuModel model2 = new MenuModel();
			model2.setType(menuModel.getType());
			List<MenuModel> menuModels = menuModelMapper.selectmenu(model2);
			for (MenuModel menu : menuModels) {
				if (menuModel.getType().equals("menu")) {
					if (!menuModel.getId().equals(menu.getId())&&menuModel.getLev1().equals(menu.getLev1())) {
						responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
						responseData.add("menu_add_Level_1_Serial_Number");					
						}												
					}
				if(menuModel.getType().equals("func")){
					if(!menuModel.getId().equals(menu.getId())&&menuModel.getLev1().equals(menu.getLev1())&&menuModel.getLev2().equals(menu.getLev2())){
						responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
						responseData.add("menu_add_Level_2_Serial_Number");	
					}
				}
				}
					
		}

		return responseData;
	}
	
	public boolean chack(MenuModel menuModel)throws ServiceException{
		boolean falg=false;
		if(menuModel.getFuncCode()==null){
			falg=true;			
		}else if (menuModel.getFuncNameEN()==null) {
			falg=true;
		}
//		else if (menuModel.getFuncNameCN()==null) {
//			falg=true;
//		}
		else if (menuModel.getFuncNameTW()==null) {
			falg=true;
		}else if (menuModel.getLev1()==null) {
			falg=true;
		}else if (menuModel.getLev2()==null) {
			falg=true;
		}
		return falg;
	}
	public int update(MenuModel model) throws ServiceException {
		try {
			return menuModelMapper.update(model);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	public int delete(MenuModel model) throws ServiceException {
		try {
			return menuModelMapper.delete(model);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	public int insert(MenuModel model) throws ServiceException {
		try {
			return menuModelMapper.insert(model);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	public List<MenuModel> selectmenu(MenuModel model) throws ServiceException {
		try {
			List<MenuModel> list = new ArrayList<MenuModel>();
			list = menuModelMapper.selectmenu(model);
			return list;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}
}
