package com.ais.sys.controllers;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ais.sys.exception.ServiceException;
import com.ais.sys.exception.SystemException;
import com.ais.sys.models.BusUnitModel;
import com.ais.sys.models.ResponseData;
import com.ais.sys.models.UserInfoModel;
import com.ais.sys.services.BusUnitService;
import com.ais.sys.utils.ConstantUtil;
import com.github.pagehelper.PageInfo;

@Controller("BusUnitController")
@RequestMapping(value = "/busUnit")
public class BusUnitController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(BusUnitController.class);
	
	@Resource(name = "busUnitService")
	private BusUnitService busUnitService;

	
	@RequestMapping(value="/search", method = {RequestMethod.POST,RequestMethod.GET}, produces={"application/xml", "application/json"})
	@ResponseBody
	public ResponseData<BusUnitModel> search(@RequestBody final BusUnitModel model)throws SystemException {
		ResponseData<BusUnitModel> responseData = responseDataService.getResponseData();
		UserInfoModel user= sessionService.getCurrentUser();

        if (user == null) {
        	responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
    		responseData.add("change_password_invalid_login");
    		return responseData;
        }
        
		try {
			List<BusUnitModel> list = busUnitService.search(model);
			if(model.getBusUnitCode()!=null){
			list.get(0).setFalg(true);
			}
			responseData.setReturnDataList(list);
			responseData.setErrorType("success");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return responseData;
	}
	
	@RequestMapping(value="/select", method = {RequestMethod.POST,RequestMethod.GET}, produces={"application/xml", "application/json"})
	@ResponseBody
	public ResponseData<BusUnitModel> select(@RequestBody final BusUnitModel model)throws SystemException {
		ResponseData<BusUnitModel> responseData = responseDataService.getResponseData();
		UserInfoModel user= sessionService.getCurrentUser();

         if (user == null) {
        	responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
    		responseData.add("change_password_invalid_login");
    		return responseData;
        }
        
		try {
			PageInfo<BusUnitModel> pageInfo = busUnitService.select(model);
			responseData.setPage(pageInfo);
			responseData.setErrorType("success");				
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return responseData;
	}
	
	@RequestMapping(value = "/busList", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/json" })
	@ResponseBody
	public List<BusUnitModel> busList(@RequestBody final BusUnitModel model)throws SystemException{
		List<BusUnitModel> list=null;
		//update by lh -8-9
//		BusUnitModel model=new BusUnitModel();
//		model.setBusUnitName(busUnitName);
		try {
			list=busUnitService.search(model);
		} catch (ServiceException e) {
			LOG.error("productInfoVos error:" + e.getMessage(), e);
			throw new SystemException(e.getMessage(),e);
		}
		return list;
		
	}
	
	@RequestMapping(value = "/run", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/json" })
	@ResponseBody
	public ResponseData<String> run(@RequestBody final BusUnitModel busUnitModel) throws SystemException{
		ResponseData<String> responseData=responseDataService.getResponseData();
		busUnitService.run(busUnitModel);
		return responseData;
	}
	
	
	@RequestMapping(value = "/delete", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/json" })
	@ResponseBody
	public ResponseData<BusUnitModel> delete(@RequestBody final BusUnitModel[] models) throws SystemException {
		ResponseData<BusUnitModel> responseData = responseDataService.getResponseData();
		UserInfoModel user= sessionService.getCurrentUser();

        if (user == null) {
        	responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
    		responseData.add("change_password_invalid_login");
    		return responseData;
        }
        
        try {
        	int result = 0;
        	for(BusUnitModel model: models){
        		result = busUnitService.delete(model);
        	}
        	if (result != 0) {
        		List<BusUnitModel> list = busUnitService.search(null);
        		responseData.setReturnDataList(list);
        		responseData.setErrorType(ConstantUtil.ERROR_TYPE_SUCCESS);
        	} else {
        		responseData.add("email_update_failed");
        		responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
        	}
			
        } catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return responseData;

	}
	
	
	
	@RequestMapping(value = "/save", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = { "application/json" })
	@ResponseBody
	public ResponseData<BusUnitModel> save(@RequestBody final BusUnitModel model) throws SystemException {

		ResponseData<BusUnitModel> responseData = responseDataService.getResponseData();
		UserInfoModel user= sessionService.getCurrentUser();

        if (user == null) {
        	responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
    		responseData.add("change_password_invalid_login");
    		return responseData;
        }
        if(model.getBusUnitCode()!=null){
        	model.setLastUpdatedBy(user.getUserCode());
        	model.setBusUnitCode(model.getBusUnitCode().toUpperCase());
        }
        boolean chacknull=busUnitService.chacknull(model);
        try {
        	int result = 0;
			if(model.getCreatedBy() == null){
				responseData=busUnitService.checkBusSave(model);
				if(responseData.getErrorType()==null&&chacknull==false){
					result = busUnitService.insert(model);
					if (result != 0) {
						List<BusUnitModel> list = busUnitService.search(model);
						responseData.setReturnData(list.get(0));
						responseData.setErrorType(ConstantUtil.ERROR_TYPE_SUCCESS);
					} else {
						responseData.add("email_update_failed");
						responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
					}
				}
				
			} else {
				result = busUnitService.update(model);
				if (result != 0) {
//					List<BusUnitModel> list = busUnitService.search(model);
//					list.get(0).setFalg(true);
//					responseData.setReturnData(list.get(0));
					responseData.setErrorType(ConstantUtil.ERROR_TYPE_SUCCESS);
				} else {
					responseData.add("email_update_failed");
					responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
				}
			}
			
        } catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return responseData;

	}
}
