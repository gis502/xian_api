package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.entity.FireFighter;
import com.ruoyi.system.mapper.FireFighterMapper;
import com.ruoyi.system.service.IFireFighterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FireFighterServiceImpl implements IFireFighterService {

    @Resource
    private FireFighterMapper fireFighterMapper;

    @Override
    public HashMap<String, List> getFireFighterList() {
        List<FireFighter> fireFighterList = fireFighterMapper.selectList(null);
        Map<String, List> fireFighterMap = processFireFighter(fireFighterList);

        return (HashMap<String, List>) fireFighterMap;
    }
    //处理数据
    private Map<String, List> processFireFighter(List<FireFighter> fireFighterList) {
        if (fireFighterList == null || fireFighterList.isEmpty()) {
            return null;
        }
        List lists = new ArrayList();
        Map<String, List> features = new HashMap<>();
        for (FireFighter fireFighter : fireFighterList) {
            Map<String, Object> feature = new HashMap<>();

            Map<String, Object> properties = new HashMap<>();
            properties.put("teamName", fireFighter.getTeamName());
            properties.put("teamType", removeBrackets(fireFighter.getTeamType()));
            properties.put("fireType", removeBrackets(fireFighter.getFireType()));
            properties.put("teamSumNum", fireFighter.getTeamNum());
            properties.put("fireCars", fireFighter.getFireCars());
            properties.put("fireDevices", fireFighter.getFireDevices());
            properties.put("position", fireFighter.getAddress());
            properties.put("province", fireFighter.getProvince());
            properties.put("city", fireFighter.getCity());
            properties.put("county", fireFighter.getCounty());
            properties.put("country", fireFighter.getCountry());
            properties.put("unitHead", fireFighter.getUnitHead());
            properties.put("phone", fireFighter.getTelephone());
            properties.put("lon", fireFighter.getLongitude());
            properties.put("lat", fireFighter.getLatitude());

            Map<String, Object> geometry = new HashMap<>();
            List<Double> coordinates = new ArrayList<>();
            coordinates.add(fireFighter.getLongitude());
            coordinates.add(fireFighter.getLatitude());

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
