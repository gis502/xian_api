package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.GeologicalDisasterRisk;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Service
public interface GeologicalDisasterRiskMapper extends BaseMapper<GeologicalDisasterRisk> {

    @Select("SELECT count(*) FROM xian_disaster_hide where county = #{county}")
    Integer getHideNumByCounty(String county);
}
