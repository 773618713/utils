package com.scy.utils.collection.list;

import com.scy.utils.collection.ObjectConvert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 取并集
 */
public class ListUnion {
    /**
     * 根据List中Map的key进行取并集
     * 如果定义的比较对象相同，但是实际对象不同，则返回ListA中的对象
     * 例：
     * <pre>
     *      listA:[{id:1,age:20},{id:2,age:21}]
     *      listB:[{id:2,age:22},{id:3,age:23}]
     *      List resultList = subtraction(listA,"id",listB,"id");
     *      resultList:[{id:1,age:20},{id:2,age:21},{id:3,age:23}]
     * </pre>
     *
     * @param listA
     * @param keyA
     * @param listB
     * @param keyB
     * @return 并集
     */
    public static List union(List<Map<String, Object>> listA, String keyA, List<Map<String, Object>> listB, String keyB) {
        List resultList = union(listA, (o) -> {
            return ((Map) o).get(keyA);
        }, listB, (o) -> {
            return ((Map) o).get(keyB);
        });
        return resultList;
    }

    /**
     * list 取并集
     *
     * @param listA
     * @param objectConvertA 对象转换器
     * @param listB
     * @param objectConvertB 对象转换器
     * @return 并集
     */
    public static List union(List listA, ObjectConvert objectConvertA, List listB, ObjectConvert objectConvertB) {
        Map mapB = ListToMap.toMap(listB, objectConvertB);
        for (int i = 0; i < listA.size(); i++) {
            mapB.put(objectConvertA.convert(listA.get(i)), listA.get(i));
        }
        List list = new ArrayList(mapB.values());
        return list;
    }
}
