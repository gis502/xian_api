package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.domain.dto.FactorAttributeDTO;
import com.ruoyi.system.domain.entity.FactorAttribute;
import com.ruoyi.system.mapper.FactorAttributeMapper;
import com.ruoyi.system.service.IFactorAttributeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: xiaodemos
 * @date: 2025-07-24 11:37
 * @description: 致灾因子属性实现类
 */


@Service
public class FactorAttributeServiceImpl implements IFactorAttributeService {


    @Resource
    private FactorAttributeMapper factorAttributeMapper;

    // 获取致灾因子属性
    @Override
    public FactorAttributeDTO getFactorAttributeByAttributeId(Integer attributeId) {

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("attribute_id", attributeId);

        FactorAttribute factorAttribute = factorAttributeMapper.selectOne(wrapper);




        FactorAttributeDTO factorAttributeDTO = new FactorAttributeDTO();
        // 拷贝对象
        BeanUtils.copyProperties(factorAttribute,factorAttributeDTO);
        // 返回因子属性字段
        return factorAttributeDTO;
    }
}
