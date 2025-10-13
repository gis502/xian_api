package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.entity.School;
import com.ruoyi.system.mapper.SchoolMapper;
import com.ruoyi.system.service.ISchoolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchoolServiceImpl implements ISchoolService {

    @Resource
    private SchoolMapper schoolMapper; ;

    @Override
    public HashMap<String, List> getSchoolList() {
        List<School> schoolList = schoolMapper.selectList(null);
        Map<String , List> schoolMap = processSchool(schoolList);
        return (HashMap<String, List>) schoolMap;
    }

    //处理数据
    private Map<String, List> processSchool (List<School> schoolList) {
        if (schoolList == null || schoolList.isEmpty()) {
            return null;
        }
        List lists = new ArrayList();
        Map<String, List> features = new HashMap<>();
        for (School school : schoolList) {
            Map<String, Object> feature = new HashMap<>();

            Map<String, Object> properties = new HashMap<>();
            properties.put("schoolName", school.getSchoolName());
            properties.put("schoolAddress", school.getSchoolAddress());
            properties.put("schoolType", school.getSchoolType());
            properties.put("schoolArea", school.getSchoolArea());
            properties.put("isImportant", school.getIsImportant());
            properties.put("students", school.getStudents());
            properties.put("phone", school.getTelephone());
            properties.put("lon", school.getLon());
            properties.put("lat", school.getLat());

            Map<String, Object> geometry = new HashMap<>();
            List<Double> coordinates = new ArrayList<>();
            coordinates.add(school.getLon());
            coordinates.add(school.getLat());

            geometry.put("coordinates", coordinates);

            feature.put("geometry", geometry);
            feature.put("properties", properties);

            lists.add(feature);
        }
        features.put("features", lists);

        return features;
    }
}
