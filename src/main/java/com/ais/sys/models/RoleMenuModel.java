package com.ais.sys.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class RoleMenuModel extends BaseModel {
	private static final long serialVersionUID = 8049983960293633523L;
	private Integer id;
	private Integer roleId;
	private Integer funcId;
	private String canSelect;
	private String canInsert;
	private String canUpdate;
	private String canDelete;
	private String canAudit;
	private String canView;
	private String active;
	
	private String roleCode;
	private String roleName;
	private String remark;
	private String orderId;
	private String activeInd;
	private Date inactiveDate;
	private String busUnitCode;
	
	private String type;
	private String funcName;
	private String lev1;
	private String lev2;
	private String lev3;
	private String funcCode;
	private String funcAction;
	private String basicFunc;
	private String funcNameEN;
	private String funcNameCN;
	private String funcNameTW;
	
	
	
	public String getCanAudit() {
		return canAudit;
	}
	public void setCanAudit(String canAudit) {
		this.canAudit = canAudit;
	}
	public String getCanView() {
		return canView;
	}
	public void setCanView(String canView) {
		this.canView = canView;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public Integer getFuncId() {
		return funcId;
	}
	public void setFuncId(Integer funcId) {
		this.funcId = funcId;
	}
	
	public String getCanSelect() {
		return canSelect;
	}
	public void setCanSelect(String canSelect) {
		this.canSelect = canSelect;
	}
	public String getCanInsert() {
		return canInsert;
	}
	public void setCanInsert(String canInsert) {
		this.canInsert = canInsert;
	}
	public String getCanUpdate() {
		return canUpdate;
	}
	public void setCanUpdate(String canUpdate) {
		this.canUpdate = canUpdate;
	}
	public String getCanDelete() {
		return canDelete;
	}
	public void setCanDelete(String canDelete) {
		this.canDelete = canDelete;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getActiveInd() {
		return activeInd;
	}
	public void setActiveInd(String activeInd) {
		this.activeInd = activeInd;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getFuncName() {
		return funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	public String getFuncCode() {
		return funcCode;
	}
	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}
	public String getLev1() {
		return lev1;
	}
	public void setLev1(String lev1) {
		this.lev1 = lev1;
	}
	public String getLev2() {
		return lev2;
	}
	public void setLev2(String lev2) {
		this.lev2 = lev2;
	}
	public String getLev3() {
		return lev3;
	}
	public void setLev3(String lev3) {
		this.lev3 = lev3;
	}
	public String getFuncAction() {
		return funcAction;
	}
	public void setFuncAction(String funcAction) {
		this.funcAction = funcAction;
	}
	public String getBasicFunc() {
		return basicFunc;
	}
	public void setBasicFunc(String basicFunc) {
		this.basicFunc = basicFunc;
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
	public String getFuncNameEN() {
		return funcNameEN;
	}
	public void setFuncNameEN(String funcNameEN) {
		this.funcNameEN = funcNameEN;
	}
	public String getFuncNameCN() {
		return funcNameCN;
	}
	public void setFuncNameCN(String funcNameCN) {
		this.funcNameCN = funcNameCN;
	}
	public String getFuncNameTW() {
		return funcNameTW;
	}
	public void setFuncNameTW(String funcNameTW) {
		this.funcNameTW = funcNameTW;
	}

}
