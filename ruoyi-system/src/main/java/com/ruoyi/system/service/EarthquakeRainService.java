package com.ruoyi.system.service;

import com.ruoyi.system.domain.dto.EarthquakeRainDTO;

import java.util.List;

public interface EarthquakeRainService {
    List<EarthquakeRainDTO> getPagedList(int pageNum, int pageSize);

    int getTotalCount();
}
