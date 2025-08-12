package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.Highway;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface HighwayMapper extends BaseMapper<Highway> {
    
    /**
     * 查询与指定面相交或在面内的高速公路数据
     * @param wktPolygon WKT格式的面数据
     * @return 高速公路列表
     */
    List<Highway> findIntersectingHighways(@Param("wktPolygon") String wktPolygon);
    
    /**
     * 查询圆形范围内的高速公路数据
     * @param centerLon 中心点经度
     * @param centerLat 中心点纬度
     * @param radiusMeters 半径（米）
     * @return 高速公路列表
     */
    List<Highway> findWithinCircle(@Param("centerLon") Double centerLon, 
                                   @Param("centerLat") Double centerLat, 
                                   @Param("radiusMeters") Double radiusMeters);
}