package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.Bridge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface BridgeMapper extends BaseMapper<Bridge> {
    
    /**
     * 查询与指定面相交或在面内的桥梁数据
     * @param wktPolygon WKT格式的面数据
     * @return 桥梁列表
     */
    List<Bridge> findIntersectingBridges(@Param("wktPolygon") String wktPolygon);
    
    /**
     * 查询圆形范围内的桥梁数据
     * @param centerLon 中心点经度
     * @param centerLat 中心点纬度
     * @param radiusMeters 半径（米）
     * @return 桥梁列表
     */
    List<Bridge> findWithinCircle(@Param("centerLon") Double centerLon, 
                                  @Param("centerLat") Double centerLat, 
                                  @Param("radiusMeters") Double radiusMeters);
}