package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.XianDisasterRain;
import com.ruoyi.system.domain.entity.XianEarthquakeList;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface XianDisasterRainMapper extends BaseMapper<XianDisasterRain> {
    List<XianDisasterRain> selectAllEq();
    XianDisasterRain getDisasterRainById(@Param("id") String id);
}
