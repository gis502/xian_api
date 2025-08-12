package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.dto.XianRainfallDuringPeriodDOT;
import com.ruoyi.system.domain.entity.XianEarthquakeList;
import com.ruoyi.system.domain.entity.XianRainfallDuringPeriod;
import com.ruoyi.system.domain.vo.EarthquakeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface XianRainfallDuringPeriodMapper extends BaseMapper<XianRainfallDuringPeriod> {
    List<XianRainfallDuringPeriod> getRainPeriodInfoByDisasterId(String Id);
}
