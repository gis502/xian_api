package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.constant.XianConstants;
import com.ruoyi.common.exception.base.ParamsException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.dto.EqDTO;
import com.ruoyi.system.domain.entity.XianDisasterRain;
import com.ruoyi.system.domain.entity.XianEarthquakeList;
import com.ruoyi.system.domain.vo.EarthquakeVo;
import com.ruoyi.system.mapper.XianEarthquakeListMapper;
import com.ruoyi.system.service.IXianEarthquakeListService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 22:04
 * @description: 风险区实现类
 */

@Service
public class XianEarthquakeListServiceImpl implements IXianEarthquakeListService {
    @Autowired
    private XianEarthquakeListMapper xianEarthquakeListMapper;

    @Override
    public List<EqDTO> selectAllEq() {

        List<XianEarthquakeList> eqlist = xianEarthquakeListMapper.selectList(new QueryWrapper<XianEarthquakeList>().eq("is_deleted", 0));

        // 抛出异常
        if (eqlist == null) {
            throw new ParamsException(XianConstants.RESULT_EMPTY);
        }
        List<EqDTO> eqDtoList = new ArrayList<>();

        for (XianEarthquakeList earthquakeList : eqlist) {
            EqDTO eqDTO = new EqDTO();
            BeanUtils.copyProperties(earthquakeList, eqDTO);
            eqDtoList.add(eqDTO);
        }
        return eqDtoList;
    }

    @Override
    public EqDTO getEarthquakeEventById(Long Id) {

        if (!StringUtils.isNull(Id)) {

            XianEarthquakeList eqEvent = xianEarthquakeListMapper
                    .selectOne(new QueryWrapper<XianEarthquakeList>()
                            .eq("disaster_id", Id)
                            .eq("is_deleted", 0));
            // 是否空
            if (!StringUtils.isNull(eqEvent)){
                EqDTO eqDTO = new EqDTO();
                BeanUtils.copyProperties(eqEvent, eqDTO);
                return eqDTO;
            }
        }
        // 抛出异常
        throw new ParamsException(XianConstants.RESULT_EMPTY);
    }

    @Override
    public boolean insertDisaster(EarthquakeVo earthquake) {
        return xianEarthquakeListMapper.insertDisaster(earthquake);
    }
}
