package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.domain.dto.FactorValueDTO;
import com.ruoyi.system.domain.entity.FactorValue;
import com.ruoyi.system.domain.vo.FactorVO;
import com.ruoyi.system.mapper.FactorValueMapper;
import com.ruoyi.system.service.IFactorValueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    // 获取所有因子值表中隐患点Id
    @Override
    public List<FactorValueDTO> getAllHideId() {

        QueryWrapper wrapper = new QueryWrapper();
        // 筛选 hide_id 列
        wrapper.select("distinct hide_id");
        List<FactorValue> list = factorValueMapper.selectList(wrapper);

        List<FactorValueDTO> factorValueDTOList = new ArrayList<>();
        for (FactorValue factorValue : list) {

            FactorValueDTO factorValueDTO = new FactorValueDTO();
            // 拷贝对象
            BeanUtils.copyProperties(factorValue, factorValueDTO);
            factorValueDTOList.add(factorValueDTO);
        }

        // 返回hideId 列表
        return factorValueDTOList;
    }


}
