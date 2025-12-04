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
            properties.put("storeName", storePoints.getName());
            properties.put("position", storePoints.getAddress());
            properties.put("storeType", removeBrackets(storePoints.getType()));
            properties.put("level", removeBrackets(storePoints.getLevel()));
            properties.put("storeVolume", storePoints.getVolume());
            properties.put("department", removeBrackets(storePoints.getDepartment()));
            properties.put("tent", storePoints.getTent());
            properties.put("rubberBoat", storePoints.getRubberBoat());
            properties.put("egenerator", storePoints.getGenerator());
            properties.put("emergencyLight", storePoints.getEmergencyLight());
            properties.put("saveTools", storePoints.getSaveTools());
            properties.put("province", storePoints.getProvince());
            properties.put("city", storePoints.getCity());
            properties.put("county", storePoints.getCounty());
            properties.put("country", storePoints.getCountry());
            properties.put("unitHead", storePoints.getUnitHead());
            properties.put("phone", storePoints.getTelephone());
            properties.put("lon", storePoints.getLongitude());
            properties.put("lat", storePoints.getLatitude());

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

    // 去除中括号的辅助方法
    private String removeBrackets(String str) {
        if (str == null) {
            return null;
        }
        // 去除字符串开头和结尾的中括号
        return str.replaceAll("^\\[|\\]$", "");
    }
}
