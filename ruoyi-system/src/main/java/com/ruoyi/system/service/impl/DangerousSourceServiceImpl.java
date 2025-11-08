package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
        List<DangerousSource> dangerousSourceList = dangerousSourceMapper.selectList(new QueryWrapper<DangerousSource>().eq("city","西安市"));
        Map<String, List> dangerousSourceMap = processDangerous(dangerousSourceList);

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
            properties.put("dangerName", dangerousSource.getName());
            properties.put("unitCode", dangerousSource.getUnitCode());
            properties.put("position", dangerousSource.getAddress());
            properties.put("province", dangerousSource.getProvince());
            properties.put("city", dangerousSource.getCity());
            properties.put("county", dangerousSource.getCounty());
            properties.put("country", dangerousSource.getCountry());
            properties.put("enterpriseType", removeBrackets(dangerousSource.getEnterpriseType()));
            properties.put("level", removeBrackets(dangerousSource.getLevel()));
            properties.put("lon", dangerousSource.getLongitude());
            properties.put("lat", dangerousSource.getLatitude());
            properties.put("unitHead", dangerousSource.getUnitHead());
            properties.put("phone", dangerousSource.getTelephone());

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

    // 去除中括号的辅助方法
    private String removeBrackets(String str) {
        if (str == null) {
            return null;
        }
        // 去除字符串开头和结尾的中括号
        return str.replaceAll("^\\[|\\]$", "");
    }
}
