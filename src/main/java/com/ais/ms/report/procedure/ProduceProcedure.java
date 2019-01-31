package com.ais.ms.report.procedure;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.ais.ms.models.SWorkSheetModel;
import com.ais.ms.report.annotation.ReportColumn;

public class ProduceProcedure extends DefaultProcedure {
	
	public void initDefaultStyle(int currentSheet){
		Font font = SWorkbook.createFont();
    	font = SWorkbook.createFont();
    	font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
    	font.setFontHeightInPoints((short) 10);
    	font.setFontName("SimSun");
		
		// create default style
    	CellStyle defaultStyle = SWorkbook.createCellStyle();
    	defaultStyle.setFont(font);
    	defaultStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    	defaultStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    	defaultStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    	defaultStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
    	defaultStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
//    	defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP); // vertical
//    	defaultStyle.setLocked(false);// -10-31 editable : cancel protected cell
    	addStyle("defaultStyle", defaultStyle);
    	
		// create title style
		CellStyle titleStyle = SWorkbook.createCellStyle();
		titleStyle.setFont(font);
//		titleStyle.setLocked(false);// editable : cancel protected cell
		addStyle("titleStyle", titleStyle);

		// create param style
		CellStyle paramStyle = SWorkbook.createCellStyle();
		paramStyle.setFont(font);
//		paramStyle.setLocked(false);// -10-31 editable : cancel protected cell
		addStyle("paramStyle", paramStyle);
    	
		// create header style
    	CellStyle headerStyle = SWorkbook.createCellStyle();
    	headerStyle.setFont(font);
    	headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    	headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
//    	headerStyle.setLocked(false);// -10-31 editable : cancel protected cell
    	addStyle("headerStyle", headerStyle);

		// create header deepen style
    	CellStyle deepenStyle = SWorkbook.createCellStyle();
    	deepenStyle.setFont(font);
    	deepenStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    	deepenStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
//    	deepenStyle.setLocked(false);// -10-31 editable : cancel protected cell
    	addStyle("deepenStyle", deepenStyle);
    	
    	createOtherStyle(currentSheet, SWorkbook, cellStyle);
	}
	
	public void createOtherStyle (int currentSheet, SXSSFWorkbook SWorkbook, Map<String, CellStyle> cellStyle) {
		
		Font fontBold = SWorkbook.createFont();
    	fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
    	fontBold.setFontHeightInPoints((short) 10);
    	fontBold.setFontName("SimSun");
    	CellStyle percentStyle = SWorkbook.createCellStyle();
    	percentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    	percentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    	percentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    	percentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
    	percentStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%")); 
    	percentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
    	percentStyle.setFont(fontBold);
//    	styleBold.setLocked(false);// -10-31 editable : cancel protected cell
    	addStyle("percent", percentStyle);
    	
		createExtraStyle(currentSheet, SWorkbook, cellStyle);
	}
	
	/**
	 * @created 2016/10/09
	 * @description replenish cell style, need to be subclasses override
	 */
	public void createExtraStyle (int currentSheet, SXSSFWorkbook SWorkbook, Map<String, CellStyle> otherStyleBold) {
		
	}
	
	/**
	 * @created 2016/11/02
	 * @description add style and copy style
	 */
	public void addStyle(String styleKey, CellStyle style){
    	cellStyle.put(styleKey, style);
    	
    	// update copy style(add property)
    	CellStyle copyStyle = copyStyle(style);
    	copyStyle.setLocked(false);
    	
    	cellStyle.put(styleKey+"-copy", copyStyle);
	}
	
	/**
	 * @created 2016/11/02
	 * @description copy cell style
	 */
	public CellStyle copyStyle(CellStyle style){
		
		CellStyle copyStyle = SWorkbook.createCellStyle();
		copyStyle.setBorderBottom(style.getBorderBottom());
		copyStyle.setBorderLeft(style.getBorderLeft());
		copyStyle.setBorderRight(style.getBorderRight());
		copyStyle.setBorderTop(style.getBorderTop());
		copyStyle.setFillPattern(style.getFillPattern());
		copyStyle.setFillForegroundColor(style.getFillForegroundColor());
		
		copyStyle.setFont(SWorkbook.getFontAt(style.getFontIndex()));
		
    	return copyStyle;
	}
	
	public int createExcelTitle(int currentSheet, String title, int startRow, int startCell) {
		if(title == null) return startRow;
		
		if(startRow <= -1 || startCell <= -1) throw new IllegalArgumentException("targetRow and targetCell must >= 0");

		CellStyle titleStyle = cellStyle.get("titleStyle");
		
		row = sheet.createRow(startRow);
		cell = row.createCell(startCell);
		cell.setCellValue(title);
		cell.setCellStyle(titleStyle);
		
		cell = row.createCell(startCell+1);
		cell.setCellValue("\u5c0e\u51fa");
		cell.setCellStyle(titleStyle);
		
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  HH:mm:ss", Locale.ENGLISH);
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern, Locale.ENGLISH);
		
		
		cell = row.createCell(startCell+4);
    	cell.setCellValue("As at  "+ sdf.format(new Date()));
    	cell.setCellStyle(titleStyle);
    	
    	startRow++;
    	
		return startRow;
	}
	
	public int createExcelQueryParams(int currentSheet, Map<String, String> queryParams, int startRow, int startCell) {
    	if(queryParams == null || queryParams.isEmpty()) return startRow;
		
		if(startRow <= -1 || startCell <= -1) throw new IllegalArgumentException("startQueryParamsRow and startQueryParamsCell must >= 0");

		CellStyle paramStyle = cellStyle.get("paramStyle");
		
		String key = null;
		String value = null;
		int i=0;
		
		for (Map.Entry<String, String> entry : queryParams.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			i++;
			if(i%2!=0){
				row = sheet.createRow(startRow);
			}else{
				row = sheet.getRow(startRow);
				startRow++;
			}
			if(i%2==0){
				cell = row.createCell(startCell + 3);
				cell.setCellValue(key);
				cell.setCellStyle(paramStyle);
				if(queryParams.get(key) != null) {
					cell = row.createCell(startCell + 4);
					cell.setCellValue(value);
					cell.setCellStyle(paramStyle);
				}
			}else{
				cell = row.createCell(startCell);
				cell.setCellValue(key);
				cell.setCellStyle(paramStyle);
				if(queryParams.get(key) != null) {
					cell = row.createCell(startCell + 1);
					cell.setCellValue(value);
					cell.setCellStyle(paramStyle);
				}
			}
		}
		return startRow;
	}
	
	public int createExcelHeaders(int currentSheet, List<String[]> headers, String cellWidth, int startRow, int startCell) {
    	if(headers == null || headers.isEmpty()) return startRow;
		
		if(startRow+headers.size() < defaultDataStartRow){
			startRow = defaultDataStartRow-headers.size();
		}
		
		CellStyle headerStyle = cellStyle.get("headerStyle");
		
		String str = "";
    	
		for(String[] header : headers){
			
			int currentCell = startCell;
    		row = sheet.createRow(startRow);
    		
    		for(int i=0; i<header.length; i++){
    			String text = header[i];
    			cell = row.createCell(currentCell);
    			
    			if(text.indexOf("(styleKey='")!=-1){
					cell.setCellStyle(cellStyle.get(text.substring(text.indexOf("(styleKey='")+11, text.indexOf("')",text.indexOf("(styleKey='")))));
    				text=text.replace(text.substring(text.indexOf("(styleKey='"), text.indexOf("')",text.indexOf("(styleKey='"))+2),"");
				}else{
					cell.setCellStyle(headerStyle);
				}
    			
    			if(text.indexOf("select")!=-1){
    			    CellRangeAddressList regions = new CellRangeAddressList(defaultDataStartRow,50000,i,i); 
    		        DataValidationConstraint constraint = sheet.getDataValidationHelper().createExplicitListConstraint(text.substring(text.indexOf("[select='")+9, text.indexOf("']",text.indexOf("[select='"))).split(","));
    		        DataValidation dataValidation = sheet.getDataValidationHelper().createValidation(constraint, regions);
    				sheet.addValidationData(dataValidation);
    				text=text.replace(text.substring(text.indexOf("[select='"), text.indexOf("']",text.indexOf("[select='"))+2),"");
    			}
    			
    			if(text.startsWith("{")||text.endsWith("}")){
    				if("".equals(str)){
    					if(currentCell>25){
        					str = coord[currentCell/26-1]+coord[currentCell%26]+defaultDataStartRow;
    					}else{
        					str = coord[currentCell]+defaultDataStartRow;
    					}
    				}else{
    					if(str.indexOf(":")!=-1){
        					str = str.substring(0, str.indexOf(":"));
    					}
    					if(currentCell>25){
        					str = str+":"+coord[currentCell/26-1]+coord[currentCell%26]+defaultDataStartRow;
    					}else{
        					str = str+":"+coord[currentCell]+defaultDataStartRow;
    					}
    				}
    				text=text.replace("{","");
    				text=text.replace("}","");
    			}
    			
				cell.setCellValue(text);
				if(cellWidth!=null){
//					sheet.setColumnWidth(currentCell, text.getBytes().length << Integer.valueOf(cellWidth));
					sheet.setColumnWidth(currentCell, Integer.valueOf(cellWidth)*1000);
				}
				currentCell++;
				
    		}
    		
    		if(!"".equals(str)){
        		CellRangeAddress ca = CellRangeAddress.valueOf(str);
    			sheet.setAutoFilter(ca);
    		}
			
    		startRow++;
		}
		
		return startRow;
	}
	
	public int createReportDate(int currentSheet, List<?> dataList, int startRow, int startCell){
		if(dataList == null || dataList.isEmpty()) return startRow;
		
		Class<?> c = null;
    	Field [] aimFields = null;
    	
    	try {
	    	
    		c = dataList.get(0).getClass();
	    	
	    	if(c == null) throw new RuntimeException("this model class can't be null!");
	    	
	    	aimFields = parseAimFields(c);// parse aim fields
			
			if(dataList != null && !dataList.isEmpty()) {
		    	
		    	for (Object model : dataList) {
		    		
		    		row = sheet.createRow(startRow++);
		    		
		    		if(model == null) continue; // blank line
		    		
		    		int currentCell = startCell;
		    		
		    		Object o;
		    		Class<?> clazz;
		    		String cellValueStr;
		    		
		    		for (Field field : aimFields) {
			   			field.setAccessible(true);
			   			o = field.get(model);
			   			clazz = field.getType();
			   			cellValueStr = "";
			   			
		    			if(isPrimitive(clazz)) {
		    				cell = row.createCell(currentCell++);
		    				
		    				if(cell == null) continue;
		    				
		    				if(o != null){
		    					
		    					// init cellValueStr
		    					cellValueStr = o.toString();
		    					
		    					if (clazz == Date.class) {
				   					if(o != null) {
						   				cellValueStr = com.ais.sys.utils.DateUtil.format((Date) o, field.getAnnotation(ReportColumn.class).dateFormat());
				   					}
		    						cell.setCellValue(cellValueStr);
				   				} else if(clazz == String.class) {
				   					
		    						cell.setCellValue(o.toString());
			    						
			        			} else if (clazz == Integer.class) {

		        					cell.setCellValue(Integer.valueOf(cellValueStr));
			        					
				   				} else if(clazz == Double.class || clazz == Float.class || clazz == Long.class) {
				   					
				   					if(field.getAnnotation(ReportColumn.class).precision()!=0) {
				   						cellValueStr = calculatePrecision(field.getAnnotation(ReportColumn.class).precision(), cellValueStr);
				   					}
				   					
				   					if(clazz == Double.class) {
					   					cell.setCellValue(Double.valueOf(cellValueStr));
					   				} else  if(clazz == Float.class) {
				   						cell.setCellValue(Float.valueOf(cellValueStr));
					   				} else if(clazz == Long.class) {
				   						cell.setCellValue(Long.valueOf(cellValueStr));
					   				}
				   					
				   				} else if(clazz == Byte.class) {
	
			   						cell.setCellValue(Byte.valueOf(cellValueStr));
	
				   				} else if(clazz == Boolean.class) {
				   					
				   					cell.setCellValue(Boolean.valueOf(cellValueStr));
				   					
					   			}
		    				}
		    				// update by lh -16-11-02
		    				// initialize maximum column Length
//	    					currentColLen = cellValueStr.getBytes().length;
//		    				columnMaxLength[columnIndex] = columnMaxLength[columnIndex] >= currentColLen ? columnMaxLength[columnIndex] : currentColLen;
//	    					columnIndex++;
		    				
	    					// set style
	    					if(!field.getAnnotation(ReportColumn.class).locked()){
	    						cell.setCellStyle(cellStyle.get(field.getAnnotation(ReportColumn.class).styleKey()+"-copy"));
	    					}else{
	    						cell.setCellStyle(cellStyle.get(field.getAnnotation(ReportColumn.class).styleKey()));
	    					}
	    					
		    			} else if(clazz.isAssignableFrom(List.class)) {
		    				if(o != null) {
		    					List<?> fieldList = (List<?>)o;
		    					
		    					CellStyle style = field.getAnnotation(ReportColumn.class).locked()?cellStyle.get(field.getAnnotation(ReportColumn.class).styleKey()):cellStyle.get(field.getAnnotation(ReportColumn.class).styleKey()+"-copy");
		    				
		    					currentCell = createInnerCell(fieldList, field, row, currentCell, style);
		    				}
		    			} else {
				    		throw new RuntimeException("Class can only be Primitive or packaging group or java.util.List which can only include Primitive!");
				    	}
		    		}
		    	}
//		    	for(i = 0; i < columnMaxLength.length; i++) {
//		    		int curWidth = sheet.getColumnWidth(i);
//		    		int valWith = columnMaxLength[i] << 9;
//		    		sheet.setColumnWidth(i, curWidth > valWith ? curWidth : valWith);
//		    	}
		    }
//				sheet.shiftRows(startRow, endRow, n);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
    	return startRow;
	}
	


	public int betweenTitleAndParamsContent(int currentSheet, SWorkSheetModel SWorkSheet, int startRow, int startCell){
		return ++startRow;
	}

	public int betweenParamsAndHeaderContent(int currentSheet, SWorkSheetModel SWorkSheet, int startRow, int startCell){
		return ++startRow;
	}
	
	public int endContent(int currentSheet, SWorkSheetModel SWorkSheet, int startRow, int startCell){
		return ++startRow;
	}
	
	
	// read	
	
	public List<?> readXlsOrXlsxBySheet(Sheet sheet, Class<?> clazz,
			int startRow, int startCell) {
		
		if(clazz == null) throw new RuntimeException("must declare model's class!");
		
		Row row = null;
		Cell cell = null;
		int rowIndex = 0;
		int fieldIndex = 0;
		int cellIndex = 0;
		List<Object> result = new ArrayList<Object>(10);
		
		try {
			
			for(rowIndex = startRow; rowIndex <= sheet.getLastRowNum(); rowIndex++,currentRowNum++) {
				Object obj = clazz.newInstance(); //create an instance
				
				row = sheet.getRow(rowIndex);
				if(row == null) continue;
				Field [] aimFields = parseAimFields(clazz);
				Field field = null;
				
				for(fieldIndex = 0,cellIndex = startCell; fieldIndex < aimFields.length; fieldIndex++, cellIndex++) {
					field = aimFields[fieldIndex];
					
					field.setAccessible(true);
		    		Class<?> fieldClazz = field.getType();

		    		// update by lh -10-20
		    		if (fieldClazz == List.class) {
		    			cellIndex = readExcelToList(obj, field, row, cellIndex) - 1;
		    			continue;
		    		}
		    		
	    			cell = row.getCell(cellIndex);
					if(cell == null) {
						field.set(obj, null);
						continue;
					}
					
		    		if(fieldClazz == String.class) {
		    			field.set(obj, getCellValue(cell));
		       		} else if (fieldClazz == Integer.class) {
						try {
		       				field.set(obj, Integer.valueOf(getCellValue(cell)));
						} catch (NullPointerException | NumberFormatException e) {
							field.set(obj, null);
						}
			   		} else if (fieldClazz == Double.class) {
						try {
		       				field.set(obj, Double.valueOf(getCellValue(cell)));
						} catch (NullPointerException | NumberFormatException e) {
							field.set(obj, null);
						}
			   		} else if (fieldClazz == Date.class) {
				   		try {
							field.set(obj, com.ais.sys.utils.DateUtil.parse(getCellValue(cell), field.getAnnotation(ReportColumn.class).dateFormat()));
						} catch (NullPointerException | ParseException e) {
							field.set(obj, null);
						}
			   		} else if(fieldClazz == Long.class) {
						try {
		       				field.set(obj, Long.valueOf(getCellValue(cell)));
						} catch (NullPointerException | NumberFormatException e) {
							field.set(obj, null);
						}
		    		} else if (fieldClazz == Float.class) {
						try {
		       				field.set(obj, Float.valueOf(getCellValue(cell)));
						} catch (NullPointerException | NumberFormatException e) {
							field.set(obj, null);
						}
			    	} else if (fieldClazz == Byte.class) {
						try {
		       				field.set(obj, Byte.valueOf(getCellValue(cell)));
						} catch (NullPointerException | NumberFormatException e) {
							field.set(obj, null);
						}
		    		} else if(fieldClazz == Boolean.class) {
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

	
}
