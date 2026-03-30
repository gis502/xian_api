package com.ruoyi.system.service;

import com.ruoyi.system.domain.dto.GeologicalDisasterRiskDTO;
import com.ruoyi.system.domain.entity.GeologicalDisasterRisk;

import java.util.HashMap;
import java.util.List;

public interface IGeologicalDisasterRiskService {

    // 获取风险区数据
    public HashMap<String, List> getGeologicalDisasterRiskList();
    public HashMap<String, List> getGeologicalAllRisk();

    /**
     * 根据区县名称查询风险区域
     * @param countyName 区县名称
     * @return 风险区域列表
     */
    List<GeologicalDisasterRisk> selectByCounty(String countyName);

}
