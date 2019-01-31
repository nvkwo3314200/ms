package com.ais.sys.controllers;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ais.ms.models.SWorkSheetModel;
import com.ais.ms.report.SystemParametersHandler;
import com.ais.sys.exception.ServiceException;
import com.ais.sys.exception.SystemException;
import com.ais.sys.models.ResponseData;
import com.ais.sys.models.SystemParametersModel;
import com.ais.sys.models.UserInfoModel;
import com.ais.sys.services.SessionService;
import com.ais.sys.services.SystemParametersService;
import com.ais.sys.utils.ConstantUtil;
import com.ais.sys.utils.ExcelUtil;
import com.github.pagehelper.PageInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("SystemParametersController")
@RequestMapping(value = "/systemPara")
public class SystemParametersController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(SystemParametersController.class);
	
	private List<SystemParametersModel> reportList;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	
	@Resource(name = "systemParametersService")
	private SystemParametersService systemParametersService;
	
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	@Resource(name="systemParametersHandler")
	private SystemParametersHandler handler;
	
	@RequestMapping(value="/getSelectList", method = {RequestMethod.POST,RequestMethod.GET}, produces={"application/xml", "application/json"})
	@ResponseBody
	public ResponseData<SystemParametersModel> getSelectList(@RequestBody final SystemParametersModel model) throws SystemException {
		ResponseData<SystemParametersModel> responseData = responseDataService.getResponseData();
		UserInfoModel userVo= sessionService.getCurrentUser();
		if (userVo == null) {
			responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
			responseData.add("change_password_invalid_login");
			return responseData;
		}
		try {
			
			PageInfo<SystemParametersModel> page = systemParametersService.searchPage(model);
			if (page.getList().size() > 0) {
				responseData.setPage(page);
				responseData.setErrorType("success");
			} else {
			    responseData.setErrorType("danger");
			}
		} catch (Exception e) {
		   LOG.error(e.getMessage(), e);
		   throw new SystemException(e.getMessage(), e);
		}
		return responseData;
	}
	
	@RequestMapping(value = "/sysDetail", method = { RequestMethod.POST, RequestMethod.GET }, produces = { "application/xml","application/json" })
	@ResponseBody
	public SystemParametersModel sysDetail(
		@RequestParam(value = "id", required = false) final Integer id) throws SystemException {
		SystemParametersModel model = new SystemParametersModel();
		try{
			if(id != null){
				model.setId(id);
				List<SystemParametersModel> list = systemParametersService.searchList(model);
				model = list.get(0);
				model.setDisFlag(true);
			}else{
				model.setActiveInd("Y");
				model.setDisFlag(false);
			}
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(),e);
		}
		return model;
	}
	
	@RequestMapping(value="/segmentList", method = {RequestMethod.POST,RequestMethod.GET}, produces={"application/xml", "application/json"})
	@ResponseBody
	public List<SystemParametersModel> segmentList() throws SystemException {
		SystemParametersModel model = new SystemParametersModel();
		List<SystemParametersModel> list = null;
		try {
			list = systemParametersService.segmentList(model);
		} catch (Exception e) {
		   LOG.error(e.getMessage(), e);
		   throw new SystemException(e.getMessage(), e);
		}
		return list;
	}

	@RequestMapping(value="/searchDate", method = {RequestMethod.POST,RequestMethod.GET}, produces={"application/xml", "application/json"})
	@ResponseBody
	public ResponseData<SystemParametersModel> searchDate(@RequestBody final SystemParametersModel model) throws SystemException {
		ResponseData<SystemParametersModel> responseData = responseDataService.getResponseData();
		List<SystemParametersModel> list = null;
		try {
			list = systemParametersService.searchList(model);
			if (list != null && list.size() > 0) {
				responseData.setReturnDataList(list);
				responseData.setErrorType("success");
			} else {
			    responseData.setErrorType("danger");
			}
			
		} catch (Exception e) {
		   LOG.error(e.getMessage(), e);
		   throw new SystemException(e.getMessage(), e);
		}
		return responseData;
	}
	
	@RequestMapping(value = "/delete", method = { RequestMethod.POST,RequestMethod.GET }, produces = { "application/xml","application/json" })
	@ResponseBody
	public ResponseData<SystemParametersModel> delete(@RequestBody final SystemParametersModel[] models) throws SystemException {
		ResponseData<SystemParametersModel> responseData = responseDataService.getResponseData();
		UserInfoModel user = sessionService.getCurrentUser();
		if (user == null) {
        	responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
    		responseData.add("change_password_invalid_login");
    		return responseData;
        }
		try {
			int result = 0;
			for(SystemParametersModel model : models){
				result = systemParametersService.deleteSystemParameters(model);
			}
//			List<SystemParametersModel> list = systemParametersService.selectList(null);
			if(result != 0){
//				responseData.setReturnDataList(list);
				responseData.setErrorType(ConstantUtil.ERROR_TYPE_SUCCESS);
			} else {
				responseData.add("email_update_failed");
				responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		    throw new SystemException(e.getMessage(),e);
		}
		return responseData;
	}
	
	@RequestMapping(value = "/saveUser", method = {RequestMethod.POST,RequestMethod.GET}, produces={"application/xml", "application/json"})
	@ResponseBody
	public ResponseData<SystemParametersModel> saveUser(@RequestBody final SystemParametersModel model) 
			throws SystemException, ServiceException {
		ResponseData<SystemParametersModel> responseData = responseDataService.getResponseData();
//	 	responseData.setResourceBundleMessageSource(messageSource);
	 	boolean flag = systemParametersService.chackNull(model);
	 	if(!flag){
		 	if(!StringUtils.isEmpty(model.getCode())){
		 		model.setCode(model.getCode().toUpperCase());
		 	}
		 	responseData = systemParametersService.checkSysSave(model);
			systemParametersService.addUser(model, responseData);
			responseData.setErrorType(ConstantUtil.ERROR_TYPE_SUCCESS);
	 	}
		return responseData;
	}
	
	@RequestMapping(value="/download", method = {RequestMethod.POST,RequestMethod.GET}, produces={"application/xml", "application/json"})
	@ResponseBody
	public void download(@RequestParam("segment") String segment,@RequestParam("data") String reportData,HttpServletRequest request, HttpServletResponse response) throws IOException, SystemException {
		SystemParametersModel model = new SystemParametersModel();
		if(StringUtils.isNotEmpty(segment)){
			model.setSegment(segment);
		}
		if(!reportData.equals("[]")){
			JSONArray jsonArray = JSONArray.fromObject(reportData);
			reportList = new ArrayList<SystemParametersModel>(JSONArray.toCollection(jsonArray, SystemParametersModel.class));
		}else{
			try {
//				model.setStartRow(1);
//				model.setEndRow(50000);
				reportList = systemParametersService.searchList(model);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		String title = messageSource.getMessage("system_paramter_title", null, Locale.TAIWAN);
		Map<String,String> queryParams = new HashMap<String,String>();
		queryParams.put(messageSource.getMessage("system_paramter_search", null, Locale.TAIWAN), segment);
		String[] headers1 = messageSource.getMessage("system_paramter_column", null, Locale.ENGLISH).split(",");
		String[] headers2 = messageSource.getMessage("system_paramter_column", null, Locale.TAIWAN).split(",");
		List<String[]> heardersList = new ArrayList<>();
		heardersList.add(headers1);
		heardersList.add(headers2);
		
		handler.clearSWorkSheet();
		String width = null;
		try {
			List<SystemParametersModel> systemParametersList = systemParametersService.searchList(new SystemParametersModel("REPORT_ATTR","WIDTH"));
			if(systemParametersList!=null&&systemParametersList.size()>0){
				width = systemParametersList.get(0).getAttrib01();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		handler.addSWorkSheet(new SWorkSheetModel("", title, queryParams, heardersList, reportList, null, width));
		
		Workbook wb = handler.doProduce();
		
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");  
		response.setDateHeader("Expires", 0);
		response.setContentType("application/vnd.ms-excel");
		response.addHeader("Content-Disposition","SystemParameter.xlsx");
		
		wb.write(response.getOutputStream());
		
		if(wb.getClass().isAssignableFrom(SXSSFWorkbook.class)) {
			((SXSSFWorkbook) wb).dispose();
		}
		
		wb.close();
	}
	
	@RequestMapping(value="/templatedownload", method = {RequestMethod.POST,RequestMethod.GET}, produces={"application/xml", "application/json"})
	@ResponseBody
	public void templatedownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String template = request.getRealPath("/file/upload/SystemParamter_Data_Template.xlsx");	
		Workbook wb = ExcelUtil.exportExcelTemplate(template);
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");  
		response.setDateHeader("Expires", 0);
		response.setContentType("application/vnd.ms-excel");
		response.addHeader("Content-Disposition","SystemParamter_Data_Template.xlsx");
		
		wb.write(response.getOutputStream());
		wb.close();
	}
	
	@RequestMapping(value="/upload", method = {RequestMethod.POST,RequestMethod.GET}, produces={"application/xml", "application/json"})
	@ResponseBody
    public String upload(@RequestParam MultipartFile file, @RequestParam(value="threadId")String threadId) throws Exception {
		List<SystemParametersModel> sysList = (List<SystemParametersModel>) handler.readExcel(file.getInputStream(),SystemParametersModel.class);
		
		LOG.debug("upload threadId:" + threadId);
		
		systemParametersService.saveUpload(sysList, threadId);
		try {
			reportList = systemParametersService.searchList(new SystemParametersModel());
			JSONArray jsonArray = JSONArray.fromObject(reportList); 
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("reportList", jsonArray);
			String response = jsonObject.toString();
			response = URLEncoder.encode(response, "utf-8");
			return response;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
    }
	
	// -lh-10-26
	@RequestMapping(value="/insertProgress", method = {RequestMethod.POST,RequestMethod.GET}, produces={"application/xml", "application/json"})
	@ResponseBody
    public String insertProgress(@RequestParam(value="threadId")String threadId) throws Exception {
		return systemParametersService.getProgress(threadId).toString();
	}
	
	@RequestMapping(value="/redProgress", method = {RequestMethod.POST,RequestMethod.GET}, produces={"application/xml", "application/json"})
	@ResponseBody
    public String redProgress(@RequestParam(value="threadId")String threadId) throws Exception {
		return handler.getRedProgress();
	}
	
	@RequestMapping(value="/initProgress", method = {RequestMethod.POST,RequestMethod.GET}, produces={"application/xml", "application/json"})
	@ResponseBody
    public void initProgress(@RequestParam(value="threadId")String threadId) throws Exception {
		handler.initRedProgress();
//		systemParametersService.initProgress();
		systemParametersService.initCache(threadId);
	}
	
	@RequestMapping(value="/cancelProgress", method = {RequestMethod.POST,RequestMethod.GET}, produces={"application/xml", "application/json"})
	@ResponseBody
    public void cancelProgress() throws Exception {
		handler.initRedProgress();
		systemParametersService.cancelProgress();
	}
}
