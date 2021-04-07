package com.scy.utils.poi.excel.write;

import java.util.List;

/**
 * 表单
 */
public class ExcelSheet {
    /**
     * 表单名称
     */
    public String sheetName = "sheet";
    /**
     * 表单行数据
     */
    public List rowList;


    public ExcelSheet(List rowList, String sheetName) {
        this.sheetName = sheetName;
        this.rowList = rowList;
    }
}
