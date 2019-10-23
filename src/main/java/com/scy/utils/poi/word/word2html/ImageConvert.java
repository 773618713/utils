package com.scy.utils.poi.word.word2html;

import net.arnx.wmf2svg.gdi.svg.SvgGdi;
import net.arnx.wmf2svg.gdi.wmf.WmfParser;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFRenderer;
import org.w3c.dom.Document;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

/**
 * 图片装换 <br>
 * 功能：
 * @author Administrator
 */
public class ImageConvert {

    /**
     * 几种常见的图片格式
     */
    public static String IMAGE_TYPE_GIF = "gif";// 图形交换格式
    public static String IMAGE_TYPE_JPG = "jpg";// 联合照片专家组
    public static String IMAGE_TYPE_JPEG = "jpeg";// 联合照片专家组
    public static String IMAGE_TYPE_BMP = "bmp";// 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
    public static String IMAGE_TYPE_PNG = "png";// 可移植网络图形
    public static String IMAGE_TYPE_PSD = "psd";// Photoshop的专用格式Photoshop



    public static void main(String[] args) throws Exception {

        List<String> wmfList = new ArrayList<>();
        String path = "/SCApp/Data/wordImport/20190926/SJAAAAX65898/picture/0.wmf";
        String pngPath = ImageConvert.emfConversionPng(path);
        System.out.println(pngPath);

    }

    /**
     * 图片转base64
     *
     * @param imgFilePath
     * @return
     */
    public static String GetImageStr(String imgFilePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;

        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgFilePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        String s = "data:image/png;base64," + encoder.encode(data);
        return s;// 返回Base64编码过的字节数组字符串
    }


    /**
     * emf或者wmf转换为png图片格式
     *
     * @param
     * @return
     * @throws IOException
     */
    public static String emfConversionPng(String path) throws IOException {
        // 对文件的命名进行重新修改
        String saveUrl = path;
        // 从doc文档解析的图片很有可能已经是png了，所以此处需要判断
        if (saveUrl.contains("emf") || saveUrl.contains("EMF")) {
            InputStream is = new FileInputStream(saveUrl);

            EMFInputStream eis = new EMFInputStream(is,
                    EMFInputStream.DEFAULT_VERSION);

            EMFRenderer emfRenderer = new EMFRenderer(eis);
            final int width = (int) eis.readHeader().getBounds()
                    .getWidth();
            final int height = (int) eis.readHeader().getBounds()
                    .getHeight();
            // 设置图片的大小和样式
            final BufferedImage result = new BufferedImage(width + 60,
                    height + 40, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2 = result.createGraphics();

            emfRenderer.paint(g2);
            String url = saveUrl.replace(
                    saveUrl.substring(saveUrl.length() - 3), "png");
            File outputfile = new File(url);
            // 写入到磁盘中(格式设置为png背景不会变为橙色)
            ImageIO.write(result, "png", outputfile);
            // 当前的图片写入到磁盘中后，将流关闭
            if (eis != null) {
                eis.close();
            }
            if (is != null) {
                is.close();
            }
        } else if (saveUrl.contains("wmf") || saveUrl.contains("WMF")) {
            // 将wmf转svg
            String svgFile = saveUrl.substring(0,
                    saveUrl.lastIndexOf(".wmf"))
                    + ".svg";
            wmfToSvg(saveUrl, svgFile);
            // 将svg转png
            String jpgFile = saveUrl.substring(0,
                    saveUrl.lastIndexOf(".wmf"))
                    + ".png";
            svgToJpg(svgFile, jpgFile);
            return jpgFile;
        }
        return null;
    }


    /**
     * 将wmf转换为svg
     *
     * @param src
     * @param dest
     */
    public static void wmfToSvg(String src, String dest) {
        File file = new File(src);
        boolean compatible = false;
        try {
            InputStream in = new FileInputStream(file);
            WmfParser parser = new WmfParser();

            final SvgGdi gdi = new SvgGdi(compatible);
            parser.parse(in, gdi);

            Document doc = gdi.getDocument();
            OutputStream out = new FileOutputStream(dest);
            if (dest.endsWith(".svgz")) {
                out = new GZIPOutputStream(out);
            }
            output(doc, out);
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 输出信息
     *
     * @param doc
     * @param out
     * @throws Exception
     */
    private static void output(Document doc, OutputStream out) throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
                "-//W3C//DTD SVG 1.0//EN");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
                "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd");
        transformer.transform(new DOMSource(doc), new StreamResult(out));
        if (out != null) {
            out.flush();
            out.close();
        }
    }

    /**
     * 将svg转化为JPG
     *
     * @param src
     * @param dest
     */
    public static void svgToJpg(String src, String dest) {
        FileOutputStream jpgOut = null;
        FileInputStream svgStream = null;
        ByteArrayOutputStream svgOut = null;
        ByteArrayInputStream svgInputStream = null;
        ByteArrayOutputStream jpg = null;
        File svg = null;
        try {
            // 获取到svg文件
            svg = new File(src);
            svgStream = new FileInputStream(svg);
            svgOut = new ByteArrayOutputStream();
            // 获取到svg的stream
            int noOfByteRead = 0;
            while ((noOfByteRead = svgStream.read()) != -1) {
                svgOut.write(noOfByteRead);
            }
            ImageTranscoder it = new PNGTranscoder();
//            it.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(1f));
            it.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, new Float(48));
            jpg = new ByteArrayOutputStream();
            svgInputStream = new ByteArrayInputStream(svgOut.toByteArray());
            it.transcode(new TranscoderInput(svgInputStream),
                    new TranscoderOutput(jpg));

            jpgOut = new FileOutputStream(dest);
            jpgOut.write(jpg.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (svgInputStream != null) {
                    svgInputStream.close();
                }
                if (jpg != null) {
                    jpg.close();
                }
                if (svgStream != null) {
                    svgStream.close();

                }
                if (svgOut != null) {
                    svgOut.close();
                }
                if (jpgOut != null) {
                    jpgOut.flush();
                    jpgOut.close();
                }
                if (svg != null) {
                    svg.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
