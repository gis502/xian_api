package com.ruoyi.system.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author: xiaodemos
 * @date: 2025-08-07 11:27
 * @description: 模型因子数据
 */

@Data
public class TriggerModelDTO {

    private List<Integer> range;
    private List<Double> probability;
}
