package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.dto.EqDTO;
import com.ruoyi.system.domain.entity.XianEarthquakeList;
import com.ruoyi.system.domain.vo.EarthquakeVo;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface XianEarthquakeListMapper extends BaseMapper<XianEarthquakeList> {

    boolean insertDisaster(EarthquakeVo earthquake);
}
