package com.scy.utils.poi.wmf2img;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import net.arnx.wmf2svg.gdi.svg.SvgGdi;
import net.arnx.wmf2svg.gdi.wmf.WmfParser;

/**
 * Wmf文件转换为Svg文件
 *
 */
public class WmfToSvg {

	/**
	 *
	 * @param wmfBytes		字节数组
	 * @param dest			保存路径
	 * @throws Exception
	 */
	public void converter(byte[] wmfBytes, String dest) throws Exception {
		InputStream in = new ByteArrayInputStream(wmfBytes);
		WmfParser parser = new WmfParser();
		final SvgGdi gdi = new SvgGdi(false);
		parser.parse(in, gdi);
		Document doc = gdi.getDocument();
		OutputStream out = new FileOutputStream(dest);
		if (dest.endsWith(".svgz")) {
			out = new GZIPOutputStream(out);
		}
		output(doc, out);
	}

	/**
	 *
	 * @param file		wmf路径
	 * @param dest		svg保存路径
	 * @throws Exception
	 */
	public void convert(String file,String dest) throws Exception{
		InputStream in = new FileInputStream(file);
		WmfParser parser = new WmfParser();
		final SvgGdi gdi = new SvgGdi(false);
		parser.parse(in, gdi);
		Document doc = gdi.getDocument();
		OutputStream out = new FileOutputStream(dest);
		if (dest.endsWith(".svgz")) {
			out = new GZIPOutputStream(out);
		}
		output(doc, out);
	}

	public byte[] encodeConvert() {
		return null;
	}

	/**
	 * 保存文件
	 * @param doc
	 * @param out
	 * @throws Exception
	 */
	private void output(Document doc, OutputStream out) throws Exception {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,"-//W3C//DTD SVG 1.0//EN");
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd");
		transformer.transform(new DOMSource(doc), new StreamResult(out));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		transformer.transform(new DOMSource(doc), new StreamResult(bos));
		out.flush();
		out.close();
	}
}