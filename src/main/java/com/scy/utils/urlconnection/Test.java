package com.scy.utils.urlconnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class Test {
	public static void main(String[] args) {
		System.out.println(sendPostWb("http://699pic.com/photo-0-0-1.html", ""));
	}

	/**
	 * 发送post请求
	 */
	public static String sendPostWb(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		System.out.println("请求参数:" + param);
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			// 设置接受的文件类型，*表示一切可以接受的

			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			// TODO 新添加的请求头编码
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			/*
			 * in = new BufferedReader( new InputStreamReader(conn.getInputStream()));
			 */
			// TODO 新添加设置，返回编码
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e.toString());
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				System.out.println("发送 POST 请求出现异常！" + ex.toString());
				ex.printStackTrace();
			}
		}
		return result;
	}
}
