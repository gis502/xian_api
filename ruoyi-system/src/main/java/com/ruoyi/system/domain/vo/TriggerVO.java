package com.ruoyi.system.domain.vo;

import com.ruoyi.system.domain.dto.GeologicalDisasterHideDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: xiaodemos
 * @date: 2025-08-06 9:31
 * @description: 触发因子
 */

@Data
public class TriggerVO implements Serializable {

    // 实体ID
    private String entityId;
    // 当前实体灾害类型
    private String disasterType;
    private Double lon;
    private Double lat;
    // 致灾因子
    private List<FactorVO> factors;
    // 风险等级
    private List<String> level;
    // 发生概率
    private List<String> probability;
    // 灾害类型
    private List<String> disaster;
    // 隐患点基本信息
    private GeologicalDisasterHideDTO geologicalDisasterHideDTO;

}
