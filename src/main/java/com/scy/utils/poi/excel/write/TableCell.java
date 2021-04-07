package com.scy.utils.poi.excel.write;

/**
 * 单元格
 */
public class TableCell {
    /**
     * 单元格的值
     */
    public Object value;
    /**
     * 所占行数
     */
    public int rowSpan = 1;
    /**
     * 所占列数
     */
    public int colspan = 1;

    /**
     * 水平居中
     */
    public boolean align_center;
    /**
     * 垂直居中
     */
    public boolean vertical_center;
    /**
     * 保留小数位数
     */
    public Integer decimalLen;

    /**
     * 百分比
     */
    public boolean percentage;

    /**
     *
     * @param value 值
     */
    public TableCell(Object value){
        this.value = value;
    }

    /**
     *
     * @param value 值
     * @param decimalLen    小数位数
     */
    public TableCell(Object value,Integer decimalLen){
        this.value = value;
        this.decimalLen = decimalLen;
    }

    /**
     *
     * @param value         值
     * @param decimalLen    小数位数
     * @param percentage    百分比
     */
    public TableCell(Object value,Integer decimalLen, boolean percentage){
        this.value = value;
        this.decimalLen = decimalLen;
        this.percentage = percentage;
    }

    /**
     *
     * @param value     值
     * @param rowSpan   占用行数
     * @param colspan   占用列数
     */
    public TableCell(Object value,int rowSpan,int colspan){
        this.value = value;
        this.rowSpan = rowSpan;
        this.colspan = colspan;
    }

    /**
     *
     * @param value             值
     * @param rowSpan           占用行数
     * @param colspan           占用列数
     * @param align_center      水平居中
     * @param vertical_center   垂直居中
     */
    public TableCell(Object value,int rowSpan,int colspan, boolean align_center , boolean vertical_center){
        this.value = value;
        this.rowSpan = rowSpan;
        this.colspan = colspan;
        this.align_center = align_center;
        this.vertical_center = vertical_center;
    }

    /**
     *
     * @param value             值
     * @param align_center      水平居中
     * @param vertical_center   垂直居中
     */
    public TableCell(String value, boolean align_center , boolean vertical_center) {
        this.value = value;
        this.align_center = align_center;
        this.vertical_center = vertical_center;
    }
}
