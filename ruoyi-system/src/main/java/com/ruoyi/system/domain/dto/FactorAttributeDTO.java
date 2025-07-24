package com.ruoyi.system.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author: xiaodemos
 * @date: 2025-07-23 16:58
 * @description: 致灾因子DTO
 */


@Data
public class FactorAttributeDTO {

    private Integer attributeId;    //因子属性ID
    private String attributeName;    //因子名称（如 “高程”“坡度”)
    private String unit;    //因子单位
    private String attributeNameAlias;

}
