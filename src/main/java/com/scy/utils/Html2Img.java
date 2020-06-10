package com.scy.utils;

import gui.ava.html.image.generator.HtmlImageGenerator;
import gui.ava.html.parser.HtmlParser;
import gui.ava.html.parser.HtmlParserImpl;
import gui.ava.html.renderer.ImageRenderer;
import gui.ava.html.renderer.ImageRendererImpl;

public class Html2Img {
    public static void main(String[] args) {
        HtmlParser htmlParser = new HtmlParserImpl();
        htmlParser.loadHtml(HtmlTemplateStr);

        // html 是我的html代码
        ImageRenderer imageRenderer = new ImageRendererImpl(htmlParser);
        imageRenderer.saveImage("E:\\lcxq1.png");

        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
        //加载html模版
        imageGenerator.loadHtml(HtmlTemplateStr);
        //把html写入到图片
        imageGenerator.saveAsImage("E:\\lcxq2.png");

    }

    static String HtmlTemplateStr = "\t<div style=\"height: 500px;width: 500px;background: #aee0ff;\">\n" +
            "\t\t这个是一个div\n" +
            "\t\t<h1>标题</h1>\n" +
            "\t\t<ol>\n" +
            "\t\t\t<li>a</li>\n" +
            "\t\t</ol>\n" +
            "\t\t<img style=\"margin-left: 1500px;\" width=\"300px\" height=\"200px\" src=\"https://inews.gtimg.com/newsapp_bt/0/11911825373/1000\">\n" +
            "\t</div>";


}
