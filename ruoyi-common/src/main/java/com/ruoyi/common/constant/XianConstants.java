package com.ruoyi.common.constant;

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

    // 定义常量和映射关系
    public static final String ROCK_TYPE_ALIAS = "rockType";
    public static final Map<String, String> ROCK_TYPE_MAPPING = new HashMap<String, String>() {{
        put("花岗岩", "1");
        put("黄土-古土壤", "2");
        put("黄土-砂岩互层", "3");
        put("片麻岩", "4");
        put("砂岩", "5");
        put("页岩", "6");
    }};


    // 触发模型接口
    public static final String BAYES_NET_MODEL_URL = "http://localhost:8085/model/bayes/prediction";
    // 修改模型参数接口
    public static final String BAYES_NET_MODEL_UPDATE_URL = "http://localhost:8085/model/bayes/change";




}
