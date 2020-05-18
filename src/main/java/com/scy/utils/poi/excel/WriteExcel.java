package com.scy.utils.poi.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

public class WriteExcel {
    /**
     * 样式数组
     * 存储了单元格用到的各种样式（目前总共有16种不同排列组合的样式）
     * CellStyle的创建数量是有限制的，所以要用styles数组来存储，实现相同的style复用。
     */
    private final static CellStyle[] styles = new CellStyle[1<<4];

    /**
     * 写出多张表
     * @param sheetList
     */
    public static void WriteExcel(List sheetList, OutputStream os) throws Exception{
        // 创建SXSSFWorkbook对象(excel的文档对象)
        SXSSFWorkbook wkb = new SXSSFWorkbook();
        for (int i = 0; i < sheetList.size(); i++) {
            ExcelSheet sheet = (ExcelSheet) sheetList.get(i);
            WriteExcelSheet(sheet.rowList, sheet.sheetName, wkb);
        }
        wkb.write(os);
        wkb.close();
    }

    /**
     * 写出单张表
     * @param rowList
     * @param sheetName
     * @param wkb
     */
    public static void WriteExcelSheet(List rowList, String sheetName, SXSSFWorkbook wkb) {
        // 建立新的sheet对象（excel的表单）
        SXSSFSheet sheet = wkb.createSheet(sheetName);

        for (int i = 0; i < rowList.size(); i++) {
            List colList = (List) rowList.get(i);
            // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
            SXSSFRow row = sheet.createRow(i);
            for (int j = 0; j < colList.size(); j++) {
                Object tableData = colList.get(j);
                // 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
                SXSSFCell cell = row.createCell(j);
                // 设置单元格内容
                if (tableData instanceof TableCell) {
                    TableCell tableCell = (TableCell) tableData;
                    //给单元格赋值
                    setCellValue(cell, tableCell.value);
                    //判断数据是否占用多个单元格
                    if (tableCell.colspan != 1 || tableCell.rowSpan != 1) {
                        int firstCol = j;
                        int lastCol = j + (tableCell.colspan - 1);
                        int firstRow = i;
                        int lastRow = i + (tableCell.rowSpan - 1);
                        //合并单元格
                        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
                    }
                    //设置单元格样式
                    setStyle(cell, tableCell, wkb);
                } else {
                    setCellValue(cell, tableData);
                }
            }
        }
    }

    /**
     * 设置表格样式
     * @param cell
     * @param tableCell
     * @param wkb
     */
    private static void setStyle(Cell cell, TableCell tableCell, SXSSFWorkbook wkb) {
        //计算样式对应的key
        byte key = 0;
        if (tableCell.align_center){
            key |= 1;
        }
        if (tableCell.vertical_center){
            key |= 1<<1;
        }
        if (tableCell.decimalLen != null){
            if (tableCell.percentage == true){
                //百分比
                key |= 1<<2;
            }else {
                key |= 1<<3;
            }
        }

        //根据key获取样式
        CellStyle style = styles[key];

        //如果样式为null,则进行构建这种样式
        if (style == null){
            style = wkb.createCellStyle(); // 样式对象
            if (tableCell.align_center){
                //style.setAlignment(XSSFCellStyle.ALIGN_CENTER);// 水平居中
                style.setAlignment(HorizontalAlignment.CENTER);
            }
            if (tableCell.vertical_center){
                //style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//    垂直居中
                style.setVerticalAlignment(VerticalAlignment.CENTER);
            }
            if (tableCell.decimalLen != null){
                if (tableCell.percentage == true){
                    //百分比
                    DataFormat dataFormat = wkb.createDataFormat();//创建格式化对象
                    style.setDataFormat(dataFormat.getFormat("#,##0.00%"));//设置数值类型格式为保留两位小数
                }else {
                    DataFormat dataFormat = wkb.createDataFormat();//创建格式化对象
                    style.setDataFormat(dataFormat.getFormat("#,##0.00"));//设置数值类型格式为保留两位小数
                }
            }
            styles[key] = style;
        }
        cell.setCellStyle(style);
    }

    /**
     * 给单元格赋值
     * @param cell
     * @param value
     */
    private static void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof String) {
            cell.setCellValue(value.toString());
        } else if (value instanceof Double || value instanceof Float) {
            cell.setCellValue(Double.parseDouble(value.toString()));
        } else if (value instanceof Integer) {
            cell.setCellValue(Integer.parseInt(value.toString()));
        }else if (value instanceof BigDecimal){
            BigDecimal b = (BigDecimal) value;
            Object num = null;
            if(new BigDecimal(b.intValue()).compareTo(b) == 0){
                //整数
                num = b.intValue();
            }else {
                //小数
                num = b.doubleValue();
            }
            setCellValue(cell, num);
        }
    }

}
