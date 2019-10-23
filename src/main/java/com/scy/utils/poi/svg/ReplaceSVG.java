package com.scy.utils.poi.svg;

import com.scy.utils.FileUtil;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;

public class ReplaceSVG {
    /**
     * 替换svg中的特殊字符
     * svg中的特殊字符转换图片时会乱码
     * @param svgStr
     * @return
     */
    public static String reSpecialChar(String svgStr){
        char[] chArr = svgStr.toCharArray();
        for (int i = 0; i < chArr.length; i++) {
            if (chArr[i] == 184){
                chArr[i] = 247;
                //System.out.println("找到减号");
                continue;
            }
            if (chArr[i] == 180){
                chArr[i] = 215;
                //System.out.println("找到加号");
                continue;
            }
            if (chArr[i] == 230 || chArr[i] ==246 || chArr[i] ==232 || chArr[i] ==248){
                chArr[i] = ' ';
                continue;
            }
            if (chArr[i] == 231){
                chArr[i] = '(';
                continue;
            }
            if (chArr[i] == 247){
                chArr[i] = ')';
                continue;
            }
            if (chArr[i] == 65292){
                chArr[i] = ',';
                continue;
            }
        }
        return new String(chArr);
    }

    public static void main(String[] args) throws Exception {
        String filePath = "D:/doc2htmltest/2019/10/11/1570768917895/image/svg2.svg";
        //String str = FileUtil.readFileByLines(filePath);
        //System.out.println(str);

        char ch = '，';
        System.out.println((int)ch);

        System.out.println("");

        //parseSVG(filePath);
    }


    /**
     * 解析svg
     * @param svgURI    svg路径
     * @return
     * @throws Exception
     */
    public static String parseSVG(String svgURI) throws Exception {
        File file = new File(svgURI);
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        Document doc = f.createDocument(file.toURI().toString());

        //获取svg元素
        NodeList nodeList = doc.getElementsByTagName("svg");
        Element element = (Element) nodeList.item(0);

        //获取svg的宽高
        Integer width = Integer.parseInt(element.getAttribute("width"));
        Integer height = Integer.parseInt(element.getAttribute("height"));
        System.out.println(width+","+height);

        //将高度固定为50，宽度等比缩放
        double rate = height / 50.0;
        width = (int)Math.round(width / rate);
        element.setAttribute("height","50");
        element.setAttribute("width",width.toString());

        //将svg转换String
        String elementStr = convertElemToSVG(element);
        System.out.println(elementStr);
        return elementStr;
    }

    /**
     * 将element转换成字符串
     * @param element svg的元素
     * @return
     */
    public static String convertElemToSVG(Element element) {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        StringWriter buffer = new StringWriter();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        try {
            transformer.transform(new DOMSource(element), new StreamResult(buffer));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        String elementStr = buffer.toString();
        return elementStr;
    }




}
