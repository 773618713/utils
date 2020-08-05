package com.scy.utils.collection.list;

import com.scy.utils.collection.ObjectConvert;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 取交集
 */
public class ListIntersection {

    /**
     * 根据List中Map的key进行取交集
     * listA和listB如果转换后比较对象相同，但是实际对象不同，则返回ListA中的对象
     * 例：
     * <pre>
     *      listA:[{id:1,age:20},{id:2,age:21}]
     *      listB:[{id:2,age:22},{id:3,age:23}]
     *      List resultList = subtraction(listA,"id",listB,"id");
     *      resultList:[{id:2,age:21}]
     * </pre>
     *
     * @param listA
     * @param keyA
     * @param listB
     * @param keyB
     * @return 交集
     */
    public static List intersection(List<Map<String, Object>> listA, String keyA, List<Map<String, Object>> listB, String keyB) {
        List resultList = intersection(listA, (o) -> {
            return ((Map) o).get(keyA);
        }, listB, (o) -> {
            return ((Map) o).get(keyB);
        });
        return resultList;
    }

    /**
     * list 取交集
     *
     * @param listA
     * @param objectConvertA 对象转换器
     * @param listB
     * @param objectConvertB 对象转换器
     * @return 交集
     */
    public static List intersection(List listA, ObjectConvert objectConvertA, List listB, ObjectConvert objectConvertB) {
        Map mapB = ListToMap.toMap(listB, objectConvertB);
        List resultList = new LinkedList();
        for (int i = 0; i < listA.size(); i++) {
            if (mapB.get(objectConvertA.convert(listA.get(i))) != null) {
                resultList.add(listA.get(i));
            }
        }
        return new ArrayList(resultList);
    }

}
