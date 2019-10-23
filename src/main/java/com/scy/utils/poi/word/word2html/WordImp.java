package com.scy.utils.poi.word.word2html;

import java.util.Map;

/**
 * word 导入
 */
public class WordImp {
    public static void main(String[] args) {
        try {
            //设置html文件的生成路径
            WordUtil.setHtmlFile("C:\\Users\\sun\\Desktop\\导入测试\\zuoye.html");
            //设置base目录
            WordUtil.setBasePath("C:\\Users\\sun\\Desktop\\导入测试\\");
            //设置web访问域名地址
            WordUtil.setWebPath("www.scy.com/");
            Map<String,Object> dataInfo =  WordUtil.getWordAndStyle("C:\\Users\\sun\\Desktop\\导入测试\\zuoye.doc");

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
