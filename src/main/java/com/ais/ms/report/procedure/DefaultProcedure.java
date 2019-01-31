package com.ais.ms.report.procedure;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.ais.ms.models.SWorkSheetModel;

public abstract class DefaultProcedure extends BaseProcedure {

	public SXSSFWorkbook doProduce(List<SWorkSheetModel> SWorkSheets) {
		if (SWorkSheets == null || SWorkSheets.isEmpty()) return null;
		
		SWorkbook = new SXSSFWorkbook (RANDOM_ACCESS_WINDOW_SIZE);
		
		int extraSheet = 1;
    	int startRow, startCell;
    	SWorkSheetModel SWorkSheet;
    	
		for(int currentSheet = 0; currentSheet < SWorkSheets.size(); currentSheet++){
			extraSheet--;
			
			SWorkSheet = SWorkSheets.get(currentSheet);
			
			int dateRow = 0;
			int dateSheetSize = 1;
			
			String sheetName = (String) SWorkSheet.getName();
			String sheetTitle = (String) SWorkSheet.getTitle();
			Map<String, String> queryParams = (Map<String, String>) SWorkSheet.getQueryParams();
			List<String[]> headers = (List<String[]>) SWorkSheet.getHeaders();
			List<?> dataList = (List<?>) SWorkSheet.getDataList();
			String password = (String) SWorkSheet.getPassword();
			String cellWidth = (String) SWorkSheet.getDefaultCellWidth();
			
			if(dataList != null && !dataList.isEmpty()) {
				dateSheetSize = (dataList.size() - 1) / sheetMaxRow + 1;
	    	}

			// init style
			initCellStyle();

			initDefaultStyle(currentSheet);

			for(int dataSheet = 0; dataSheet < dateSheetSize; dataSheet++,extraSheet++) {
				
				startRow = defaultRow;
				startCell = defaultCell;
				sheet = SWorkbook.createSheet();
				
				// -10-31 protected cell
				if(StringUtils.isNotEmpty(password)){
					sheet.protectSheet(password); 
				}
				
				// set sheet name
				if(StringUtils.isNotEmpty(sheetName)){
					SWorkbook.setSheetName(currentSheet + extraSheet, dateSheetSize>1?sheetName+"("+(dataSheet+1)+")":sheetName);
				}
				
				// craete title
				startRow = createExcelTitle(currentSheet, sheetTitle, startRow, startCell);
				
				startRow = betweenTitleAndParamsContent(currentSheet, SWorkSheet, startRow, startCell);
				
				// craete query params
				startRow = createExcelQueryParams(currentSheet, queryParams, startRow, startCell);

				// create time
//				createExcelCreatedTime(workbook, currentSheet);
				
				startRow = betweenParamsAndHeaderContent(currentSheet, SWorkSheet, startRow, startCell);
				
				// create headers
				startRow = createExcelHeaders(currentSheet, headers, cellWidth, startRow, startCell);
				
				// freeze cell	-10/18
				sheet.createFreezePane(0, startRow, 0, startRow);   
				
				if(dataList == null || dataList.isEmpty()) continue;
				
				// get sheet data
				List<?> list = dataList.subList(dateRow, (dateRow + sheetMaxRow - startRow) > dataList.size() ? dataList.size() : (dateRow + sheetMaxRow - startRow));
				
				// init next data start key
				dateRow = dateRow + sheetMaxRow - startRow + 1;
				
				// create data
				startRow = createReportDate(currentSheet, list, startRow, startCell);
				
				endContent(currentSheet, SWorkSheet, startRow, startCell);
				
			}
			
			
		}
		return SWorkbook;
	}
	
	/**
	 * @created 2016/10/09
	 * @description create cell other style
	 * @param currentSheet : current sheet key
	 * @param SWorkbook ： excel file
	 * @param cellStyle : style storage spaces
	 */
	public abstract void createOtherStyle(int currentSheet, SXSSFWorkbook SWorkbook, Map<String, CellStyle> cellStyle);
	
	/**
	 * @created 2016/09/29
	 * @description init Workbook default font and style
	 * @param currentSheet : current sheet key
	 */
	public abstract void initDefaultStyle(int currentSheet);
	
	/**
	 * @created 2016/09/29
	 * @description DIY content
	 * @param currentSheet : current sheet key
	 * @param SWorkSheet ： current sheet data
	 * @param startRow : title start row
	 * @param startCell : title start cell
	 * @return next content start row
	 */
	public abstract int betweenTitleAndParamsContent(int currentSheet, SWorkSheetModel SWorkSheet, int startRow, int startCell);

	/**
	 * @created 2016/09/29
	 * @description DIY content
	 * @param currentSheet : current sheet key
	 * @param SWorkSheet ： current sheet data
	 * @param startRow : title start row
	 * @param startCell : title start cell
	 * @return next content start row
	 */
	public abstract int betweenParamsAndHeaderContent(int currentSheet, SWorkSheetModel SWorkSheet, int startRow, int startCell);
	
	/**
	 * @created 2016/09/29
	 * @description DIY end content
	 * @param currentSheet : current sheet key
	 * @param SWorkSheet ： current sheet data
	 * @param startRow : title start row
	 * @param startCell : title start cell
	 * @return next content start row
	 */
	public abstract int endContent(int currentSheet, SWorkSheetModel SWorkSheet, int startRow, int startCell);
	
	/**
	 * @created 2016/09/29
	 * @description create title
	 * @param currentSheet : current sheet key
	 * @param title ： title value
	 * @param startRow : title start row
	 * @param startCell : title start cell
	 * @return next content start row
	 */
	public abstract int createExcelTitle(int currentSheet, String title, int startRow, int startCell);
	
	/**
	 * @created 2016/09/29
	 * @description create report params
	 * @param currentSheet : current sheet key
	 * @param queryParams ： param values
	 * @param startRow : title start row
	 * @param startCell : title start cell
	 * @return next content start row
	 */
	public abstract int createExcelQueryParams(int currentSheet, Map<String, String> queryParams, int startRow, int startCell);
	
	/**
	 * @created 2016/09/29
	 * @description create report headers
	 * @param currentSheet : current sheet key
	 * @param headers ： header values
	 * @param cellWidth : default cell width
	 * @param startRow : header start row
	 * @param startCell : header start cell
	 * @return next content start row
	 */
	public abstract int createExcelHeaders(int currentSheet, List<String[]> headers, String cellWidth, int startRow, int startCell);
	
	/**
	 * @created 2016/09/29
	 * @description create report data
	 * @param currentSheet : current sheet key
	 * @param dataList ： data values
	 * @param startRow : report data  start row
	 * @param startCell : report data start cell
	 * @return next content start row
	 */
	public abstract int createReportDate(int currentSheet, List<?> dataList, int startRow, int startCell);

	
	// read	
	
	public List<?> readExcel(InputStream inputStream, Class<?> clazz) {

    	int startRow = defaultDataStartRow;
    	int startCell = defaultCell;

    	Workbook wb = null;
		List<Object> resultList = new ArrayList<Object>();
		
		try {
			wb = WorkbookFactory.create(inputStream);
			int sheetLength = wb.getNumberOfSheets();
			// init rowNum -10-24
			currentRowNum = 0;
			for(int i = 0; i < sheetLength; i++) {
				allRowNum = allRowNum + wb.getSheetAt(i).getLastRowNum();
			}
			for(int i = 0; i < sheetLength; i++) {
				if(wb.getSheetAt(i).getLastRowNum()>=defaultDataStartRow){
					currentRowNum = currentRowNum + defaultDataStartRow - 1;
					resultList.addAll(readXlsOrXlsxBySheet(wb.getSheetAt(i), clazz, startRow, startCell));
				}
			}
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
	
	/**
	 * @created 2016/09/29
	 * @description read current sheet data
	 * @param sheet : current sheet
	 * @param clazz ： current sheet data model class
	 * @param startRow : sheet data  start row
	 * @param startCell : sheet data start cell
	 * @return current sheet data list
	 */
	public abstract List<?> readXlsOrXlsxBySheet(Sheet sheet, Class<?> clazz, int startRow, int startCell);

}
