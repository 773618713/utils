package com.scy.utils.poi.word.word2html;

import com.scy.utils.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @Title:导出工具类
 * @Description:
 * @Author:
 * @Since:2018年10月30日
 * @Version:1.1.0
 * @Copyright:Copyright (c) 上海启疆信息科技有限公司 2017 ~ 2018 版权所有
 */
public class WordUtil {

    /**
     * 回车符ASCII码
     */
    private static final short ENTER_ASCII = 13;

    /**
     * 空格符ASCII码
     */
    private static final short SPACE_ASCII = 32;

    /**
     * 水平制表符ASCII码
     */
    private static final short TABULATION_ASCII = 9;

    public static String htmlText = "";
    public static String htmlTextTbl = "";
    public static int counter = 0;
    public static int beginPosi = 0;
    public static int endPosi = 0;
    public static int beginArray[];
    public static int endArray[];
    public static String htmlTextArray[];
    public static boolean tblExist = false;

    public static final String inputFile = "D:\\wordImport\\template\\试卷导入模板测试.doc";

    private static String htmlFile;
    private static String basePath;
    private static String webPath;

    private static Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|]");
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    /**
     * @Description 基于freemarker导出包含富文本的word
     *
     */

    public static void main(String[] args) {

        try {
            getWordAndStyle(inputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Map<String, Object> initOrderMap() {
        Map<String, Object> orderMap = new HashMap<String, Object>();
        orderMap.put("一", "1");
        orderMap.put("二", "2");
        orderMap.put("三", "3");
        orderMap.put("四", "4");
        orderMap.put("五", "5");
        orderMap.put("六", "6");
        orderMap.put("七", "7");
        orderMap.put("八", "8");
        orderMap.put("九", "9");
        orderMap.put("十", "10");
        orderMap.put("十一", "11");
        orderMap.put("十二", "12");
        orderMap.put("十三", "13");
        orderMap.put("十四", "14");
        orderMap.put("十五", "15");

        return orderMap;
    }

    /**
     * word文档图片存储路径
     * @return
     */
    public static String wordImageFilePath() {
        return  basePath + webPath + "picture/";
    }

    /**
     *  word文档图片Web访问路径
     * @return
     */
    public static String wordImgeWebPath() {
        return  webPath + "picture/";
    }

    public static String getFileUploadPath(MultipartFile file, String uploadPath, String userId) {
        if(file.isEmpty()){
            return "";
        }
        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize();
        //System.out.println(fileName + "-->" + size);

        basePath = uploadPath;
        webPath = "wordImport/" + sdf.format(new Date()) + "/" + userId + "/";
        String path = getBasePath() + webPath;
        htmlFile = path + "/" + fileName.substring(0, fileName.lastIndexOf(".")) + ".html";

        try {

            FileUtil.uploadFile(file.getBytes(), path, fileName); //保存文件
            String fullPath = path + "/" + fileName;

            return fullPath;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * word 解析
     * @param fileName      文件路径
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getWordAndStyle(String fileName) throws Exception {
        FileInputStream is = new FileInputStream(new File(fileName));
        HWPFDocument doc = new HWPFDocument(is);

        Range rangetbl = doc.getRange();//得到文档的读取范围
        TableIterator it = new TableIterator(rangetbl);

        int num = 100;

        beginArray = new int[num];
        endArray = new int[num];
        htmlTextArray = new String[num];

        //取得文档中字符的总数
        int length = doc.characterLength();
        //创建图片容器
        PicturesTable pTable = doc.getPicturesTable();

        //htmlText = "<html><head><title>" + doc.getSummaryInformation().getTitle() + "</title></head><body>";
        htmlText = "<html><head><title></title></head><body>";
        System.out.println("htmlText======="+htmlText);
        //创建临时字符串，加以判断一串字符是否存在相同格式

        //读取表格
        if(it.hasNext()) {
            readTable(it, rangetbl);
        }

        int cur = 0;
        boolean mathFlag = false;

        String tempString = "";
        for (int i = 0; i < length-1; i++) {
            // 整篇文章的字符通过一个个字符来判断,range为得到文档的范围
            Range range = new Range(i, i+1, doc);

            CharacterRun cr = range.getCharacterRun(0);

            if(tblExist) {
                if(i == beginArray[cur]) {
                    htmlText += tempString + htmlTextArray[cur];
                    tempString = "";
                    i = endArray[cur] - 1;
                    cur ++;
                    continue;
                }
            }
            if(pTable.hasPicture(cr)) {
                htmlText += tempString;
                //读写图片
                readPicture(pTable, cr);
                tempString = "";
            }else {
                Range range2 = new Range(i+1, i+2, doc);
                //第二个字符
                CharacterRun cr2 = range2.getCharacterRun(0);
                char c = cr.text().charAt(0);

                if(c == '\u0013') {
                    mathFlag = true;
                }else if(c == '\u0014') {
                    mathFlag = false;
                }
                if(!mathFlag && c != '\u0014' && c != '\u0015') {
                    if (c == SPACE_ASCII) {//判断是否为空字符串
                        tempString += "&nbsp;";
                    } else if (c == TABULATION_ASCII) {//判断是否为空字符串
                        tempString += "&nbsp;&nbsp;&nbsp;&nbsp;";
                    }

                    // 比较前后2个字符是否具有相同的格式
                    boolean flag = compareCharStyle(cr, cr2);
                    if (flag && c != ENTER_ASCII) {
                        tempString += cr.text();
                    } else {
                        String fontStyle = "<span style='font-family:" + cr.getFontName() + ";font-size:" + cr.getFontSize() / 2
                                + "pt;color:" + getHexColor(cr.getIco24()) + ";";

                        if (cr.isBold()) {
                            fontStyle += "font-weight:bold;";
                        }
                        if (cr.isItalic()) {
                            fontStyle += "font-style:italic;";
                        }
                        if (cr.getUnderlineCode() == 1) { //判断是否有下划线
                            fontStyle += "text-decoration:underline;";
                        }

                        htmlText += fontStyle + "' >" + tempString + cr.text();
                        htmlText += "</span>";
                        tempString = "";
                    }
                    // 判断是否为回车符
                    if (c == ENTER_ASCII) {
                        htmlText += "<br/>";
                    }
                }
            }
        }
        htmlText += tempString + "</body></html>";
        //htmlText.substring(htmlText.indexOf("EMBED"), htmlText.indexOf("<img"));

        //生成html文件
        writeFile(htmlText);
        System.out.println("------------WordToHtml转换成功----------------");

        //word试卷数据模型化
        Map<String, Object> wordToHtml = analysisHtmlString(htmlText);
        System.out.println("------------WordToHtml模型化成功----------------");

//        WordExtractor ex = new WordExtractor(is);
//        String text2003 = ex.getText();
//        System.out.println(text2003);

//        OPCPackage opcPackage = POIXMLDocument.openPackage(fileName);
//        POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
//        String text2007 = extractor.getText();
//        System.out.println(text2007);

        return wordToHtml;
    }

    /**
     * @Description读写文档中的表格
     *
     * @params pTable
     * @params cr
     * @throws Exception
     *
     */
    public static void readTable(TableIterator it, Range rangetbl) throws Exception {
        htmlTextTbl = "";

        //迭代文档中的表格

        counter = -1;
        while(it.hasNext()) {
            tblExist = true;
            htmlTextTbl = "";
            Table tb = (Table) it.next();
            beginPosi = tb.getStartOffset();
            endPosi = tb.getEndOffset();

            counter = counter + 1;
            //迭代行，默认从0开始
            beginArray[counter] = beginPosi;
            endArray[counter] = endPosi;

            htmlTextTbl += "<table border>";
            for (int i = 0; i < tb.numRows(); i++) {
                TableRow tr = tb.getRow(i);

                htmlTextTbl += "<tr>";
                //迭代列，默认从0开始
                for (int j = 0; j < tr.numCells(); j++) {
                    TableCell td = tr.getCell(j);//取得单元格
                    int cellWidth = td.getWidth();

                    //取得单元格的内容
                    for(int k = 0; k < td.numParagraphs(); k++) {
                        Paragraph para = td.getParagraph(k);
                        String s = para.text().toLowerCase().trim();
                        if(s == "") {
                            s = " ";
                        }
                        htmlTextTbl += "<td width=" + cellWidth + ">" + s + "</td>";
                    }
                }
            }
            htmlTextTbl += "</table>";
            htmlTextArray[counter] = htmlTextTbl;
        }
    }

    /**
     * @Description 读写文档中的图片
     *
     * @param pTable
     * @param cr
     * @throws Exception
     *
     */
    public static void readPicture(PicturesTable pTable, CharacterRun cr) throws Exception {
        //提取图片
        Picture pic = pTable.extractPicture(cr, false);
        // 返回POI建议的图片文件名
        String afileName = pic.suggestFullFileName();

        File file = new File(wordImageFilePath());
        if(!file.exists()) {
            file.mkdirs();
        }
        String path = wordImageFilePath() + File.separator + afileName;
        OutputStream out = new FileOutputStream(new File(path));
        pic.writeImageContent(out);
        //是.wmf公式，转成png
        if (afileName.contains("wmf") || afileName.contains("WMF")) {
            //emf或者wmf转换为png图片格式
            String pngPath = ImageConvert.emfConversionPng(path);
            //图片转base64
            //String s = GetImageStr(pngPath);
            //String img = "<img src='" + s + "' />";
            //String img = "<img src='" + pngPath + "' />";
            //str.append(img);
            afileName = afileName.replace(
                    afileName.substring(afileName.length() - 3), "png");
        } else if(afileName.contains("emf") || afileName.contains("EMF")) {
            String pngPath = ImageConvert.emfConversionPng(path);
            afileName = afileName.replace(
                    afileName.substring(afileName.length() - 3), "png");
        }
        htmlText += "<img src='" + wordImgeWebPath() + afileName + "' />";
    }

    public static boolean compareCharStyle(CharacterRun cr1, CharacterRun cr2) {
        boolean flag = false;
        if(cr1.isBold() == cr2.isBold() && cr1.isItalic() == cr2.isItalic() && cr1.getFontName().equals(cr2.getFontName())
                && cr1.getFontSize() == cr2.getFontSize() && cr1.getColor() == cr2.getColor() && cr1.getUnderlineCode() == cr2.getUnderlineCode()) {
            flag = true;
        }
        return flag;
    }

    /****** 字体颜色模块start ******/
    public static int red(int c) {
        return c & 0XFF;
    }

    public static int green(int c) {
        return (c >> 8) & 0XFF;
    }

    public static int blue(int c) {
        return (c >> 16) & 0XFF;
    }

    public static int rgb(int c) {
        return (red(c) << 16) | (green(c) << 8) | blue(c);
    }

    public static String rgbToSix(String rgb) {
        int length = 6 - rgb.length();
        String str = "";
        while(length > 0) {
            str += "0";
            length --;
        }
        return str + rgb;
    }

    public static String getHexColor(int color) {
        color = color == -1 ? 0 :color;
        int rgb = rgb(color);
        return "#" + rgbToSix(Integer.toHexString(rgb));
    }
    /****** 字体颜色模块end ******/

    /**
     * @Description 写文件
     *
     * @param s
     *
     */
    public static void writeFile(String s) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        PrintWriter writer = null;

        try {
            File file = new File(htmlFile);
            if(!file.exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(s);
            bw.close();
            fos.close();

            //编码转换
            writer = new PrintWriter(file, "utf-8");
            writer.write(s);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * @Description分析html
     *
     * @param s
     */
    public static Map<String, Object> analysisHtmlString (String s) {
        String q[] = s.split("<br/>");

        LinkedList<String> list = new LinkedList<String>();

        //清除空字符
        for (int i = 0; i < q.length; i++) {
            if(StringUtils.isNotBlank(q[i].toString().replaceAll("</?[^>]+>","").trim()) || q[i].toString().contains("<img")) {
                list.add(q[i].toString().trim()+"<br/>");
            }
        }
        String [] result = {};
        String ws[] = list.toArray(result);
//    	int singleScore = 0; //单选题分数
//    	int multipleScore = 0; //多选题分数
//    	int fillingScore = 0; //填空题分数
//    	int judgeScore = 0; //判断题分数
//    	int askScore = 0; //解答题分数
//    	int singleNum = 0;
//    	int multipleNum = 0;
//    	int fillingNum = 0;
//    	int judgeNum = 0;
//    	int askNum = 0;

        /*********** 试卷基础数据赋值 **************/
//    	for (int i = 0; i < ws.length; i++) {
//    		String delHtml = ws[i].toString().replaceAll("</?[^>]+>", "").trim(); //去除html
//    		if(delHtml.contains("、单选题")) {
//    			String numScore = numScore(delHtml);
//    			singleNum = Integer.parseInt(numScore.split(",")[0]);
//    			singleScore = Integer.parseInt(numScore.split(",")[1]);
//    		}else if(delHtml.contains("、多择题")) {
//    			String numScore = numScore(delHtml);
//    			multipleNum = Integer.parseInt(numScore.split(",")[0]);
//    			multipleScore = Integer.parseInt(numScore.split(",")[1]);
//    		}else if(delHtml.contains("、填空题")) {
//    			String numScore = numScore(delHtml);
//    			fillingNum = Integer.parseInt(numScore.split(",")[0]);
//    			fillingScore = Integer.parseInt(numScore.split(",")[1]);
//    		}else if(delHtml.contains("、判断题")) {
//    			String numScore = numScore(delHtml);
//    			judgeNum = Integer.parseInt(numScore.split(",")[0]);
//    			judgeScore = Integer.parseInt(numScore.split(",")[1]);
//    		}else if(delHtml.contains("、问答题")) {
//    			String numScore = numScore(delHtml);
//    			askNum = Integer.parseInt(numScore.split(",")[0]);
//    			askScore = Integer.parseInt(numScore.split(",")[1]);
//    		}
//    	}

        /************ 试卷数据模型化 ************/
        Map<String, Object> dataInfo = new HashMap<String, Object>();
        List<Map<String, Object>> bigTiMaps = new ArrayList<Map<String,Object>>();
        List<Map<String, Object>> smalMaps  = new ArrayList<Map<String,Object>>();
        List<Map<String, Object>> sleMaps  = new ArrayList<Map<String,Object>>();

        String htmlText = "";
        String answer = "";

//    	int smalScore = 0;
        for (int j = ws.length - 1; j >= 0; j--) {

            String html = ws[j].toString().trim(); //html格式
            String delHtml = ws[j].toString().replaceAll("</?[^>]+>","").trim();//去除html
            System.out.println("delHtml======"+delHtml);
//			System.out.println("html======"+html);

            if(delHtml.contains("答案：")) {
                answer = delHtml.substring(3);
            }else {

                if(!isOption(delHtml) && !isTitle(delHtml) && !isBigTitle(delHtml)) {//无
//					if(isTitle(delHtml)) {
//						smalScore = itemNum(delHtml);
//					}
                    htmlText = html + htmlText;
                    System.out.println("htmlText======"+htmlText);
                    if(j == 0) {
                        dataInfo.put("title", spaceTransform(delHtml));
                    }
                }else if(isOption(delHtml)) {
                    //选择题选择项
                    Map<String, Object> sleMap = new HashMap<String, Object>();
                    System.out.println("sleMap======"+html.replaceFirst("([a-zA-z]+：)", "") + htmlText);

                    String subStr = delHtml.substring(0, 1);
                    sleMap.put("order_number", subStr);
                    sleMap.put("content_text", spaceTransform(delHtml.replaceFirst("([a-zA-z]+：)", "")));
                    sleMap.put("content_html", html.replaceFirst("([a-zA-z]+：)", "") + htmlText);
                    if(answer.contains(subStr)) {
                        sleMap.put("is_correct", "1");
                    }else {
                        sleMap.put("is_correct", "0");
                    }
                    sleMaps.add(sleMap);
                    htmlText = "";
                }else if(isTitle(delHtml)){
                    //小标题
                    Map<String, Object> smalMap = new HashMap<String, Object>();
                    System.out.println("smalMap======"+ html.replaceFirst("([0-9]+、)", "") + htmlText);
                    smalMap.put("title_html", html.replaceFirst("([0-9]+、)", "") + htmlText);
                    smalMap.put("title", spaceTransform(delHtml.replaceFirst("([0-9]+、)", "")));
                    //smalMap.put("score", smalScore > 0 ? smalScore + "" : itemNum(delHtml) + ""); //分值
                    smalMap.put("order_number", delHtml.split("、")[0]);
                    smalMap.put("answer", answer);
                    if(sleMaps.size() > 0) {
                        smalMap.put("is_select", "1");
                        smalMap.put("option_list", sleMaps);
                    }else {
                        smalMap.put("is_select", "0");
                    }
                    smalMaps.add(smalMap);

                    sleMaps  = new ArrayList<Map<String,Object>>();
                    htmlText = "";
                    answer = "";
                }else if(isBigTitle(delHtml)) {
                    //大标题
                    Map<String, Object> bigTiMap = new HashMap<String, Object>();
                    bigTiMap.put("name", spaceTransform(delHtml.substring(delHtml.indexOf("、")+1)));
                    Map<String, Object> orderMap = initOrderMap();
                    for(Entry<String, Object> str : orderMap.entrySet()) {
                        if(delHtml.split("、")[0].equals(str.getKey())) {
                            bigTiMap.put("order_number", str.getValue());
                        }
                    }
                    bigTiMap.put("question_list", smalMaps);
                    bigTiMaps.add(bigTiMap);

                    smalMaps  = new ArrayList<Map<String,Object>>();
                    htmlText = "";
                }
            }
        }
        dataInfo.put("block_list", bigTiMaps);
        //导入
        dataInfo.put("add_type", "2");
        //保密
        dataInfo.put("is_public", "0");
        //默认值可修改
        dataInfo.put("exam_time", "90");
        //选项 横向排列
        dataInfo.put("arrange_way", "1");
        return dataInfo;
    }

    //获取大题-题目数量以及题目总计分数
    public static String numScore(String delHtml) {
        String regEx = "[^0-9+，|,+^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(delHtml);
        String s = m.replaceAll("").trim();
        if(StringUtils.isNotBlank(s)) {
            if(s.contains(",")) {
                return s;
            }else if(s.contains("，")) {
                return s.replace("，", ",");
            }else {
                return "0,0";
            }
        }else {
            return "0,0";
        }
    }

    //获取每小题分数
    public static int itemNum(String delHtml) {
        Pattern pattern = Pattern.compile("（(.*?)）"); //中文括号
        Matcher matcher = pattern.matcher(delHtml);
        if (matcher.find() && isNumeric(matcher.group(1))) {
            return Integer.parseInt(matcher.group(1));
        }else {
            return 0;
        }
    }

    private static Pattern numberPattern = Pattern.compile("[0-9]*");
    private static Pattern titlePattern = Pattern.compile("^([0-9]+[-\\、].*)");
    private static Pattern optionPattern = Pattern.compile("^([a-zA-Z]+[-\\：].*)");

    //判断Str是否为 数字
    public static boolean isNumeric(String str) {
        return numberPattern.matcher(str).matches();
    }

    //判断Str是否存在小标题号
    public static boolean isTitle(String str) {
        return titlePattern.matcher(str).matches();
    }

    //判断Str是否是选择题选择项
    public static boolean isOption(String str) {
        return optionPattern.matcher(str).matches();
    }

    //判断Str是否是大标题
    public static boolean isBigTitle(String str) {
        boolean iso = false;
        if(str.contains("一、")) {
            iso = true;
        }else if(str.contains("二、")) {
            iso = true;
        }else if(str.contains("三、")) {
            iso = true;
        }else if(str.contains("四、")) {
            iso = true;
        }else if(str.contains("五、")) {
            iso = true;
        }else if(str.contains("六、")) {
            iso = true;
        }else if(str.contains("七、")) {
            iso = true;
        }else if(str.contains("八、")) {
            iso = true;
        }else if(str.contains("九、")) {
            iso = true;
        }else if(str.contains("十、")) {
            iso = true;
        }else if(str.contains("十一、")) {
            iso = true;
        }else if(str.contains("十二、")) {
            iso = true;
        }else if(str.contains("十三、")) {
            iso = true;
        }else if(str.contains("十四、")) {
            iso = true;
        }else if(str.contains("十五、")) {
            iso = true;
        }else if(str.contains("十六、")) {
            iso = true;
        }

        return iso;
    }

    /**
     * @Description word导出
     */
    public static void exportWord(Map<String, Object> dataMap, String fileName) {
        try {
            @SuppressWarnings("deprecation")
            Configuration configuration = new Configuration();
            configuration.setDefaultEncoding("UTF-8");

            WordUtil.getData(dataMap);
            WordHtmlGeneratorHelper.handleAllObject(dataMap);

            //String filePath = WordUtil.class.getClassLoader().getResource("ftl/ExportWord.ftl").getPath();
            String filePath = "/SCApp/Data/ftl/ExportWord.ftl";
            configuration.setDirectoryForTemplateLoading(new File(filePath).getParentFile());//模板文件所在路径
            Template t = null;
            t = configuration.getTemplate("ExportWord.ftl","UTF-8"); //获取模板文件

            File outFile = new File(fileName);
            if(!outFile.exists()){
                outFile.getParentFile().mkdirs();
            }

            Writer out = null;
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
            t.process(dataMap, out); //将填充数据填入模板文件并输出到目标文件
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description 获取当前特定格式时间
     * @return
     */
    private static String getTime(String pattern){
        return new SimpleDateFormat(pattern).format(new Date());
    }

    /**
     * @Description 封装数据
     * @param res
     *
     * @return
     */
    private static Map<String, Object> getData(Map<String, Object> res) {

        try{
            List<Map<String, Object>> questionList = (List<Map<String, Object>>) res.get("questionList");
            for (int i = 0; i < questionList.size(); i++) {
                Map<String, Object> questionMap = questionList.get(i);

                String key = "content";
                Object val = questionMap.get("title_html");

                if("content".equals(key)){//处理富文本
                    RichHtmlHandler handler = new RichHtmlHandler(val.toString());
                    handler.setDocSrcLocationPrex("file:///C:/5B2DDB9F");
                    handler.setDocSrcParent("file7059.files");
                    handler.setNextPartId("01D47044.24034C70");
                    handler.setShapeidPrex("_x56fe__x7247__x0020");
                    handler.setSpidPrex("_x0000_i");
                    handler.setTypeid("#_x0000_t75");

                    handler.handledHtml(false);

                    String bodyBlock = handler.getHandledDocBodyBlock();

                    String handledBase64Block = "";
                    if (handler.getDocBase64BlockResults() != null
                            && handler.getDocBase64BlockResults().size() > 0) {
                        for (String item : handler.getDocBase64BlockResults()) {
                            handledBase64Block += item + "\n";
                        }
                    }
                    if(StringUtils.isBlank(handledBase64Block)){
                        handledBase64Block = "";
                    }
                    questionMap.put("imagesBase64String", handledBase64Block);
                    String xmlimaHref = "";
                    if (handler.getXmlImgRefs() != null
                            && handler.getXmlImgRefs().size() > 0) {
                        for (String item : handler.getXmlImgRefs()) {
                            xmlimaHref += item + "\n";
                        }
                    }

                    if(StringUtils.isBlank(xmlimaHref)){
                        xmlimaHref = "";
                    }
                    questionMap.put("imagesXmlHrefString", xmlimaHref);
                    questionMap.put("title_html", bodyBlock);
                }

                List<Map<String, Object>> optionList = (List<Map<String, Object>>) questionMap.get("optionList");
                for (int j = 0; j < optionList.size(); j++) {
                    Map<String, Object> optionMap = optionList.get(j);
                    RichHtmlHandler handler = new RichHtmlHandler(optionMap.get("content_html").toString());
                    handler.setDocSrcLocationPrex("file:///C:/5B2DDB9F");
                    handler.setDocSrcParent("file7059.files");
                    handler.setNextPartId("01D47044.24034C70");
                    handler.setShapeidPrex("_x56fe__x7247__x0020");
                    handler.setSpidPrex("_x0000_i");
                    handler.setTypeid("#_x0000_t75");

                    handler.handledHtml(false);

                    String bodyBlock = handler.getHandledDocBodyBlock();

                    String handledBase64Block = "";
                    if (handler.getDocBase64BlockResults() != null
                            && handler.getDocBase64BlockResults().size() > 0) {
                        for (String item : handler.getDocBase64BlockResults()) {
                            handledBase64Block += item + "\n";
                        }
                    }
                    if(StringUtils.isBlank(handledBase64Block)){
                        handledBase64Block = "";
                    }
                    optionMap.put("imagesBase64String", handledBase64Block);
                    String xmlimaHref = "";
                    if (handler.getXmlImgRefs() != null
                            && handler.getXmlImgRefs().size() > 0) {
                        for (String item : handler.getXmlImgRefs()) {
                            xmlimaHref += item + "\n";
                        }
                    }

                    if(StringUtils.isBlank(xmlimaHref)){
                        xmlimaHref = "";
                    }
                    optionMap.put("imagesXmlHrefString", xmlimaHref);
                    optionMap.put("content_html", bodyBlock);
                }

            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return res;
    }

    /**
     * @title 替换文本中的特殊字符
     * @param str
     * @return
     */
    private static String transform(String str){
        if(str.contains("<")||str.contains(">")||str.contains("&")){
            str=str.replaceAll("&", "&amp;");
            str=str.replaceAll("<", "&lt;");
            str=str.replaceAll(">", "&gt;");
        }
        return str;
    }

    /**
     * @Description 替换文本中&nbsp为空格
     * @param str
     * @return
     */
    private static String spaceTransform(String str){
        if(str.contains("&nbsp;")){
            str = str.replaceAll("&nbsp;", " ");
        }
        return str;
    }

//    /**
//     * @title 导出Excel
//     * @param ids
//     * @param response
//     * @param newsNoticeService
//     * @param newsNoticeProcessService
//     * @param iNewsTypeService
//     * @param newsFileService
//     */
//    public static void ExportNoticeExcel(String ids, HttpServletResponse response, INewsNoticeService newsNoticeService, INewsNoticeProcessService newsNoticeProcessService, INewsTypeService iNewsTypeService, INewsFileService newsFileService){
//
//        // 导入2007excel
//        XSSFWorkbook workBook = new XSSFWorkbook();
//        try {
//            // 生成一个表格
//            XSSFSheet xsheet = workBook.createSheet();
//            workBook.setSheetName(0, "Sheet1");
//
//            XSSFPrintSetup xhps = xsheet.getPrintSetup();
//            xhps.setPaperSize((short) 9); // 设置A4纸
//
//            int i = 0;
//            // 设置列宽
//            xsheet.setColumnWidth(i++, 2000);
//            xsheet.setColumnWidth(i++, 4000);
//            xsheet.setColumnWidth(i++, 4000);
//            xsheet.setColumnWidth(i++, 4500);
//            xsheet.setColumnWidth(i++, 4500);
//
//            xsheet.setColumnWidth(i++, 4000);
//            xsheet.setColumnWidth(i++, 4000);
//            xsheet.setColumnWidth(i++, 4000);
//            xsheet.setColumnWidth(i++, 4000);
//            xsheet.setColumnWidth(i++, 6000);
//
//            xsheet.setColumnWidth(i++, 6000);
//            xsheet.setColumnWidth(i++, 4000);
//            xsheet.setColumnWidth(i++, 5000);
//            xsheet.setColumnWidth(i++, 4000);
//            xsheet.setColumnWidth(i++, 4000);
//
//            xsheet.setColumnWidth(i++, 4000);
//            xsheet.setColumnWidth(i++, 4000);
//
//            // 设置字段字体
//            XSSFFont xFieldFont = workBook.createFont();
//            xFieldFont.setFontName("微软雅黑");
//            xFieldFont.setFontHeightInPoints((short) 11);// 字体大小
//            xFieldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
//            // 字段单元格样式
//            XSSFCellStyle xFieldStyle = workBook.createCellStyle();
//            xFieldStyle.setFont(xFieldFont);
//            xFieldStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
//            xFieldStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
//            xFieldStyle.setBorderLeft(BorderStyle.THIN);// 边框的大小
//            xFieldStyle.setBorderRight(BorderStyle.THIN);
//            xFieldStyle.setBorderTop(BorderStyle.THIN);
//            xFieldStyle.setBorderBottom(BorderStyle.THIN);
//
//            // 设置字段字体
//            XSSFFont xField2Font = workBook.createFont();
//            xField2Font.setFontName("微软雅黑");
//            xField2Font.setFontHeightInPoints((short) 10);// 字体大小
//            xField2Font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
//            // 字段单元格样式
//            XSSFCellStyle xField2Style = workBook.createCellStyle();
//            xField2Style.setFont(xField2Font);
//            xField2Style.setAlignment(HorizontalAlignment.CENTER);// 左右居中
//            xField2Style.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
//            xField2Style.setBorderLeft(BorderStyle.THIN);// 边框的大小
//            xField2Style.setBorderRight(BorderStyle.THIN);
//            xField2Style.setBorderTop(BorderStyle.THIN);
//            xField2Style.setBorderBottom(BorderStyle.THIN);
//
//
//
//            // 设置字段内容字体
//            XSSFFont xContentFont = workBook.createFont();
//            xContentFont.setFontName("微软雅黑");
//            xContentFont.setFontHeightInPoints((short) 10);// 字体大小
//            // 字段单元格样式
//            XSSFCellStyle xContentStyle = workBook.createCellStyle();
//            xContentStyle.setFont(xContentFont);
//            xContentStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
//            xContentStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
//            xContentStyle.setWrapText(true);// 自动换行
//            xContentStyle.setBorderLeft(BorderStyle.THIN);// 边框的大小
//            xContentStyle.setBorderRight(BorderStyle.THIN);
//            xContentStyle.setBorderTop(BorderStyle.THIN);
//            xContentStyle.setBorderBottom(BorderStyle.THIN);
//
//            XSSFRow xrow1 = null;
//            xrow1 = xsheet.createRow(0);
//            xrow1.setHeight((short) 400);
//
//            XSSFCell xrow1_cell1 = xrow1.createCell(0);//创建第1行第一列
//            xrow1_cell1.setCellValue("亚厦公文发布清单（查询时间："+getTime("yyyy年MM月hh日HH时mm分")+"）");
//            xrow1_cell1.setCellStyle(xFieldStyle);
//
//            CellRangeAddress address = new CellRangeAddress(0, 0, 0, i-1);//合并单元格 参数依次为 行start end 列start end
//            xsheet.addMergedRegion(address);
//
//            XSSFRow xrow2 = null;
//            xrow2 = xsheet.createRow(1);
//            xrow2.setHeight((short) 400);
//
//            for(int j=0;j<i;j++){
//                XSSFCell xrow2_cell1 = xrow2.createCell(j);
//                xrow2_cell1.setCellStyle(xField2Style);
//                String temp = "";
//                if(j == 0){
//                    temp = "序号";
//                }else if(j == 1){
//                    temp = "发文类型";
//                }else if(j == 2){
//                    temp = "发文主体";
//                }else if(j == 3){
//                    temp = "发文编号";
//                }else if(j == 4){
//                    temp = "标题";
//                }else if(j == 5){
//                    temp = "发文单位";
//                }else if(j == 6){
//                    temp = "发文部门";
//                }else if(j == 7){
//                    temp = "提交人";
//                }else if(j == 8){
//                    temp = "状态";
//                }else if(j == 9){
//                    temp = "提交时间";
//                }else if(j == 10){
//                    temp = "发文时间";
//                }else if(j == 11){
//                    temp = "发布版块";
//                }else if(j == 12){
//                    temp = "关键词";
//                }else if(j == 13){
//                    temp = "有无附件";
//                }else if(j == 14){
//                    temp = "报送审计时间";
//                }else if(j == 15){
//                    temp = "报送审计经办人";
//                }else if(j == 16){
//                    temp = "档号";
//                }
//                xrow2_cell1.setCellValue(temp);
//            }
//
//            String []idArr = ids.split(",");
//            for(int k=0;k<idArr.length;k++){
//                XSSFRow xrow3 = null;
//                xrow3 = xsheet.createRow(k+2);
//                NewsNotice notice = newsNoticeService.getNoticeById(idArr[k]);
//
//                for(int j=0;j<i;j++){
//                    String temp = "";
//                    XSSFCell xrow3_cell1 = xrow3.createCell(j);
//                    xrow3_cell1.setCellStyle(xContentStyle);
//                    if(j == 0){
//                        temp = k+1+"";
//                    }else if(j == 1){
//                        temp = notice.getCategoryName();
//                    }else if(j == 2){
//                        temp = notice.getOwnerName();
//                    }else if(j == 3){
//                        temp = notice.getArticleNo();
//                    }else if(j == 4){
//                        temp = notice.getTitle();
//                    }else if(j == 5){
//                        temp = notice.getCreateCompanyName();
//                    }else if(j == 6){
//                        temp = notice.getCreateDeptmentName();
//                    }else if(j == 7){
//                        NewsNoticeProcess newsNoticeProcess = new NewsNoticeProcess();
//                        newsNoticeProcess.setNewNoticeId(notice.getId());
//                        List<NewsNoticeProcess> newsNoticeProcessList = newsNoticeProcessService.getAll(newsNoticeProcess);
//                        if(null !=newsNoticeProcessList && newsNoticeProcessList.size()>0){
//                            temp = newsNoticeProcessList.get(0).getUsername();
//                        }
//                    }else if(j == 8){
//                        int status = notice.getPublishStatus();
//                        if(status == 0) {
//                            temp  = "未发布";
//                        } else if (status == -1) {
//                            temp =  "撤回";
//                        } else if (status == -2) {
//                            temp =  "终止";
//                        } else if (status == 1) {
//                            if (notice.getPublishTime().getTime() > new Date().getTime()) {
//                                temp =  "待发布";
//                            }
//                            temp = "已发布";
//                        }
//                    }else if(j == 9){
//                        temp = DateUtil.format( notice.getWritingTime(),"yyyy-MM-dd HH:mm:ss");
//                    }else if(j == 10){
//                        if(notice.getPublishStatus() == 1){
//                            temp = DateUtil.format( notice.getPublishTime(),"yyyy-MM-dd HH:mm:ss");
//                        }else{
//                            temp = "";
//                        }
//                    }else if(j == 11){
//                        String typeIds = notice.getTypeIdArray();
//                        temp = "";
//                        if(StringUtils.isNotBlank(typeIds)){
//                            String arr[] = typeIds.split(",");
//                            for(int arrIndex=0;arrIndex<arr.length;arrIndex++){
//                                NewsType type = iNewsTypeService.getNewsTypeById(arr[arrIndex]);
//                                if(arrIndex == arr.length-1){
//                                    temp += type.getName();
//                                }else{
//                                    temp += type.getName()+";";
//                                }
//                            }
//                        }
//                    }else if(j == 12){
//                        temp = notice.getKeyword();
//                    }else if(j == 13){
//                        NewsFile newsFile = new NewsFile();
//                        newsFile.setRefId(notice.getId());
//                        PagerModel<NewsFile> pm = null;
//                        if (StringUtils.isNotBlank(newsFile.getRefId())) {
//                            pm = newsFileService.getPagerModelByQuery(newsFile, new Query());
//                        }
//                        List<NewsFile> files = pm.getRows();
//                        if(null!=files&&files.size()>0){
//                            temp = "有";
//                        }else{
//                            temp = "无";
//                        }
//
//                    }else {
//                        temp = "";
//                    }
//                    xrow3_cell1.setCellValue(temp);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            String title = "公文清单"+getTime("yyyyMMdd")+".xlsx";
//            String formatTitle = StringUtils.isNotBlank(title)?FilePattern.matcher(title).replaceAll("")
//                    :"标题为空";
//            // 输出Excel文件
//            OutputStream output = response.getOutputStream();
//            response.reset();
//            response.setHeader("Content-Disposition",
//                    "attachment;filename=" + new String(formatTitle.getBytes("utf-8"), "ISO8859-1"));
//            response.setContentType("application/msexcel");
//            workBook.write(output);
//            output.close();
//            output.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static String getHtmlFile() {
        return htmlFile;
    }

    public static void setHtmlFile(String htmlFile) {
        WordUtil.htmlFile = htmlFile;
    }

    public static String getBasePath() {
        return basePath;
    }

    public static void setBasePath(String basePath) {
        WordUtil.basePath = basePath;
    }

    public static String getWebPath() {
        return webPath;
    }

    public static void setWebPath(String webPath) {
        WordUtil.webPath = webPath;
    }
}
