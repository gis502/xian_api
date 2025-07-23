package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.entity.GeologicalDisasterHide;
import com.ruoyi.system.domain.entity.GeologicalDisasterRisk;
import com.ruoyi.system.mapper.GeologicalDisasterRiskMapper;
import com.ruoyi.system.service.IGeologicalDisasterRiskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 22:04
 * @description: 风险区实现类
 */

@Service
public class GeologicalDisasterRiskServiceImpl implements IGeologicalDisasterRiskService {


    @Resource
    private GeologicalDisasterRiskMapper geologicalDisasterRiskMapper;


    // 获取风险区数据
    @Override
    public HashMap<String, List> getGeologicalDisasterRiskList() {

        List<GeologicalDisasterRisk> riskList = geologicalDisasterRiskMapper.selectList(null);
        Map<String, List> processedList = processDisasters(riskList);
        processedList.put("DangerAreaData", processedList.get("features"));

        return null;
    }




    // 格式化数据
    private Map<String, List> processDisasters(List<GeologicalDisasterRisk> disasters) {

        if (disasters == null || disasters.isEmpty()) {
            return null;
        }

        List lists = new ArrayList<>();
        Map<String, List> features = new HashMap<>();

        for (GeologicalDisasterRisk disaster : disasters) {
            // 创建特征对象
            Map<String, Object> feature = new HashMap<>();

            // 创建属性对象
            Map<String, Object> properties = new HashMap<>();
            properties.put("disasterName", disaster.getDisasterName());
            properties.put("unitCode", disaster.getUnitCode());
            properties.put("position", disaster.getPosition());
            properties.put("lon", disaster.getLon());
            properties.put("lat", disaster.getLat());
            properties.put("area", disaster.getArea());
            properties.put("residentCounts", disaster.getResidentCounts());
            properties.put("addressPopulation", disaster.getAddressPopulation());
            properties.put("riskProperty", disaster.getRiskProperty());
            properties.put("permanentPopulation", disaster.getPermanentPopulation());
            properties.put("housing", disaster.getHousing());
            properties.put("inspectorName", disaster.getInspectorName());
            properties.put("inspectorTele", disaster.getInspectorTele());
            Map<String, Object> geometry = new HashMap<>();
            // coordinates为经度、纬度的数组
            List<Double> coordinates = new ArrayList<>();
            coordinates.add(disaster.getLon());  // 经度
            coordinates.add(disaster.getLat());  // 纬度
            geometry.put("coordinates", coordinates);

            // 组装特征对象
            feature.put("properties", properties);
            feature.put("geometry", geometry);

            lists.add(feature);
        }

        features.put("features", lists);

        return features;
    }


}
