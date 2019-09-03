package com.scy.ssm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scy.ssm.dao.OpenDao;

@Service
public class OpenServiceImpl implements OpenService{
	@Autowired
	OpenDao openDao;
	
	
	@Override
	//@Transactional
	public void addDept2() {
		Map<String, Object> map = new HashMap<>();
		map.put("deptno", "11");
		map.put("dname", "部门11");
		map.put("loc", "ROOT");
		map.put("sqlMapId", "addDept");
		openDao.insert(map);
		
		int a = 0;
		//System.out.println(1/a);
		
		addDept();
	}
	
	@Override
	@Transactional
	public void addDept() {
		Map<String, Object> map = new HashMap<>();
		map.put("deptno", "12");
		map.put("dname", "部门12");
		map.put("loc", "ROOT");
		map.put("sqlMapId", "addDept");
		openDao.insert(map);
		
		int a = 0;
		System.out.println(1/a);
		
		map.put("deptno", "13");
		map.put("dname", "部门13");
		map.put("loc", "ROOT");
		map.put("sqlMapId", "addDept");
		openDao.insert(map);
	}
	

	@Override
	public List<Map<String, Object>> findDept() {
		Map<String, Object> map = new HashMap<>();
		map.put("sqlMapId", "findDeptList");
		List<Map<String, Object>> list = openDao.queryForList(map);
		for (Map<String, Object> map2 : list) {
			System.out.println(map2.toString());
		}
		return list;
	}

}
