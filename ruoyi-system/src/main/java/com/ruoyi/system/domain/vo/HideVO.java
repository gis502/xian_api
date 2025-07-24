package com.ruoyi.system.domain.vo;

import com.ruoyi.system.domain.dto.FactorValueDTO;
import com.ruoyi.system.domain.dto.GeologicalDisasterHideDTO;
import lombok.Data;

import java.util.List;

/**
 * @author: xiaodemos
 * @date: 2025-07-24 10:37
 * @description: 隐患点+因子值视图类
 */

@Data
public class HideVO {

    // 隐患点
    private GeologicalDisasterHideDTO geologicalDisasterHideDTO;
    // 因子值
    private List<FactorVO> factorVoList;

}
