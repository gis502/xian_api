package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.entity.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.IAroundAnalysisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AroundAnalysisServiceImpl implements IAroundAnalysisService {
    @Resource
    private AnalysisBridgeMapper analysisBridgeMapper;
    @Resource
    private AnalysisReservoirMapper analysisReservoirMapper;
    @Resource
    private XianSubwayStationMapper xianSubwayStationMapper;

    @Override
    public HashMap<String, List>getAllBridge(){
        List<AnalysisBridge> bridgeList = analysisBridgeMapper.selectList(null);
        Map<String, List> map = processBridge(bridgeList);
        return (HashMap<String, List>) map;
    }

    @Override
    public HashMap<String ,List>getAllReservoir(){
        List<AnalysisReservoir> reservoirListList = analysisReservoirMapper.selectList(null);
        Map<String, List> map = processReservoir(reservoirListList);
        return (HashMap<String, List>) map;
    }

    @Override
    public HashMap<String, List>getAllSubwayStation(){
        List<XianSubwayStation> subwayStationList = xianSubwayStationMapper.selectList(null);
        Map<String, List> map = processSubway(subwayStationList);
        return (HashMap<String, List>) map;
    }

    private Map<String, List> processBridge(List<AnalysisBridge> bridgeList) {

        if (bridgeList == null || bridgeList.isEmpty()) {
            return null;
        }

        List lists = new ArrayList<>();
        Map<String, List> features = new HashMap<>();

        for (AnalysisBridge bridge : bridgeList) {
            // 创建特征对象
            Map<String, Object> feature = new HashMap<>();
            // 创建属性对象
            Map<String, Object> properties = new HashMap<>();
            properties.put("bridgeName", bridge.getBridgeName());
            properties.put("buildTime", bridge.getBuildTime());
            properties.put("bridgeType", bridge.getBridgeType());
            properties.put("techType", bridge.getTechType());
            properties.put("lon", bridge.getLon());
            properties.put("lat", bridge.getLat());
            properties.put("area", bridge.getArea());

            Map<String, Object> geometry = new HashMap<>();
            // coordinates为经度、纬度的数组
            List<Double> coordinates = new ArrayList<>();
            coordinates.add(bridge.getLon());  // 经度
            coordinates.add(bridge.getLat());  // 纬度
            geometry.put("coordinates", coordinates);

            // 组装特征对象
            feature.put("properties", properties);
            feature.put("geometry", geometry);
            lists.add(feature);
        }

        features.put("features", lists);

        return features;
    }

    private Map<String, List> processReservoir(List<AnalysisReservoir> reservoirList) {

        if (reservoirList == null || reservoirList.isEmpty()) {
            return null;
        }

        List lists = new ArrayList<>();
        Map<String, List> features = new HashMap<>();

        for (AnalysisReservoir reservoir : reservoirList) {
            // 创建特征对象
            Map<String, Object> feature = new HashMap<>();
            // 创建属性对象
            Map<String, Object> properties = new HashMap<>();
            properties.put("reservoirName", reservoir.getName());
            properties.put("location", reservoir.getLocation());
            properties.put("safetyLv", reservoir.getSafety());
            properties.put("lon", reservoir.getLongitude());
            properties.put("lat", reservoir.getLatitude());

            Map<String, Object> geometry = new HashMap<>();
            // coordinates为经度、纬度的数组
            List<Double> coordinates = new ArrayList<>();
            coordinates.add(reservoir.getLongitude());  // 经度
            coordinates.add(reservoir.getLatitude());  // 纬度
            geometry.put("coordinates", coordinates);

            // 组装特征对象
            feature.put("properties", properties);
            feature.put("geometry", geometry);
            lists.add(feature);
        }

        features.put("features", lists);

        return features;
    }

    private Map<String, List> processSubway(List<XianSubwayStation> subwayStationList) {

        if (subwayStationList == null || subwayStationList.isEmpty()) {
            return null;
        }

        List lists = new ArrayList<>();
        Map<String, List> features = new HashMap<>();

        for (XianSubwayStation subwayStation : subwayStationList) {
            // 创建特征对象
            Map<String, Object> feature = new HashMap<>();
            // 创建属性对象
            Map<String, Object> properties = new HashMap<>();
            properties.put("stationName", subwayStation.getStationName());
            properties.put("referToWater", subwayStation.getReferToWater());
            properties.put("depthOfWater", subwayStation.getDepthOfWater());
            properties.put("accumulatedWaterAfterAccounting", subwayStation.getAccumulatedWaterAfterAccounting());
            properties.put("lon", subwayStation.getLon());
            properties.put("lat", subwayStation.getLat());

            Map<String, Object> geometry = new HashMap<>();
            // coordinates为经度、纬度的数组
            List<Double> coordinates = new ArrayList<>();
            coordinates.add(subwayStation.getLon());  // 经度
            coordinates.add(subwayStation.getLat());  // 纬度
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
