package com.ruoyi.system.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author: xiaodemos
 * @date: 2025-07-23 16:59
 * @description:
 */


@Data
public class FactorValueDTO {

    private Integer valueId;

    private Integer hideId;

    private Integer attributeId;

    private String factorValue;

}
