package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.XianEarthquakeList;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface XianEarthquakeListMapper extends BaseMapper<XianEarthquakeList> {
    List<XianEarthquakeList> selectAllEq();

    XianEarthquakeList getEarthquakeEventById(@Param("id") String id);
}
