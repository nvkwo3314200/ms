package com.ais.ms.models;

import java.util.List;
import java.util.Map;

public class SWorkSheetModel {
	private String name;
	private String title;
	private Map<String, String> queryParams;
	private List<String[]> headers;
	private List<?> dataList;
	

	private String password;
	private String defaultCellWidth;
	
	public SWorkSheetModel(String name, String title, Map<String, String> queryParams, List<String[]> headers, List<?> dataList, String password, String defaultCellWidth){
		this.name = name;
		this.title = title;
		this.queryParams = queryParams;
		this.headers = headers;
		this.dataList = dataList;
		this.password = password;
		this.defaultCellWidth = defaultCellWidth;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Map<String, String> getQueryParams() {
		return queryParams;
	}
	public void setQueryParams(Map<String, String> queryParams) {
		this.queryParams = queryParams;
	}
	public List<String[]> getHeaders() {
		return headers;
	}
	public void setHeaders(List<String[]> headers) {
		this.headers = headers;
	}
	public List<?> getDataList() {
		return dataList;
	}
	public void setDataList(List<?> dataList) {
		this.dataList = dataList;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDefaultCellWidth() {
		return defaultCellWidth;
	}
	public void setDefaultCellWidth(String defaultCellWidth) {
		this.defaultCellWidth = defaultCellWidth;
	}
	
	
}