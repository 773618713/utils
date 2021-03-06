package com.scy.utils.http;

import java.util.Vector;

/**
 * Http的返回值
 * @author sun
 *
 */
public class HttpRespons {
	/**
	 * 请求返回的内容，每个元素为一行数据
	 */
	public Vector<String> contentCollection;
	public String urlString;
	public int defaultPort;
	public String file;
	public String host;
	public String path;
	public int port;
	public String protocol;
	public String query;
	public String ref;
	public String userInfo;
	public String content;
	public String contentEncoding;
	public int code;
	public String message;
	public String contentType;
	public String method;
	public int connectTimeout;
	public int readTimeout;
}
