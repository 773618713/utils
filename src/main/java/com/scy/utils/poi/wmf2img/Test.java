package com.scy.utils.poi.wmf2img;

public class Test {
	
	public static void main(String[] args) throws Exception{

		WmfToSvg wmf2Svg = new WmfToSvg();
		String filePath = "D:\\xymh\\云教案模块测试\\image\\921.wmf";
		String dest = "D:\\xymh\\云教案模块测试\\image\\921.svg";
		wmf2Svg.convert(filePath, dest);
		
		SvgToImg saveJ = new SvgToImg();
//		saveJ.SaveAsJPEG(dest);
		
		saveJ.SaveAsPNG(dest,"out.png");
//		SvgToImg svg2Img = new SvgToImg();
//		svg2Img.convert2JPEG(dest);
	}
}