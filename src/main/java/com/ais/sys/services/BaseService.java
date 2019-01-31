package com.ais.sys.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ais.ms.report.bean.ProgressBean;

@Service
public class BaseService {
	
	// -lh-10-24
	protected BigDecimal progress;
	
	protected boolean cancel;
	
	protected Map<String , ProgressBean> cache = new HashMap<String, ProgressBean>(); 
	
	@Resource
	protected  ResponseDataService responseDataService;
	
	@Resource
	protected SessionService sessionService;

	public BigDecimal getProgress() {
		if(progress==null) new BigDecimal("0");
		return progress;
	}

	public void setProgress(BigDecimal progress) {
		this.progress = progress;
	}
	
	public void initProgress() {
		this.progress = new BigDecimal("0");
		this.cancel = false;
	}
	
	public void cancelProgress() {
		this.cancel = true;
	}
	
	public void initCache(String threadId) {
		ProgressBean progressBean = new ProgressBean();
		progressBean.setProgress(new BigDecimal("0"));
		progressBean.setCancel(false);
		cache.put(threadId, progressBean);
	}
	
	protected void updateProgress(String threadId, BigDecimal progress) {
		ProgressBean progressBean = cache.get(threadId);
		if(progressBean != null) progressBean.setProgress(progress);
	}
	
	protected void removeThread(String threadId){
		cache.remove(threadId);
	}
	
	public BigDecimal getProgress(String threadId) {
		ProgressBean progressBean = cache.get(threadId);
		return progressBean.getProgress();
	}
}
