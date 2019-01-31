package com.ais.sys.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class BusUnitModel extends BaseModel {
	private String busUnitCode;
	private String busUnitName;
	private String sql;
	
	
	private Boolean falg;
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public Boolean getFalg() {
		return falg;
	}
	public void setFalg(Boolean falg) {
		this.falg = falg;
	}
	public String getBusUnitCode() {
		return busUnitCode;
	}
	public void setBusUnitCode(String busUnitCode) {
		this.busUnitCode = busUnitCode;
	}
	public String getBusUnitName() {
		return busUnitName;
	}
	public void setBusUnitName(String busUnitName) {
		this.busUnitName = busUnitName;
	}
	
}