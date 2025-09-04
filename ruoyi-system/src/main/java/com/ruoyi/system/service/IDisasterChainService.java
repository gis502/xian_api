package com.ruoyi.system.service;

import com.ruoyi.system.domain.entity.FactorAnalysis;
import com.ruoyi.system.domain.entity.XianDisasterRain;

import java.util.List;

public interface IDisasterChainService {
    public XianDisasterRain getLastRainChain();

    public List<FactorAnalysis> getAllRainDisasterProbability(Integer DisasterId);

    public List<FactorAnalysis> getRainProbabilityByType(Long DisasterId, String disasterType);

}
