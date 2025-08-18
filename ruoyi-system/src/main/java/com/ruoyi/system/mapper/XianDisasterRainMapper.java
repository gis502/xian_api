package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.XianDisasterRain;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface XianDisasterRainMapper extends BaseMapper<XianDisasterRain> {

    List<Map<String, Object>> queryOverview(Long disasterId);

    @Select("SELECT disaster_id FROM xian_disaster_rain ORDER BY create_time DESC LIMIT 1")
    Long getLatestRainDisasterId();

}
