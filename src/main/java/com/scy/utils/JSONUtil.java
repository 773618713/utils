package com.scy.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONUtil {

    public static void main(String[] args) {
        //List examList2 = JSON.parseObject(jsonStr, new TypeReference<List<Map<String, Object>>>() {});
        //System.out.println(examList2);

        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        List list = (List) json2ListMap(jsonArray);
        System.out.println(list);
    }

    /**
     * JSON对象转换ListMap对象
     * @param obj
     * @return
     */
    public static Object json2ListMap(Object obj){
        if (obj instanceof JSONArray){
            JSONArray jsonArray = (JSONArray)obj;
            List list = new ArrayList();
            for (int i = 0; i < jsonArray.size(); i++) {
                list.add(json2ListMap(jsonArray.get(i)));
            }
            return list;
        }else if (obj instanceof JSONObject){
            JSONObject jsonObject = (JSONObject) obj;
            Map map = new HashMap();
            for (String key:(jsonObject.keySet()))  {
                map.put(key,json2ListMap(jsonObject.get(key)));
                /*if (!"$$hashKey".equals(key)){
                    map.put(key,json2ListMap(jsonObject.get(key)));
                }else {
                    System.out.println(jsonObject);
                }*/
            }
            return map;
        }else {
            return obj.toString();
        }
    }

    static String jsonStr = "[\n" +
            "    {\n" +
            "        \"examName\": \"2019第一学期语数英质量抽测\", \n" +
            "        \"examId\": \"123\", \n" +
            "        \"schools\": [\n" +
            "            {\n" +
            "                \"id\": \"01\", \n" +
            "                \"name\": \"一中\", \n" +
            "                \"classOpen\": false, \n" +
            "                \"classs\": [\n" +
            "                    {\n" +
            "                        \"id\": \"123\", \n" +
            "                        \"name\": \"一班\", \n" +
            "                        \"checked\": true\n" +
            "                    }, \n" +
            "                    {\n" +
            "                        \"id\": \"456\", \n" +
            "                        \"name\": \"三年级02班\", \n" +
            "                        \"checked\": true\n" +
            "                    }, \n" +
            "                    {\n" +
            "                        \"id\": \"789\", \n" +
            "                        \"name\": \"三班\", \n" +
            "                        \"checked\": true\n" +
            "                    }, \n" +
            "                    {\n" +
            "                        \"id\": \"1231\", \n" +
            "                        \"name\": \"四班\", \n" +
            "                        \"checked\": true\n" +
            "                    }, \n" +
            "                    {\n" +
            "                        \"id\": \"4561\", \n" +
            "                        \"name\": \"三年级05班\", \n" +
            "                        \"checked\": true\n" +
            "                    }, \n" +
            "                    {\n" +
            "                        \"id\": \"7891\", \n" +
            "                        \"name\": \"六班\", \n" +
            "                        \"checked\": true\n" +
            "                    }\n" +
            "                ], \n" +
            "                \"checked\": true\n" +
            "            }, \n" +
            "            {\n" +
            "                \"id\": \"02\", \n" +
            "                \"name\": \"二中\", \n" +
            "                \"classOpen\": false, \n" +
            "                \"classs\": [\n" +
            "                    {\n" +
            "                        \"id\": \"123\", \n" +
            "                        \"name\": \"一班\", \n" +
            "                        \"checked\": true\n" +
            "                    }, \n" +
            "                    {\n" +
            "                        \"id\": \"456\", \n" +
            "                        \"name\": \"二班\", \n" +
            "                        \"checked\": true\n" +
            "                    }, \n" +
            "                    {\n" +
            "                        \"id\": \"789\", \n" +
            "                        \"name\": \"三班\", \n" +
            "                        \"checked\": true\n" +
            "                    }\n" +
            "                ], \n" +
            "                \"checked\": true\n" +
            "            }\n" +
            "        ], \n" +
            "        \"courses\": [\n" +
            "            {\n" +
            "                \"id\": \"S1\", \n" +
            "                \"name\": \"语文\", \n" +
            "                \"checked\": true\n" +
            "            }, \n" +
            "            {\n" +
            "                \"id\": \"S2\", \n" +
            "                \"name\": \"数学\", \n" +
            "                \"checked\": true\n" +
            "            }\n" +
            "        ]\n" +
            "    }, \n" +
            "    {\n" +
            "        \"examName\": \"2018第一学期语数英质量抽测\", \n" +
            "        \"examId\": \"456\", \n" +
            "        \"schools\": [\n" +
            "            {\n" +
            "                \"id\": \"01\", \n" +
            "                \"name\": \"一中\", \n" +
            "                \"classOpen\": false, \n" +
            "                \"classs\": [\n" +
            "                    {\n" +
            "                        \"id\": \"123\", \n" +
            "                        \"name\": \"一班\", \n" +
            "                        \"checked\": true\n" +
            "                    }, \n" +
            "                    {\n" +
            "                        \"id\": \"456\", \n" +
            "                        \"name\": \"二班\", \n" +
            "                        \"checked\": true\n" +
            "                    }, \n" +
            "                    {\n" +
            "                        \"id\": \"789\", \n" +
            "                        \"name\": \"三班\", \n" +
            "                        \"checked\": true\n" +
            "                    }\n" +
            "                ], \n" +
            "                \"checked\": true\n" +
            "            }, \n" +
            "            {\n" +
            "                \"id\": \"02\", \n" +
            "                \"name\": \"二中\", \n" +
            "                \"classOpen\": false, \n" +
            "                \"classs\": [\n" +
            "                    {\n" +
            "                        \"id\": \"123\", \n" +
            "                        \"name\": \"一班\", \n" +
            "                        \"checked\": true\n" +
            "                    }, \n" +
            "                    {\n" +
            "                        \"id\": \"456\", \n" +
            "                        \"name\": \"二班\", \n" +
            "                        \"checked\": true\n" +
            "                    }, \n" +
            "                    {\n" +
            "                        \"id\": \"789\", \n" +
            "                        \"name\": \"三班\", \n" +
            "                        \"checked\": true\n" +
            "                    }\n" +
            "                ], \n" +
            "                \"checked\": true\n" +
            "            }\n" +
            "        ], \n" +
            "        \"courses\": [\n" +
            "            {\n" +
            "                \"id\": \"S1\", \n" +
            "                \"name\": \"语文\", \n" +
            "                \"checked\": true\n" +
            "            }, \n" +
            "            {\n" +
            "                \"id\": \"S2\", \n" +
            "                \"name\": \"数学\", \n" +
            "                \"checked\": true\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "]";

}
