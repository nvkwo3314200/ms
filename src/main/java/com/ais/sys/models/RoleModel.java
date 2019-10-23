package com.ais.sys.models;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class RoleModel extends BaseModel {
	private static final long serialVersionUID = -4600275773098401495L;
	private Integer id;
    private String roleCode;
    private String roleName;
    private String roleRemark;
    private Integer roleOrder;
    private String roleActive;
    private Date inactiveDate;
    private String busUnitCode;
    private List<RoleMenuModel> list;
    private boolean falg=false;
    
    
    
    private Integer userId; 
    private Integer roleId;
    
    
    //lh
    private String language;
    
	public boolean isFalg() {
		return falg;
	}
	public void setFalg(boolean falg) {
		this.falg = falg;
	}
			

	public List<RoleMenuModel> getList() {
		return list;
	}
	public void setList(List<RoleMenuModel> list) {
		this.list = list;
	}
	public Date getInactiveDate() {
		return inactiveDate;
	}
	public void setInactiveDate(Date inactiveDate) {
		this.inactiveDate = inactiveDate;
	}
	public String getBusUnitCode() {
		return busUnitCode;
	}
	public void setBusUnitCode(String busUnitCode) {
		this.busUnitCode = busUnitCode;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleRemark() {
		return roleRemark;
	}
	public void setRoleRemark(String roleRemark) {
		this.roleRemark = roleRemark;
	}

	public void setRoleOrder(Integer roleOrder) {
		this.roleOrder = roleOrder;
	}

	public Integer getRoleOrder() {
		return roleOrder;
	}
	public String getRoleActive() {
		return roleActive;
	}
	public void setRoleActive(String roleActive) {
		this.roleActive = roleActive;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
	
	
	
}
