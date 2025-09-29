package com.ruoyi.system.service;

import com.ruoyi.system.domain.entity.FactorAnalysis;
import com.ruoyi.system.domain.entity.XianDisasterRain;
import com.ruoyi.system.domain.entity.XianEarthquakeList;

import java.util.List;

public interface IDisasterChainService {
    public List<XianDisasterRain> getAllRainChain();

    public List<XianEarthquakeList> getAllEarthquakeList();

    public List<FactorAnalysis> getEarthQuakeProbabilityByType(Long DisasterId, String disasterType);

    public List<FactorAnalysis> getRainProbabilityByType(Long DisasterId, String disasterType);

}
