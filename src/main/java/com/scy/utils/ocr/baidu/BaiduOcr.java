package com.scy.utils.ocr.baidu;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scy.utils.Base64Util;
import com.scy.utils.SslUtils;
import com.scy.utils.http.HttpRespons;
import com.scy.utils.http.HttpUtils;
import com.sun.corba.se.spi.orbutil.fsm.Input;

/**
 * 百度 ocr 识别接口
 * 
 * @author sun
 *
 */
public class BaiduOcr {
	final Base64.Decoder decoder = Base64.getDecoder();
	final Base64.Encoder encoder = Base64.getEncoder();
	final static String otherHost = "https://aip.baidubce.com/rest/2.0/ocr/v1/handwriting";
	
	/**
	 * 百度 手写文字识别
	 * 
	 * @param bi
	 * @return
	 * @throws Exception
	 */
	public String getPicWords(BufferedImage bi, int left, int top, int width, int height) throws Exception {
		String words = "";
		bi = bi.getSubimage(left, top, width, height);
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
		ImageIO.write(bi, "jpg", imOut);
		byte[] imgByte = bs.toByteArray();
		String imgBase64 = encoder.encodeToString(imgByte);
		
		//线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
		String accessToken = getAuth("GVBmkEi37vtZQGY2Amt0jfhS", "EyzdeyxwIc4PgYeKMmLnW5T6wjSKjRdf");

		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		//图片数据
		params.put(URLEncoder.encode("image", "UTF-8"), URLEncoder.encode(imgBase64, "UTF-8"));

		HttpUtils httpUtils = new HttpUtils();
		HttpURLConnection conn = (HttpURLConnection) httpUtils.sendPostRequest(otherHost, params, null);
		InputStream in = conn.getInputStream();
		String result = HttpUtils.read2String(in);
	
		
		System.out.println(result);
		com.alibaba.fastjson.JSONObject jo = com.alibaba.fastjson.JSONObject.parseObject(result);
		JSONArray words_result = jo.getJSONArray("words_result");

		if (words_result.size() > 0) {
			words = words_result.getJSONObject(0).getString("words");
			System.out.println("words=" + words);
		}
		return words;
	}
	
	/**
	 * 获取 access_token
	 * 
	 * @param ak
	 * @param sk
	 * @return
	 */
	public static String getAuth(String ak, String sk) {
		// 获取token地址
		String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
		try {
			Map<String, String> params = new HashMap<String, String>();
			// 1. grant_type为固定参数
			params.put("grant_type", "client_credentials");
			//2. 官网获取的 API Key
			params.put("client_id", ak);
			// 3. 官网获取的 Secret Key
			params.put("client_secret", sk);
			
			HttpURLConnection conn = (HttpURLConnection) HttpUtils.sendPostRequest(authHost, params, null);
			InputStream in = conn.getInputStream();
			String result = HttpUtils.read2String(in);
			
			/**
			 * 返回结果示例
			 */
			System.err.println("result:" + result);
			JSONObject jsonObject = JSONObject.parseObject(result);
			String access_token = jsonObject.getString("access_token");
			return access_token;
		} catch (Exception e) {
			System.err.printf("获取token失败！");
			e.printStackTrace(System.err);
		}
		return null;
	}

	public static void main(String[] args) {
		try {
			BufferedImage bi = ImageIO.read(new File("C:\\Users\\sun\\Desktop\\丹阳考试时间.png"));
			//new BaiduOcr().getPicWords(bi, 245, 89, 917 - 245, 113 - 89);
			new BaiduOcr().getPicWords(bi, 0, 0, bi.getWidth(), bi.getHeight());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
