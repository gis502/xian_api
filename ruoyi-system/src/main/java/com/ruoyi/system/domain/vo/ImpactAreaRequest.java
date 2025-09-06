package com.ruoyi.system.domain.vo;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * @author: xiaodemos
 * @date: 2025-01-XX
 * @description: 灾害触发请求参数
 */
@Data
public class ImpactAreaRequest {

    private String county;


    private Integer disasterId;

    /**
     * 灾害类型（如：滑坡、泥石流等）
     */
    private String type;
    
    /**
     * 多边形坐标数组
     */
    private List<Map<String, Double>> polygon;
    
    /**
     * 实体ID
     */
    private Integer entityId;
}