package com.ruoyi.system.domain.dto;

import com.ruoyi.system.domain.vo.FactorAnalysisLevelProbabilityVO;
import com.ruoyi.system.domain.vo.FactorVO;
import lombok.Data;

import java.util.List;

@Data
public class ModelGetDataDTO {

    // 隐患点
    private GeologicalDisasterHideDTO geologicalDisasterHideDTO;
    // 因子值
    private List<FactorVO> factorVoList;
    // 预测结果
    private FactorAnalysisLevelProbabilityVO predict;
    // 实体id
    private String entityId;
}
