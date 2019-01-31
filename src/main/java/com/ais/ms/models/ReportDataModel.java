package com.ais.ms.models;

import java.util.HashMap;
import java.util.Map;

import com.ais.sys.models.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ReportDataModel extends BaseModel {
//	private List<?> reportList;
	private Map<String, Object> baseDatas;

	public ReportDataModel(){
//		reportList = new ArrayList();
		baseDatas = new HashMap<String, Object>();
	}
	
//	public List<?> getReportList() {
//		return reportList;
//	}
//
//	public void setReportList(List<?> reportList) {
//		this.reportList = reportList;
//	}

	public Map<String, Object> getBaseDatas() {
		return baseDatas;
	}

	public void setBaseDatas(Map<String, Object> baseDatas) {
		this.baseDatas = baseDatas;
	}
}