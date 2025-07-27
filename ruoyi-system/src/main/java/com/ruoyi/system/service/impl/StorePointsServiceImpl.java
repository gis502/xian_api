package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.entity.StorePoints;
import com.ruoyi.system.mapper.StorePointsMapper;
import com.ruoyi.system.service.IStorePointsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StorePointsServiceImpl implements IStorePointsService {

    @Resource
    private StorePointsMapper storePointsMapper;

    @Override
    public HashMap<String, List> getAllStorePointsList(){
        List<StorePoints> storePointsList = storePointsMapper.selectList(null);
        Map<String, List> storePointsMap = processStorePoints(storePointsList);
        return (HashMap<String, List>) storePointsMap;
    }

    //处理数据
    private Map<String, List> processStorePoints (List<StorePoints> storePointsList) {
        if (storePointsList == null || storePointsList.isEmpty()) {
            return null;
        }
        List lists = new ArrayList();
        Map<String, List> features = new HashMap<>();
        for (StorePoints storePoints : storePointsList) {
            Map<String, Object> feature = new HashMap<>();

            Map<String, Object> properties = new HashMap<>();
            properties.put("name", storePoints.getName());
            properties.put("address", storePoints.getAddress());
            properties.put("type", storePoints.getType());
            properties.put("level", storePoints.getLevel());
            properties.put("volume", storePoints.getVolume());
            properties.put("department", storePoints.getDepartment());
            properties.put("tent", storePoints.getTent());
            properties.put("rubberBoat", storePoints.getRubberBoat());
            properties.put("generator", storePoints.getGenerator());
            properties.put("emergencyLight", storePoints.getEmergencyLight());
            properties.put("saveTools", storePoints.getSaveTools());
            properties.put("province", storePoints.getProvince());
            properties.put("city", storePoints.getCity());
            properties.put("county", storePoints.getCounty());
            properties.put("country", storePoints.getCountry());
            properties.put("unitHead", storePoints.getUnitHead());
            properties.put("telephone", storePoints.getTelephone());

            Map<String, Object> geometry = new HashMap<>();
            List<Float> coordinates = new ArrayList<>();
            coordinates.add(storePoints.getLongitude());
            coordinates.add(storePoints.getLatitude());

            geometry.put("coordinates", coordinates);

            feature.put("geometry", geometry);
            feature.put("properties", properties);

            lists.add(feature);
        }
        features.put("features", lists);

        return features;
    }
}
