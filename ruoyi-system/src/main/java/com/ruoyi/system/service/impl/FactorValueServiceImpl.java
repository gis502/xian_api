package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.dto.BatchHideIdsDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.domain.dto.FactorValueDTO;
import com.ruoyi.system.domain.entity.FactorValue;
import com.ruoyi.system.domain.vo.FactorVO;
import com.ruoyi.system.mapper.FactorValueMapper;
import com.ruoyi.system.service.IFactorValueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author: xiaodemos
 * @date: 2025-07-24 10:50
 * @description: 因子值实现类
 */

@Service
public class FactorValueServiceImpl implements IFactorValueService {

    @Resource
    private FactorValueMapper factorValueMapper;


    // 根据 隐患id 查询因子值
    @Override
    public List<FactorVO> getFactorValueByHideId(Integer hideId) {
        return factorValueMapper.getFactorValueByHideId(hideId);
    }

    // 批次查询因子值
    @Override
    public List<FactorVO> getFactorValuesByHideIds(List<Integer> hideIds) {
        List<FactorVO> factorValuesByHideIds = factorValueMapper.getFactorValuesByHideIds(hideIds);
        return factorValuesByHideIds;
    }

    // 获取所有因子值表中隐患点Id
    // TODO 改为Mapper查询ID
    @Override
    public Set<Integer> getAllHideId() {
        Set<Integer> allHideId = factorValueMapper.getAllHideId();
        // 返回hideId 列表
        return allHideId;
    }

    // 获取因子列表值
    @Override
    public Map<String, List<String>> getFactorValueList() {

        // 获取岩土类型
        List<String> rockType = factorValueMapper.getRockType();
        // 获取坡型类别
        List<String> slopeType = factorValueMapper.getSlopeType();
        // 获取土地利用率类别
        List<String> landUseType = factorValueMapper.getLandUseType();

        Map <String, List<String>> factorValueList = new HashMap<>();
        factorValueList.put("rock", rockType);
        factorValueList.put("slope", slopeType);
        factorValueList.put("landUse", landUseType);

        return factorValueList;
    }


}
