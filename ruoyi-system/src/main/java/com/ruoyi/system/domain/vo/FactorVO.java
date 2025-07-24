package com.ruoyi.system.domain.vo;

import lombok.Data;

/**
 * @author: xiaodemos
 * @date: 2025-07-24 17:29
 * @description: 因子值视图
 */

@Data
public class FactorVO {

    private Integer hideId;
    private Integer attributeId;    //因子属性ID
    private Integer valueId;    // 因子值ID
    private String attributeName;    //因子名称（如 “高程”“坡度”)
    private String factorValue;     //因子值
    private String unit;    //因子单位
    private String attributeNameAlias;


}
