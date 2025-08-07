package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.WaterPipe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface WaterPipeMapper extends BaseMapper<WaterPipe> {
    
    /**
     * 查询与指定面相交或在面内的供水管网数据
     * @param wktPolygon WKT格式的面数据
     * @return 供水管网列表
     */
    List<WaterPipe> findIntersectingWaterPipes(@Param("wktPolygon") String wktPolygon);
}