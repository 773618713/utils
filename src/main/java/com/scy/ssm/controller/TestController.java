package com.scy.ssm.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.scy.ssm.dao.OpenDao;
import com.scy.ssm.service.OpenService;
import com.scy.utils.opencv.Test;

import redis.clients.jedis.Jedis;

@Controller
public class TestController {
	Logger logger = LogManager.getLogger(TestController.class);
	
	@Resource
	OpenService openService;
	
	/**
	 * 请求测试
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/funRequest")
	@ResponseBody
	public String funRequest(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		System.out.println("funRequest" + name);
		logger.info(name);
		return "返回fun";
	}
	
	/**
	 * 查询数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/funfindData")
	@ResponseBody
	public String funfindData(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		System.out.println("funfindData" + name);
		List<Map<String, Object>> list = openService.findDept();
		return JSON.toJSONString(list);
		
		/*try {response.setCharacterEncoding("UTF-8");
			response.getWriter().append("你好");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	/**
	 * 添加数据测试
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/funAddData")
	@ResponseBody
	public String funAddData(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		System.out.println("funAddData" + name);
		logger.info(name);
		openService.addDept();
		return "返回fun";
	}
	
	/**
	 * redis的session同步
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/funRedisSession")
	@ResponseBody
	public String funRedisSession(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		System.out.println("funRedisSession" + name);
		request.getSession().setAttribute("name", "zhangsan");
		/*ModelAndView mv=new ModelAndView();
		mv.setViewName("item");
		return mv;*/
		return "返回fun";
	}
	
	

	
	/**
	 * oepnCV测试
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/funOpenCV")
	@ResponseBody
	public String funOpenCV(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		System.out.println("funOpenCV" + name);
		Test.pic();
		return "返回fun";
	}
	
	/**
	 * redis测试
	 * @param args
	 */
	public static void main(String[] args) {
		//连接本地的 Redis 服务
        Jedis jedis = new Jedis("192.168.91.1");
        System.out.println("连接成功");
        //设置 redis 字符串数据
        jedis.set("runoobkey", "www.runoob.com");
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: "+ jedis.get("runoobkey"));
	}
	
}
