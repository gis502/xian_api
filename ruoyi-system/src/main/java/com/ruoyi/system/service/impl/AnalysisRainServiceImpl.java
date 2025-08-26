package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.system.domain.entity.AnalysisRain;
import com.ruoyi.system.domain.entity.DangerousSource;
import com.ruoyi.system.mapper.AnalysisRainMapper;
import com.ruoyi.system.service.IAnalysisRainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalysisRainServiceImpl implements IAnalysisRainService {
    @Resource
    private AnalysisRainMapper analysisRainMapper;

    @Override
    public HashMap<String, List> getAdminCodeRain(int adminCode) {
        List<AnalysisRain> rainList = analysisRainMapper.selectList(new QueryWrapper<AnalysisRain>().eq("admin_code_chn",adminCode));
        Map<String, List> rainMap = processRain(rainList);

        return (HashMap<String, List>) rainMap;
    }

    @Override
    public HashMap<String, List> getRainPreHours() {
        List<AnalysisRain> rainph = analysisRainMapper.selectList(null);
        Map<String, List> rainMap = processRain(rainph);

        return (HashMap<String, List>) rainMap;
    }
    //处理数据
    private Map<String, List> processRain(List<AnalysisRain> rain) {
        if (rain == null || rain.isEmpty()) {
            return null;
        }
        List lists = new ArrayList();
        Map<String, List> features = new HashMap<>();
        for (AnalysisRain rainph : rain) {
            Map<String, Object> feature = new HashMap<>();

            Map<String, Object> properties = new HashMap<>();
            properties.put("stationName", rainph.getStationName());
            properties.put("adminCode", rainph.getAdminCode());
            properties.put("rainPreHours", rainph.getRain1H());
            properties.put("relativeHumidity", rainph.getRelativeHumidity());
            properties.put("temperature", rainph.getTemperature());

            Map<String, Object> geometry = new HashMap<>();
            List<Float> coordinates = new ArrayList<>();
            coordinates.add(rainph.getLon());
            coordinates.add(rainph.getLat());

            geometry.put("coordinates", coordinates);

            feature.put("geometry", geometry);
            feature.put("properties", properties);

            lists.add(feature);
        }
        features.put("features", lists);

        return features;
    }
}
