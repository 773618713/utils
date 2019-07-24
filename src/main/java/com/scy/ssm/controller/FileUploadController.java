package com.scy.ssm.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class FileUploadController{  
	private static Logger logger = Logger.getLogger(FileUploadController.class);
	
	/**
	 * 单文件上传接收方法
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/FileUpload")
	public String ExamExportDataUpload(HttpServletRequest request,HttpServletResponse response,HttpSession session){
    	try {
    		System.out.println("开始上传");
    		String PAGENO = request.getParameter("PAGENO");
    		String EXAMPAPERID = request.getParameter("EXAMPAPERID");
    		
    		String pathString =this.getClass().getClassLoader().getResource("").getPath().replace("/WEB-INF/classes/", "/assets/pic/");
			File file2= new File(pathString);
			if (!file2.exists())      
			{       
				file2.mkdir();
			}
			
    		
    		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; 
		  	InputStream in =null;  
		  	FileOutputStream fos =null;
	        List<List<Object>> listob = null;  
	        MultipartFile file = multipartRequest.getFile("upfile");  
	        if(file.isEmpty()){  
	            throw new Exception("文件不存在！");  
	        }  
	        in = file.getInputStream();
	        String fileName = file.getOriginalFilename();
	        int index = fileName.lastIndexOf('.');
	        String fileType = fileName.substring(index+1,fileName.length());
	        fileName = EXAMPAPERID+"_"+PAGENO+"."+fileType;
	        file.transferTo(new File(pathString+fileName));
	        
	        System.out.println(pathString);
			logger.info(pathString);
	        
    	} catch (Exception e) {
		}
    	
    	/*PrintWriter writer;
		try {
			writer = response.getWriter();
			 writer.println("文件上传完毕");
		        writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
       
    	
    	return "上传完毕";
	}
	
	/**
	 * 多文件上传接收方法
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/FilesUpload")
	public String FilesUpload(HttpServletRequest request,HttpServletResponse response,HttpSession session){
    	try {
    		System.out.println("开始上传");
    		String pathString =this.getClass().getClassLoader().getResource("").getPath().replace("/WEB-INF/classes/", "/assets/pic/");
			File file2= new File(pathString);
			if (!file2.exists())      
			{       
				file2.mkdir();
			}
    		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; 
		  	InputStream in =null;  
		  	FileOutputStream fos =null;
	        List<List<Object>> listob = null;  
	        List<MultipartFile> files = multipartRequest.getFiles("file");  
	        if(files.isEmpty()){  
	            throw new Exception("文件不存在！");  
	        }
	        for (int i = 0; i < files.size(); i++) {
	        	MultipartFile file = files.get(i);
	        	in = file.getInputStream();
	 	        String fileName = file.getOriginalFilename();
	 	        int index = fileName.lastIndexOf('.');
	 	        String fileType = fileName.substring(index+1,fileName.length());
	 	        //fileName = EXAMPAPERID+"_"+PAGENO+"."+fileType;
	 	        file.transferTo(new File(pathString+fileName));
	 	        
	 	        System.out.println(pathString);
	 			logger.info(pathString);
			}
    	} catch (Exception e) {
		}
    	return "上传完毕";
	}
  
}
	