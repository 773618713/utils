package com.scy.ssm;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.scy.ssm.service.OpenService;
/**
 * 	事务测试
 * <p>Title: AppTest</p>  
 * <p>Description: </p> 
 * @author sun
 * @date 2019年8月28日
 * @version 1.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试  
//@ContextConfiguration({"classpath:spring-mybatis.xml"}) //加载配置文件  
@ContextConfiguration({"classpath*:spring-my*.xml"})
public class AppTest {
	
	Logger logger = LogManager.getLogger(AppTest.class);
	
	@Resource
	OpenService openService;
	
	@Test
	public void transactionTest() {
		System.out.println("funfindData");
		openService.addDept2();
		List<Map<String, Object>> list = openService.findDept();
	}
}
