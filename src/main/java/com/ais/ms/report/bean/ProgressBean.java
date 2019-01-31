package com.ais.ms.report.bean;

import java.math.BigDecimal;

public class ProgressBean {
	private BigDecimal progress;
	private boolean cancel;
	
	public BigDecimal getProgress() {
		return progress;
	}
	public void setProgress(BigDecimal progress) {
		this.progress = progress;
	}
	public boolean isCancel() {
		return cancel;
	}
	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}
}
