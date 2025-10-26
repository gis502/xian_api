package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.entity.test;
import com.ruoyi.system.mapper.TestMapper;
import com.ruoyi.system.service.ITestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestServiceImpl implements ITestService {
    @Resource
    private TestMapper testMapper;

    @Override
    public HashMap<String , List> getAll(){
        List<test> List = testMapper.selectList(null);
        Map<String, List> Map = process(List);

        return (HashMap<String, List>) Map;
    }
    //处理数据
    private Map<String, List> process(List<test> AList){
        if(AList == null || AList.isEmpty()){
            return null;
        }
        List lists = new ArrayList<>();
        Map<String, List> features = new HashMap<>();
        for(test adm : AList){
            Map<String, Object> feature = new HashMap<>();

            Map<String, Object> properties = new HashMap<>();
            properties.put("riskName", adm.getRiskName());

            Map<String, Object> geometry = new HashMap<>();
            List<Double> coordinates = new ArrayList<>();
            coordinates.add(adm.getLon());
            coordinates.add(adm.getLat());

            geometry.put("coordinates", coordinates);

            feature.put("properties", properties);
            feature.put("geometry", geometry);

            lists.add(feature);
        }

        features.put("features", lists);

        return features;
    }
}
