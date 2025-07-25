package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.dto.EarthquakeRainDTO;
import com.ruoyi.system.mapper.EarthquakeRainMapper;
import com.ruoyi.system.service.EarthquakeRainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EarthquakeRainServiceImpl implements EarthquakeRainService {

    @Resource
    private EarthquakeRainMapper earthquakeRainMapper;

    @Override
    public List<EarthquakeRainDTO> getPagedList(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return earthquakeRainMapper.selectEarthquakeRainPage(offset, pageSize);
    }

    @Override
    public int getTotalCount() {
        return earthquakeRainMapper.countTotal();
    }
}
