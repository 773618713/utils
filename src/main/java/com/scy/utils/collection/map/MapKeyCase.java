package com.scy.utils.collection.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Map的key转换
 */
public class MapKeyCase {

    /**
     * 将一个list下的Map的key转换小写
     *
     * @param list
     * @return
     */
    public static void toLowerCase(List<Map<String, Object>> list) {
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> orgMap = list.get(i);
            list.set(i, toLowerCase(orgMap));
        }
    }

    /**
     * 将map的key值全部转换为小写
     *
     * @param orgMap
     * @return
     */
    public static Map<String, Object> toLowerCase(Map<String, Object> orgMap) {
        Map<String, Object> resultMap = new HashMap<>();
        if (orgMap == null || orgMap.isEmpty()) {
            return resultMap;
        }
        Set<String> keySet = orgMap.keySet();
        for (String key : keySet) {
            String newKey = key.toLowerCase();
            resultMap.put(newKey, orgMap.get(key));
        }
        return resultMap;
    }

}
