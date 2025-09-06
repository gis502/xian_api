package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.system.domain.entity.FactorAnalysis;
import com.ruoyi.system.domain.entity.GeologicalDisasterHide;
import com.ruoyi.system.domain.params.DisasterParam;
import com.ruoyi.system.mapper.FactorAnalysisMapper;
import com.ruoyi.system.mapper.GeologicalDisasterHideMapper;
import com.ruoyi.system.service.IHistoricalSimilarityAnalysisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HistoricalSimilarityAnalysisServiceImpl implements IHistoricalSimilarityAnalysisService {

    @Resource
    private FactorAnalysisMapper factorAnalysisMapper;

    @Resource
    private GeologicalDisasterHideMapper geologicalDisasterHideMapper;

    @Override
    public HashMap<String, List> getRainAffectPoints (DisasterParam disasterParam){

        QueryWrapper<FactorAnalysis> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rain_disaster_id", disasterParam.getDisasterId());

        List<FactorAnalysis> factorAnalysisList = factorAnalysisMapper.selectList(queryWrapper);
        Map<String,List> factorAnalysisMap = processRainAnalysis(factorAnalysisList);
        return (HashMap<String, List>) factorAnalysisMap;
    }

    private HashMap<String, List> processRainAnalysis(List<FactorAnalysis> factorAnalysisList) {

        if (factorAnalysisList == null || factorAnalysisList.isEmpty()) {
            return null;
        }

        List lists = new ArrayList();
        Map<String, List> points = new HashMap<>();
        for (FactorAnalysis factorAnalysis : factorAnalysisList) {

            Map<String, Object> pointInfo = new HashMap<>();

            //获取隐患点的id
            String entityId = factorAnalysis.getEntityId();
            // 使用正则表达式提取数字部分
            // \\d+ 表示匹配1个或多个数字
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(entityId);
            Integer number = null;
            if (matcher.find()) {
                // 提取匹配到的数字字符串并转换为Integer
                String numberStr = matcher.group();
                number = Integer.parseInt(numberStr);
            }
            GeologicalDisasterHide geologicalDisasterHide = geologicalDisasterHideMapper.selectById(number);
            pointInfo.put("probability", factorAnalysis.getProbability());
            pointInfo.put("level", factorAnalysis.getLevel());
            pointInfo.put("province", geologicalDisasterHide.getProvince());
            pointInfo.put("city", geologicalDisasterHide.getCity());
            pointInfo.put("county", geologicalDisasterHide.getCounty());
            pointInfo.put("village", geologicalDisasterHide.getVillage());
            pointInfo.put("disaster_name", geologicalDisasterHide.getDisasterName());
            pointInfo.put("lon", geologicalDisasterHide.getLon());
            pointInfo.put("lat", geologicalDisasterHide.getLat());
            pointInfo.put("position", geologicalDisasterHide.getPosition());
            pointInfo.put("disasterType", geologicalDisasterHide.getDisasterType());
            pointInfo.put("risk_grade", geologicalDisasterHide.getRiskGrade());
            pointInfo.put("scale_grade", geologicalDisasterHide.getScaleGrade());
            lists.add(pointInfo);
        }
        points.put("pointInfos", lists);
        return (HashMap<String, List>) points;
    }

}
