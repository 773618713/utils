package com.scy.utils.socket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocketUtil {
	
	public static void main(String[] args) {
		try {
			new ClientSocketUtil().socketUse2();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void socketUse() throws Exception {
		//客户端
		//1、创建客户端Socket，指定服务器地址和端口
		Socket socket =new Socket("localhost",10086);
		//2、获取输出流，向服务器端发送信息
		OutputStream os = socket.getOutputStream();//字节输出流
		PrintWriter pw =new PrintWriter(os);//将输出流包装成打印流
		pw.write("用户名：admin;密码：123");
		pw.flush();
		socket.shutdownOutput();
		//3、获取输入流，并读取服务器端的响应信息
		InputStream is = socket.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String info = null;
		while((info=br.readLine()) != null){
		 System.out.println("我是客户端，服务器说："+info);
		}
	
		//4、关闭资源
		br.close();
		is.close();
		pw.close();
		os.close();
		socket.close();
	}
	
	
	public void socketUse2() throws Exception {
		//客户端
		//1、创建客户端Socket，指定服务器地址和端口
		Socket socket =new Socket("localhost", 80);
		//2、获取输出流，向服务器端发送信息
		OutputStream os = socket.getOutputStream();//字节输出流
		PrintWriter pw =new PrintWriter(os);//将输出流包装成打印流
		//pw.write("用户名：admin;密码：123");
		
		String context = 
		"POST /utils/json/fun HTTP/1.1\r\n"+
		"Host: localhost\r\n"+
		"Connection: keep-alive\r\n"+
		"Content-Length: 725\r\n"+
		"Cache-Control: max-age=0\r\n"+
		"Upgrade-Insecure-Requests: 1\r\n"+
		"Origin: null\r\n"+
		"Content-Type: multipart/form-data; boundary=----WebKitFormBoundaryeJv1sYaefdBG32FU\r\n"+
		"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36\r\n"+
		"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r\n"+
		"Accept-Encoding: gzip, deflate, br\r\n"+
		"Accept-Language: zh-CN,zh;q=0.9,en;q=0.8\r\n"+
		"Cookie: _ctid=cea11bda-5ebb-4672-b897-1d5b3d1812b2; JSESSIONID=5AACD3B94C53BDAF14E46B243E487084; _platinfo={\"screen\":\"1536 x 864\",\"browser\":\"Chrome\",\"browserVersion\":\"73.0.3683.103\",\"mobile\":false,\"os\":\"Windows\",\"osVersion\":\"NT 4.0\",\"cookies\":true}\r\n"+
		"\r\n"+
		"------WebKitFormBoundaryeJv1sYaefdBG32FU\r\n"+
		"Content-Disposition: form-data; name=\"file\"; filename=\"QQ截图20190614142524.jpg\"\r\n"+
		"Content-Type: image/jpeg\r\n"+
		"\r\n"+
		"����\r\n"+
		"------WebKitFormBoundaryeJv1sYaefdBG32FU--";
		pw.write(context);
		
		pw.flush();
		socket.shutdownOutput();
		//3、获取输入流，并读取服务器端的响应信息
		InputStream is = socket.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String info = null;
		while((info=br.readLine()) != null){
		 System.out.println("我是客户端，服务器说："+info);
		}
	
		//4、关闭资源
		br.close();
		is.close();
		pw.close();
		os.close();
		socket.close();
	}
	
}
