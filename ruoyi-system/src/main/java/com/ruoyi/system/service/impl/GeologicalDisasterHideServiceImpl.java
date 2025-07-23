package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.dto.GeologicalDisasterHideDTO;
import com.ruoyi.system.domain.entity.GeologicalDisasterHide;
import com.ruoyi.system.mapper.GeologicalDisasterHideMapper;
import com.ruoyi.system.service.IGeologicalDisasterHideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 18:02
 * @description: 获取地质灾害隐患点实现类
 */

@Service
public class GeologicalDisasterHideServiceImpl implements IGeologicalDisasterHideService {

    @Resource
    private GeologicalDisasterHideMapper geologicalDisasterHideMapper;

    @Override
    public HashMap<String,Map> getGeologicalDisasterHideList() {

        QueryWrapper wrapper = new QueryWrapper();
        // 崩塌、地裂缝、地面塌陷、滑坡、泥石流
        wrapper.eq("disaster_type","滑坡");
        // 滑坡数据
        List<GeologicalDisasterHide> landSlide = geologicalDisasterHideMapper.selectList(new QueryWrapper<GeologicalDisasterHide>().eq("disaster_type", "滑坡"));
        // 泥石流数据
        List<GeologicalDisasterHide> debrisFlow = geologicalDisasterHideMapper.selectList(new QueryWrapper<GeologicalDisasterHide>().eq("disaster_type", "泥石流"));
        // 合并后的数据
        HashMap<String, Map> mergedData = mergeData(landSlide, debrisFlow);

        return mergedData;
    }

    // 合并风险点数据
    private HashMap<String,Map> mergeData(List<GeologicalDisasterHide> landSlide, List<GeologicalDisasterHide> debrisFlow) {

        HashMap<String, Map> hashMap = new HashMap<>();
        Map<String, List> slidefeatures = processDisasters(landSlide, "滑坡");
        Map<String, List> flowfeatures = processDisasters(debrisFlow, "泥石流");

        hashMap.put("slide", slidefeatures);
        hashMap.put("flow", flowfeatures);

        return hashMap;
    }

    // 格式化数据
    private Map<String, List> processDisasters(List<GeologicalDisasterHide> disasters, String disasterType) {

        if (disasters == null || disasters.isEmpty()) {
            return null;
        }

        List lists = new ArrayList<>();
        Map<String, List> features = new HashMap<>();

        for (GeologicalDisasterHide disaster : disasters) {
            // 创建特征对象
            Map<String, Object> feature = new HashMap<>();

            // 创建属性对象
            Map<String, Object> properties = new HashMap<>();
            properties.put("fieldCode", disaster.getFieldCode());
            properties.put("disasterName", disaster.getDisasterName());
            properties.put("longitude", disaster.getLongitude());
            properties.put("latitude", disaster.getLatitude());
            properties.put("lon", disaster.getLon());
            properties.put("lat", disaster.getLat());
            properties.put("position", disaster.getPosition());
            properties.put("disasterType", disasterType);
            properties.put("scaleGrade", disaster.getScaleGrade());
            properties.put("riskGrade", disaster.getRiskGrade());

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
