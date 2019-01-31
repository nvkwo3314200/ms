package com.ais.ms.report;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ais.ms.models.ReportDataModel;
import com.ais.ms.models.SWorkSheetModel;
import com.ais.ms.report.logic.ProcessLogic;
import com.ais.ms.report.procedure.BaseProcedure;
import com.ais.ms.report.procedure.ProduceProcedure;

public class ReportHandler {
	
	private ReportDataModel reportData;

	// map : title, params, headers, data
	private List<SWorkSheetModel> SWorkSheets;
	
	private ProcessLogic processLogic;
	
	private BaseProcedure produceProcedure;

	private Logger LOG = LoggerFactory.getLogger(ReportHandler.class);
	
	public ReportHandler(){
		initReportData();
	}
	
	/**
	 * @description the report data processing
	 */
	public List<?> doProcess() {
		if (reportData == null && processLogic ==null) return null;
		return processLogic.doProcess(reportData);
	}
	
	/**
	 * @description create SWorkbook
	 */
	public SXSSFWorkbook doProduce() {
		if (produceProcedure == null && (SWorkSheets == null || SWorkSheets.isEmpty())) return null;
		return produceProcedure.doProduce(SWorkSheets);
	}
	
	/**
	 * @description read excel
	 */
	public List<?> readExcel(InputStream inputStream, Class<?> clazz) {
		if (produceProcedure == null) return null;
		return produceProcedure.readExcel(inputStream, clazz);
	}
	
	/**
	 * @deprecated
	 */
	public void downloadXlsFile(HttpServletResponse response, Workbook wb, String fileName) {
		ServletOutputStream out = null;
		try {
//			HSSFWorkbook wb = (HSSFWorkbook)file;
			if(wb == null){
				return;
			}
			response.setHeader("Pragma", "No-cache");  
			response.setHeader("Cache-Control", "No-cache");  
			response.setDateHeader("Expires", 0);
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", fileName);
			out = response.getOutputStream();
			wb.write(out);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			try {
				if(out != null) {
					out.close();
				}
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}
	
	public void downloadCurrentXlsFile(OutputStream out) {
		produceProcedure.downloadCurrentXlsFile(out);
	}
	
	public void closeCurrentSWorkbook() {
		produceProcedure.closeCurrentSWorkbook();
	}
	
	public void clearSWorkSheet() {
		initSWorkSheet();
	}
	
	public void initSWorkSheet() {
		SWorkSheets =  new ArrayList<SWorkSheetModel>();
	}

	public void addSWorkSheet(SWorkSheetModel sWorkSheet) {
		checkSWorkSheets();
		SWorkSheets.add(sWorkSheet);
	}
	
	public void addSWorkSheets(List<SWorkSheetModel> sWorkSheets) {
		checkSWorkSheets();
		SWorkSheets.addAll(sWorkSheets);
	}
	
	public void checkSWorkSheets(){
		if(SWorkSheets==null){
			initSWorkSheet();
		}
	}
	
	public void setProcessLogic(ProcessLogic processLogic) {
		this.processLogic = processLogic;
	}
	
	public void setProduceProcedure(BaseProcedure produceProcedure) {
		this.produceProcedure = produceProcedure;
	}
	
	public BaseProcedure getProduceProcedure() {
		return this.produceProcedure;
	}
	
	public void initReportData() {
		reportData = new ReportDataModel();
		produceProcedure = new ProduceProcedure();
//		reportData.setStrObject(new HashMap<String, Object>());
	}

	public ReportDataModel getReportData() {
		return reportData;
	}

	public void setReportData(ReportDataModel reportData) {
		this.reportData = reportData;
	}

	public List<SWorkSheetModel> getSWorkSheets() {
		return SWorkSheets;
	}

	public void setSWorkSheets(List<SWorkSheetModel> sWorkSheets) {
		SWorkSheets = sWorkSheets;
	}
	
	// set|get excel default parameter -start-
	public int getDefaultRow() {
		if(produceProcedure == null) return 0;
		return produceProcedure.getDefaultRow();
	}

	public void setDefaultRow(int defaultRow) {
		if(produceProcedure == null) return;
		produceProcedure.setDefaultRow(defaultRow);
	}

	public int getDefaultCell() {
		if(produceProcedure == null) return 0;
		return produceProcedure.getDefaultCell();
	}

	public void setDefaultCell(int defaultCell) {
		if(produceProcedure == null) return;
		produceProcedure.setDefaultCell(defaultCell);
	}

	public int getDefaultDataStartRow() {
		if(produceProcedure == null) return 0;
		return produceProcedure.getDefaultDataStartRow();
	}

	public void setDefaultDataStartRow(int defaultDataStartRow) {
		if(produceProcedure == null) return;
		produceProcedure.setDefaultDataStartRow(defaultDataStartRow);
	}

	public int getSheetMaxRow() {
		if(produceProcedure == null) return 0;
		return produceProcedure.getSheetMaxRow();
	}

	/**
	 * @param  sheetMaxRow : max = 50000
	 */
	public void setSheetMaxRow(int sheetMaxRow) {
		if(produceProcedure == null) return;
		produceProcedure.setSheetMaxRow(sheetMaxRow);
	}
	// set|get excel default parameter -end-
	
	// 10-24
	public String getRedProgress() {
		return produceProcedure.getRedProgress();
	}
	
	public void initRedProgress() {
		produceProcedure.initRedProgress();
	}
	
}
