package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.entity.GeologicalDisasterRisk;
import com.ruoyi.system.domain.entity.MountainTorrentDisaster;
import com.ruoyi.system.mapper.MountainTorrentDisasterMapper;
import com.ruoyi.system.service.IMountainTorrentDisasterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MountainTorrentDisasterServiceImpl implements IMountainTorrentDisasterService {

    @Resource
    private MountainTorrentDisasterMapper mountainTorrentDisasterMapper;

    @Override
    public HashMap<String, List> getMountainTorrentDisasterList() {
        List<MountainTorrentDisaster> disasterList = mountainTorrentDisasterMapper.selectList(null);
        Map<String, List> processedList = processDisasters(disasterList);
        return (HashMap<String, List>) processedList;
    }

    private Map<String, List> processDisasters(List<MountainTorrentDisaster> disasters) {

        if (disasters == null || disasters.isEmpty()) {
            return null;
        }

        List lists = new ArrayList<>();
        Map<String, List> features = new HashMap<>();

        for (MountainTorrentDisaster disaster : disasters) {
            // 创建特征对象
            Map<String, Object> feature = new HashMap<>();

            // 创建属性对象
            Map<String, Object> properties = new HashMap<>();
            properties.put("county", disaster.getCounty());
            properties.put("disasterName", disaster.getDisasterName());
            properties.put("streetName", disaster.getStreetName());
            properties.put("villageName", disaster.getVillageName());
            properties.put("riskLevel", disaster.getRiskLevel());
            properties.put("riskPeople", disaster.getRiskPeople());
            properties.put("household", disaster.getHousehold());
            properties.put("house", disaster.getHouse());
            properties.put("adminName", disaster.getAdminName());
            properties.put("adminPosition", disaster.getAdminPosition());
            properties.put("adminTelephone", disaster.getAdminTele());
            properties.put("monitorName", disaster.getMonitorName());
            properties.put("monitorPosition", disaster.getMonitorPosition());
            properties.put("monitorTelephone", disaster.getMonitorTele());
            properties.put("transferName", disaster.getTransferName());
            properties.put("transferPosition", disaster.getTransferPosition());
            properties.put("transferTelephone", disaster.getTransferTele());

            // 组装特征对象
            feature.put("properties", properties);

            lists.add(feature);
        }

        features.put("features", lists);
        return features;
    }
}
