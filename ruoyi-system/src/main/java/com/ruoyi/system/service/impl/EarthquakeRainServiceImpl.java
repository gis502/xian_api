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
    public List<EarthquakeRainDTO> getPagedList(int pageNum, int pageSize, List<String> disasterTypes) {
        int offset = (pageNum - 1) * pageSize;
        List<EarthquakeRainDTO> earthquakeRainDTOS = earthquakeRainMapper.selectEarthquakeRainPage(offset, pageSize, disasterTypes);
        System.out.println("earthquakeRainDTOS = " + earthquakeRainDTOS);
        return earthquakeRainDTOS;
    }


    @Override
    public int getTotalCount(List<String> disasterTypes) {
        return earthquakeRainMapper.countTotal(disasterTypes);
    }
}
