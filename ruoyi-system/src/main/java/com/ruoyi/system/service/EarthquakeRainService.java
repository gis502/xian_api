package com.ruoyi.system.service;

import com.ruoyi.system.domain.dto.EarthquakeRainDTO;

import java.util.List;

public interface EarthquakeRainService {
    List<EarthquakeRainDTO> getPagedList(int pageNum, int pageSize, List<String> disasterTypes);

    int getTotalCount(List<String> disasterTypes);

    String getInfoByName(String disasterName);
}
