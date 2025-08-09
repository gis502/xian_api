package com.ruoyi.system.domain.vo;

import com.ruoyi.system.domain.dto.TriggerModelDTO;
import lombok.Data;

/**
 * @author: xiaodemos
 * @date: 2025-08-07 11:25
 * @description: 模型修改先验概率
 */

@Data
public class TriggerModelVO {

    private TriggerModelDTO elevation;
    private TriggerModelDTO slope;
    private TriggerModelDTO waterDistance;
    private TriggerModelDTO vegetationCoverage;
    private TriggerModelDTO rainfall;
    private TriggerModelDTO duration;
    private TriggerModelDTO rockType;

}
