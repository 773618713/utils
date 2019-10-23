package com.scy.utils.poi.wmf2img;

import java.io.*;

import com.scy.utils.FileUtil;
import com.scy.utils.poi.svg.ReplaceSVG;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;

/**
 * svg转换为图片
 */
public class SvgToImg {

	/**
	 * 将 svg转换为 jpg图片
	 * @param filePath
	 * @throws Exception
	 */
	public void SaveAsJPEG(String filePath, String toPath) throws Exception {
		// Create a JPEG transcoder
		JPEGTranscoder t = new JPEGTranscoder();
		// Set the transcoding hints.
		t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
				new Float(.8));
		// Create the transcoder input.
		String svgURI = new File(filePath).toURL().toString();
		TranscoderInput input = new TranscoderInput(svgURI);
		// Create the transcoder output.
		OutputStream ostream = new FileOutputStream(toPath);
		TranscoderOutput output = new TranscoderOutput(ostream);
		// Save the image.
		t.transcode(input, output);
		// Flush and close the stream.
		ostream.flush();
		ostream.close();
		System.exit(0);
	}

	/**
	 * 将svg保存为png图片
	 * @param filePath
	 * @throws Exception
	 */
	public void SaveAsPNG(String filePath, String toPath) throws Exception {
		PNGTranscoder t = new PNGTranscoder();
		t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
				new Float(.8));
		String svgURI = new File(filePath).toURL().toString();
		TranscoderInput input = new TranscoderInput(svgURI);
		OutputStream ostream = new FileOutputStream(toPath);
		TranscoderOutput output = new TranscoderOutput(ostream);
		t.transcode(input, output);
		ostream.flush();
		ostream.close();
		System.exit(0);
	}



	public static void main(String[] args) throws Exception{

		SvgToImg svg2img = new SvgToImg();
		String filePath = "D:\\xymh\\云教案模块测试\\image\\921.svg";

		//读取文件的字符串
		String str = FileUtil.readFileByLines(filePath);
		//替换 特殊 字符
		str = ReplaceSVG.reSpecialChar(str);
		//将文本写出
		FileUtil.writeFile("D:\\xymh\\云教案模块测试\\image","921.svg",str);

		//将svg 转换 jpg
		svg2img.SaveAsJPEG(filePath,"out.jpg");
	}
}