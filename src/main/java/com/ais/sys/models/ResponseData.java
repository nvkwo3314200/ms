package com.ais.sys.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;

import com.ais.sys.utils.ConstantUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.pagehelper.PageInfo;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ResponseData<T> {
	private String errorType;
	private T returnData;
	private List<T> returnDataList;
	private PageInfo<T> page;
	private List<String> errorList = new ArrayList<>();
	private String errorMessage;
	private MessageSource messageSource;
	private Locale locale;
	private byte[] fileContent;
	
	public ResponseData() {
		
	}
	
	public ResponseData(PageInfo<T> page, T returnData) {
		this.setPage(page);
		this.setReturnData(returnData);
		this.setErrorType(ConstantUtil.ERROR_TYPE_SUCCESS);
	}
	
	public static ResponseData<?> success(PageInfo<?> page, Object returnData) {
		return new ResponseData(page, returnData);
	}
	
	public static ResponseData<String> danger() {
		ResponseData<String> responseData = new ResponseData<>();
		responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
		return responseData;
	}
	
	private Map<String, String[]> argsMap =new HashMap<>();

	public void  putMessagesParamArray(String key,String[] value){
		argsMap.put(key, value);
	}
	
	public Map<String, String[]> getArgsMap() {
		return argsMap;
	}

	public void setArgsMap(Map<String, String[]> argsMap) {
		this.argsMap = argsMap;
	}

	public Locale getLocale() {
		if(locale==null){
			return Locale.TRADITIONAL_CHINESE;
		}
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public List<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

	public ResponseData<T> add(String errorMsg) {
		errorList.add(errorMsg);
		return this;
	}


	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public String getErrorMessage() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < errorList.size(); i++) {
			//String msg = getMessageSource().getMessage(errorList.get(i), argsMap.get(errorList.get(i)), getLocale());
			String msg = errorList.get(i);
			sb.append(msg);
			if(i+1!=errorList.size()){
				sb.append("<br/>");
			}

		}
		
		String msg=sb.toString();
		if("".equals(msg)){
			msg=this.errorMessage;
		}
				
		return msg;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public T getReturnData() {
		return returnData;
	}

	public void setReturnData(T returnData) {
		this.returnData = returnData;
	}


	public List<T> getReturnDataList() {
		return returnDataList;
	}

	public void setReturnDataList(List<T> returnDataList) {
		this.returnDataList = returnDataList;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public PageInfo<T> getPage() {
		return page;
	}

	public void setPage(PageInfo<T> page) {
		this.page = page;
	}
	
}
