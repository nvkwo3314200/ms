package com.ais.sys.daos;

import java.util.List;

import com.ais.sys.models.RoleMenuModel;

public interface RoleMenuModelMapper {
	List <RoleMenuModel> selectMenuModelList(Integer id);
	List<RoleMenuModel> selectlist(RoleMenuModel menuModel);
	int creatRolePermission(RoleMenuModel menuModel);
	int updateRolePermission(RoleMenuModel menuModel);
	int deleteRoleMenu(Integer roleId);
	int deleteRole(Integer funcId);
}

