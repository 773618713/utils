package com.scy.utils.collection.list;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.scy.utils.JSONUtil;
import com.scy.utils.collection.ObjectConvert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * List转换Map工具类
 */
public class ListToMap {

    /**
     * list转换map
     *
     * @param list          要转换的list
     * @param objectConvert Map的key值转换器
     * @return
     */
    public static Map toMap(List list, ObjectConvert objectConvert) {
        Map map = new HashMap(list.size() * 4 / 3);
        for (int i = 0; i < list.size(); i++) {
            map.put(objectConvert.convert(list.get(i)), list.get(i));
        }
        return map;
    }

    /**
     * list转换map
     *
     * @param list 要转换的List<Map<String, Object>>
     * @param keys list中的key值
     * @return
     */
    public static Map toMap(List<Map<String, Object>> list, String... keys) {
        Map resultMap = toMap(list, new ObjectConvert<Map<String, Object>, String>() {
            @Override
            public String convert(Map<String, Object> map) {
                StringBuilder sb = new StringBuilder();
                for (String key : keys) {
                    sb.append(key);
                }
                return sb.toString();
            }
        });
        return resultMap;
    }

    public static void main(String[] args) {
        String strA = "[{id:1,age:20},{id:2,age:21}]";
        JSONArray jsonArrayA = JSON.parseArray(strA);

        String strB = "[{id:2,age:22},{id:3,age:23}]";
        JSONArray jsonArrayB = JSON.parseArray(strB);

        List listA = (List) JSONUtil.json2ListMap(jsonArrayA);
        List listB = (List) JSONUtil.json2ListMap(jsonArrayB);

        Map tMap = new HashMap();
        tMap.put("id", null);
        listA.add(tMap);
        listB.add(tMap);


        Map resultMap = toMap(listA, new ObjectConvert<Map, Object>() {
            @Override
            public Object convert(Map o) {
                return o.get("id");
            }
        });
        System.out.println(resultMap.toString());

        List intersectionList = ListIntersection.intersection(listA, "id", listB, "id");
        System.out.println(intersectionList);

        List subtractionList = ListSubtraction.subtraction(listA, "id", listB, "id");
        System.out.println(subtractionList);

        List unionList = ListUnion.union(listA, "id", listB, "id");
        System.out.println(unionList);
    }

}