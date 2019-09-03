package com.scy.ssm.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 持久层
 * 	提供了增删改查的方法。
 * @author sun
 *
 */
@Repository
public class OpenDao {
	@Resource
    private SqlSessionTemplate sqlSessionTemplate;
	
	/**
	 * 查询
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> queryForList(Map<String, Object> map) {
		//PageHelper.startPage(1, 2);
		List<Map<String, Object>> resultList = sqlSessionTemplate.selectList(map.get("sqlMapId").toString(), map, new RowBounds(0, 15));
		PageInfo pageInfo = new PageInfo<>(resultList, 3);
		System.out.println(pageInfo.toString());
		return resultList;
	}
	
	/**
	 * 新增
	 * @param map
	 * @return
	 */
	public int insert(Map<String, Object> map) {
		int result = sqlSessionTemplate.insert(map.get("sqlMapId").toString(), map);
		return result;
	}
	
}
