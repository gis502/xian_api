package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.entity.EmergencyShelter;
import com.ruoyi.system.mapper.EmergencyShelterMapper;
import com.ruoyi.system.service.IEmergencyShelterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmergencyShelterServiceImpl implements IEmergencyShelterService {

    @Resource
    private EmergencyShelterMapper emergencyShelterMapper;

    @Override
    public HashMap<String, List> getEmergencyShelterList() {
        List<EmergencyShelter> emergencyShelterList = emergencyShelterMapper.selectList(null);
        Map<String , List> emergencyShelterMap = processEmergencyShelter(emergencyShelterList);
        return (HashMap<String, List>) emergencyShelterMap;
    }

    //处理数据
    private Map<String, List> processEmergencyShelter (List<EmergencyShelter> emergencyShelterList) {
        if (emergencyShelterList == null || emergencyShelterList.isEmpty()) {
            return null;
        }
        List lists = new ArrayList();
        Map<String, List> features = new HashMap<>();
        for (EmergencyShelter emergencyShelter : emergencyShelterList) {
            Map<String, Object> feature = new HashMap<>();

            Map<String, Object> properties = new HashMap<>();
            properties.put("name", emergencyShelter.getName());
            properties.put("address", emergencyShelter.getAddress());
            properties.put("hospitalType", emergencyShelter.getType());
            properties.put("level", emergencyShelter.getLevel());
            properties.put("volume", emergencyShelter.getVolume());
            properties.put("department", emergencyShelter.getDepartment());
            properties.put("store", emergencyShelter.getStore());
            properties.put("province", emergencyShelter.getProvince());
            properties.put("city", emergencyShelter.getCity());
            properties.put("county", emergencyShelter.getCounty());
            properties.put("country", emergencyShelter.getCountry());
            properties.put("unitHead", emergencyShelter.getUnitHead());
            properties.put("telephone", emergencyShelter.getTelephone());

            Map<String, Object> geometry = new HashMap<>();
            List<Double> coordinates = new ArrayList<>();
            coordinates.add(emergencyShelter.getLongitude());
            coordinates.add(emergencyShelter.getLatitude());

            geometry.put("coordinates", coordinates);

            feature.put("geometry", geometry);
            feature.put("properties", properties);

            lists.add(feature);
        }
        features.put("features", lists);

        return features;
    }
}
