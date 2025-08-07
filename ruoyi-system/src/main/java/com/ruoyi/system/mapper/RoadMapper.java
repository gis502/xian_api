package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.Road;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RoadMapper extends BaseMapper<Road> {
    
    /**
     * 查询与指定面相交或在面内的道路数据
     * @param wktPolygon WKT格式的面数据
     * @return 道路列表
     */
    List<Road> findIntersectingRoads(@Param("wktPolygon") String wktPolygon);
}