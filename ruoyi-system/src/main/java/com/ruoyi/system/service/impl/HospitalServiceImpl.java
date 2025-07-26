package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.entity.Hospital;
import com.ruoyi.system.mapper.HospitalMapper;
import com.ruoyi.system.service.IHospitalService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HospitalServiceImpl implements IHospitalService {
    @Resource
    private HospitalMapper hospitalMapper;

    @Override
    public HashMap<String, List> getHospitalList() {
        List<Hospital> hospitalList = hospitalMapper.selectList(null);
        Map<String, List> hospitalMap = processHospital(hospitalList);
        hospitalMap.put("HospitalData", hospitalMap.get("features"));

        return (HashMap<String, List>) hospitalMap;
    }
    //处理数据
    private Map<String, List> processHospital(List<Hospital> hospitalList) {
        if (hospitalList == null || hospitalList.isEmpty()) {
            return null;
        }
        List lists = new ArrayList();
        Map<String, List> features = new HashMap<>();
        for (Hospital hospital : hospitalList) {
            Map<String, Object> feature = new HashMap<>();

            Map<String, Object> properties = new HashMap<>();
            properties.put("name", hospital.getName());
            properties.put("address", hospital.getAddress());
            properties.put("hospitalTypeCode", hospital.getTypeCode());
            properties.put("hospitalType", hospital.getType());
            properties.put("level", hospital.getLevel());
            properties.put("institutionNature", hospital.getInstitutionNature());
            properties.put("hospitalDevices", hospital.getHospitalDevices());
            properties.put("hospitalWorkers", hospital.getHospitalWorkers());
            properties.put("sumPeople", hospital.getSumPeople());
            properties.put("beds", hospital.getBeds());
            properties.put("province", hospital.getProvince());
            properties.put("city", hospital.getCity());
            properties.put("county", hospital.getCounty());
            properties.put("country", hospital.getCountry());
            properties.put("unitHead", hospital.getUnitHead());
            properties.put("telephone", hospital.getTelephone());

            Map<String, Object> geometry = new HashMap<>();
            List<Double> coordinates = new ArrayList<>();
            coordinates.add(hospital.getLongitude());
            coordinates.add(hospital.getLatitude());

            geometry.put("coordinates", coordinates);

            feature.put("geometry", geometry);
            feature.put("properties", properties);

            lists.add(feature);
        }
        features.put("features", lists);

        return features;
    }
}
