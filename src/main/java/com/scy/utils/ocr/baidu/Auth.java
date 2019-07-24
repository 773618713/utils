package com.scy.utils.ocr.baidu;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.scy.utils.http.HttpRespons;
import com.scy.utils.http.HttpUtils;

public class Auth {
	
	public static void main(String[] args) {
		String accessToken = getAuth("GVBmkEi37vtZQGY2Amt0jfhS", "EyzdeyxwIc4PgYeKMmLnW5T6wjSKjRdf");
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
			System.out.println("result:" + result);
			JSONObject jsonObject = JSONObject.parseObject(result);
			String access_token = jsonObject.getString("access_token");
			return access_token;
		} catch (Exception e) {
			System.err.printf("获取token失败！");
			e.printStackTrace(System.err);
		}
		return null;
	}
}
