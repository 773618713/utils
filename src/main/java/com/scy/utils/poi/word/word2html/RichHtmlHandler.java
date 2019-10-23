package com.scy.utils.poi.word.word2html;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * @Description:富文本Html处理器，主要处理图片及编码
 * @Author:
 * @Since:2018年10月30日
 * @Version:1.1.0
 * @Copyright:Copyright (c) 上海启疆信息科技有限公司 2017 ~ 2018 版权所有
 */
public class RichHtmlHandler {

    private Document doc = null;
    private String html;

    private String docSrcParent = "";
    private String docSrcLocationPrex = "";
    private String nextPartId;
    private String shapeidPrex;
    private String spidPrex;
    private String typeid;

    private String handledDocBodyBlock;
    private List<String> docBase64BlockResults = new ArrayList<String>();
    private List<String> xmlImgRefs = new ArrayList<String>();

    public String getDocSrcLocationPrex() {
        return docSrcLocationPrex;
    }

    public void setDocSrcLocationPrex(String docSrcLocationPrex) {
        this.docSrcLocationPrex = docSrcLocationPrex;
    }

    public String getNextPartId() {
        return nextPartId;
    }

    public void setNextPartId(String nextPartId) {
        this.nextPartId = nextPartId;
    }

    public String getHandledDocBodyBlock() {
        String raw=   WordHtmlGeneratorHelper.string2Ascii(doc.getElementsByTag("body").html());
        return raw.replace("=3D", "=").replace("=", "=3D");
    }

    public String getRawHandledDocBodyBlock() {
        String raw=  doc.getElementsByTag("body").html();
        return raw.replace("=3D", "=").replace("=", "=3D");
    }
    public List<String> getDocBase64BlockResults() {
        return docBase64BlockResults;
    }

    public List<String> getXmlImgRefs() {
        return xmlImgRefs;
    }

    public String getShapeidPrex() {
        return shapeidPrex;
    }

    public void setShapeidPrex(String shapeidPrex) {
        this.shapeidPrex = shapeidPrex;
    }

    public String getSpidPrex() {
        return spidPrex;
    }

    public void setSpidPrex(String spidPrex) {
        this.spidPrex = spidPrex;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getDocSrcParent() {
        return docSrcParent;
    }

    public void setDocSrcParent(String docSrcParent) {
        this.docSrcParent = docSrcParent;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public RichHtmlHandler(String html) {
        this.html = html;
        doc = Jsoup.parse(wrapHtml(this.html));
    }

    public void re_init(String html){
        doc=null;
        doc = Jsoup.parse(wrapHtml(html));
        docBase64BlockResults.clear();
        xmlImgRefs.clear();
    }

    /**
     * @Description: 获得已经处理过的HTML文件
     * @param @return
     * @return String
     * @throws IOException
     * @throws
     * @author:
     */
    public void handledHtml(boolean isWebApplication)
            throws IOException {
        Elements imags = doc.getElementsByTag("img");
        if (imags == null || imags.size() == 0) {
            // 返回编码后字符串
            return;
            //handledDocBodyBlock = WordHtmlGeneratorHelper.string2Ascii(html);
        }

        // 转换成word mht 能识别图片标签内容，去替换html中的图片标签

        for (Element item : imags) {
            // 把文件取出来
            String src = item.attr("src");
            if(src.contains("wordImport")) {
                src = "https://sc.sjedu.cn/" + src;
            }
            String srcRealPath = src;

//			String thepaths = RichHtmlHandler.class.getClassLoader().getResource("").toString();
//			System.out.println("src="+src+"     thepaths="+thepaths);
            if (isWebApplication) {
//				String contentPath=RequestResponseContext.getRequest().getContextPath();
//				if(!StringUtils.isEmpty(contentPath)){
//					if(src.startsWith(contentPath)){
//						src=src.substring(contentPath.length());
//					}
//				}
//
//				srcRealPath = RequestResponseContext.getRequest().getSession()
//						.getServletContext().getRealPath(src);

            }

            File imageFile = new File(srcRealPath);
            String imageFileShortName = imageFile.getName();
            String fileTypeName = WordImageConvertor.getFileSuffix(imageFileShortName);
            String docFileName = "image" + UUID.randomUUID().toString() + "."+ fileTypeName;
            String srcLocationShortName = docSrcParent + "/" + docFileName;

            String styleAttr = item.attr("style"); // 样式
            //高度
            String imagHeightStr=item.attr("height");;
            if(StringUtils.isEmpty(imagHeightStr)){
                imagHeightStr = getStyleAttrValue(styleAttr, "height");
            }
            //宽度
            String imagWidthStr=item.attr("width");;
            if(StringUtils.isEmpty(imagHeightStr)){
                imagHeightStr = getStyleAttrValue(styleAttr, "width");
            }

            imagHeightStr = imagHeightStr.replace("px", "");
            imagWidthStr = imagWidthStr.replace("px", "");
            if(StringUtils.isEmpty(imagHeightStr)){
                //去得到默认的文件高度
                imagHeightStr="0";
            }
            if(StringUtils.isEmpty(imagWidthStr)){
                imagWidthStr="0";
            }
            int imageHeight = Integer.parseInt(imagHeightStr);
            int imageWidth = Integer.parseInt(imagWidthStr);

            // 得到文件的word mht的body块
            String handledDocBodyBlock = WordImageConvertor.toDocBodyBlock(srcRealPath,
                    imageFileShortName, imageHeight, imageWidth,styleAttr,
                    srcLocationShortName, shapeidPrex, spidPrex, typeid);

            //这里的顺序有点问题：应该是替换item，而不是整个后面追加
            //doc.rreplaceAll(item.toString(), handledDocBodyBlock);
            item.after(handledDocBodyBlock);
//			item.parent().append(handledDocBodyBlock);
            item.remove();
            // 去替换原生的html中的image

            String base64Content = WordImageConvertor.imageToBase64(srcRealPath);
            String contextLoacation = docSrcLocationPrex + "/" + docSrcParent + "/" + docFileName;

            String docBase64BlockResult = WordImageConvertor.generateImageBase64Block(nextPartId, contextLoacation,
                    fileTypeName, base64Content);
            docBase64BlockResults.add(docBase64BlockResult);

            String imagXMLHref = "<o:File HRef=3D\"" + docFileName + "\"/>";
            xmlImgRefs.add(imagXMLHref);

        }

    }

    private String getStyleAttrValue(String style, String attributeKey) {
        if (StringUtils.isEmpty(style)) {
            return "";
        }

        // 以";"分割
        String[] styleAttrValues = style.split(";");
        for (String item : styleAttrValues) {
            // 在以 ":"分割
            String[] keyValuePairs = item.split(":");
            if (attributeKey.equals(keyValuePairs[0])) {
                return keyValuePairs[1];
            }
        }

        return "";
    }

    public static String wrapHtml(String html) {
        if(!StringUtils.isEmpty(html)) {
            while(html.startsWith("<p") || html.startsWith("</p")) {
                html = html.substring(html.indexOf(">")+1);
                wrapHtml(html);
            }
//			System.out.println(html.indexOf(">"));
            while(html.endsWith("</p>")) {
                html = html.substring(0, html.lastIndexOf("</p>")).trim();
                wrapHtml(html);
            }

            html = html.replaceAll("<p>", "<br>");
            html = html.replaceAll("</p>", "");

            while(html.endsWith("&nbsp;")) {
                html = html.substring(0, html.lastIndexOf("&nbsp;"));
                html = html.trim();
            }
//			System.out.println("html=====:"+html);
        }

        // 因为传递过来都是不完整的doc
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<body>");
        sb.append(html);

        sb.append("</body>");
        sb.append("</html>");

        String[] filterCsses = {"font-size","line-height","font-family"};//去掉标签的指定样式

        return formatHtml(sb.toString(), filterCsses);
    }

    /**
     * @Description: 替换指定空格
     * @params  String  html字符串
     * @return String
     * @author:
     */
    public String blankReplace(String html) {
        return html.trim();
    }

    /**
     * @Description: 去掉标签的指定样式
     * @params  String  html字符串
     * @params  String[]  filterCsses样式数组
     * @return String
     * @author:
     */
    public static String formatHtml(String html, String[] filterCsses) {
        if (StringUtils.isBlank(html)) {
            return null;
        }

        Document doc = Jsoup.parse(html);
        Elements esd = doc.select("[style]");
        Iterator<Element> iterator = esd.iterator();
        while (iterator.hasNext()) {
            Element eTemp = iterator.next();
            String styleStr = eTemp.attr("style");
            eTemp.removeAttr("style");

            String newStyle = cssFilter(styleStr, filterCsses);
            if(StringUtils.isNotBlank(newStyle)) {
                eTemp.attr("style", newStyle);
            }

//	        if (eTemp.attr("style").contains("text-decoration")) {
//	        	eTemp.removeAttr("style");
//	        	eTemp.addClass("quizPutTag");
//			}
        }
//	    Elements els = doc.select("span.quizPutTag");
//		iterator = els.iterator();
//		while (iterator.hasNext())
//		{
//			Element eTemp = iterator.next();
//			System.out.println("eTemp.text()==="+eTemp.text());
//			eTemp.text("");
//			System.out.println("eTemp.text()==="+eTemp.text());
//		}

        return doc.body().html();
    }

    private static String cssFilter(String str, String[] filterCsses) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        String[] types = str.split(";");
        List<String> newTypes = new ArrayList<>();
        for (String type : types) {
            boolean find = false;
            for (String filterCss : filterCsses) {
                if(type.contains(filterCss)){
                    find = true;
                    break;
                }
            }

            if(!find) {
                newTypes.add(type);
            }

        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < newTypes.size(); i++) {
            sb.append(newTypes.get(i)).append(";");
        }

        return sb.toString().substring(0, sb.toString().length());
    }

    public static String wrapQuestionHtml(String html){
        // 因为传递过来都是不完整的doc
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<body>");
        sb.append(html);

        sb.append("</body>");
        sb.append("</html>");

        String[] filterCsses = {"font-size","line-height","font-family","text-indent"};//去掉标签的指定样式

        return formatHtml(sb.toString(), filterCsses);
    }

}
