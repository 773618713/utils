package com.scy.utils.poi.excel.write;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import org.apache.poi.ss.usermodel.CellType;

/**
 * Excel操作类
 *
 */
public class ExcelPoi {

	public static void main(String[] args) {
		try {
			// 根据参数传入的数据文件路径和文件名称，组合出excel 数据文件的绝对路径
			// 声明一个file 文件对象
			File file = new File("C:\\Users\\sun\\Desktop\\123\\123.xlsx");
			// 创建FileInputStream 对象用于读取excel 文件
			FileInputStream fis = new FileInputStream(file);
			List<List<Object>> excelDataList = readFirstSheet(fis);
			// 关闭输入流
			fis.close();
			for (int i = 0; i < excelDataList.size(); i++) {
				List<Object> list = excelDataList.get(i);
				for (int j = 0; j < list.size(); j++) {
					System.out.println(list.get(j));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * poi读取excel工作簿
	 * @param in	输入流
	 * @return	List<List<List<Object>>> 三层list代表 工作表，行，列
	 */
	public static List<List<List<Object>>> readExcel(InputStream in) {
		try {
			// 根据上述创建的输入流 创建工作簿对象
			Workbook wb = WorkbookFactory.create(in);
			int sheetNum = wb.getNumberOfSheets();
			List<List<List<Object>>> excelData = new ArrayList<>();
			for (int i = 0; i < sheetNum; i++) {
				excelData.add(readSheet(wb, i));
			}
			return excelData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * poi读取excel的第一张工作表
	 * @param in
	 * @return	List<List<Object>> 双层list代表行列
	 */
	private static List<List<Object>> readFirstSheet(InputStream in) {
		try {
			// 根据上述创建的输入流 创建工作簿对象
			Workbook wb = WorkbookFactory.create(in);
			return readSheet(wb, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 读取工作表
	 * @param wb			工作簿
	 * @param sheetIndex	工作表序号，序号从0开始
	 * @return	List<List<Object>> 双层list代表行列
	 */
	private static List<List<Object>> readSheet(Workbook wb, int sheetIndex) {
		// 页Sheet是从0开始索引的
		Sheet sheet = wb.getSheetAt(sheetIndex);
		//int rowNum = sheet.getPhysicalNumberOfRows();
		int rowNum = sheet.getLastRowNum();
		List<List<Object>> sheetData = new ArrayList<List<Object>>(rowNum+1);
		for (int i = 0; i <= rowNum; i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			/*
			 * 如果存在空列,无法读取到准确的列数。 int colNum = row.getPhysicalNumberOfCells();
			 */
			int colNum = row.getLastCellNum();
			List<Object> rowList = new ArrayList<Object>(colNum);
			for (int j = 0; j < colNum; j++) {
				Cell cell = row.getCell(j);
				rowList.add(getCellFormatValue(cell));
			}
			sheetData.add(rowList);
		}
		return sheetData;
	}
	

	/**
	 * 格式转换
	 * 
	 * @param cell
	 * @return
	 */
	private static String getCellFormatValue(Cell cell) {
		String cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			case NUMERIC:
				/*
				 * 这样转换会导致数值变科学计数法 cellvalue = String.valueOf(cell.getNumericCellValue());
				 */
				DecimalFormat df = new DecimalFormat("#.#########");
				cellvalue = df.format(cell.getNumericCellValue());

				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
					cellvalue = sdf.format(date).toString();
				}
				break;
			case BOOLEAN:
				cellvalue = String.valueOf(cell.getBooleanCellValue());
				break;
			case BLANK:
				cellvalue = "";
				break;
			case FORMULA: {
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					cellvalue = sdf.format(date);
				} else {
					DecimalFormat df2 = new DecimalFormat("#.#########");
					cellvalue = df2.format(cell.getNumericCellValue());
				}
				break;
			}
			case STRING:
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			default:
				cellvalue = "";
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;
	}
}
