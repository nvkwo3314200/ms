package com.ais.sys.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.ais.sys.daos.SystemParametersDao;
import com.ais.sys.exception.ServiceException;
import com.ais.sys.exception.SystemException;
import com.ais.sys.models.ResponseData;
import com.ais.sys.models.SystemParametersModel;
import com.ais.sys.utils.ConstantUtil;
import com.ais.sys.utils.ReportArithmeticUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("systemParametersService")
public class SystemParametersService extends BaseService {
	private static final Logger LOG = LoggerFactory.getLogger(SystemParametersService.class);
	
	private SystemParametersDao systemParametersDao;
	
	@Resource(name = "sessionService")
	SessionService sessionService;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

	public SystemParametersDao getSystemParametersDao() {
		return systemParametersDao;
	}
	
	@Autowired
	public void setSystemParametersDao(SystemParametersDao systemParametersDao) {
		this.systemParametersDao = systemParametersDao;
	}
	
	public PageInfo<SystemParametersModel> searchPage(SystemParametersModel model) throws ServiceException {
		try {
			return PageHelper.startPage(model.getPage(), model.getSize()).doSelectPageInfo(()->systemParametersDao.selectSystemParameters(model));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	public List<SystemParametersModel> searchList(SystemParametersModel model) throws ServiceException {
		try {
			return systemParametersDao.selectSystemParameters(model);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	public List<SystemParametersModel> segmentList(SystemParametersModel model) throws ServiceException {
		try {
			List<SystemParametersModel> list = systemParametersDao.selectSystemParameters(model);
			List<SystemParametersModel> selectList = new ArrayList<>(); 
			if (list != null && list.size() > 0) {
				Set<String> hs = new HashSet<>();
				for(SystemParametersModel selectModel : list){
					if (!hs.contains(selectModel.getSegment())) {
						selectList.add(selectModel);
						hs.add(selectModel.getSegment());
					}
				}
				return selectList;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return null;
	}
	
	public int deleteSystemParameters(SystemParametersModel model) throws ServiceException {
		try {
			return systemParametersDao.deleteSystemParameters(model);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	public boolean chackNull(SystemParametersModel model){
		boolean falg = false;
		if(model != null){
			if(StringUtils.isEmpty(model.getSegment())){
				falg=true;
		 	}
			if(StringUtils.isEmpty(model.getCode())){
				falg=true;
		 	}
			if(model.getDispSeq() == null){
				falg=true;
		 	}
			if(StringUtils.isEmpty(model.getAttrib01())){
				falg=true;
		 	}
			if(StringUtils.isEmpty(model.getActiveInd())){
				falg=true;
		 	}
		}
		return falg;
	}
	
	public ResponseData<SystemParametersModel> checkSysSave(SystemParametersModel model) throws SystemException, ServiceException {
		ResponseData<SystemParametersModel> responseData = responseDataService.getResponseData();
		if(model != null){
			if(StringUtils.isEmpty(model.getCreatedBy())){
				List<SystemParametersModel> list = searchList(null);
				if (list != null && list.size() > 0) {
					for(SystemParametersModel selectModel : list){
						if(model.getId() != null){
							if(selectModel.getId().compareTo(model.getId())!=0 && selectModel.getCode().equals(model.getCode())){
								responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
								responseData.add("system_parameter_code_already_exsits");
							}
						}else{
							if(selectModel.getCode().equals(model.getCode()) && selectModel.getSegment().equals(model.getSegment())){
								responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
								responseData.add("system_parameter_code_already_exsits");
							}
						}
						if(model.getId() != null && selectModel.getId().compareTo(model.getId()) != 0 && 
							selectModel.getSegment().equals(model.getSegment()) && selectModel.getDispSeq().compareTo(model.getDispSeq()) == 0){
							responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
							responseData.add("system_parameter_dispseq_already_exsits");
						}else if(model.getId() == null && 
							selectModel.getSegment().equals(model.getSegment()) && selectModel.getDispSeq().compareTo(model.getDispSeq()) == 0){
							responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
							responseData.add("system_parameter_dispseq_already_exsits");
						}
					}
				}
			}
		}
		return responseData;
	}
	
	public void addUser(SystemParametersModel model, ResponseData<SystemParametersModel> responseData) throws ServiceException {
		try {
			if (responseData.getErrorType() == null || !responseData.getErrorType().equals(ConstantUtil.ERROR_TYPE_DANGER)) {
				if (StringUtils.isEmpty(model.getCreatedBy())) {	//model.getId() == null
					insertSysPare(model);
				} else {
					updateSysPare(model);
				}
				responseData.setErrorType("success");
			}
			responseData.setReturnData(model);
		} catch (Exception e) {
			LOG.error("userVo not save:" + e.getMessage(), e);
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	public String saveUpload(List<SystemParametersModel> sysList, String threadId){
		//lh
		BigDecimal progress = new BigDecimal("0");
		initCache(threadId);
		List<SystemParametersModel> dbList = null;
		Set<String> hs = new HashSet<String>();
		try {
			if(sysList!=null && sysList.size()>0){
				for(int i=0;i<sysList.size();i++){
					if(cancel){
						initProgress();
						System.out.println("事务已终止！");
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return new String(messageSource.getMessage("report_import_fail", null, LocaleContextHolder.getLocale()));
					}
					progress = ReportArithmeticUtil.divide((i+1)+"",sysList.size()+"");
//					System.out.println("当前上传进度："+progress+".");
					updateProgress(threadId, progress);
					
					SystemParametersModel searchModel = new SystemParametersModel();
					searchModel.setSegment(sysList.get(i).getSegment());
					dbList = this.searchList(searchModel);
					if(dbList!=null && dbList.size()>0){
						if(!hs.contains(sysList.get(i).getSegment())){
							this.deleteAll(searchModel);
							hs.add(sysList.get(i).getSegment());
						}
					}
					this.insertSysPare(sysList.get(i));
				}
			}
		} catch (ServiceException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public int insertSysPare(SystemParametersModel model) {
		model.setCreatedBy(sessionService.getCurrentUser().getUserCode());
		model.setCreatedDate(new Date());
		model.setLastUpdatedBy(sessionService.getCurrentUser().getUserCode());
		model.setLastUpdatedDate(new Date());
		return systemParametersDao.insertSystemParameters(model);
	}
	
	public int updateSysPare(SystemParametersModel model) {
		int inserSupper = 0;
		model.setLastUpdatedBy(sessionService.getCurrentUser().getUserCode());
		model.setLastUpdatedDate(new Date());
		inserSupper = systemParametersDao.updateSystemParameters(model);
		return inserSupper;
	}
	
	public int deleteAll(SystemParametersModel model) throws ServiceException {
		try {
			return systemParametersDao.deleteAll(model);
		} catch (Exception e) {
			LOG.error("userVo not save:" + e.getMessage(), e);
			throw new ServiceException(e.getMessage(), e);
		}
	}
}
