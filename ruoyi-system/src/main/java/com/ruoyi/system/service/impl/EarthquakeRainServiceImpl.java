package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.system.domain.dto.EarthquakeRainDTO;
import com.ruoyi.system.domain.entity.XianDisasterRain;
import com.ruoyi.system.domain.entity.XianEarthquakeList;
import com.ruoyi.system.mapper.EarthquakeRainMapper;
import com.ruoyi.system.mapper.XianDisasterRainMapper;
import com.ruoyi.system.mapper.XianEarthquakeListMapper;
import com.ruoyi.system.service.EarthquakeRainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EarthquakeRainServiceImpl implements EarthquakeRainService {

    @Resource
    private EarthquakeRainMapper earthquakeRainMapper;
    @Resource
    private XianEarthquakeListMapper xianEarthquakeListMapper;
    @Resource
    private XianDisasterRainMapper xianDisasterRainMapper;

    @Override
    public List<EarthquakeRainDTO> getPagedList(int pageNum, int pageSize, List<String> disasterTypes) {
        int offset = (pageNum - 1) * pageSize;
        List<EarthquakeRainDTO> earthquakeRainDTOS = earthquakeRainMapper.selectEarthquakeRainPage(offset, pageSize, disasterTypes);
        return earthquakeRainDTOS;
    }


    @Override
    public int getTotalCount(List<String> disasterTypes) {
        return earthquakeRainMapper.countTotal(disasterTypes);
    }

    @Override
    public String getInfoById(Integer Id){
        XianEarthquakeList earthquakeList = xianEarthquakeListMapper.selectOne(new QueryWrapper<XianEarthquakeList>().eq("disaster_id",Id));
        if(earthquakeList==null){
            XianDisasterRain xianDisasterRain = xianDisasterRainMapper.selectOne(new QueryWrapper<XianDisasterRain>().eq("disaster_id",Id));
            if(xianDisasterRain==null){
                return null;
            }
            return xianDisasterRain.getRainfall();
        }else{
            return earthquakeList.getMagnitude();
        }
    }
}
