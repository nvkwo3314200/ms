package com.ais.sys.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PowerModel implements Serializable{
	private static final long serialVersionUID = -1478988670242738530L;
	private Integer userId;
	private String url;
	private String funcName;
	
	private String create;
	private String delete;
	private String update;
	private String search;
	private String audit;
	private String view;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean getCreate() {
		return stringToBoolean(create);
	}
	public void setCreate(String create) {
		this.create = create;
	}
	public boolean getDelete() {
		return stringToBoolean(delete);
	}
	public void setDelete(String delete) {
		this.delete = delete;
	}
	public boolean getUpdate() {
		return stringToBoolean(update);
	}
	public void setUpdate(String update) {
		this.update = update;
	}
	public boolean getSearch() {
		return stringToBoolean(search);
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public boolean getAudit() {
		return stringToBoolean(audit);
	}
	public void setAudit(String audit) {
		this.audit = audit;
	}
	public boolean getView() {
		return stringToBoolean(view);
	}
	public void setView(String view) {
		this.view = view;
	}
	public String getFuncName() {
		return funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	
	public boolean stringToBoolean(String state){
		if("Y".equals(state)){
			return true;
		}
		return false;
	}
}
