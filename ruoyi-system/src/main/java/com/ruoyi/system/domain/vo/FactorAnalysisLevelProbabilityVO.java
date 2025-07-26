package com.ruoyi.system.domain.vo;

import lombok.Data;

@Data
public class FactorAnalysisLevelProbabilityVO {
    // 风险等级
    private String level;
    // 发生概率
    private Double probability;
}
