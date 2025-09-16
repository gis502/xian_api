package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.system.domain.entity.FactorAnalysis;
import com.ruoyi.system.domain.entity.XianDisasterRain;
import com.ruoyi.system.mapper.FactorAnalysisMapper;
import com.ruoyi.system.mapper.XianDisasterRainMapper;
import com.ruoyi.system.service.IDisasterChainService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DisasterChainServiceImpl implements IDisasterChainService {

    @Resource
    private XianDisasterRainMapper xianDisasterRainMapper;
    @Resource
    private FactorAnalysisMapper factorAnalysisMapper;

    @Override
    public List<XianDisasterRain> getAllRainChain() {
        QueryWrapper<XianDisasterRain> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("occurrence_time"); // 按时间降序排列
        List<XianDisasterRain> latestRain = xianDisasterRainMapper.selectList(wrapper);
        // 将XianDisasterRain转换为DisasterRainDTO
        return latestRain;
    }

    @Override
    public List<FactorAnalysis> getAllRainDisasterProbability(Integer disasterId){
        QueryWrapper<FactorAnalysis> wrapper = new QueryWrapper<>();
        wrapper.eq("disaster_id",disasterId);
        List<FactorAnalysis> list = factorAnalysisMapper.selectList(wrapper);
        return list;
    }

    @Override
    public List<FactorAnalysis> getRainProbabilityByType(Long disasterId, String disasterType){
        QueryWrapper<FactorAnalysis> wrapper = new QueryWrapper<>();
        wrapper.eq("rain_disaster_id",disasterId);
        wrapper.eq("disaster_type",disasterType);
        List<FactorAnalysis> list = factorAnalysisMapper.selectList(wrapper);
        return processProbability(list);
    }

    private List<FactorAnalysis> processProbability(List<FactorAnalysis> factorAnalysisList) {
        if (factorAnalysisList == null || factorAnalysisList.isEmpty()) {
            return factorAnalysisList;
        }

        for (FactorAnalysis factor : factorAnalysisList) {

            // 处理probability：去除中括号
            String probability = factor.getProbability();
            if (probability != null && !probability.trim().isEmpty()) {
                // 去除中括号
                String cleanedProbability = probability.replaceAll("[\\[\\]]", "");
                factor.setProbability(cleanedProbability);
            }

            String level = factor.getLevel();
            if (level != null && !level.trim().isEmpty()) {
                // 去除中括号
                String cleanedLevel = level.replaceAll("[\\[\\]]", "");
                factor.setLevel(cleanedLevel);
            }

            String entityId = factor.getEntityId();
            if (entityId != null && !entityId.trim().isEmpty()) {
                // 使用正则表达式提取数字部分
                java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\\d+").matcher(entityId);
                if (matcher.find()) {
                    String numbers = matcher.group();
                    // 取最后3位数字
                    if (numbers.length() > 3) {
                        numbers = numbers.substring(numbers.length() - 3);
                    }
                    factor.setEntityId(numbers);
                }
            }
        }
        return factorAnalysisList;
    }
}
