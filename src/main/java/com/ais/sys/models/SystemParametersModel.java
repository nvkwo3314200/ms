package com.ais.sys.models;

import java.util.Date;
import java.util.List;

import com.ais.ms.report.annotation.ReportColumn;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SystemParametersModel extends BaseModel implements Cloneable{
	private static final long serialVersionUID = -6539301774758872295L;

	private Integer id;
	
	@ReportColumn
	private String	   segment;
	@ReportColumn
	private String	   code;
	@ReportColumn
	private String	   description;
	@ReportColumn
	private Integer    dispSeq;
	@ReportColumn
	private String	   attrib01;
	@ReportColumn
	private String	   attrib02;
	@ReportColumn
	private String	   attrib03;
	@ReportColumn
	private String	   attrib04;
	@ReportColumn
	private String	   attrib05;
	@ReportColumn
	private String	   attrib06;
	@ReportColumn
	private String	   attrib07;
	@ReportColumn
	private String	   attrib08;
	@ReportColumn
	private String	   attrib09;
	@ReportColumn
	private String	   attrib10;
	@ReportColumn
	private String	   activeInd;
	@ReportColumn
	private Date	   inactiveDate;
	
	private List<SystemParametersModel> segmentList;
	private boolean	   disFlag;
	
	private String ids;
	
	private String fileUrl;
	
	// by lh -9-7
	
	public SystemParametersModel() {}
	
	public SystemParametersModel(String segment, String code) {
		this.segment = segment;
		this.code = code;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDispSeq() {
		return dispSeq;
	}
	public void setDispSeq(Integer dispSeq) {
		this.dispSeq = dispSeq;
	}
	public String getSegment() {
		return segment;
	}
	public void setSegment(String segment) {
		this.segment = segment;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAttrib01() {
		return attrib01;
	}
	public void setAttrib01(String attrib01) {
		this.attrib01 = attrib01;
	}
	public String getAttrib02() {
		return attrib02;
	}
	public void setAttrib02(String attrib02) {
		this.attrib02 = attrib02;
	}
	public String getAttrib03() {
		return attrib03;
	}
	public void setAttrib03(String attrib03) {
		this.attrib03 = attrib03;
	}
	public String getAttrib04() {
		return attrib04;
	}
	public void setAttrib04(String attrib04) {
		this.attrib04 = attrib04;
	}
	public String getAttrib05() {
		return attrib05;
	}
	public void setAttrib05(String attrib05) {
		this.attrib05 = attrib05;
	}
	public String getAttrib06() {
		return attrib06;
	}
	public void setAttrib06(String attrib06) {
		this.attrib06 = attrib06;
	}
	public String getAttrib07() {
		return attrib07;
	}
	public void setAttrib07(String attrib07) {
		this.attrib07 = attrib07;
	}
	public String getAttrib08() {
		return attrib08;
	}
	public void setAttrib08(String attrib08) {
		this.attrib08 = attrib08;
	}
	public String getAttrib09() {
		return attrib09;
	}
	public void setAttrib09(String attrib09) {
		this.attrib09 = attrib09;
	}
	public String getAttrib10() {
		return attrib10;
	}
	public void setAttrib10(String attrib10) {
		this.attrib10 = attrib10;
	}
	public String getActiveInd() {
		return activeInd;
	}
	public void setActiveInd(String activeInd) {
		this.activeInd = activeInd;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getInactiveDate() {
		return inactiveDate;
	}
	public void setInactiveDate(Date inactiveDate) {
		this.inactiveDate = inactiveDate;
	}
	public List<SystemParametersModel> getSegmentList() {
		return segmentList;
	}
	public void setSegmentList(List<SystemParametersModel> segmentList) {
		this.segmentList = segmentList;
	}
	public boolean isDisFlag() {
		return disFlag;
	}
	public void setDisFlag(boolean disFlag) {
		this.disFlag = disFlag;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
}
