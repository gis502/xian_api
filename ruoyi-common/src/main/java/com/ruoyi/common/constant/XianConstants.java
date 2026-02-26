package com.ruoyi.common.constant;

import com.alibaba.fastjson2.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: xiaodemos
 * @date: 2025-08-06 11:12
 * @description: 常量
 */


public class XianConstants {

    public static final String PARAMS_EMPTY = "请求参数为空!";
    public static final String RESULT_EMPTY = "获取结果为空!";
    public static final String REQUEST_SUCCESS = "请求成功!";
    public static final String AUTH_ERROR = "授权失败!";
    public static final String TRIGGER_ERROR = "地震触发异常!";
    public static final String THEMATIC_EMPTY = "正在生成专题图,请等待...";
    public static final String RESULT_EMPTY_RAIN = "报告正在生成中，请等待...";
//    public static String IP1 = "http://10.22.245.246";
//    public static String IP2 = "http://10.22.245.247";
    public static String IP1 = "http://localhost";
    public static String IP2 = "http://localhost";

    public static final boolean SEISMIC_TYPE = false;
    public static final boolean STORM_TYPE = true;


    // 定义常量和映射关系
    public static final String ROCK_TYPE_ALIAS = "rockType";
    public static final Map<String, String> ROCK_TYPE_MAPPING = new HashMap<String, String>() {{
        put("花岗岩", "0");
        put("黄土-古土壤", "1");
        put("黄土-砂岩互层", "2");
        put("片麻岩", "3");
        put("砂岩", "4");
        put("页岩", "5");
    }};
    public static final Map<String, String> ROCK_TYPE_PARSE = new HashMap<String, String>() {{
        put("0", "花岗岩");
        put("1", "黄土-古土壤");
        put("2", "黄土-砂岩互层");
        put("3", "片麻岩");
        put("4", "砂岩");
        put("5", "页岩");
    }};

    public static final JSONObject authBody = new JSONObject();
    static {
        authBody.put("username", "admin");
        authBody.put("password", "admin123");
    }

    // 触发模型接口
    public static final String BAYES_NET_MODEL_URL = IP1+":8085/model/bayes/prediction";
    // 修改模型参数接口
    public static final String BAYES_NET_MODEL_UPDATE_URL = IP1+":8085/model/bayes/change";

    // 灾评系统授权
    public static final String AUTH_URL = IP2+":18008/api/open/auth";

    // 地震触发接口
    public static final String TRIGGER_EARTHQUAKE_URL = IP2+":18008/api/open/eq/trigger";
    // 暴雨触发接口
    public static final String TRIGGER_STORM_URL = IP2+":18008/api/open/rain/trigger";

    // 专题图接口
    public static final String THEMATIC_MAP_URL = IP2+":18008/api/open/eq/getMap";
    // 灾情报告接口
    public static final String DISASTER_REPORT_URL = IP2+":18008/api/open/eq/getMap";



}
