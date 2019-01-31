package com.ais.sys.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ais.ms.report.annotation.ReportColumn;

public final class ExcelUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExcelUtil.class);
	
//	private static final String NUMBER_REX = "^(-?\\d+)(\\.\\d*)?$";
	
//	private static final String DATE_REX = "^(\\d{5,6})$";
	
	private static final String DEFAULT_EXCEL_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	
	public static final int SHEET_MAX_ROW = 50000;
	
	public static final int RANDOM_ACCESS_WINDOW_SIZE = 1000;
	
	public static final int DEFAULT_ROW = 0;

	public static final int DEFAULT_CELL = 0;
	
	public static final int DEFAULT_DATA_ROW = 10;
	
	private ExcelUtil() {}
	
	public static Workbook exportExcel(String title, Map<String, String> queryParams, List<String[]> headers, List<?> reportData) {
		SXSSFWorkbook workbook = new SXSSFWorkbook (RANDOM_ACCESS_WINDOW_SIZE);
		createReport(workbook, title, queryParams, headers, reportData);
		return workbook;
	}
	
//	public static Workbook exportExcel(String title, Map<String, String> queryParams, String[] headers1, String[] headers2, List<?> reportData) {
//		SXSSFWorkbook workbook = new SXSSFWorkbook (RANDOM_ACCESS_WINDOW_SIZE);
//		createReport(workbook, title, queryParams, headers1, headers2, reportData);
//		return workbook;
//	}
	
	public static Workbook exportExcelTemplate(String template) {
		Workbook wb = null;
		try {
			File templateFile = new File(template);
			wb =  WorkbookFactory.create(templateFile);
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wb;
	}
	
	private static boolean isPrimitive(Class<?> clazz) {
		if(clazz == null) return false;
		return (
//				clazz.isPrimitive() ||
				clazz == String.class ||
				clazz == Double.class ||
				clazz == Integer.class ||
				clazz == Date.class ||
				clazz == Long.class ||
				clazz == Float.class ||
//				clazz == Character.class ||
				clazz == Short.class ||
				clazz == Boolean.class ||
//				clazz == boolean.class ||
				clazz == Byte.class
				);
	}
	
	private static Field[] parseAimFields(Class<?> clazz) {
		int fieldIndex = 0;
		Field [] fields = clazz.getDeclaredFields();
		Field [] annotationFields = new Field [0];
		
		//all fields should be set in one excel
		if(clazz.isAnnotationPresent(ReportColumn.class)) {
			annotationFields = fields;
		} else { //some given fileds should be set in one excel
			for (Field field : fields) {
				if(field.isAnnotationPresent(ReportColumn.class)) {
					annotationFields = Arrays.copyOf(annotationFields, annotationFields.length + 1);
					annotationFields[fieldIndex++] = field;
				}
			}
		}
		
		return annotationFields;
	}
	
	private static int createInnerCell(List<?> cellsModelList, Field outfield, Row row, int startCell, CellStyle styleBold, int [] columnMaxLength, int columnIndex) throws IllegalArgumentException, IllegalAccessException {
		if(cellsModelList != null && cellsModelList.size() > 0) {
			Cell cell = null;
			
			Type fc = outfield.getGenericType();
			if (fc instanceof ParameterizedType) {// if generic parameter type
	            ParameterizedType pt = (ParameterizedType) fc;
	            Class<?> fieldClazz = (Class<?>) pt.getActualTypeArguments()[0]; //get generic parameter type
	            int currentColLen = 1;
	            
	            if(isPrimitive(fieldClazz)) {
	            	
	            	if(fieldClazz == String.class) {
	            		for (Object model : cellsModelList) {
	            			
		            		cell = row.createCell(startCell++);
		            		if(cell == null) continue;
		            		
		            		if(model != null) {
		            			currentColLen = model.toString().getBytes().length;
		            			cell.setCellValue(model.toString());
		            		} else {
		            			currentColLen = 1;
		            		}
		            		columnMaxLength[columnIndex] = columnMaxLength[columnIndex] >= currentColLen ? columnMaxLength[columnIndex] : currentColLen;
	    					columnIndex++;
		            		cell.setCellStyle(styleBold);
						}
        			} else if (fieldClazz == Integer.class) {
        				
        				for (Object model : cellsModelList) {
		            		cell = row.createCell(startCell++);
		            		if(cell == null) continue;
		            		
		            		if(model != null) {
		            			currentColLen = model.toString().getBytes().length;
		            			cell.setCellValue(Integer.valueOf(model.toString()));
		            		} else {
		            			currentColLen = 1;
		            		}
		            		columnMaxLength[columnIndex] = columnMaxLength[columnIndex] >= currentColLen ? columnMaxLength[columnIndex] : currentColLen;
	    					columnIndex++;
		            		cell.setCellStyle(styleBold);
						}
	   				} else if(fieldClazz == Double.class) {
	   					for (Object model : cellsModelList) {
		            		cell = row.createCell(startCell++);
		            		if(cell == null) continue;
		            		
		            		if(model != null) {
		            			currentColLen = model.toString().getBytes().length;
		            			cell.setCellValue(Double.valueOf(model.toString()));
		            		} else {
		            			currentColLen = 1;
		            		}
		            		columnMaxLength[columnIndex] = columnMaxLength[columnIndex] >= currentColLen ? columnMaxLength[columnIndex] : currentColLen;
	    					columnIndex++;
		            		cell.setCellStyle(styleBold);
						}
	   				} else if(fieldClazz == Float.class) {
	   					for (Object model : cellsModelList) {
		            		cell = row.createCell(startCell++);
		            		if(cell == null) continue;
		            		
		            		if(model != null) {
		            			currentColLen = model.toString().getBytes().length;
		            			cell.setCellValue(Float.valueOf(model.toString()));
		            		} else {
		            			currentColLen = 1;
		            		}
		            		columnMaxLength[columnIndex] = columnMaxLength[columnIndex] >= currentColLen ? columnMaxLength[columnIndex] : currentColLen;
	    					columnIndex++;
		            		cell.setCellStyle(styleBold);
						}
	   				} else if(fieldClazz == Long.class) {
	   					for (Object model : cellsModelList) {
		            		cell = row.createCell(startCell++);
		            		if(cell == null) continue;
		            		
		            		if(model != null) {
		            			currentColLen = model.toString().getBytes().length;
		            			cell.setCellValue(Long.valueOf(model.toString()));
		            		} else {
		            			currentColLen = 1;
		            		}
		            		columnMaxLength[columnIndex] = columnMaxLength[columnIndex] >= currentColLen ? columnMaxLength[columnIndex] : currentColLen;
	    					columnIndex++;
		            		cell.setCellStyle(styleBold);
						}
	   				} else if(fieldClazz == Byte.class) {
	   					for (Object model : cellsModelList) {
		            		cell = row.createCell(startCell++);
		            		if(cell == null) continue;
		            		
		            		if(model != null) {
		            			currentColLen = model.toString().getBytes().length;
		            			cell.setCellValue(Byte.valueOf(model.toString()));
		            		} else {
		            			currentColLen = 1;
		            		}
		            		columnMaxLength[columnIndex] = columnMaxLength[columnIndex] >= currentColLen ? columnMaxLength[columnIndex] : currentColLen;
	    					columnIndex++;
		            		cell.setCellStyle(styleBold);
						}
	   				} else if (fieldClazz == Date.class) {
//	   					String pattern = DEFAULT_EXCEL_DATE_PATTERN;
//	   					if(outfield.isAnnotationPresent(ExcelDateFormat.class)) {
//		   					ExcelDateFormat dateFormatAnnotation = outfield.getAnnotation(ExcelDateFormat.class);
//		   					pattern = dateFormatAnnotation.pattern();
//		   				}
	   					String pattern = outfield.getAnnotation(ReportColumn.class).dateFormat();
	   					for (Object model : cellsModelList) {
		            		cell = row.createCell(startCell++);
		            		String dateStr = "";
		            		if(cell == null) continue;
		            		
		            		if(model != null) {
		            			dateStr = com.ais.sys.utils.DateUtil.format((Date) model, pattern);
		            			cell.setCellValue(dateStr);
		            		}
		            		currentColLen = dateStr.getBytes().length;
		            		columnMaxLength[columnIndex] = columnMaxLength[columnIndex] >= currentColLen ? columnMaxLength[columnIndex] : currentColLen;
	    					columnIndex++;
		            		cell.setCellStyle(styleBold);
						}
	   				} else if(fieldClazz == Boolean.class) {
	   					for (Object model : cellsModelList) {
		            		cell = row.createCell(startCell++);
		            		if(cell == null) continue;
		            		
		            		if(model != null) {
		            			currentColLen = model.toString().getBytes().length;
		            			cell.setCellValue(Boolean.valueOf(model.toString()));
		            		} else {
		            			currentColLen = 1;
		            		}
		            		columnMaxLength[columnIndex] = columnMaxLength[columnIndex] >= currentColLen ? columnMaxLength[columnIndex] : currentColLen;
	    					columnIndex++;
		            		cell.setCellStyle(styleBold);
						}
	   				}
	            } else {
	            	throw new RuntimeException("Class can only be Primitive or packaging group or java.util.List which can only include Primitive!");
	            }
			}
		}
		return columnIndex;
	}
	
	private static void createReport(
			Workbook workbook,
			String title,
			Map<String, String> queryParams,
			List<String[]> headers,
			List<?> dataList){
		
		if(workbook == null) return;
		
		//init cell style
		Font fontBold = workbook.createFont();
    	fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
    	fontBold.setFontHeightInPoints((short) 10);
    	fontBold.setFontName("SimSun");
    	
		CellStyle styleBold = workbook.createCellStyle();
    	styleBold.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    	styleBold.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    	styleBold.setBorderRight(HSSFCellStyle.BORDER_THIN);
    	styleBold.setBorderTop(HSSFCellStyle.BORDER_THIN);
    	styleBold.setFont(fontBold);
    	//set data
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;
		
		try {
			int sheetSize = 1;
			int currentSheet = 0;
			
			//copy sheet
//			if(workbook.getNumberOfSheets() < sheetSize) {
//				for(currentSheet = workbook.getNumberOfSheets(); currentSheet < sheetSize; currentSheet++) {
//					workbook.createSheet();
//				}
//			}
//			for(currentSheet = 1; currentSheet < sheetSize; currentSheet++) {
//				try {
//					copySheet(workbook.getSheetAt(currentSheet), workbook.getSheetAt(0), workbook, workbook);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
			
			Class<?> c = null;
	    	Field [] aimFields = null;
	    	int [] columnMaxLength = null;
	    	int i, j, nextIndex, startRow, startCell;
	    	
	    	if(dataList != null && !dataList.isEmpty()) {
	    		c = dataList.get(0).getClass();
		    	
		    	if(c == null) throw new RuntimeException("this model class can't be null!");
		    	
		    	aimFields = parseAimFields(c);// parse aim fields
		    	
		    	sheetSize = (dataList.size() - 1) / SHEET_MAX_ROW + 1;
	    	}
			
			for(currentSheet = 0; currentSheet < sheetSize; currentSheet++) {
//				sheet = workbook.getSheetAt(currentSheet);
//				((SXSSFSheet)sheet).setRandomAccessWindowSize(RANDOM_ACCESS_WINDOW_SIZE);
				
				startRow = DEFAULT_ROW;
				startCell = DEFAULT_CELL;
				
				sheet = workbook.createSheet();
				//create title
				startRow = createExcelTitle(workbook, currentSheet, title, startRow, startCell);
				
				//craete query params

				if(queryParams!=null){
					startRow++;
					startRow = createExcelQueryParams(workbook, currentSheet, queryParams, startRow, startCell);
				}
				//create time
//				createExcelCreatedTime(workbook, currentSheet);
				//create headers
				if(headers!=null&&headers.size()>0){
					startRow++;
					startRow = createExcelHeaders(workbook, currentSheet, headers, startRow, startCell);
				}
				//create data
				if(dataList == null || dataList.isEmpty()) return;
				columnMaxLength = new int [aimFields.length];
				i = 0;
				startCell = 0;
				
				nextIndex = (currentSheet + 1) * SHEET_MAX_ROW;
				List<?> list = dataList.subList(currentSheet * SHEET_MAX_ROW, nextIndex > dataList.size() ? dataList.size() : nextIndex);
				
				if(list != null && !list.isEmpty()) {
			    	
			    	for (Object model : list) {
//			    		c = model.getClass();
//			    		
//			    		if(c == null) throw new RuntimeException("this model class can't be null!");
//			    		
//			    		aimFields = parseAimFields(c);// parse aim fields
//			    		columnMaxLength = new int [aimFields.length];
			    		
			    		row = sheet.createRow(startRow + i);
			    		
			    		if(model == null) continue; // blank line
			    		
			    		j = startCell;
			    		int columnIndex = 0;
			    		int currentColLen = -1;
			    		
			    		for (Field field : aimFields) {
				   			field.setAccessible(true);
				   			Class<?> clazz = field.getType();
				   			Object o = field.get(model);
				    		
			    			if(isPrimitive(clazz)) {
			    				cell = row.createCell(j);
			    				
			    				if(cell == null) continue;
			    				
			    				if(clazz == String.class) {
			    					if(o != null) {
			    						currentColLen = o.toString().getBytes().length;
			    						cell.setCellValue(o.toString());
			    					} else {
			    						currentColLen = 1;
			    					}
			    					columnMaxLength[columnIndex] = columnMaxLength[columnIndex] >= currentColLen ? columnMaxLength[columnIndex] : currentColLen;
			    					columnIndex++;
			    					cell.setCellStyle(styleBold);
			        			} else if (clazz == Integer.class) {
			        				if(o != null) {
			        					currentColLen = o.toString().getBytes().length;
			        					cell.setCellValue(Integer.valueOf(o.toString()));
			    					} else {
			    						currentColLen = 1;
			    					}
			        				columnMaxLength[columnIndex] = columnMaxLength[columnIndex] >= currentColLen ? columnMaxLength[columnIndex] : currentColLen;
			    					columnIndex++;
			        				cell.setCellStyle(styleBold);
				   				} else if(clazz == Double.class) {
				   					if(o != null) {
				   						currentColLen = o.toString().getBytes().length;
				   						cell.setCellValue(Double.valueOf(o.toString()));
			    					} else {
			    						currentColLen = 1;
			    					}
				   					columnMaxLength[columnIndex] = columnMaxLength[columnIndex] >= currentColLen ? columnMaxLength[columnIndex] : currentColLen;
			    					columnIndex++;
				   					cell.setCellStyle(styleBold);
				   				} else if (clazz == Date.class) {
				   					String dateStr = "";
				   					if(o != null) {
//				   						if(field.isAnnotationPresent(ExcelDateFormat.class)) {
//						   					ExcelDateFormat dateFormatAnnotation = field.getAnnotation(ExcelDateFormat.class);
//						   					String pattern = dateFormatAnnotation.pattern();
//						   					dateStr = com.ais.sys.utils.DateUtil.format((Date) o, pattern);
//						   				} else {
//						   					dateStr = com.ais.sys.utils.DateUtil.format((Date) o, DEFAULT_EXCEL_DATE_PATTERN);
//						   				}
				   						dateStr = com.ais.sys.utils.DateUtil.format((Date) o, field.getAnnotation(ReportColumn.class).dateFormat());
				   						cell.setCellValue(dateStr);
				   					}
				   					currentColLen = dateStr.getBytes().length;
				   					columnMaxLength[columnIndex] = columnMaxLength[columnIndex] >= currentColLen ? columnMaxLength[columnIndex] : currentColLen;
			    					columnIndex++;
				   					cell.setCellStyle(styleBold);
				   				} else if(clazz == Float.class) {
				   					if(o != null) {
				   						currentColLen = o.toString().getBytes().length;
				   						cell.setCellValue(Float.valueOf(o.toString()));
			    					} else {
			    						currentColLen = 1;
			    					}
				   					columnMaxLength[columnIndex] = columnMaxLength[columnIndex] >= currentColLen ? columnMaxLength[columnIndex] : currentColLen;
			    					columnIndex++;
				   					cell.setCellStyle(styleBold);
				   				} else if(clazz == Long.class) {
				   					if(o != null) {
				   						currentColLen = o.toString().getBytes().length;
				   						cell.setCellValue(Long.valueOf(o.toString()));
			    					} else {
			    						currentColLen = 1;
			    					}
				   					columnMaxLength[columnIndex] = columnMaxLength[columnIndex] >= currentColLen ? columnMaxLength[columnIndex] : currentColLen;
			    					columnIndex++;
				   					cell.setCellStyle(styleBold);
				   				} else if(clazz == Byte.class) {
				   					if(o != null) {
				   						currentColLen = o.toString().getBytes().length;
				   						cell.setCellValue(Byte.valueOf(o.toString()));
			    					} else {
			    						currentColLen = 1;
			    					}
				   					columnMaxLength[columnIndex] = columnMaxLength[columnIndex] >= currentColLen ? columnMaxLength[columnIndex] : currentColLen;
			    					columnIndex++;
				   					cell.setCellStyle(styleBold);
				   				} else if(clazz == Boolean.class) {
					   				if(o != null) {
					   					currentColLen = o.toString().getBytes().length;
					   					cell.setCellValue(Boolean.valueOf(o.toString()));
					   				} else {
			    						currentColLen = 1;
			    					}
				   					columnMaxLength[columnIndex] = columnMaxLength[columnIndex] >= currentColLen ? columnMaxLength[columnIndex] : currentColLen;
			    					columnIndex++;
					   				cell.setCellStyle(styleBold);
					   			}
			    			} else if(clazz.isAssignableFrom(List.class)) {
			    				if(o != null) {
			    					List<?> fieldList = (List<?>)o;
			    					if(i == 0) columnMaxLength = Arrays.copyOf(columnMaxLength, columnMaxLength.length + fieldList.size() - 1);
			    					j = createInnerCell(fieldList, field, row, j, styleBold, columnMaxLength, columnIndex) - 1;
			    					columnIndex += fieldList.size();
			    				}
			    			} else {
					    		throw new RuntimeException("Class can only be Primitive or packaging group or java.util.List which can only include Primitive!");
					    	}
			   				j++;
			    		}
			    		i++;
			    	}
			    	for(i = 0; i < columnMaxLength.length; i++) {
			    		int curWidth = sheet.getColumnWidth(i);
			    		int valWith = columnMaxLength[i] << 9;
			    		sheet.setColumnWidth(i, curWidth > valWith ? curWidth : valWith);
			    	}
			    }
//				sheet.shiftRows(startRow, endRow, n);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private static int createExcelHeaders(Workbook workbook, int currentSheetIndex, List<String[]> headers, 
			int targetRow, int targetCell) {
		Sheet currentSheet = workbook.getSheetAt(currentSheetIndex);
    	
		Font fontBold2 = workbook.createFont();
    	fontBold2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
    	fontBold2.setFontHeightInPoints((short) 10);
    	fontBold2.setFontName("SimSun");
    	CellStyle styleBold2 = workbook.createCellStyle();
    	styleBold2.setFont(fontBold2);
    	styleBold2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleBold2.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
//    	styleBold2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//    	styleBold2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//    	styleBold2.setBorderRight(HSSFCellStyle.BORDER_THIN);
//    	styleBold2.setBorderTop(HSSFCellStyle.BORDER_THIN);
    	
    	Font fontBold = workbook.createFont();
    	fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
    	fontBold.setFontHeightInPoints((short) 10);
    	fontBold.setFontName("SimSun");
    	CellStyle styleBold = workbook.createCellStyle();
    	styleBold.setFont(fontBold);
		styleBold.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleBold.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
//    	styleBold.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//    	styleBold.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//    	styleBold.setBorderRight(HSSFCellStyle.BORDER_THIN);
//    	styleBold.setBorderTop(HSSFCellStyle.BORDER_THIN);

		if(targetRow+headers.size() < DEFAULT_DATA_ROW){
			targetRow = DEFAULT_DATA_ROW-headers.size();
		}
    	
		for(String[] header : headers){
			int cellIndex = 0;
    		Row row = currentSheet.createRow(targetRow);
    		Cell cell = null;
    		for(String text : header){
    			cell = row.createCell(cellIndex);
    			if(text.indexOf("*")!=-1){
    				text=text.replace("*","");
					cell.setCellStyle(styleBold2);
				}else{
					cell.setCellStyle(styleBold);
				}
				cell.setCellValue(text);
				currentSheet.setColumnWidth(cellIndex++, text.getBytes().length << 9);
    		}
    		targetRow++;
		}
		return targetRow;
	}

//	private static void createExcelCreatedTime(Workbook workbook,
//			int currentSheetIndex) {
//		
//		Sheet currentSheet = workbook.getSheetAt(currentSheetIndex);
//		
//		Row row = currentSheet.createRow(currentSheet.getLastRowNum() + 2);
//		Cell cell = row.createCell(0);
//		
//		Font fontBold = workbook.createFont();
//    	fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
//    	fontBold.setFontHeightInPoints((short) 10);
//    	fontBold.setFontName("SimSun");
//		CellStyle styleBold = workbook.createCellStyle();
//    	styleBold.setFont(fontBold);
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  HH:mm:ss", Locale.ENGLISH);
//    	cell.setCellValue("As at  "+ sdf.format(new Date()));
//    	cell.setCellStyle(styleBold);
//	}

	private static int createExcelQueryParams(Workbook workbook,
			int currentSheetIndex, Map<String, String> queryParams,
			int targetRow, int targetCell) {
		if(targetRow <= -1 || targetCell <= -1) throw new IllegalArgumentException("startQueryParamsRow and startQueryParamsCell must >= 0"); 
		Sheet currentSheet = workbook.getSheetAt(currentSheetIndex);
		Row row = null;
		Cell cell = null;
		String key = null;
		String value = null;
		
		Font fontBold = workbook.createFont();
    	fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
    	fontBold.setFontHeightInPoints((short) 10);
    	fontBold.setFontName("SimSun");
		CellStyle styleBold = workbook.createCellStyle();
    	styleBold.setFont(fontBold);
		
		List<String> list1 = new ArrayList<>();
		List<String> list2 = new ArrayList<>();
		int i=0;
		if(queryParams != null && !queryParams.isEmpty()) {
			for (Map.Entry<String, String> entry : queryParams.entrySet()) {
				key = entry.getKey();
				value = entry.getValue();
//				if(key != null) {
//					if(i<5){
//						list1.add(key);
//					}else{
//						list2.add(key);
//					}
					i++;
//				}
				if(i%2!=0){
					row = currentSheet.createRow(targetRow);
				}else{
					row = currentSheet.getRow(targetRow);
					targetRow++;
				}
				
				if(i%2==0){
					cell = row.createCell(targetCell + 3);
					cell.setCellValue(key);
					cell.setCellStyle(styleBold);
					if(queryParams.get(key) != null) {
						cell = row.createCell(targetCell + 4);
						cell.setCellValue(value);
						cell.setCellStyle(styleBold);
					}
				}else{
					cell = row.createCell(targetCell);
					cell.setCellValue(key);
					cell.setCellStyle(styleBold);
					if(queryParams.get(key) != null) {
						cell = row.createCell(targetCell + 1);
						cell.setCellValue(value);
						cell.setCellStyle(styleBold);
					}
				}
			}
//			for(int j=0;j<list1.size();j++){	//j<5
//				row = currentSheet.createRow(rowIndex++);
//				if(list1.size()>j){
//					cell = row.createCell(startQueryParamsCell);
//					cell.setCellValue(list1.get(j));
//					cell.setCellStyle(styleBold);
//					if(queryParams.get(list1.get(j)) != null) {
//						cell = row.createCell(startQueryParamsCell + 1);
//						cell.setCellValue(queryParams.get(list1.get(j)));
//						cell.setCellStyle(styleBold);
//					}
//					
//				}
//				if(list2.size()>j){
//					cell = row.createCell(startQueryParamsCell+3);
//					cell.setCellValue(list2.get(j));
//					cell.setCellStyle(styleBold);
//					if(queryParams.get(list2.get(j)) != null) {
//						cell = row.createCell(startQueryParamsCell + 4);
//						cell.setCellValue(queryParams.get(list2.get(j)));
//						cell.setCellStyle(styleBold);
//					}
//				}
//			}
		}
		return targetRow;
	}

	private static int createExcelTitle(Workbook workbook, int currentSheetIndex,
			String title, int targetRow, int targetCell) {
		
		if(targetRow <= -1 || targetCell <= -1) throw new IllegalArgumentException("targetRow and targetCell must >= 0");
		
		Sheet currentSheet = workbook.getSheetAt(currentSheetIndex);
		Row row = null;
		Cell cell = null;
		
		if(title != null) {
			Font fontBold = workbook.createFont();
	    	fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
	    	fontBold.setFontHeightInPoints((short) 10);
	    	fontBold.setFontName("SimSun");
			CellStyle styleBold = workbook.createCellStyle();
	    	styleBold.setFont(fontBold);
	    	
			row = currentSheet.createRow(targetRow);
			cell = row.createCell(targetCell);
			cell.setCellValue(title);
			cell.setCellStyle(styleBold);
			

			cell = row.createCell(targetCell+1);
			cell.setCellValue("\u5c0e\u51fa");
			cell.setCellStyle(styleBold);
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  HH:mm:ss", Locale.ENGLISH);
			cell = row.createCell(targetCell+4);
	    	cell.setCellValue("As at  "+ sdf.format(new Date()));
	    	cell.setCellStyle(styleBold);
	    	
	    	targetRow++;
		}
		return targetRow;
	}

	public static List<?> readExcel(InputStream in, Class<?> clazz, int startRow, int startCell) {
		Workbook wb = null;
		List<Object> resultList = new ArrayList<Object>();
		try {
			wb = WorkbookFactory.create(in);
			int sheetLength = wb.getNumberOfSheets();
			for(int i = 0; i < sheetLength; i++) {
				resultList.addAll(readXlsOrXlsxBySheet(wb.getSheetAt(i), clazz, startRow, startCell));
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
	
	private static String getCellValue(Cell cell) {
		int cellType = cell.getCellType();
		String value = null;
		
		switch (cellType) {
		case Cell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if(DateUtil.isCellDateFormatted(cell)) {
				value = com.ais.sys.utils.DateUtil.format(cell.getDateCellValue(), DEFAULT_EXCEL_DATE_PATTERN);
			} else {
				DecimalFormat df = new DecimalFormat("########################.########################");
				value = df.format(cell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_BLANK:
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			value = cell.getBooleanCellValue()+"";
			break;
		case Cell.CELL_TYPE_ERROR:
			break;
		case Cell.CELL_TYPE_FORMULA:
			value = cell.getCellFormula();
			break;
		default:
			break;
		}
		return value;
	}
	
	private static int readExcelToList(Object target, Field outerField, Row row, int cellIndex) {
		Cell cell = null;
		int cpacity = 12;
		
	
		
		if(cpacity < 0) throw new RuntimeException("cpacity can't be a minus");
		
		//list<?> list = (list<?>) clazz.newInstance();
		List<Object> list = new ArrayList<Object>(10);
		
		Type fc = outerField.getGenericType();
		if (fc instanceof ParameterizedType) {// if generic parameter type
            ParameterizedType pt = (ParameterizedType) fc;
            Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0]; //get generic parameter type
            
            for(int i = 0; i < cpacity; i++, cellIndex++) {
            	cell = row.getCell(cellIndex);
				if(cell == null) {
					list.add(null);
					continue;
				}
            	
            	Object o = null;
            	
        		if(genericClazz == String.class) {
        			o = getCellValue(cell);
       			} else if (genericClazz == Integer.class) {
       				try {
       					o = Integer.valueOf(getCellValue(cell));
       				} catch (NullPointerException | NumberFormatException e) {
						o = null;
					}
	   			} else if (genericClazz == Double.class) {
	   				try {
       					o = Double.valueOf(getCellValue(cell));
	   				} catch (NullPointerException | NumberFormatException e) {
						o = null;
					}
	   			} else if (genericClazz == Date.class) {
    		   		try {
//    		   			String pattern = DEFAULT_EXCEL_DATE_PATTERN;
//			   			if(outerField.isAnnotationPresent(ExcelDateFormat.class)) {
//			   				pattern = outerField.getAnnotation(ExcelDateFormat.class).pattern();
//			   			}
						o = com.ais.sys.utils.DateUtil.parse(getCellValue(cell), outerField.getAnnotation(ReportColumn.class).dateFormat());
					} catch (NullPointerException | ParseException e) {
						o = null;
					}
	   			} else if(genericClazz == Long.class) {
	   				try {
       					o = Long.valueOf(getCellValue(cell));
	   				} catch (NullPointerException | NumberFormatException e) {
						o = null;
					}
    			} else if (genericClazz == Float.class) {
    				try {
       					o = Float.valueOf(getCellValue(cell));
    				} catch (NullPointerException | NumberFormatException e) {
						o = null;
					}
	    		} else if (genericClazz == Byte.class) {
	    			try {
       					o = Byte.valueOf(getCellValue(cell));
	    			} catch (NullPointerException | NumberFormatException e) {
						o = null;
					}
	    		} else if(genericClazz == Boolean.class) {
	    			String cellValue = getCellValue(cell);
	    			if(cellValue == null) {
	    				o = null;
	    			} else {
	    				o = Boolean.valueOf(cellValue);
	    			}
	    		} else {
	    			throw new RuntimeException("unsurported class type!");
	    		}
        		list.add(o);
			}
            try {
				outerField.set(target, list);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return cellIndex;
	}
	
	private static List<?> readXlsOrXlsxBySheet(Sheet sheet, Class<?> clazz, int startRow, int startCell) {
		if(clazz == null) throw new RuntimeException("must declare model's class!");
		Row row = null;
		Cell cell = null;
		int rowIndex = 0;
		int fieldIndex = 0;
		int cellIndex = 0;
		List<Object> result = new ArrayList<Object>(10);
		try {
			for(rowIndex = startRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				Object obj = clazz.newInstance(); //create an instance
				
				row = sheet.getRow(rowIndex);
				if(row == null) continue;
				Field [] aimFields = parseAimFields(clazz);
				Field field = null;
				
				for(fieldIndex = 0,cellIndex = startCell; fieldIndex < aimFields.length; fieldIndex++, cellIndex++) {
					field = aimFields[fieldIndex];
					
					field.setAccessible(true);
		    		Class<?> fieldClazz = field.getType();
		    		
		    		if(fieldClazz == String.class) {
		    			cell = row.getCell(cellIndex);
						if(cell == null) {
							field.set(obj, null);
							continue;
						}
		    			
		    			field.set(obj, getCellValue(cell));
		       		} else if (fieldClazz == Integer.class) {
		       			cell = row.getCell(cellIndex);
						if(cell == null) {
							field.set(obj, null);
							continue;
						}
						
						try {
		       				field.set(obj, Integer.valueOf(getCellValue(cell)));
						} catch (NullPointerException | NumberFormatException e) {
							field.set(obj, null);
						}
			   		} else if (fieldClazz == Double.class) {
			   			cell = row.getCell(cellIndex);
						if(cell == null) {
							field.set(obj, null);
							continue;
						}
			   			
						try {
		       				field.set(obj, Double.valueOf(getCellValue(cell)));
						} catch (NullPointerException | NumberFormatException e) {
							field.set(obj, null);
						}
			   		} else if (fieldClazz == Date.class) {
			   			cell = row.getCell(cellIndex);
						if(cell == null) {
							field.set(obj, null);
							continue;
						}
			   			
				   		try {
//				   			String pattern = DEFAULT_EXCEL_DATE_PATTERN;
//				   			if(field.isAnnotationPresent(ExcelDateFormat.class)) {
//				   				pattern = field.getAnnotation(ExcelDateFormat.class).pattern();
//				   			}
							field.set(obj, com.ais.sys.utils.DateUtil.parse(getCellValue(cell), field.getAnnotation(ReportColumn.class).dateFormat()));
						} catch (NullPointerException | ParseException e) {
							field.set(obj, null);
						}
			   		} else if (fieldClazz == List.class) {
		    			cellIndex = readExcelToList(obj, field, row, cellIndex) - 1;
		    		} else if(fieldClazz == Long.class) {
		    			cell = row.getCell(cellIndex);
						if(cell == null) {
							field.set(obj, null);
							continue;
						}
		    			
						try {
		       				field.set(obj, Long.valueOf(getCellValue(cell)));
						} catch (NullPointerException | NumberFormatException e) {
							field.set(obj, null);
						}
		    		} else if (fieldClazz == Float.class) {
		    			cell = row.getCell(cellIndex);
						if(cell == null) {
							field.set(obj, null);
							continue;
						}
		    			
						try {
		       				field.set(obj, Float.valueOf(getCellValue(cell)));
						} catch (NullPointerException | NumberFormatException e) {
							field.set(obj, null);
						}
			    	} else if (fieldClazz == Byte.class) {
			    		cell = row.getCell(cellIndex);
						if(cell == null) {
							field.set(obj, null);
							continue;
						}
			    		
						try {
		       				field.set(obj, Byte.valueOf(getCellValue(cell)));
						} catch (NullPointerException | NumberFormatException e) {
							field.set(obj, null);
						}
		    		} else if(fieldClazz == Boolean.class) {
		    			cell = row.getCell(cellIndex);
						if(cell == null) {
							field.set(obj, null);
							continue;
						}
		    			
						String cellValue = getCellValue(cell);
						if(cellValue == null) {
							field.set(obj, null);
						} else {
							field.set(obj, Boolean.valueOf(cellValue));
						}
		    		} else {
		    			throw new RuntimeException("unsurported class type!");
		    		}
				}
			result.add(obj);
		}
	} catch (InstantiationException e) {
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		e.printStackTrace();
	}
		return result;
	}
	
	/**
	 * copy sheet
	 * copySheet(targetSheet, sourceSheet, targetWork, sourceWork, true)
	 * @param targetSheet
	 * @param sourceSheet
	 * @param targetWork
	 * @param sourceWork                                                                   
	 */
	public static void copySheet(Sheet targetSheet, Sheet sourceSheet,
			Workbook targetWork, Workbook sourceWork) throws Exception{
		if(targetSheet == null || sourceSheet == null || targetWork == null || sourceWork == null){
			throw new IllegalArgumentException("parameter can't be null");
		}
		copySheet(targetSheet, sourceSheet, targetWork, sourceWork, true);
	}

	/**
	 * copy sheet
	 * @param targetSheet
	 * @param sourceSheet
	 * @param targetWork
	 * @param sourceWork
	 * @param copyStyle					boolean copy style?
	 */
	public static void copySheet(Sheet targetSheet, Sheet sourceSheet,
			Workbook targetWork, Workbook sourceWork, boolean copyStyle)throws Exception {
		
		if(targetSheet == null || sourceSheet == null || targetWork == null || sourceWork == null){
			throw new IllegalArgumentException("parameter can't be null");
		}
		
		int maxColumnNum = 0;

		Map styleMap = (copyStyle) ? new HashMap() : null;
		
		Drawing patriarch = targetSheet.createDrawingPatriarch(); //copy comment
		for (int i = sourceSheet.getFirstRowNum(); i <= sourceSheet.getLastRowNum(); i++) {
			Row sourceRow = sourceSheet.getRow(i);
			Row targetRow = targetSheet.createRow(i);
			
			if (sourceRow != null) {
				copyRow(targetRow, sourceRow,
						targetWork, sourceWork, patriarch, styleMap);
				if (sourceRow.getLastCellNum() > maxColumnNum) {
					maxColumnNum = sourceRow.getLastCellNum();
				}
			}
		}
		
		//copy merged regions
		mergerRegion(targetSheet, sourceSheet);
		
		//set target column width
		for (int i = 0; i <= maxColumnNum; i++) {
			targetSheet.setColumnWidth(i, sourceSheet.getColumnWidth(i));
		}
	}
	
	/**
	 * copy row
	 * @param targetRow
	 * @param sourceRow
	 * @param styleMap
	 * @param targetWork
	 * @param sourceWork
	 * @param targetPatriarch
	 */
	public static void copyRow(Row targetRow, Row sourceRow,
			Workbook targetWork, Workbook sourceWork,Drawing targetPatriarch, Map styleMap) throws Exception {
		if(targetRow == null || sourceRow == null || targetWork == null || sourceWork == null || targetPatriarch == null){
			throw new IllegalArgumentException("parameter can't be null");
		}
		
		//set row height
		targetRow.setHeight(sourceRow.getHeight());
		
		for (int i = sourceRow.getFirstCellNum(); i <= sourceRow.getLastCellNum(); i++) {
			Cell sourceCell = sourceRow.getCell(i);
			Cell targetCell = targetRow.getCell(i);
			
			if (sourceCell != null) {
				if (targetCell == null) {
					targetCell = targetRow.createCell(i);
				}
				
				copyCell(targetCell, sourceCell, targetWork, sourceWork, styleMap);
				
				copyComment(targetCell, sourceCell, targetPatriarch);
			}
		}
	}
	
	/**
	 * copy cell
	 * @param targetCell			not null
	 * @param sourceCell			not null
	 * @param targetWork			not null
	 * @param sourceWork			not null
	 * @param styleMap				not null				
	 */
	public static void copyCell(Cell targetCell, Cell sourceCell, Workbook targetWork, Workbook sourceWork,Map styleMap) {
		if(targetCell == null || sourceCell == null || targetWork == null || sourceWork == null ){
			throw new IllegalArgumentException("parameter can't be null");
		}
		
		//处理单元格样式
		if(styleMap != null){
			if (targetWork == sourceWork) {
				targetCell.setCellStyle(sourceCell.getCellStyle());
			} else {
				String stHashCode = "" + sourceCell.getCellStyle().hashCode();
				CellStyle targetCellStyle = (HSSFCellStyle) styleMap
						.get(stHashCode);
				if (targetCellStyle == null) {
					targetCellStyle = targetWork.createCellStyle();
					targetCellStyle.cloneStyleFrom(sourceCell.getCellStyle());
					styleMap.put(stHashCode, targetCellStyle);
				}
				
				targetCell.setCellStyle(targetCellStyle);
			}
		}
		
		//handle content
		switch (sourceCell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			targetCell.setCellValue(sourceCell.getRichStringCellValue());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			targetCell.setCellValue(sourceCell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_BLANK:
			targetCell.setCellType(HSSFCell.CELL_TYPE_BLANK);
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			targetCell.setCellValue(sourceCell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			targetCell.setCellFormula(sourceCell.getCellFormula());
			break;
		default:
			break;
		}
	}
	
	/**
	 * copy comment
	 * @param targetCell
	 * @param sourceCell
	 * @param targetPatriarch
	 */
	public static void copyComment(Cell targetCell,Cell sourceCell,Drawing targetPatriarch)throws Exception{
		if(targetCell == null || sourceCell == null || targetPatriarch == null){
			throw new IllegalArgumentException("parameter can't be null");
		}
		
		//handle comment
		Comment comment = sourceCell.getCellComment();
		if(comment != null){
			Comment newComment = targetPatriarch.createCellComment(new HSSFClientAnchor());
			newComment.setAuthor(comment.getAuthor());
			newComment.setColumn(comment.getColumn());
//			newComment.setFillColor(comment.getFillColor());
//			newComment.setHorizontalAlignment(comment.getHorizontalAlignment());
//			newComment.setLineStyle(comment.getLineStyle());
//			newComment.setLineStyleColor(comment.getLineStyleColor());
//			newComment.setLineWidth(comment.getLineWidth());
//			newComment.setMarginBottom(comment.getMarginBottom());
//			newComment.setMarginLeft(comment.getMarginLeft());
//			newComment.setMarginTop(comment.getMarginTop());
//			newComment.setMarginRight(comment.getMarginRight());
//			newComment.setNoFill(comment.isNoFill());
			newComment.setRow(comment.getRow());
//			newComment.setShapeType(comment.getShapeType());
			newComment.setString(comment.getString());
//			newComment.setVerticalAlignment(comment.getVerticalAlignment());
			newComment.setVisible(comment.isVisible());
			targetCell.setCellComment(newComment);
		}
	}
	
	/**
	 * copy merged regions
	 * 
	 * @param sheetCreat
	 * @param sourceSheet
	 */
	public static void mergerRegion(Sheet targetSheet, Sheet sourceSheet)throws Exception {
		if(targetSheet == null || sourceSheet == null){
			throw new IllegalArgumentException("parameter can't be null");
		}
		
		for (int i = 0; i < sourceSheet.getNumMergedRegions(); i++) {
			CellRangeAddress oldRange = sourceSheet.getMergedRegion(i);
			CellRangeAddress newRange = new CellRangeAddress(
					oldRange.getFirstRow(), oldRange.getLastRow(),
					oldRange.getFirstColumn(), oldRange.getLastColumn());
			targetSheet.addMergedRegion(newRange);
		}
	}
}
