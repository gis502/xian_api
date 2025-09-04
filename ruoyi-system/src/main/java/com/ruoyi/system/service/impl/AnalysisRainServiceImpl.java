package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.system.domain.entity.AnalysisRain;
import com.ruoyi.system.domain.entity.DangerousSource;
import com.ruoyi.system.mapper.AnalysisRainMapper;
import com.ruoyi.system.service.IAnalysisRainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
        List<String> adminCodes = Arrays.asList("610111", "610116", "610117", "610118", "610122", "610115", "610112", "610102", "610114", "610124");
        List<AnalysisRain> rainph = analysisRainMapper.selectList(
                new QueryWrapper<AnalysisRain>().in("admin_code_chn", adminCodes)
        );
        Map<String, List> rainMap = processRain(rainph);
        return (HashMap<String, List>) rainMap;
    }
    //处理数据
    private Map<String, List> processRain(List<AnalysisRain> rain) {
        if (rain == null || rain.isEmpty()) {
            return null;
        }
        // 按监测站分组，并计算每个站12小时降雨量
        Map<String, Double> station12HourRain = calculate12HourRainfall(rain);
        // 按admin_code分组，找出每个行政区中降雨量最大的监测站
        Map<String, AnalysisRain> maxRainByAdminCode = new HashMap<>();
        Map<String, Double> maxRainfallByAdminCode = new HashMap<>();
        for (AnalysisRain rainph : rain) {
            String adminCode = rainph.getAdminCodeChn();
            String stationKey = rainph.getStationName();
            double currentRainfall = station12HourRain.getOrDefault(stationKey, 0.0);

            // 如果当前行政区还没有数据，或者当前降雨量更大，则更新
            if (!maxRainfallByAdminCode.containsKey(adminCode) ||
                    currentRainfall > maxRainfallByAdminCode.get(adminCode)) {
                maxRainfallByAdminCode.put(adminCode, currentRainfall);
                maxRainByAdminCode.put(adminCode, rainph);
            }
        }

        List lists = new ArrayList();
        Map<String, List> features = new HashMap<>();

        // 只处理每个行政区降雨量最大的监测站
        for (String adminCode : maxRainByAdminCode.keySet()) {
            AnalysisRain rainph = maxRainByAdminCode.get(adminCode);
            Map<String, Object> feature = new HashMap<>();

            Map<String, Object> properties = new HashMap<>();
            properties.put("stationName", rainph.getStationName());
            properties.put("adminCode", rainph.getAdminCodeChn());
            properties.put("rainPreHours", rainph.getPre1h());
            properties.put("relativeHumidity", rainph.getRelativeHumidity());
            properties.put("temperature", rainph.getTemperature());

            // 使用该行政区最大降雨量
            String stationKey = rainph.getStationName();
            double rain12Hours = station12HourRain.getOrDefault(stationKey, 0.0);
            properties.put("rainPre12Hours", rain12Hours);

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

    // 简化的12小时降雨量计算方法
    private Map<String, Double> calculate12HourRainfall(List<AnalysisRain> rainList) {
        Map<String, Double> result = new HashMap<>();

        if (rainList == null || rainList.isEmpty()) {
            return result;
        }

        // 按监测站分组
        Map<String, List<AnalysisRain>> stationMap = new HashMap<>();
        for (AnalysisRain data : rainList) {
            String stationName = data.getStationName();
            if (!stationMap.containsKey(stationName)) {
                stationMap.put(stationName, new ArrayList<>());
            }
            stationMap.get(stationName).add(data);
        }

        // 为每个站取最新的12条记录（假设数据是按时间顺序的）
        for (Map.Entry<String, List<AnalysisRain>> entry : stationMap.entrySet()) {
            String stationName = entry.getKey();
            List<AnalysisRain> stationData = entry.getValue();

            // 按时间倒序排序（最新的在前面）
            stationData.sort((a, b) -> b.getDatetime().compareTo(a.getDatetime()));

            // 取前12条记录（最新的12小时）
            double totalRainfall = 0.0;
            int count = Math.min(12, stationData.size());
            for (int i = 0; i < count; i++) {
                try {
                    totalRainfall += Double.parseDouble(stationData.get(i).getPre1h());
                } catch (NumberFormatException e) {
                    // 忽略格式错误的数据
                }
            }

            result.put(stationName, totalRainfall);
        }

        return result;
    }
}
