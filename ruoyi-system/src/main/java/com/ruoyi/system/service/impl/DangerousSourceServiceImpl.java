package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.entity.DangerousSource;
import com.ruoyi.system.mapper.DangerousSourceMapper;
import com.ruoyi.system.service.IDangerousSourceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DangerousSourceServiceImpl implements IDangerousSourceService {

    @Resource
    private DangerousSourceMapper dangerousSourceMapper;

    @Override
    public HashMap<String, List> getDangerousSourceList() {

        List<DangerousSource> dangerousSourceList = dangerousSourceMapper.selectList(null);
        Map<String, List> dangerousSourceMap = processDangerous(dangerousSourceList);
        dangerousSourceMap.put("DangerousSourceData", dangerousSourceMap.get("features"));

        return (HashMap<String, List>) dangerousSourceMap;
    }
    //处理数据
    private Map<String, List> processDangerous(List<DangerousSource> dangerousSourceList){
        if(dangerousSourceList == null || dangerousSourceList.isEmpty()){
            return null;
        }
        List lists = new ArrayList<>();
        Map<String, List> features = new HashMap<>();
        for(DangerousSource dangerousSource : dangerousSourceList){
            Map<String, Object> feature = new HashMap<>();

            Map<String, Object> properties = new HashMap<>();
            properties.put("name", dangerousSource.getName());
            properties.put("address", dangerousSource.getAddress());
            properties.put("province", dangerousSource.getProvince());
            properties.put("city", dangerousSource.getCity());
            properties.put("county", dangerousSource.getCounty());
            properties.put("country", dangerousSource.getCountry());
            properties.put("enterpriseType", dangerousSource.getEnterpriseType());
            properties.put("level", dangerousSource.getLevel());
            properties.put("position", dangerousSource.getPosition());
            properties.put("unitHead", dangerousSource.getUnitHead());
            properties.put("telephone", dangerousSource.getTelephone());

            Map<String, Object> geometry = new HashMap<>();
            List<Double> coordinates = new ArrayList<>();
            coordinates.add(dangerousSource.getLongitude());
            coordinates.add(dangerousSource.getLatitude());

            geometry.put("coordinates", coordinates);

            feature.put("properties", properties);
            feature.put("geometry", geometry);

            lists.add(feature);
        }

        features.put("features", lists);

        return features;
    }
}
