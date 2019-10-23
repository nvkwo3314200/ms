package com.ais.sys.models;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MenuModel extends BaseModel {
	
	private static final long serialVersionUID = -7266959489206555797L;

	public MenuModel(){
		
	}
	public MenuModel(String other){
		this.other = other;
	}
	private BigDecimal id;
	private String funcName;
	private String funcCode;
	private String uiSref;
	private String type;
	private Integer lev1;
	private Integer lev2;
	private BigDecimal menuid;
	private String funcUrl;
	private boolean falg;
	
	private String funcNameEN;
	private String funcNameCN;
	private String funcNameTW;

	private String other;
	private String funcNames;
	
	public boolean isFalg() {
		return falg;
	}
	public void setFalg(boolean falg) {
		this.falg = falg;
	}
	public String getFuncUrl() {
		return funcUrl;
	}
	public void setFuncUrl(String funcUrl) {
		this.funcUrl = funcUrl;
	}
	public BigDecimal getMenuid() {
		return menuid;
	}
	public void setMenuid(BigDecimal menuid) {
		this.menuid = menuid;
	}
	public BigDecimal getId() {
		return id;
	}
	public void setId(BigDecimal id) {
		this.id = id;
	}
  
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}	
	
	public Integer getLev1() {
		return lev1;
	}
	public void setLev1(Integer lev1) {
		this.lev1 = lev1;
	}
	public Integer getLev2() {
		return lev2;
	}
	public void setLev2(Integer lev2) {
		this.lev2 = lev2;
	}
	public String getUiSref() {
		return uiSref;
	}
	public void setUiSref(String uiSref) {
		this.uiSref = uiSref;
	}
	public String getFuncName(String language) {
		funcName = "";
		if("en".equals(language)&&funcNameEN!=null){
			funcName = funcNameEN;
		}else if("zh_CN".equals(language)&&funcNameCN!=null){
			funcName = funcNameCN;
//		}else if("zh_TW".equals(language)&&funcNameTW!=null){
		}else{
			funcName = funcNameTW;
		}
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
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getFuncNames() {
		funcNames = "";
		if("en".equals(other)&&funcNameEN!=null){
			funcNames = funcNameEN;
		}else if("zh_CN".equals(other)&&funcNameCN!=null){
			funcNames = funcNameCN;
		}else if("zh_TW".equals(other)&&funcNameTW!=null){
			funcNames = funcNameTW;
		}
		return funcNames;
	}
	public void setFuncNames(String funcNames) {
		this.funcNames = funcNames;
	}
	
	

}