package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.dto.EarthquakeRainDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EarthquakeRainMapper extends BaseMapper<com.ruoyi.system.domain.entity.XianEarthquakeList> {

    List<EarthquakeRainDTO> selectEarthquakeRainPage(@Param("offset") int offset,
                                                     @Param("limit") int limit);

    int countTotal();
}
