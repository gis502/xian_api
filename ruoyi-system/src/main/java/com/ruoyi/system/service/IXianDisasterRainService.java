package com.ruoyi.system.service;


import com.ruoyi.system.domain.dto.DisasterRainDTO;
import com.ruoyi.system.domain.entity.XianDisasterRain;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface IXianDisasterRainService {


    // 暴雨灾害存库
    public void saveDisasterRain(DisasterRainDTO disasterRainDTO);
    // 获取所有暴雨数据
    public List<DisasterRainDTO> selectAllEq();
    // 根据Id获取暴雨数据
    DisasterRainDTO getDisasterRainById(Long Id);
}
