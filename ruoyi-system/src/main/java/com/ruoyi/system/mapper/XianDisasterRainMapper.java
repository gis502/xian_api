package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.XianDisasterRain;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface XianDisasterRainMapper extends BaseMapper<XianDisasterRain> {
    List<XianDisasterRain> selectAllEq();
    XianDisasterRain getDisasterRainById(@Param("id") String id);
}
