package com.ais.sys.services;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ais.sys.daos.RoleMenuModelMapper;
import com.ais.sys.exception.ServiceException;
import com.ais.sys.models.RoleMenuModel;


@Service("roleMenuService")
public class RoleMenuService extends BaseService{
	private static final Logger LOG = LoggerFactory
			.getLogger(RoleMenuService.class);	
	
	private RoleMenuModelMapper roleMenuModelMapper;
	public RoleMenuModelMapper getRoleMenuModelMapper() {
		return roleMenuModelMapper;
	}
	@Autowired
	public void setRoleMenuModelMapper(RoleMenuModelMapper roleMenuModelMapper) {
		this.roleMenuModelMapper = roleMenuModelMapper;
	}
	
	public List<RoleMenuModel> selectMenuModelList(Integer id) throws ServiceException{
		try {
			List<RoleMenuModel> list=roleMenuModelMapper.selectMenuModelList(id);
			if (list != null && list.size() > 0) {
				return list;
			}
			return null;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}		
	}
	
	public List<RoleMenuModel> selectList(RoleMenuModel menuModel) throws ServiceException{
		try {
			List<RoleMenuModel> list=roleMenuModelMapper.selectlist(menuModel);
			if (list != null && list.size() > 0) {
				return list;
			}
			return null;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}	
	}
	public int creatRolePermission(RoleMenuModel menuModel)throws ServiceException{
		try {
			menuModel.setCreatedBy(sessionService.getCurrentUser().getUserCode());
			menuModel.setLastUpdatedBy(sessionService.getCurrentUser().getUserCode());
			return roleMenuModelMapper.creatRolePermission(menuModel);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	public int deleteRoleMenu(Integer roleId)throws ServiceException{
		try {
			return roleMenuModelMapper.deleteRoleMenu(roleId);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	public int deleteRole(Integer funcId)throws ServiceException{
		try {
			return roleMenuModelMapper.deleteRole(funcId);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	public int updateRolePermission(RoleMenuModel menuModel) throws ServiceException{
		try {
			menuModel.setLastUpdatedBy(sessionService.getCurrentUser().getUserCode());
			return roleMenuModelMapper.updateRolePermission(menuModel);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
