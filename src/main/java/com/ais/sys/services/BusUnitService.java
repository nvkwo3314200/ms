package com.ais.sys.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ais.sys.daos.BusUnitModelMapper;
import com.ais.sys.exception.ServiceException;
import com.ais.sys.exception.SystemException;
import com.ais.sys.models.BusUnitModel;
import com.ais.sys.models.ResponseData;
import com.ais.sys.utils.ConstantUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("busUnitService")
public class BusUnitService extends BaseService{
	private static final Logger LOG = LoggerFactory.getLogger(BusUnitService.class);
	private BusUnitModelMapper busUnitModelMapper;

	public BusUnitModelMapper getBusUnitModelMapper() {
		return busUnitModelMapper;
	}

	@Autowired
	public void setBusUnitModelMapper(BusUnitModelMapper busUnitModelMapper) {
		this.busUnitModelMapper = busUnitModelMapper;
	}

	public List<BusUnitModel> search(BusUnitModel model) throws ServiceException {
		try {
			List<BusUnitModel> list = busUnitModelMapper.search(model);
			if (list != null && list.size() > 0) {
				return list;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return null;
	}
	
	public PageInfo<BusUnitModel> select(BusUnitModel model) throws ServiceException {
		try {
			return PageHelper.startPage(model.getPage(), model.getSize()).doSelectPageInfo(() ->busUnitModelMapper.select(model));		
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}
	
	public String run(BusUnitModel busUnitModel){		
		String a=busUnitModel.getSql().substring(busUnitModel.getSql().length()-1, busUnitModel.getSql().length());		
			if(a.equals(";")){
				String b=busUnitModel.getSql().substring(0, busUnitModel.getSql().length()-1);
				busUnitModel.setSql(b);
				String [] sql=busUnitModel.getSql().split(";");
				BusUnitModel model=new BusUnitModel();
				if(sql.length>0){
					for(int i=0;i<sql.length;i++){
						model.setSql(sql[i]);
						busUnitModelMapper.insertRun(model);
					}				
				}
			}else{			
				busUnitModelMapper.insertRun(busUnitModel);
			}					
		return null;
	}
	
	public ResponseData<BusUnitModel> checkBusSave(BusUnitModel busUnitModel) throws SystemException{
		ResponseData<BusUnitModel> responseData =responseDataService.getResponseData();
		if(busUnitModel.getBusUnitCode()!=null){
			List<BusUnitModel> list = busUnitModelMapper.search(null);
			for(BusUnitModel unitModel:list){
				if(busUnitModel.getBusUnitCode().equals(unitModel.getBusUnitCode())){
					responseData.setErrorType(ConstantUtil.ERROR_TYPE_DANGER);
					responseData.add("bus_add_bus_Unit_code");
					break;
				}
			}
		}
		return responseData;
	}
	
	public boolean chacknull(BusUnitModel model){
		boolean falg=false;
		if(model.getBusUnitCode()==null){
			falg=true;
		}else if (model.getBusUnitName()==null) {
			falg =true;
		}
		return falg;
	}
	
	public int update(BusUnitModel model) throws ServiceException {
		try {
			return busUnitModelMapper.update(model);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	public int delete(BusUnitModel model) throws ServiceException {
		try {
			return busUnitModelMapper.delete(model);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	public int insert(BusUnitModel model) throws ServiceException {
		try {
			return busUnitModelMapper.insert(model);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
