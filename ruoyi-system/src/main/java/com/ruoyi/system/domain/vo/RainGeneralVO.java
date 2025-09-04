package com.ruoyi.system.domain.vo;

import com.ruoyi.system.domain.params.RainQuery;
import com.ruoyi.system.domain.params.ThematicQuery;
import lombok.Data;

/**
 * @author: xiaodemos
 * @date: 2025-08-26 17:17
 * @description: 通用VO
 */

@Data
public class RainGeneralVO {

    private String msg;
    private Integer code;
    private RainQuery data;
}
