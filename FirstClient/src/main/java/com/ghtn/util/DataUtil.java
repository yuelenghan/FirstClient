package com.ghtn.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 14-1-14.
 */
public class DataUtil {

    public static List<Map<String, Object>> yhBaseInfoList;

    public static List<Map<String, Object>> swBaseInfoList;

    public static List<Map<String, Object>> rjxxBaseInfoList;

    static {
        // 隐患基础信息测试数据
        yhBaseInfoList = new LinkedList<>();
        for (int i = 0; i < 25; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("infoid", i + 1);
            map.put("infoname", "隐患基础信息" + (i + 1));

            yhBaseInfoList.add(map);
        }

        // 三违基础信息测试数据
        swBaseInfoList = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("infoid", i + 1);
            map.put("infoname", "三违基础信息" + (i + 1));

            swBaseInfoList.add(map);
        }

        // 入井基础信息数据
        rjxxBaseInfoList = new LinkedList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("infoid", 1);
        map1.put("infoname", "正常");
        rjxxBaseInfoList.add(map1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("infoid", 2);
        map2.put("infoname", "代班");
        rjxxBaseInfoList.add(map2);
    }

}
