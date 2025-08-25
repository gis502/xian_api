package com.ruoyi.system.service;

import com.ruoyi.system.domain.dto.EqDTO;
import com.ruoyi.system.domain.vo.EarthquakeVo;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface IXianEarthquakeListService {

    // 获取所有地震事件
    List<EqDTO> selectAllEq();

    // 根据Id获取地震事件
    EqDTO getEarthquakeEventById(Long Id);

    Integer insertDisaster(EarthquakeVo earthquake);

    boolean insertEarthquake(EarthquakeVo earthquake);
}
