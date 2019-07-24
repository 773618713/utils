package com.scy.utils.http;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class HttpTest {
	public static void main(String[] args) {
		try {
			fileUploadTest();
			//requestTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	private static void fileUploadTest() throws Exception {
		HttpUtils httpUtils = new HttpUtils();
		Map<String, String> params = new HashMap<String, String>();
		params.put("EXAMPAPERNAME", "2019年4月七年级数学（测试）");
		params.put("EXAMPAPERID", "7b160016b771407fa82bcd74203bea04");
		params.put("PAGENO", "2");
		String fileName = "C:\\Users\\sun\\Desktop\\九年级测试答题卡\\9c7e56424baa4ad6a6cb4000efb29c8a_2.jpeg";
		FormFile fromFile = new FormFile("9c7e56424baa4ad6a6cb4000efb29c8a_2.jpeg", new File(fileName), "upfile", null);
		String url = "http://localhost/utils/json/FileUpload";
		//String url = "http://localhost/utils/json/fun";
		//String url = "http://localhost/utils/UploadServlet";
		//String url = "http://dev.score1.sjedu.cn/score/json/FileUpload_insert_addExamPaperPic.json";
		httpUtils.uploadFile(url, params, fromFile);
	}
	
	// 测试函数
	private static void requestTest() throws Exception {
		HttpUtils httpUtils = new HttpUtils();
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", "张三");
		String url = "http://localhost/utils/json/fun";
		HttpURLConnection conn = (HttpURLConnection) httpUtils.sendPostRequest(url, params, null);
		//int code = conn.getResponseCode();
		InputStream in = conn.getInputStream();
		System.out.print(httpUtils.read2String(in));
	}
	
	
}
