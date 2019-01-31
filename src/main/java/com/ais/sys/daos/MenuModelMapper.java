package com.ais.sys.daos;

import java.util.List;

import com.ais.sys.models.MenuModel;
import com.ais.sys.models.PowerModel;

public interface MenuModelMapper {
	List<MenuModel> search(MenuModel model);
	List<MenuModel> searchMenu(MenuModel model);
	int insert(MenuModel model);
	int update(MenuModel model);
	int delete(MenuModel model);
	List<MenuModel> selectmenu(MenuModel model);
	List<MenuModel> getMenuname(MenuModel model);
}