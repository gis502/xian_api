package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.system.domain.entity.HistoryDisaster;
import com.ruoyi.system.mapper.HistoryDisasterMapper;
import com.ruoyi.system.service.IHistoryDisasterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class HistoryDisasterServiceImpl implements IHistoryDisasterService {

    @Resource
    private HistoryDisasterMapper historyDisasterMapper;

    @Override
    public HashMap<String, List> getHistoryDisasterList(String regionName) {
        List<HistoryDisaster> historyDisasterList = historyDisasterMapper.selectList(new QueryWrapper<HistoryDisaster>().eq("regional_name", regionName));
        Map<String, List> historyDisasterMap = processDangerous(historyDisasterList);
        return (HashMap<String, List>) historyDisasterMap;
    }
    //处理数据
    private Map<String, List> processDangerous(List<HistoryDisaster> historyDisasterList){
        if (historyDisasterList == null || historyDisasterList.isEmpty()) {
            return null;
        };
        List lists = new ArrayList<>();
        Map<String, List> features = new HashMap<>();
        for (HistoryDisaster historyDisaster : historyDisasterList) {
            Map<String, Object> feature = new HashMap<>();
            Map<String, Object> properties = new HashMap<>();
            properties.put("locateName", historyDisaster.getName());
            properties.put("historyDisasterevent", historyDisaster.getDisasterEvent());
            properties.put("historyDisastertype", removeBrackets(historyDisaster.getDisasterType()));
            properties.put("disaster_population", historyDisaster.getDisasterPopulation());
            properties.put("missing_persons", historyDisaster.getMissingPersons());
            properties.put("direct_economic_losses", historyDisaster.getEconomicLosses());
            properties.put("collapsedHouses", historyDisaster.getCollapsedHouses());
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
