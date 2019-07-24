package com.scy.ssm.core.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础控制类
 * 	提供了线程安全的成员属性
 * 
 * @author sun
 *
 */
public class BaseController {
    /*@Resource(name = "openService")
    protected OpenService openService;*/
	
	/**
	 * 使用@Autowired注入，可以保证线程安全，@ModelAttribute不能保证线程安全。
	 */
	@Autowired
    protected HttpServletRequest request;
	@Autowired
    protected HttpServletResponse response;
	@Autowired
    protected HttpSession session;
	
	protected Map<String, Object> getParameterMap() {
		final Map<String, Object> hashMap = new HashMap<>();
		final Map map = request.getParameterMap();
		
		String s = "";
		for (Iterator<Map.Entry<String, String[]>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
			final Map.Entry<String, String[]> entry = iterator.next();
            final String key = entry.getKey();
            final String[] value = entry.getValue();
            if (value == null) {
                s = "";
            }
            else if (value instanceof String[]) {
                final String[] array = value;
                for (int i = 0; i < array.length; ++i) {
                    s = String.valueOf(array[i]) + ",";
                }
                s = s.substring(0, s.length() - 1);
            }
            else {
            	/*数组使用toString(),这将产生一个相当无用的结果(内存地址标识符)
                s = value.toString();*/
                s = Arrays.toString(value);
            }
            hashMap.put(key, s);
		}
		return hashMap;
	}
	
	
	public static void main(String[] args) {
		final Map<String, Object> hashMap = new HashMap<>();
		hashMap.put("name", "张三");
	}
	
}
