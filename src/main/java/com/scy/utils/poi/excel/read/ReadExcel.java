package com.scy.utils.poi.excel.read;


import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * EXCEL读取工具类
 */
public class ReadExcel {
    private static DataFormatter formatter = new DataFormatter();

    public List<List<Object>> readExcel(InputStream in) {
        List<List<Object>> excelDataList = new ArrayList<List<Object>>();
        try {
            // poi读取excel
            // 创建要读入的文件的输入流
            InputStream inp = in;
            // 根据上述创建的输入流 创建工作簿对象
            Workbook wb = WorkbookFactory.create(inp);
            // 得到第一页 sheet
            // 页Sheet是从0开始索引的
            Sheet sheet = wb.getSheetAt(0);
            int rowNum = sheet.getPhysicalNumberOfRows();
            for (int i = 0; i < rowNum; i++) {
                Row row = sheet.getRow(i);
				/*如果存在空列,无法读取到准确的列数。
				int colNum = row.getPhysicalNumberOfCells();*/
                int colNum = row.getLastCellNum();

                List<Object> rowList = new ArrayList<Object>();

                for (int j = 0; j < colNum; j++) {
                    Cell cell = row.getCell(j);
                    rowList.add(getCellFormatValue(cell));
                    // System.out.print(getCellFormatValue(cell)+"\t\t");
                }
                // System.out.println();
                excelDataList.add(rowList);
                //GradeList.add(stuList);
            }
            // 关闭输入流
            inp.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return excelDataList;
    }


    private static String getCellFormatValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            /*
            通过获取单元格值并应用任何数据格式（Date、0.00、1.23e9、$1.23等），获取显示在单元格中的文本。
            注意：公式获取的是过程，不是结果。
            如果对读取的excel的数据没有公式,可以使用这个方法将所有数据读取为String格式。
            */
            /*String text = formatter.formatCellValue(cell);
            System.out.println(text);*/

            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                case NUMERIC:
                    cellvalue = String.valueOf(cell.getNumericCellValue());
                    if (DateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                        Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
                        cellvalue = sdf.format(date).toString();
                    }
                    //应对科学计数法，将科学计数法转换为正常数值。
                    BigDecimal bd = new BigDecimal(cellvalue);
                    cellvalue = bd.toString();
                    break;
                case BOOLEAN:
                    cellvalue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case BLANK:
                    cellvalue = "";
                    break;
                case FORMULA: {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        cellvalue = sdf.format(date);
                    } else {
                        cellvalue = String.valueOf(cell.getNumericCellValue());
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