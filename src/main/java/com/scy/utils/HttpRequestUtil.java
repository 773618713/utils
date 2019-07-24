package com.scy.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;



/**
 * HTTP请求工具类
 * 
 * @author sun
 *
 */
public class HttpRequestUtil {
	
	/*final static String requestMethod_GET = "GET";
	final static String requestMethod_POST = "POST";
	
	
	public void request(String httpsUrl, String xmlStr) {
		
		HttpsURLConnection urlCon = null;
		try {
			urlCon = (HttpsURLConnection) (new URL(httpsUrl)).openConnection();
			//是否写入
			urlCon.setDoInput(true);
			//是否写出
			urlCon.setDoOutput(true);
			//请求类型
			urlCon.setRequestMethod("GET");
			//
			urlCon.setConnectTimeout(5000);
			//
			urlCon.setReadTimeout(30000);
			
			
			urlCon.setRequestProperty("Content-Length", String.valueOf(xmlStr.getBytes().length));
			urlCon.setUseCaches(false);
        
			urlCon.setUseCaches(false);
			urlCon.setRequestProperty("Connection", "Keep-Alive");
			urlCon.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			urlCon.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
			
			
			
			urlCon.getOutputStream().write(xmlStr.getBytes("UTF-8"));
			urlCon.getOutputStream().flush();
			urlCon.getOutputStream().close();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
			String line;
			StringBuffer resultStr = new StringBuffer();
			while ((line = in.readLine()) != null) {
				resultStr.append(line);
			}
			//ResultVo resultVo = JSON.parseObject(resultStr.toString(), ResultVo.class);
			//return resultVo;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	*//**
	 * 获得KeyStore.
	 * 
	 * @param keyStorePath
	 *            密钥库路径
	 * @param password
	 *            密码
	 * @return 密钥库
	 * @throws Exception
	 *//*
	public static KeyStore getKeyStore(String password, String keyStorePath) throws Exception {
		// 实例化密钥库
		KeyStore ks = KeyStore.getInstance("JKS");
		// 获得密钥库文件流
		FileInputStream is = new FileInputStream(keyStorePath);
		// 加载密钥库
		ks.load(is, password.toCharArray());
		// 关闭密钥库文件流
		is.close();
		return ks;
	}

	*//**
	 * 获得SSLSocketFactory.
	 * 
	 * @param password
	 *            密码
	 * @param keyStorePath
	 *            密钥库路径
	 * @param trustStorePath
	 *            信任库路径
	 * @return SSLSocketFactory
	 * @throws Exception
	 *//*
	public static SSLContext getSSLContext(String password, String keyStorePath, String trustStorePath)
			throws Exception {
		// 实例化密钥库
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		// 获得密钥库
		KeyStore keyStore = getKeyStore(password, keyStorePath);
		// 初始化密钥工厂
		keyManagerFactory.init(keyStore, password.toCharArray());

		// 实例化信任库
		TrustManagerFactory trustManagerFactory = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		// 获得信任库
		KeyStore trustStore = getKeyStore(password, trustStorePath);
		// 初始化信任库
		trustManagerFactory.init(trustStore);
		// 实例化SSL上下文
		SSLContext ctx = SSLContext.getInstance("TLS");
		// 初始化SSL上下文
		ctx.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
		// 获得SSLSocketFactory
		return ctx;
	}

	*//**
	 * 初始化HttpsURLConnection.
	 * 
	 * @param password
	 *            密码
	 * @param keyStorePath
	 *            密钥库路径
	 * @param trustStorePath
	 *            信任库路径
	 * @throws Exception
	 *//*
	public static void initHttpsURLConnection(String password, String keyStorePath, String trustStorePath)
			throws Exception {
		// 声明SSL上下文
		SSLContext sslContext = null;
		
		try {
			sslContext = getSSLContext(password, keyStorePath, trustStorePath);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		if (sslContext != null) {
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		}
		//HttpsURLConnection.setDefaultHostnameVerifier(hnv);
	}*/
	
}
