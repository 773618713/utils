package com.scy.utils.poi.excel.write;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TestApp {
    public static void main(String[] args) throws Exception{
        try {
            List colList = new ArrayList();
            colList.add(new TableCell("3.14",true,true));
            colList.add(new TableCell(1,2,1));
            colList.add(2);
            colList.add(3);

            List rowList = new ArrayList();
            rowList.add(colList);

            List sheetList = new ArrayList();
            ExcelSheet excelSheet = new ExcelSheet(rowList,"sheet");
            sheetList.add(excelSheet);

            FileOutputStream fos = new FileOutputStream("C:\\Users\\sun\\Desktop\\new1.xlsx");
            WriteExcel.WriteExcel(sheetList, fos);
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
