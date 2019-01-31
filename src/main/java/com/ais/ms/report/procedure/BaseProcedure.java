package com.ais.ms.report.procedure;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ais.ms.models.SWorkSheetModel;
import com.ais.ms.report.annotation.ReportColumn;

import com.ais.sys.utils.ReportArithmeticUtil;

public abstract class BaseProcedure {
	
	private Logger LOG = LoggerFactory.getLogger(BaseProcedure.class);
	
	protected SXSSFWorkbook SWorkbook;
	
	protected Sheet sheet;
	
	protected Map<String, CellStyle> cellStyle;

	protected Row row;
	
	protected Cell cell;
	
	protected String datePattern = "yyyy-MM-dd'T'HH:mm:ss";

	protected int RANDOM_ACCESS_WINDOW_SIZE = 1000;
	
	protected int sheetMaxRow = 50000;
	
	protected int defaultRow = 0;

	protected int defaultCell = 0;

	protected int defaultDataStartRow = 10;
	
	protected int allRowNum = 0;
	
	protected int currentRowNum = 0;
	
	protected String[] coord = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","L","S","T","U","V","W","X","Y","Z"};
	
	protected String NUMBER_TYPE = "NUMBER";
	
	protected String DATE_TYPE = "DATE";
	
	protected String OTHER_TYPE = "OTHER";
	
	/**
	 * @created 2016/10/09
	 * @description produce file object
	 * @param SWorkSheets : excel sheets data
	 * @return excel file object
	 */
	public abstract SXSSFWorkbook doProduce(List<SWorkSheetModel> SWorkSheets);
	
	/**
	 * @created 2016/10/09
	 * @description read file data
	 * @param inputStream : excel data stream
	 * @param clazz : excel data model class
	 * @return excel data
	 */
	public abstract List<?> readExcel(InputStream inputStream, Class<?> clazz);
	
	protected void initCellStyle(){
		cellStyle = new HashMap<String, CellStyle>();
	}
	
	/**
	 * @created 2016/10/20
	 * @description identify all report column
	 * @param clazz : excel data model class
	 * @return all report column
	 */
	protected final Field[] parseAimFields(Class<?> clazz) {
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
	
//	protected final int createInnerCell(List<?> cellsModelList, Field outfield, Row row, int startCell, CellStyle styleBold, int [] columnMaxLength, int columnIndex) throws IllegalArgumentException, IllegalAccessException {
	protected final int createInnerCell(List<?> cellsModelList, Field outfield, Row row, int startCell, CellStyle styleBold) throws IllegalArgumentException, IllegalAccessException {
		if(cellsModelList != null && cellsModelList.size() > 0) {
			Cell cell = null;
			
			Type fc = outfield.getGenericType();
			if (fc instanceof ParameterizedType) {// if generic parameter type
	            ParameterizedType pt = (ParameterizedType) fc;
	            Class<?> fieldClazz = (Class<?>) pt.getActualTypeArguments()[0]; //get generic parameter type
	            
	            if(isPrimitive(fieldClazz)) {
	            	
	            	if(fieldClazz == String.class) {
	            		for (Object model : cellsModelList) {
		            		cell = row.createCell(startCell++);
		            		if(cell == null) continue;
		            		
		            		if(model != null) {
		            			cell.setCellValue(model.toString());
		            		}
		            		cell.setCellStyle(styleBold);
						}
        			} else if (fieldClazz == Integer.class) {
        				for (Object model : cellsModelList) {
		            		cell = row.createCell(startCell++);
		            		if(cell == null) continue;
		            		
		            		if(model != null) {
		            			cell.setCellValue(Integer.valueOf(model.toString()));
		            		}
		            		cell.setCellStyle(styleBold);
						}
	   				} else if(fieldClazz == Double.class) {
	   					for (Object model : cellsModelList) {
		            		cell = row.createCell(startCell++);
		            		if(cell == null) continue;
		            		
		            		if(model != null) {
		            			cell.setCellValue(Double.valueOf(model.toString()));
		            		}
		            		cell.setCellStyle(styleBold);
						}
	   				} else if(fieldClazz == Float.class) {
	   					for (Object model : cellsModelList) {
		            		cell = row.createCell(startCell++);
		            		if(cell == null) continue;
		            		
		            		if(model != null) {
		            			cell.setCellValue(Float.valueOf(model.toString()));
		            		}
		            		cell.setCellStyle(styleBold);
						}
	   				} else if(fieldClazz == Long.class) {
	   					for (Object model : cellsModelList) {
		            		cell = row.createCell(startCell++);
		            		if(cell == null) continue;
		            		
		            		if(model != null) {
		            			cell.setCellValue(Long.valueOf(model.toString()));
		            		}
		            		cell.setCellStyle(styleBold);
						}
	   				} else if(fieldClazz == Byte.class) {
	   					for (Object model : cellsModelList) {
		            		cell = row.createCell(startCell++);
		            		if(cell == null) continue;
		            		
		            		if(model != null) {
		            			cell.setCellValue(Byte.valueOf(model.toString()));
		            		}
		            		cell.setCellStyle(styleBold);
						}
	   				} else if (fieldClazz == Date.class) {
	   					String pattern = outfield.getAnnotation(ReportColumn.class).dateFormat();
	   					for (Object model : cellsModelList) {
		            		cell = row.createCell(startCell++);
		            		String dateStr = "";
		            		if(cell == null) continue;
		            		
		            		if(model != null) {
		            			dateStr = com.ais.sys.utils.DateUtil.format((Date) model, pattern);
		            			cell.setCellValue(dateStr);
		            		}
		            		cell.setCellStyle(styleBold);
						}
	   				} else if(fieldClazz == Boolean.class) {
	   					for (Object model : cellsModelList) {
		            		cell = row.createCell(startCell++);
		            		if(cell == null) continue;
		            		
		            		if(model != null) {
		            			cell.setCellValue(Boolean.valueOf(model.toString()));
		            		}
		            		cell.setCellStyle(styleBold);
						}
	   				}
	            } else {
	            	throw new RuntimeException("Class can only be Primitive or packaging group or java.util.List which can only include Primitive!");
	            }
			}
		}
		return startCell;
	}
	
	/**
	 * @created 2016/11/03
	 * @description check data type
	 * @param clazz : model class
	 * @return yes or no
	 */
	protected final boolean isPrimitive(Class<?> clazz) {
//		if(clazz == null){
//			return null;
//		}else if(
//			clazz == Double.class ||
//			clazz == Integer.class ||
//			clazz == Long.class ||
//			clazz == Float.class ){
//			return NUMBER_TYPE;
//		}else if(clazz == Date.class){
//			return DATE_TYPE;
//		}else{
//			return OTHER_TYPE;
//		}
		
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
	
	protected final String getCellValue(Cell cell) {
		int cellType = cell.getCellType();
		String value = null;
		
		switch (cellType) {
		case Cell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if(DateUtil.isCellDateFormatted(cell)) {
				value = com.ais.sys.utils.DateUtil.format(cell.getDateCellValue(), datePattern);
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
	
	protected final int readExcelToList(Object target, Field outerField, Row row, int cellIndex) {
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
    		   			String pattern = outerField.getAnnotation(ReportColumn.class).dateFormat();
//			   			if(outerField.isAnnotationPresent(ExcelDateFormat.class)) {
//			   				pattern = outerField.getAnnotation(ExcelDateFormat.class).pattern();
//			   			}
						o = com.ais.sys.utils.DateUtil.parse(getCellValue(cell), pattern);
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
	
	public final String calculatePrecision(int Precision, String value){
		return new BigDecimal(value).setScale(Precision, RoundingMode.HALF_UP).toString();
	}
	
	public final void downloadCurrentXlsFile(OutputStream out) {
		try {
			if(SWorkbook != null){
				SWorkbook.write(out);
				if(SWorkbook.getClass().isAssignableFrom(SXSSFWorkbook.class)) {
					((SXSSFWorkbook) SWorkbook).dispose();
				}
				SWorkbook.close();
			}
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
	
	public final void closeCurrentSWorkbook() {
		try {
			if(SWorkbook != null){
				SWorkbook.close();
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public int getDefaultRow() {
		return defaultRow;
	}

	public void setDefaultRow(int defaultRow) {
		this.defaultRow = defaultRow;
	}

	public int getDefaultCell() {
		return defaultCell;
	}

	public void setDefaultCell(int defaultCell) {
		this.defaultCell = defaultCell;
	}

	public int getDefaultDataStartRow() {
		return defaultDataStartRow;
	}

	public void setDefaultDataStartRow(int defaultDataStartRow) {
		this.defaultDataStartRow = defaultDataStartRow;
	}

	public int getSheetMaxRow() {
		return sheetMaxRow;
	}

	/**
	 * @param  sheetMaxRow : max = 50000
	 */
	public void setSheetMaxRow(int sheetMaxRow) {
		if(sheetMaxRow>50000){
			sheetMaxRow = 50000;
		}
		this.sheetMaxRow = sheetMaxRow;
	}
	
	public int getAllRowNum() {
		return allRowNum;
	}

	public void setAllRowNum(int allRowNum) {
		this.allRowNum = allRowNum;
	}

	public int getCurrentRowNum() {
		return currentRowNum;
	}

	public void setCurrentRowNum(int currentRowNum) {
		this.currentRowNum = currentRowNum;
	}
	
	public void initRedProgress() {
		this.allRowNum = 0;
		this.currentRowNum = 0;
	}
	
	public String getRedProgress() {
		System.out.println("currentRowNum="+currentRowNum+",allRowNum="+allRowNum);
		return ReportArithmeticUtil.divide(currentRowNum+"", allRowNum+"").toString();
	}
	
}
