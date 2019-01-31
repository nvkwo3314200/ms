package com.ais.ms.report.logic;

import java.util.List;

import com.ais.ms.models.ReportDataModel;

public interface ProcessLogic {
	public List<?> doProcess(ReportDataModel reportData);
}
