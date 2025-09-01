package com.ruoyi.system.service;

import com.ruoyi.system.domain.dto.EqDTO;
import com.ruoyi.system.domain.vo.EarthquakeVo;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IXianEarthquakeListService {

    // 获取所有地震事件
    List<EqDTO> selectAllEq();

    // 根据Id获取地震事件
    EqDTO getEarthquakeEventById(Long Id);

    Integer insertDisaster(EarthquakeVo earthquake);

    HashMap<String, Object> insertEarthquake(EarthquakeVo earthquake);

    HashMap<String, Object> selectAffectPoints(EarthquakeVo earthquake);
}
